package com.example.johnb.nfcseniorproject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.nfc.tech.NdefFormatable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.Locale;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class Home extends AppCompatActivity {
    private AreaCheck.NFCManager nfcMger;
    IntentFilter[] intentFiltersArray;
    PendingIntent nfcPendingIntent;
    BarcodeScanner barcode = new BarcodeScanner();
    Integer idCount = 0;
    public static final String MIME_TEXT_PLAIN = "text/plain";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        int userId = GlobalInformation.getInstance().userId;

        Button areaCheckButton = (Button) findViewById(R.id.scanForArea);
        areaCheckButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                try {
                    OnScanForArea(v);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (TimeoutException e) {
                    e.printStackTrace();
                }
            }
        });

        Button Manage = (Button) findViewById(R.id.Manage);
        Manage.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String scanResult = barcode.scanResult;
                String sv = null;
                Intent adminIntent = new Intent(getApplicationContext() , AdminPage.class);
                startActivity(adminIntent);

            }
        });
    }

    public void OnScanForArea(View view) throws InterruptedException, ExecutionException, TimeoutException {
        Intent areaIntent = new Intent(getApplicationContext() ,AreaCheck.class);
        startActivity(areaIntent);
    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (intent != null && NfcAdapter.ACTION_NDEF_DISCOVERED.equals(intent.getAction())) {
            Parcelable[] rawMessages =
                    intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
            if (rawMessages != null) {
                NdefMessage[] messages = new NdefMessage[rawMessages.length];
                for (int i = 0; i < rawMessages.length; i++) {
                    messages[i] = (NdefMessage) rawMessages[i];
                    try {
                        String areaName = new String(messages[i].getRecords()[0].getPayload());
                        BackroundWorker backroundWorker = new BackroundWorker(this);
                        backroundWorker.execute("getAreaId",areaName );
                        backroundWorker.get(5000, TimeUnit.MILLISECONDS);
                        GlobalInformation.getInstance().areaId = Integer.parseInt(GlobalInformation.getInstance().queryResult);
                        Intent areaIntent = new Intent(getApplicationContext() ,AreaCheck.class);
                        startActivity(areaIntent);
                    } catch (NumberFormatException e) {
                        AlertDialog alertDialog;
                        alertDialog = new AlertDialog.Builder(this).create();
                        alertDialog.setTitle("Bad Area Tag");
                        alertDialog.setMessage("The tag scanned is not configured for an area properly");
                        alertDialog.show();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    } catch (TimeoutException e) {
                        e.printStackTrace();
                    }
                }
                // Process the messages array.
            }
        }
    }

    public class NFCManager {
        private Activity activity;
        private NfcAdapter nfcAdpt;

        public NFCManager(Activity activity) {
            this.activity = activity;
        }

        public void verifyNFC(){

            nfcAdpt = NfcAdapter.getDefaultAdapter(activity);

            if (nfcAdpt == null) {
                Toast.makeText(this.activity, "This device doesn't support NFC.", Toast.LENGTH_LONG).show();
                finish();
                return;
            }
            if (!nfcAdpt.isEnabled()) {
                Toast.makeText(this.activity, "NFC is disabled.", Toast.LENGTH_LONG).show();
                finish();
                return;
            }

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        nfcMger.verifyNFC();
        Intent nfcIntent = new Intent(this, getClass());
        nfcIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, nfcIntent, 0);
        IntentFilter[] intentFiltersArray = new IntentFilter[] {};
        String[][] techList = new String[][] {
                { android.nfc.tech.Ndef.class.getName() },
                { android.nfc.tech.NdefFormatable.class.getName() }
        };
        NfcAdapter nfcAdpt = NfcAdapter.getDefaultAdapter(this);
        nfcAdpt.enableForegroundDispatch(this, pendingIntent, intentFiltersArray, techList);

        nfcAdpt.enableForegroundDispatch(
                this,
                nfcPendingIntent,
                intentFiltersArray,
                null);
        handleIntent(getIntent());
    }

    private void handleIntent(Intent intent) {
        String action = intent.getAction();
        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action)) {

            String type = intent.getType();
            if (MIME_TEXT_PLAIN.equals(type)) {

                Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
                new Home.NdefReaderTask().execute(tag);

            } else {
                //Log.d(TAG, "Wrong mime type: " + type);
            }
        } else if (NfcAdapter.ACTION_TECH_DISCOVERED.equals(action)) {

            // In case we would still use the Tech Discovered Intent
            Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
            String[] techList = tag.getTechList();
            String searchedTech = Ndef.class.getName();

            for (String tech : techList) {
                if (searchedTech.equals(tech)) {
                    new Home.NdefReaderTask().execute(tag);
                    break;
                }
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        NfcAdapter nfcAdpt = NfcAdapter.getDefaultAdapter(this);
        nfcAdpt.disableForegroundDispatch(this);
    }

    public void writeTag(Tag tag, NdefMessage message) {
        if (tag != null) {
            try {
                Ndef ndefTag = Ndef.get(tag);
                if (ndefTag == null)  {
                    // Let's try to format the Tag in NDEF
                    NdefFormatable nForm = NdefFormatable.get(tag);
                    if (nForm != null) {
                        nForm.connect();
                        nForm.format(message);
                        nForm.close();
                    }
                }
                else {
                    ndefTag.connect();
                    ndefTag.writeNdefMessage(message);
                    ndefTag.close();
                }
            }
            catch(Exception e) {
                e.printStackTrace();
            }
        }
    }

    public NdefMessage createTextMessage(String content) {
        try {
            // Get UTF-8 byte
            byte[] lang = Locale.getDefault().getLanguage().getBytes("UTF-8");
            byte[] text = content.getBytes("UTF-8"); // Content in UTF-8

            int langSize = lang.length;
            int textLength = text.length;

            ByteArrayOutputStream payload = new ByteArrayOutputStream(1 + langSize + textLength);
            payload.write((byte) (langSize & 0x1F));
            payload.write(lang, 0, langSize);
            payload.write(text, 0, textLength);
            NdefRecord record = new NdefRecord(NdefRecord.TNF_WELL_KNOWN,
                    NdefRecord.RTD_TEXT, new byte[0],
                    payload.toByteArray());
            return new NdefMessage(new NdefRecord[]{record});
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    private class NdefReaderTask extends AsyncTask<Tag, Void, String> {

        @Override
        protected String doInBackground(Tag... params) {
            Tag tag = params[0];

            Ndef ndef = Ndef.get(tag);
            if (ndef == null) {
                // NDEF is not supported by this Tag.
                return null;
            }

            NdefMessage ndefMessage = ndef.getCachedNdefMessage();

            NdefRecord[] records = ndefMessage.getRecords();
            for (NdefRecord ndefRecord : records) {
                if (ndefRecord.getTnf() == NdefRecord.TNF_WELL_KNOWN && Arrays.equals(ndefRecord.getType(), NdefRecord.RTD_TEXT)) {
                    try {
                        return readText(ndefRecord);
                    } catch (UnsupportedEncodingException e) {
                    }
                }
            }

            return null;
        }

        private String readText(NdefRecord record) throws UnsupportedEncodingException, UnsupportedEncodingException {
        /*
         * See NFC forum specification for "Text Record Type Definition" at 3.2.1
         *
         * http://www.nfc-forum.org/specs/
         *
         * bit_7 defines encoding
         * bit_6 reserved for future use, must be 0
         * bit_5..0 length of IANA language code
         */

            byte[] payload = record.getPayload();

            // Get the Text Encoding
            String textEncoding = ((payload[0] & 128) == 0) ? "UTF-8" : "UTF-16";

            // Get the Language Code
            int languageCodeLength = payload[0] & 0063;

            // String languageCode = new String(payload, 1, languageCodeLength, "US-ASCII");
            // e.g. "en"

            // Get the Text
            return new String(payload, languageCodeLength + 1, payload.length - languageCodeLength - 1, textEncoding);
        }

        @Override
        protected void onPostExecute(String result) {
            AlertDialog alertDialog;
            alertDialog = new AlertDialog.Builder(getApplicationContext()).create();
            alertDialog.setTitle("Item Content Error");

            if (result != null) {
                alertDialog.setMessage("Read content: " + result);
                alertDialog.show();
            }
        }
    }
}

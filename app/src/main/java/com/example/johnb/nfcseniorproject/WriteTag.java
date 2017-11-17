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
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.Locale;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import javax.microedition.khronos.opengles.GL;

/**
 * Created by Johnb on 11/2/2017.
 */

public class WriteTag extends AppCompatActivity {
    private WriteTag.NFCManager nfcMger;
    IntentFilter[] intentFiltersArray;
    PendingIntent nfcPendingIntent;
    public static final String MIME_TEXT_PLAIN = "text/plain";
    EditText tagText;
    ToggleButton tagToggle;
    Integer tagId = -1;
    String toggleText;

    protected void onCreate(Bundle savedInstanceState) {

        final BackroundWorker backroundWorker = new BackroundWorker(getApplicationContext());

        GlobalInformation.getInstance().newMessage = null;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_tag);

        tagText = (EditText) findViewById(R.id.tagText);
        tagToggle = (ToggleButton) findViewById(R.id.tagToggle);
        toggleText = tagToggle.getTextOff().toString();
        //get the spinner from the xml.
        final Spinner dropdown = (Spinner)findViewById(R.id.areaSpinner);
        backroundWorker.execute("getAreaNames");
        try {
            backroundWorker.get(5000, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
        String[] items = GlobalInformation.getInstance().areaNames;
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, items);
        dropdown.setAdapter(adapter);

        nfcMger = new NFCManager(this);
        Intent nfcIntent = new Intent(this, getClass());
        nfcIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);

        nfcPendingIntent = PendingIntent.getActivity(this, 0, nfcIntent, 0);

        IntentFilter tagIntentFilter =
                new IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED);
        try {
            tagIntentFilter.addDataType("text/plain");
            intentFiltersArray = new IntentFilter[]{tagIntentFilter};
        }
        catch (Throwable t) {
            t.printStackTrace();
        }

        tagToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    toggleText = tagToggle.getTextOn().toString();
                } else {
                    toggleText = tagToggle.getTextOff().toString();
                }
            }
        });

        TextView writeButton = (TextView) findViewById(R.id.saveTag);
        writeButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                String name = tagText.getText().toString();
                GlobalInformation.getInstance().newMessage = name;
/*                backroundWorker.execute(toggleText,name,dropdown.getSelectedItem().toString());
                try {
                    backroundWorker.get(50000, TimeUnit.MILLISECONDS);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (TimeoutException e) {
                    e.printStackTrace();
                }*/
            }
        });
    }

    public String parseTag(String rawTagInfo){
        String[] splitTagInfo = rawTagInfo.split(",");
        String tagName = splitTagInfo[1];
        String tempId = splitTagInfo[0];
        try{
            tagId = Integer.parseInt(splitTagInfo[0]);
            if(tagId < 0){
                tagName = "New Tag";
            }
        }catch(NumberFormatException e) {
            tagName = "New Tag";
        } catch(NullPointerException e) {
            tagName = "New Tag";
        }

        return tagName;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        final Spinner dropdown = (Spinner)findViewById(R.id.areaSpinner);
        super.onNewIntent(intent);
        if (intent != null && NfcAdapter.ACTION_NDEF_DISCOVERED.equals(intent.getAction())) {
            Parcelable[] rawMessages =
                    intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
            if (rawMessages != null) {
                NdefMessage[] messages = new NdefMessage[rawMessages.length];
                for (int i = 0; i < rawMessages.length; i++) {
                    messages[i] = (NdefMessage) rawMessages[i];
                    GlobalInformation.getInstance().itemIds[0] = new String(messages[i].getRecords()[0].getPayload());
                    String tagFormat = "\u0002en" + tagText.getText().toString();
                    if(Arrays.asList(GlobalInformation.getInstance().itemIds).contains(tagFormat)){
                        break;
                    }
                    messages[i] = (NdefMessage) rawMessages[i];
                    Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
                    if(GlobalInformation.getInstance().newMessage != null){
                        NdefMessage ndefMessage = createTextMessage(GlobalInformation.getInstance().newMessage);
                        writeTag(tag,ndefMessage);
                        BackroundWorker backroundWorker = new BackroundWorker(getApplicationContext());
                        backroundWorker.execute(toggleText, tagText.getText().toString(),dropdown.getSelectedItem().toString());
                        try {
                            backroundWorker.get(5000, TimeUnit.MILLISECONDS);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        } catch (TimeoutException e) {
                            e.printStackTrace();
                        }
                        break;
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
                new WriteTag.NdefReaderTask().execute(tag);

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
                    new WriteTag.NdefReaderTask().execute(tag);
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

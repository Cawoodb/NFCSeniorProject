package com.example.johnb.nfcseniorproject;

import android.app.Activity;
import android.nfc.NfcAdapter;
import android.widget.Toast;

/**
 * Created by Johnb on 11/20/2017.
 */

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
            activity.finish();
            return;
        }
        if (!nfcAdpt.isEnabled()) {
            Toast.makeText(this.activity, "NFC is disabled.", Toast.LENGTH_LONG).show();
            activity.finish();
            return;
        }

    }
}

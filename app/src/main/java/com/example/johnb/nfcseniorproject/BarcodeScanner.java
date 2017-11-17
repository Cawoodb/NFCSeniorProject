package com.example.johnb.nfcseniorproject;

import android.content.Intent;

/**
 * Created by Johnb on 11/6/2017.
 */

public class BarcodeScanner {

    public String scanResult;

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {

        IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);

        if (scanningResult != null) {

            scanResult = scanningResult.getContents();

        } else {

            // No result
        }
    }
}

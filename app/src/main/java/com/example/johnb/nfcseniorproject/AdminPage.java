package com.example.johnb.nfcseniorproject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

/**
 * Created by Johnb on 11/2/2017.
 */

public class AdminPage extends AppCompatActivity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_page);


        TextView writeButton = (TextView) findViewById(R.id.writeButton);
        writeButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent writeIntent = new Intent(getApplicationContext(),WriteTag.class);
                startActivity(writeIntent);
            }
        });
    }
};


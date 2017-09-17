package com.example.johnb.nfcseniorproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class RegisterUser extends AppCompatActivity {

    EditText userNameInput = (EditText) findViewById(R.id.registerUserName);
    EditText passwordInput = (EditText) findViewById(R.id.registerPassword);
    EditText passwordConfirmationInput = (EditText) findViewById(R.id.registerPasswordConfirmation);
    EditText firstNameInput = (EditText) findViewById(R.id.registerFirstName);
    EditText lastNameInput = (EditText) findViewById(R.id.registerLastName);
    EditText companyRegistrationNumberInput = (EditText) findViewById(R.id.companyRegistrationNumber);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);
        Button registerButton = (Button) findViewById(R.id.registerButton);

        registerButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                OnRegister(v);
            }
        });
    }

    public void OnRegister(View view){
        String userName = userNameInput.getText().toString();
        String password = passwordInput.getText().toString();
        String passwordConfirmation = passwordConfirmationInput.getText().toString();
        String firstName = firstNameInput.getText().toString();
        String lastName = lastNameInput.getText().toString();
        Integer companyRegistrationNumber = Integer.parseInt(companyRegistrationNumberInput.getText().toString());

        String type = "login";
        BackroundWorker backroundWorker = new BackroundWorker(this);

        backroundWorker.execute(type, userName, password);

        /*Intent loginIntent = new Intent(getApplicationContext(),Home.class);
                startActivity(loginIntent);*/
    }
}

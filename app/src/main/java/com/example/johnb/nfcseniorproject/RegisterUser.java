package com.example.johnb.nfcseniorproject;

import android.app.AlertDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class RegisterUser extends AppCompatActivity {

    EditText userNameInput;
    EditText passwordInput;
    EditText passwordConfirmationInput;
    EditText firstNameInput;
    EditText lastNameInput;
    EditText companyRegistrationNumberInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        userNameInput = (EditText) findViewById(R.id.registerUserName);
        passwordInput = (EditText) findViewById(R.id.registerPassword);
        passwordConfirmationInput = (EditText) findViewById(R.id.registerPasswordConfirmation);
        firstNameInput = (EditText) findViewById(R.id.registerFirstName);
        lastNameInput = (EditText) findViewById(R.id.registerLastName);
        companyRegistrationNumberInput = (EditText) findViewById(R.id.companyRegistrationNumber);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);
        Button registerButton = (Button) findViewById(R.id.registerButton);

        registerButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                try {
                    OnRegister(v);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (TimeoutException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void OnRegister(View view) throws InterruptedException, ExecutionException, TimeoutException {
        String userName = ((EditText) findViewById(R.id.registerUserName)).getText().toString();
        String password = ((EditText) findViewById(R.id.registerPassword)).getText().toString();
        String passwordConfirmation = ((EditText) findViewById(R.id.registerPasswordConfirmation)).getText().toString();
        String firstName = ((EditText) findViewById(R.id.registerFirstName)).getText().toString();
        String lastName = ((EditText) findViewById(R.id.registerLastName)).getText().toString();
        Integer companyRegistrationNumber = Integer.parseInt(((EditText) findViewById(R.id.companyRegistrationNumber)).getText().toString());

        String type = "register";
        BackroundWorker backroundWorker = new BackroundWorker(this);

        AlertDialog alertDialog;
        alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle("Registration Status");

        if(password.equals(passwordConfirmation)) {
            backroundWorker.execute(type, userName, password, firstName, lastName, companyRegistrationNumber.toString());
            backroundWorker.get(1000, TimeUnit.MILLISECONDS);
            String result = GlobalInformation.getInstance().queryResult;

            if(result.equals("SUCCESS")){
                Intent loginIntent = new Intent(getApplicationContext(),Login.class);
                startActivity(loginIntent);
            }
            else if(result.equals("FAILURE")){

            }
            else{

            }
        }
        else{
            alertDialog.setMessage("Your password must match your password confirmation");
            alertDialog.show();
        }

        /*Intent loginIntent = new Intent(getApplicationContext(),Home.class);
                startActivity(loginIntent);*/
    }
}

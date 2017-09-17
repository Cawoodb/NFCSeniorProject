package com.example.johnb.nfcseniorproject;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.TextView;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class Login extends AppCompatActivity {

    EditText loginUserName, loginPassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Button logInButton = (Button) findViewById(R.id.logInButton);
        logInButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                try {
                    OnLogin(v);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (TimeoutException e) {
                    e.printStackTrace();
                }
            }
        });

        TextView newUserButton = (TextView) findViewById(R.id.newUserButton);
        newUserButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent loginIntent = new Intent(getApplicationContext(),RegisterUser.class);
                startActivity(loginIntent);
            }
        });

        loginUserName = (EditText)findViewById(R.id.loginUserName);
        loginPassword = (EditText)findViewById(R.id.loginPassword);
    }

    public void OnLogin(View view) throws InterruptedException, ExecutionException, TimeoutException {
        AlertDialog alertDialog;
        String userName = loginUserName.getText().toString();
        String password = loginPassword.getText().toString();
        String type = "login";
        BackroundWorker backroundWorker = new BackroundWorker(this);

        backroundWorker.execute(type, userName, password);
        backroundWorker.get(1000, TimeUnit.MILLISECONDS);
        String result = GlobalInformation.getInstance().queryResult;

        alertDialog = new AlertDialog.Builder(getApplicationContext()).create();
        alertDialog.setTitle("Login Status");

        if(result.equals("ConnectionError")){
            alertDialog.setMessage("Connection error");
            alertDialog.show();
        }else if (result.equals("LoginError") || result.equals("")){
            alertDialog.setMessage("Username and password combination not found");
            alertDialog.show();
        }else{
            GlobalInformation.getInstance().userId = (Integer.parseInt(result));
            Intent loginIntent = new Intent(getApplicationContext(),Home.class);
            startActivity(loginIntent);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

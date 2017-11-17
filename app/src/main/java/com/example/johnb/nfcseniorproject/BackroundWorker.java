package com.example.johnb.nfcseniorproject;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.renderscript.ScriptGroup;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by Johnb on 9/7/2017.
 */

public class BackroundWorker extends AsyncTask<String, Void, Void> {
    Context context;
    BackroundWorker (Context ctx){
        context = ctx;
    }
/*
    AlertDialog alertDialog;
*/

    @Override
    protected Void doInBackground(String... params) {
        String type = params[0];
        String ipAddress = "http://192.168.0.7:8082/";

        if(type.equals("login")){
            try {
                String login_url = ipAddress + "login.php";
                String user_name = params[1];
                String password = params[2];
                URL url = new URL(login_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                httpURLConnection.setConnectTimeout(5000);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));

                String post_data = URLEncoder.encode("user_name","UTF-8") + "=" + URLEncoder.encode(user_name,"UTF-8") + "&"
                        + URLEncoder.encode("password","UTF-8") + "=" + URLEncoder.encode(password,"UTF-8");
                bufferedWriter.write(post_data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();

                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));
                String result = "";
                String line = "";
                while((line = bufferedReader.readLine()) != null){
                    result += line;
                }
                GlobalInformation.getInstance().queryResult = (result);
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (java.net.SocketTimeoutException e){
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else if(type.equals("register")){
            try {
                String register_url = ipAddress + "register.php";
                String user_name = params[1];
                String password = params[2];
                String firstName = params[3];
                String lastName = params[4];
                String companyRegistrationNumber = params[5];
                URL url = new URL(register_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));

                String post_data = URLEncoder.encode("user_name","UTF-8") + "=" + URLEncoder.encode(user_name,"UTF-8") + "&"
                        + URLEncoder.encode("password","UTF-8") + "=" + URLEncoder.encode(password,"UTF-8") + "&"
                        + URLEncoder.encode("first_name","UTF-8") + "=" + URLEncoder.encode(firstName,"UTF-8") + "&"
                        + URLEncoder.encode("last_name","UTF-8") + "=" + URLEncoder.encode(lastName,"UTF-8") + "&"
                        + URLEncoder.encode("company_number","UTF-8") + "=" + URLEncoder.encode(companyRegistrationNumber,"UTF-8");
                bufferedWriter.write(post_data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();

                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));
                String result = "";
                String line = "";
                while((line = bufferedReader.readLine()) != null){
                    result += line;
                }
                GlobalInformation.getInstance().queryResult = (result);
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else if(type.equals("checkArea")){
            try {
                String check_area_url = ipAddress + "checkArea.php";
                String area_id = params[1];
                URL url = new URL(check_area_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));

                String post_data = URLEncoder.encode("area_id","UTF-8") + "=" + URLEncoder.encode(area_id,"UTF-8");
                bufferedWriter.write(post_data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();

                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));
                String result = "";
                String line = "";
                while((line = bufferedReader.readLine()) != null){
                    result += line;
                }
                GlobalInformation.getInstance().queryResult = (result);
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else if(type.equals("getItems")){
            try {
                String check_area_url = ipAddress + "getItems.php";
                String start_position = params[1];
                URL url = new URL(check_area_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));

                String post_data = URLEncoder.encode("start_position","UTF-8") + "=" + URLEncoder.encode(start_position,"UTF-8");
                bufferedWriter.write(post_data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();

                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));
                String result = "";
                String line = "";
                while((line = bufferedReader.readLine()) != null){
                    result += line;
                }
                GlobalInformation.getInstance().queryResult = (result);
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else if(type.equals("area")){
            try {
                String check_area_url = ipAddress + "area.php";
                String area_name = params[1];
                URL url = new URL(check_area_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));

                String post_data = URLEncoder.encode("area_name","UTF-8") + "=" + URLEncoder.encode(area_name,"UTF-8");
                bufferedWriter.write(post_data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();

                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));
                String result = "";
                String line = "";
                while((line = bufferedReader.readLine()) != null){
                    result += line;
                }
                GlobalInformation.getInstance().queryResult = (result);
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else if(type.equals("item")){
            try {
                String check_area_url = ipAddress + "item.php";
                String item_name = params[1];
                String area_name = params[2];
                URL url = new URL(check_area_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));

                String post_data = URLEncoder.encode("area_name","UTF-8") + "=" + URLEncoder.encode(area_name,"UTF-8") + "&"
                        + URLEncoder.encode("item_name","UTF-8") + "=" + URLEncoder.encode(item_name,"UTF-8");
                bufferedWriter.write(post_data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();

                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));
                String result = "";
                String line = "";
                while((line = bufferedReader.readLine()) != null){
                    result += line;
                }
                GlobalInformation.getInstance().queryResult = (result);
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else if(type.equals("getAreaNames")) {
            try {
                String check_area_url = ipAddress + "getAreaNames.php";
                URL url = new URL(check_area_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("GET");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                outputStream.close();

                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));
                String result = "";
                String line = "";
                while ((line = bufferedReader.readLine()) != null) {
                    result += line;
                }
                GlobalInformation.getInstance().queryResult = (result);
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                GlobalInformation.getInstance().areaNames = GlobalInformation.getInstance().queryResult.split(",");
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else if(type.equals("getAreaId")){
            try {
                String check_area_url = ipAddress + "getAreaId.php";
                String area_name = params[1];
                URL url = new URL(check_area_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));

                String post_data = URLEncoder.encode("area_name","UTF-8") + "=" + URLEncoder.encode(area_name,"UTF-8");
                bufferedWriter.write(post_data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();

                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));
                String result = "";
                String line = "";
                while((line = bufferedReader.readLine()) != null){
                    result += line;
                }
                GlobalInformation.getInstance().queryResult = (result);
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    protected void onPreExecute() {
        /*alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setTitle("Login Status");*/
    }

    public void scanBarcode(int requestCode, int resultCode, Intent intent) {
        /**
         * Retrieve scan results
         */

//        IntentIntegrator scanBarcode = new IntentIntegrator(BackroundWorker.this);
//        IntentIntegrator.initiateScan();

//        IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
//        if (scanningResult != null) {
//            // There's a result
//            String strBarcodeNumber = scanningResult.getContents();
//            try {
//                strBarcodeNumber = URLEncoder.encode(strBarcodeNumber, "UTF-8");
//                return strBarcodeNumber;
//            } catch (/*UnsupportedEncoding*/Exception e) {
//                e.printStackTrace();
//            }
//        } else {
//            //No result
//        }
//        return "Failed";
    }

    @Override
    protected void onPostExecute(Void result) {
        /*alertDialog.setMessage(result);
        alertDialog.show();*/
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }
}

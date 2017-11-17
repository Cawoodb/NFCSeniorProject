package com.example.johnb.nfcseniorproject;

import android.app.Application;

import java.util.Date;

/**
 * Created by Johnb on 9/7/2017.
 */

public class GlobalInformation{
    private static GlobalInformation mInstance = null;

    public int userId = 0;
    public String queryResult = "";
    public int areaId = 0;
    public String[] itemIds = new String[25];
    public String newMessage;
    public String[] areaNames = new String[25];

    protected GlobalInformation(){}

    public static synchronized GlobalInformation getInstance(){
        if(null == mInstance){
            mInstance = new GlobalInformation();
        }
        return mInstance;
    }
}

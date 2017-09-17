package com.example.johnb.nfcseniorproject;

import android.app.Application;

/**
 * Created by Johnb on 9/7/2017.
 */

public class GlobalInformation{
    private static GlobalInformation mInstance = null;

    public int userId = 0;
    public String queryResult = "";

    protected GlobalInformation(){}

    public static synchronized GlobalInformation getInstance(){
        if(null == mInstance){
            mInstance = new GlobalInformation();
        }
        return mInstance;
    }
}

package com.hesham.sawarstudent.networkmodule;

import android.content.Context;


import com.hesham.sawarstudent.networkmodule.ApiRequest;
import com.hesham.sawarstudent.networkmodule.NetworkManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by ayman on 2019-05-13.
 */

public class Apiservice {
    public static Apiservice newInstance=null;
    public ApiRequest apiRequest = NetworkManager.getInstance().createService("a");
    private Apiservice(){}

    public static Apiservice getInstance(){
        if (newInstance == null) {
            synchronized (Apiservice.class) {
                if (newInstance == null) {
                    newInstance = new Apiservice();
                }
            }
        }
        return newInstance;
    }
}

package com.hesham.sawarstudent.networkmodule;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;
import android.widget.Toast;


/**
 * Created by ayman on 2018-01-15.
 */

public final class NetworkUtilities {

    private static String baseUrl="";

    public static void networkConnectionFailure(Context context){
        if (context!= null){
//            progressBar.setVisibility(View.GONE);
            Toast.makeText(context, "خطا فالتحميل ", Toast.LENGTH_SHORT).show();
        }
    }

    //check if mobile is connected to network or not
    public static boolean isOnline(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    public static boolean isFast(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo network = cm.getActiveNetworkInfo();
        int subType = network.getSubtype();
        int type = network.getType();
        if(type == ConnectivityManager.TYPE_WIFI){
            return true;

        } else if(type == ConnectivityManager.TYPE_MOBILE){

            switch(subType){
                case TelephonyManager.NETWORK_TYPE_1xRTT:
                    return false; // ~ 50-100 kbps
                case TelephonyManager.NETWORK_TYPE_CDMA:
                    return false; // ~ 14-64 kbps
                case TelephonyManager.NETWORK_TYPE_EDGE:
                    return false; // ~ 50-100 kbps
                case TelephonyManager.NETWORK_TYPE_EVDO_0:
                    return true; // ~ 400-1000 kbps
                case TelephonyManager.NETWORK_TYPE_EVDO_A:
                    return true; // ~ 600-1400 kbps
                case TelephonyManager.NETWORK_TYPE_GPRS:
                    return false; // ~ 100 kbps
                case TelephonyManager.NETWORK_TYPE_HSDPA:
                    return true; // ~ 2-14 Mbps
                case TelephonyManager.NETWORK_TYPE_HSPA:
                    return true; // ~ 700-1700 kbps
                case TelephonyManager.NETWORK_TYPE_HSUPA:
                    return true; // ~ 1-23 Mbps
                case TelephonyManager.NETWORK_TYPE_UMTS:
                    return true; // ~ 400-7000 kbps

                // Above API level 7, make sure to set android:targetSdkVersion to appropriate level to use these

                case TelephonyManager.NETWORK_TYPE_EHRPD: // API level 11
                    return true; // ~ 1-2 Mbps
                case TelephonyManager.NETWORK_TYPE_EVDO_B: // API level 9
                    return true; // ~ 5 Mbps
                case TelephonyManager.NETWORK_TYPE_HSPAP: // API level 13
                    return true; // ~ 10-20 Mbps
                case TelephonyManager.NETWORK_TYPE_IDEN: // API level 8
                    return false; // ~25 kbps
                case TelephonyManager.NETWORK_TYPE_LTE: // API level 11
                    return true; // ~ 10+ Mbps
                // Unknown
                case TelephonyManager.NETWORK_TYPE_UNKNOWN:
                default:
                    return false;
            }
        } else {
            return false;
        }

    }


    public static void onResponseFailure(Context context,String errorMsg){
        if (context!= null){
//            progressBar.setVisibility(View.GONE);
            Toast.makeText(context, errorMsg, Toast.LENGTH_SHORT).show();
        }
    }

    public static String getBaseUrl() {

        return baseUrl;
    }

    public static void setBaseUrl(String baseUrl) {
        NetworkUtilities.baseUrl = baseUrl;
    }
}

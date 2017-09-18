package com.axxess.yongjili.mylist.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by yongjili on 9/16/17.
 */

/**
 * This class has a set of helper methods in charge of updating the user of the
 * device's network status
 */

public class NetworkUtils {
    /**
     * Constant that represents the type of connection, this one
     * represents WIFI type.
     */
    public static int TYPE_WIFI = 1;

    /**
     * This constant represents that the device is running on the internet
     * from the data provider.
     */
    public static int TYPE_MOBILE = 2;

    /**
     * This constant represents that the device is not currently connected to
     * any internet access.
     */
    public static int TYPE_NOT_CONNECTED = 0;

    /**
     * These constants represents the status of the connection to the internet.
     */
    public static final int NETWORK_STATUS_NOT_CONNECTED = 0,
            NETWORK_STATUS_WIFI = 1,
            NETWORK_STATUS_MOBILE = 2;

    /**
     * Returns an integer that represents the value of the connection state of the
     * device.
     * @param context The context calling the current method.
     * @return Returns an integer value that represents whether the device is connected or
     * isn't connected.
     */
    public static int getConnectivityStatus(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (null != activeNetwork) {
            if(activeNetwork.getType() == ConnectivityManager.TYPE_WIFI)
                return TYPE_WIFI;

            if(activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE)
                return TYPE_MOBILE;
        }
        return TYPE_NOT_CONNECTED;
    }

    /**
     * This method checks whether the current device is or isn't connected to the internet.
     * @param context The context calling this method.
     * @return True if the device is connected to the internet, false otherwise.
     */
    public static boolean hasConnection(Context context){
        return getConnectivityStatus(context) != TYPE_NOT_CONNECTED;
    }

    /**
     * Same as the previously described method.
     * @param context The context calling this function
     * @return
     */
    public static int getConnectivityStatusString(Context context) {
        int conn = NetworkUtils.getConnectivityStatus(context);
        int status = 0;
        if (conn == NetworkUtils.TYPE_WIFI) {
            status = NETWORK_STATUS_WIFI;
        } else if (conn == NetworkUtils.TYPE_MOBILE) {
            status =NETWORK_STATUS_MOBILE;
        } else if (conn == NetworkUtils.TYPE_NOT_CONNECTED) {
            status = NETWORK_STATUS_NOT_CONNECTED;
        }
        return status;
    }
}

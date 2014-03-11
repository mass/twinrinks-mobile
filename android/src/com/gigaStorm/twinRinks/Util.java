
package com.gigaStorm.twinRinks;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

/**
 * <code>Util</code> provides several utility functions which will appear
 * frequently throughout the code.
 * 
 * @author Andrew Mass
 */
public class Util {

    /**
     * The context of the parent activity, used to interact with the application
     * from a non-android class.
     */
    private Context parentContext;

    public Util(Context context) {
        this.parentContext = context;
    }

    /**
     * Toasts the specified message in the parent activity context.
     * 
     * @param message the message to be toasted.
     * @see Toast
     */
    public void toast(String message) {
        Toast toast = Toast.makeText(parentContext, message, Toast.LENGTH_LONG);
        toast.show();
    }

    /**
     * Toasts the message and outputs it to the standard error stream.
     * 
     * @param error the error to be toasted and logged.
     */
    public void err(String error) {
        toast(error);
        System.err.println(error);
    }

    /**
     * Checks to make sure we have a functioning Internet connection.
     * 
     * @return a boolean relating whether this device has a valid Internet
     *         connection.
     */
    public boolean checkInternet() {
        ConnectivityManager connec = (ConnectivityManager) parentContext
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifi = connec.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo mobile = connec.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

        return (wifi != null && wifi.isConnected() || mobile != null
                & mobile.isConnected());
    }
}

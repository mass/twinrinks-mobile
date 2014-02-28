package com.gigaStorm.twinRinks;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

/**
<<<<<<< HEAD
 * <code>Util</code> provides several utility functions which will appear frequently throughout the
 * code.
=======
 * <code>Util</code> provides several utility functions which will appear
 * frequently throughout the code.
>>>>>>> 188a691... Fix data fetch bug.
 * 
 * @author Andrew Mass
 */
public class Util {

  /**
<<<<<<< HEAD
   * The context of the parent activity, used to interact with the application from a non-android
   * class.
=======
   * The context of the parent activity, used to interact with the application
   * from a non-android class.
>>>>>>> 188a691... Fix data fetch bug.
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
<<<<<<< HEAD
   * @return a boolean relating whether this device has a valid Internet connection.
=======
   * @return a boolean relating whether this device has a valid Internet
   *         connection.
>>>>>>> 188a691... Fix data fetch bug.
   */
  public boolean checkInternet() {
    ConnectivityManager connec = (ConnectivityManager) parentContext
        .getSystemService(Context.CONNECTIVITY_SERVICE);
    NetworkInfo wifi = connec.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
    NetworkInfo mobile = connec.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

<<<<<<< HEAD
    return (wifi != null && wifi.isConnected() || mobile != null & mobile.isConnected());
=======
    return (wifi != null && wifi.isConnected() || mobile != null
        & mobile.isConnected());
>>>>>>> 188a691... Fix data fetch bug.
  }
}

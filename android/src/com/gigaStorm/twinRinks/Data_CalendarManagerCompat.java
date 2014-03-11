package com.gigaStorm.twinRinks;

import android.content.Context;

/**
 * <code>Data_CalendarManagerCompat</code> handles addition of games to a user's calendar on a
 * pre-ICS device.
 * <p>
 * Note: This feature is not yet implemented.
 * </p>
 * 
 * @author Andrew Mass
 */
public class Data_CalendarManagerCompat {

  private Util util;

  public Data_CalendarManagerCompat(Context context) {
    this.util = new Util(context);
  }

  public void saveGamesToCalendar() {
    util.toast("This feature is not currently supported in this version of android");
  }
}

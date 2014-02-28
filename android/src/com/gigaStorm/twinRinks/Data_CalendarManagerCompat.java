package com.gigaStorm.twinRinks;

import android.content.Context;
import android.widget.Toast;

// Class which handles addition of games to a user's calendar on pre-ICS devices
public class Data_CalendarManagerCompat {
  private Context context;

  public Data_CalendarManagerCompat(Context context) {
    this.context = context;
  }

  public void saveGamesToCalendar() {
    toast("This feature is not currently supported in this version of android");
  }

  public void toast(Object obj) {
    Toast toast = Toast.makeText(context, obj.toString(), Toast.LENGTH_LONG);
    toast.show();
  }
}

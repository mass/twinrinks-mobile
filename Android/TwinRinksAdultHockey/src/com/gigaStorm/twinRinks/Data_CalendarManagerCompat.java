package com.gigaStorm.twinRinks;

import android.content.Context;
import android.widget.Toast;

public class Data_CalendarManagerCompat {
    private Context context;

    // Default constructor for a new instance of MemoryManager
    public Data_CalendarManagerCompat(Context context) {
	this.context = context;
    }

    public void saveGamesToCalendar() {
	toast("Not yet supported on this version of android");
    }

    public void toast(Object obj) {
	Toast toast = Toast.makeText(context, obj.toString(), Toast.LENGTH_LONG);
	toast.show();
    }
}
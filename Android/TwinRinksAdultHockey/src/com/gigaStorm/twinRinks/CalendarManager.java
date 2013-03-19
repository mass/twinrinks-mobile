package com.gigaStorm.twinRinks;

import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CalendarContract;
import android.util.Log;
import android.widget.Toast;

public class CalendarManager {
    private Context context;
    private MemoryManager memoryManager;

    // Default constructor for a new instance of MemoryManager
    public CalendarManager(Context context) {
	this.context = context;
	memoryManager = new MemoryManager(context);
    }

    public void saveGamesToCalendar() {
	showCalendarPopup();
    }

    private void loopThroughGames(int whichCalendar) {
	ArrayList<Game> games = memoryManager.getGamesFromMemory();

	// for(Game e: games)
	Log.d("Game", games.get(100).toString());
	addGameToCalendar(games.get(100), whichCalendar);

	Log.d("ID", "" + whichCalendar);
    }

    private void addGameToCalendar(Game game,int whichCalendar) {
	ContentResolver cr = context.getContentResolver();
	ContentValues values = new ContentValues();

	try {
	    values.put(CalendarContract.Events.CALENDAR_ID, whichCalendar);
	    values.put(CalendarContract.Events.TITLE, "Hockey- " + game.getLeague() + ": " + game.getTeamH() + " vs " + game.getTeamA());
	    values.put(CalendarContract.Events.EVENT_LOCATION, "Twin Rinks Ice Arena- " + game.getRink() + " Rink");
	    values.put(CalendarContract.Events.DTSTART, game.getCalendarObject().getTimeInMillis());
	    values.put(CalendarContract.Events.DTEND, game.getCalendarObject().getTimeInMillis() + 5400000);
	    values.put(CalendarContract.Events.ALL_DAY, 0);
	    values.put(CalendarContract.Events.EVENT_TIMEZONE, TimeZone.getDefault().getID());

	    cr.insert(CalendarContract.Events.CONTENT_URI, values);
	} catch(Exception e) {
	    Log.e("Error", e.getMessage());
	    toast(e.getMessage());
	}
    }

    private void showCalendarPopup() {
	final ContentResolver cr;
	final Cursor result;
	final Uri uri;
	List<String> listCals = new ArrayList<String>();
	final String[] projection = new String[] {CalendarContract.Calendars._ID, CalendarContract.Calendars.ACCOUNT_NAME, CalendarContract.Calendars.CALENDAR_DISPLAY_NAME, CalendarContract.Calendars.NAME,};

	uri = CalendarContract.Calendars.CONTENT_URI;

	cr = context.getContentResolver();
	result = cr.query(uri, projection, null, null, null);
	if(result.getCount() > 0 && result.moveToFirst()) {
	    do {
		listCals.add(result.getString(result.getColumnIndex(CalendarContract.Calendars.NAME)));
	    } while(result.moveToNext());
	}
	CharSequence[] calendars = listCals.toArray(new CharSequence[listCals.size()]);

	AlertDialog.Builder builder = new AlertDialog.Builder(context);
	builder.setTitle("Calendar to use:");
	builder.setItems(calendars, new DialogInterface.OnClickListener() {
	    @Override
	    public void onClick(DialogInterface dialog,int itemCal) {
		Log.d("SettingsActivity", "CalendarID: " + itemCal);
		loopThroughGames(itemCal);
	    };
	});
	AlertDialog alert = builder.create();
	alert.show();
    }

    public void toast(Object obj) {
	Toast toast = Toast.makeText(context, obj.toString(), Toast.LENGTH_LONG);
	toast.show();
    }
}
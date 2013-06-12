
package com.gigaStorm.twinRinks;

import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.net.Uri;
import android.provider.BaseColumns;
import android.provider.CalendarContract;
import android.widget.Toast;

//Class which handles the addition of events to a user's calendar
@SuppressLint({
        "InlinedApi", "NewApi"
})
public class Data_CalendarManager {
    private Context context;

    private Data_MemoryManager memoryManager;

    public Data_CalendarManager(Context context) {
        this.context = context;
        memoryManager = new Data_MemoryManager(context);
    }

    public void saveGamesToCalendar() {
        showCalendarPopup();
    }

    private void showCalendarPopup() {
        final ContentResolver cr;
        final Cursor result;
        final Uri uri;
        List<String> listCals = new ArrayList<String>();
        final String[] projection = new String[] {
                BaseColumns._ID, CalendarContract.Calendars.ACCOUNT_NAME,
                CalendarContract.Calendars.CALENDAR_DISPLAY_NAME, CalendarContract.Calendars.NAME,
        };

        uri = CalendarContract.Calendars.CONTENT_URI;

        cr = context.getContentResolver();
        result = cr.query(uri, projection, null, null, null);
        if (result.getCount() > 0 && result.moveToFirst()) {
            do {
                listCals.add(result.getString(result
                        .getColumnIndex(CalendarContract.Calendars.NAME)));
            } while (result.moveToNext());
        }
        CharSequence[] calendars = listCals.toArray(new CharSequence[listCals.size()]);

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Calendar to use:");
        builder.setItems(calendars, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int itemCal) {
                loopThroughGames(itemCal);
            };
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    private void loopThroughGames(int whichCalendar) {
        ArrayList<Model_Game> games = memoryManager.getGames();

        for (Model_Game e1 : games)
            for (Model_Team e : memoryManager.getYourTeams())
                if ((e1.getTeamA().equalsIgnoreCase(e.getTeamName()) || e1.getTeamH()
                        .equalsIgnoreCase(e.getTeamName()))
                        && e1.getLeague().equalsIgnoreCase(e.getLeague()))
                    if (!e1.hasPassed())
                        addGameToCalendar(e1, whichCalendar + 1);
    }

    private void addGameToCalendar(Model_Game game, int whichCalendar) {
        ContentResolver cr = context.getContentResolver();
        ContentValues values = new ContentValues();
        try {
            values.put(CalendarContract.Events.CALENDAR_ID, whichCalendar);
            values.put(
                    CalendarContract.Events.TITLE,
                    "Hockey- " + game.getLeague() + ": " + game.getTeamH() + " vs "
                            + game.getTeamA());
            values.put(CalendarContract.Events.EVENT_LOCATION,
                    "Twin Rinks Ice Arena - " + game.getRink() + " Rink");
            values.put(CalendarContract.Events.DTSTART, game.getCalendarObject().getTimeInMillis());
            values.put(CalendarContract.Events.DTEND,
                    game.getCalendarObject().getTimeInMillis() + 5400000);
            values.put(CalendarContract.Events.EVENT_TIMEZONE, TimeZone.getDefault().getID());
            cr.insert(CalendarContract.Events.CONTENT_URI, values);
        } catch (Exception e) {
            toast(e.getMessage());
            e.printStackTrace();
        }
    }

    public void toast(Object obj) {
        Toast toast = Toast.makeText(context, obj.toString(), Toast.LENGTH_LONG);
        toast.show();
    }
}

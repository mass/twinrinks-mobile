package com.gigaStorm.twinRinks;

import java.util.Calendar;
import java.util.TimeZone;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.provider.CalendarContract;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

// Custom view class for game data
public class GameDisplay extends RelativeLayout {
	private Context context;
	private TextView leagueView;
	private TextView awayView;
	private TextView rinkView;
	private TextView homeView;
	private TextView dateView;
	private int year;
	private int month;
	private int day;
	private int hour;
	private int minutes;
	private static final String PREF_GROUP = "TwinRinks";
	private static final String PREF_NAME = "calendarID";
	private SharedPreferences pref;
	private String calID;

	public GameDisplay(Context context) {
		super(context);
		this.context = context;
		init();
	}

	public GameDisplay(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		init();
	}

	public GameDisplay(Context context, AttributeSet attrs, int default_style) {
		super(context, attrs, default_style);
		this.context = context;
		init();
	}

	private void init() {
		LayoutInflater layoutInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		layoutInflater.inflate(R.layout.gameview, this);
		leagueView = (TextView) this.findViewById(R.id.leagueView);
		dateView = (TextView) this.findViewById(R.id.dateView);
		awayView = (TextView) this.findViewById(R.id.awayView);
		rinkView = (TextView) this.findViewById(R.id.rinkView);
		homeView = (TextView) this.findViewById(R.id.homeView);
		final Button dispB = (Button) findViewById(R.id.addCal);

		pref = this.context.getSharedPreferences(PREF_GROUP, 0);
		calID = pref.getString(PREF_NAME, null).toString();

		Log.d("GameDisplay", "CalID = " + calID);

		dispB.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				ContentResolver cr = context.getContentResolver();
				ContentValues values = new ContentValues();
				/*
				 * String calendarID_pref = PreferenceManager
				 * .getDefaultSharedPreferences(context).getString(
				 * "CalendarID", "NullAndVoid");
				 */
				TimeZone timeZone = TimeZone.getDefault();
				String title = leagueView.getText() + ": " + awayView.getText()
						+ " vs. " + homeView.getText();
				long startTime = 0;
				try {
					Calendar c = Calendar.getInstance();
					c.set(year, month - 1, day, hour + 12, minutes, 0);
					startTime = c.getTimeInMillis();
					values.put(CalendarContract.Events.CALENDAR_ID,
							Integer.parseInt(calID));
					values.put(CalendarContract.Events.TITLE, title);
					values.put(CalendarContract.Events.EVENT_LOCATION,
							"Twin Rinks");
					values.put(CalendarContract.Events.DTSTART, startTime);
					// All games are assumed to be 1.5 hours. Which means we
					// need to
					// add 1hour*90mins*60secs*1000msecs to the start time
					values.put(CalendarContract.Events.DTEND,
							startTime + 5400000);
					values.put(CalendarContract.Events.ALL_DAY, 0);
					values.put(CalendarContract.Events.EVENT_TIMEZONE,
							timeZone.getID());

					cr.insert(CalendarContract.Events.CONTENT_URI, values);
					CharSequence s = (CharSequence) "Event has been added to the calendar";
					Toast toast = Toast.makeText(context, s, Toast.LENGTH_LONG);
					toast.show();
				} catch (Exception e) {
					CharSequence s = (CharSequence) e.getMessage();
					Log.e("GameDisplay", (String) s);
					Toast toast = Toast.makeText(context, s, Toast.LENGTH_LONG);
					toast.show();
				}
			}
		});
	}

	public void setGame(Game g) {
		leagueView.setText(g.getLeague());
		dateView.setText(g.getFullDateString());
		awayView.setText(g.getTeamA());
		rinkView.setText(g.getRink() + " Rink");
		homeView.setText(g.getTeamH());
		year = g.getYear();
		month = g.getMonth();
		day = g.getDay();
		hour = g.getHour();
		minutes = g.getMinutes();
	}
}

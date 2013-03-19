package com.gigaStorm.twinRinks;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceCategory;
import android.provider.CalendarContract;
import android.text.InputType;
import android.util.Log;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockPreferenceActivity;

// Activity which allows the user to choose settings
public class SettingsActivity extends SherlockPreferenceActivity {

	private PreferenceCategory addTeamCategory;
	private ArrayList<Team> yourTeams;
	private ArrayList<Team> allTeams;
	private MemoryManager memoryManager;
	private Preference addNewTeamPref;
	private Integer calendarID;
	// private String calendarName;
	private CharSequence[] calendars;
	private static final String PREF_NAME = "calendarID";
	private static final String PREF_GROUP = "TwinRinks";

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Adds the settings XML to the activity
		addPreferencesFromResource(R.xml.settings_main);
		/*
		 * actionBar = getSupportActionBar();
		 * actionBar.setHomeButtonEnabled(true);
		 * actionBar.setDisplayShowTitleEnabled(false);
		 */
		addTeamCategory = (PreferenceCategory) findPreference("addTeamCategory");
		addTeamCategory.setOrderingAsAdded(false);

		addNewTeamPref = findPreference("addNewTeamPref");
		addNewTeamPref.setOrder(999);
		addNewTeamPref
				.setOnPreferenceClickListener(new OnPreferenceClickListener() {
					@Override
					public boolean onPreferenceClick(Preference preference) {
						showAddTeamPopup();
						return true;
					}
				});

		memoryManager = new MemoryManager(this);
		yourTeams = memoryManager.getYourTeamsFromMemory();
		allTeams = memoryManager.getAllTeamsFromMemory();
		updatePreferencesFromTeams();

		ListPreference calendarID_pref = (ListPreference) findPreference("calendarID");
		final ContentResolver cr;
		final Cursor result;
		final Uri uri;
		List<String> listCals = new ArrayList<String>();
		final String[] projection = new String[] {
				CalendarContract.Calendars._ID,
				CalendarContract.Calendars.ACCOUNT_NAME,
				CalendarContract.Calendars.CALENDAR_DISPLAY_NAME,
				CalendarContract.Calendars.NAME, };

		uri = CalendarContract.Calendars.CONTENT_URI;

		cr = this.getContentResolver();
		result = cr.query(uri, projection, null, null, null);
		if (result.getCount() > 0 && result.moveToFirst()) {
			do {
				listCals.add(result.getString(result
						.getColumnIndex(CalendarContract.Calendars.NAME)));
			} while (result.moveToNext());
		}
		calendarID_pref.setEntries((CharSequence[]) listCals
				.toArray(new CharSequence[listCals.size()]));

		calendarID_pref.setEntryValues((CharSequence[]) listCals
				.toArray(new CharSequence[listCals.size()]));

		calendarID_pref
				.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
					@Override
					public boolean onPreferenceChange(Preference pref,
							Object newValue) {
						final ListPreference listPref = (ListPreference) pref;
						final int idx = listPref
								.findIndexOfValue((String) newValue);

						calendarID = idx + 1;
						getSharedPreferences(PREF_GROUP, MODE_PRIVATE).edit()
								.putString(PREF_NAME, calendarID.toString())
								.commit();
						return true;
					}
				});

		final EditTextPreference autoLogInUsername = (EditTextPreference) findPreference("autoLogInUsername");
		final EditTextPreference autoLogInPassword = (EditTextPreference) findPreference("autoLogInPassword");

		autoLogInUsername.getEditText().setInputType(
				InputType.TYPE_CLASS_TEXT
						| InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
		autoLogInPassword.getEditText().setInputType(
				InputType.TYPE_CLASS_TEXT
						| InputType.TYPE_TEXT_VARIATION_PASSWORD);

		final CheckBoxPreference autoLogInCheckBox = (CheckBoxPreference) findPreference("autoLogInCheckbox");
		autoLogInCheckBox
				.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
					public boolean onPreferenceChange(Preference preference,
							Object newValue) {
						if (autoLogInCheckBox.isChecked()) {
							autoLogInPassword.setEnabled(false);
							autoLogInUsername.setEnabled(false);
						} else {
							autoLogInPassword.setEnabled(true);
							autoLogInUsername.setEnabled(true);
						}
						return true;
					}
				});

		if (autoLogInCheckBox.isChecked() == false) {
			autoLogInPassword.setEnabled(false);
			autoLogInUsername.setEnabled(false);
		} else {
			autoLogInPassword.setEnabled(true);
			autoLogInUsername.setEnabled(true);
		}
	}

	@Override
	public void onBackPressed() {
		memoryManager.saveYourTeamsToMemory(yourTeams);
		super.onBackPressed();
	}

	@Override
	protected void onDestroy() {
		memoryManager.saveYourTeamsToMemory(yourTeams);
		super.onDestroy();
	}

	public void updatePreferencesFromTeams() {
		addTeamCategory.removeAll();
		for (int i = 0; i < yourTeams.size(); i++) {
			addTeamCategory.addPreference(getNewPreference(yourTeams.get(i)
					.getLeague(), yourTeams.get(i).getTeamName()));
		}
		addTeamCategory.addPreference(addNewTeamPref);
	}

	public void addNewTeam(String league, String team) {
		if (!alreadyEntered(league, team)) {
			yourTeams.add(new Team(league, team));
			addTeamCategory.addPreference(getNewPreference(league, team));
		} else
			toast("Team already entered");
	}

	public CharSequence[] getTeamsFromLeague(String league) {
		ArrayList<String> temp = new ArrayList<String>();
		for (Team e : allTeams)
			if (e.getLeague().equalsIgnoreCase(league))
				temp.add(e.getTeamName());

		CharSequence[] toReturn = new CharSequence[temp.size()];
		for (int i = 0; i < temp.size(); i++)
			toReturn[i] = temp.get(i);
		return toReturn;
	}

	public boolean alreadyEntered(String league, String team) {
		for (int i = 0; i < yourTeams.size(); i++)
			if (yourTeams.get(i).getLeague().equals(league)
					&& yourTeams.get(i).getTeamName().equals(team))
				return true;
		return false;
	}

	public Preference getNewPreference(String leagueP, String teamP) {
		final Context context = this;
		final String league = leagueP;
		final String team = teamP;

		Preference newPref = new Preference(this);
		newPref.setTitle(league + "-" + team);
		newPref.setOrder(addTeamCategory.getPreferenceCount() - 1);

		newPref.setOnPreferenceClickListener(new OnPreferenceClickListener() {
			@Override
			public boolean onPreferenceClick(final Preference preference) {
				AlertDialog.Builder builder = new AlertDialog.Builder(context);
				builder.setMessage("Remove this team?");
				builder.setCancelable(false);
				builder.setPositiveButton("Yes",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								yourTeams.remove(getIndexOfTeam(league, team));
								addTeamCategory.removePreference(preference);
							}
						});
				builder.setNegativeButton("No",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								dialog.cancel();
							}
						});
				AlertDialog alert = builder.create();
				alert.show();
				return true;
			}
		});
		return newPref;
	}

	public int getIndexOfTeam(String league, String name) {
		for (int i = 0; i < yourTeams.size(); i++)
			if (yourTeams.get(i).getLeague().equals(league)
					&& yourTeams.get(i).getTeamName().equals(name))
				return i;
		return -1;
	}

	public void showAddTeamPopup() {
		final CharSequence[] leagues = getResources().getTextArray(
				R.array.leagues);
		final Context context = this;

		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle("Select your league:");
		builder.setItems(leagues, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, final int itemLeague) {
				final CharSequence[] teams = getTeamsFromLeague(leagues[itemLeague]
						.toString());
				AlertDialog.Builder builder2 = new AlertDialog.Builder(context);
				builder2.setTitle("Select your team:");
				builder2.setItems(teams, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int itemTeam) {
						addNewTeam(
								getResources().getTextArray(R.array.leagues)[itemLeague]
										.toString(), teams[itemTeam].toString());
					}
				});
				AlertDialog alert2 = builder2.create();
				alert2.show();
			}
		});
		AlertDialog alert = builder.create();
		alert.show();
	}

	public void showListCalendarsPopup() {
		final ContentResolver cr;
		final Cursor result;
		final Uri uri;
		List<String> listCals = new ArrayList<String>();
		final String[] projection = new String[] {
				CalendarContract.Calendars._ID,
				CalendarContract.Calendars.ACCOUNT_NAME,
				CalendarContract.Calendars.CALENDAR_DISPLAY_NAME,
				CalendarContract.Calendars.NAME, };

		uri = CalendarContract.Calendars.CONTENT_URI;

		cr = this.getContentResolver();
		result = cr.query(uri, projection, null, null, null);
		if (result.getCount() > 0 && result.moveToFirst()) {
			do {
				listCals.add(result.getString(result
						.getColumnIndex(CalendarContract.Calendars.NAME)));
			} while (result.moveToNext());
		}
		calendars = listCals.toArray(new CharSequence[listCals.size()]);

		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Calendar to use:");
		builder.setItems(calendars, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int itemCal) {
				calendarID = itemCal;
				Log.d("SettingsActivity", "CalendarID: " + calendarID);
			};
		});
		AlertDialog alert = builder.create();
		alert.show();

	}

	public void toast(String desc) {
		Toast toast = Toast.makeText(getApplicationContext(), desc,
				Toast.LENGTH_LONG);
		toast.show();
	}
}
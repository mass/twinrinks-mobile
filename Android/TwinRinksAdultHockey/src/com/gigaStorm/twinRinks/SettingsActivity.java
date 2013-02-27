package com.gigaStorm.twinRinks;

import java.util.ArrayList;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceCategory;
import android.text.InputType;
import android.widget.Toast;
import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockPreferenceActivity;

// Activity which allows the user to choose settings
public class SettingsActivity extends SherlockPreferenceActivity {

    private PreferenceCategory addTeamCategory;
    private ArrayList<Team> yourTeams;
    private ArrayList<Team> allTeams;
    private MemoryManager memoryManager;
    private Preference addNewTeamPref;
    private ActionBar actionBar;

    @SuppressWarnings("deprecation")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

	super.onCreate(savedInstanceState);
	// Adds the settings XML to the activity
	addPreferencesFromResource(R.xml.settings_main);

	actionBar = getSupportActionBar();
	actionBar.setHomeButtonEnabled(true);
	actionBar.setDisplayShowTitleEnabled(false);

	addTeamCategory = (PreferenceCategory) findPreference("addTeamCategory");
	addTeamCategory.setOrderingAsAdded(false);

	addNewTeamPref = findPreference("addNewTeamPref");
	addNewTeamPref.setOrder(999);
	addNewTeamPref.setOnPreferenceClickListener(new OnPreferenceClickListener() {
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

	final EditTextPreference autoLogInUsername = (EditTextPreference) findPreference("autoLogInUsername");
	final EditTextPreference autoLogInPassword = (EditTextPreference) findPreference("autoLogInPassword");

	autoLogInUsername.getEditText().setInputType(
		InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
	autoLogInPassword.getEditText().setInputType(
		InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);

	final CheckBoxPreference autoLogInCheckBox = (CheckBoxPreference) findPreference("autoLogInCheckbox");
	autoLogInCheckBox.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
	    public boolean onPreferenceChange(Preference preference,Object newValue) {
		if(autoLogInCheckBox.isChecked()) {
		    autoLogInPassword.setEnabled(false);
		    autoLogInUsername.setEnabled(false);
		} else {
		    autoLogInPassword.setEnabled(true);
		    autoLogInUsername.setEnabled(true);
		}
		return true;
	    }
	});

	if(autoLogInCheckBox.isChecked() == false) {
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
	for(int i = 0; i < yourTeams.size(); i++) {
	    addTeamCategory
		    .addPreference(getNewPreference(yourTeams.get(i).getLeague(), yourTeams.get(i).getTeamName()));
	}
	addTeamCategory.addPreference(addNewTeamPref);
    }

    public void addNewTeam(String league,String team) {
	if(!alreadyEntered(league, team)) {
	    yourTeams.add(new Team(league, team));
	    addTeamCategory.addPreference(getNewPreference(league, team));
	} else
	    toast("Team already entered");
    }

    public CharSequence[] getTeamsFromLeague(String league) {
	ArrayList<String> temp = new ArrayList<String>();
	for(Team e: allTeams)
	    if(e.getLeague().equalsIgnoreCase(league))
		temp.add(e.getTeamName());

	CharSequence[] toReturn = new CharSequence[temp.size()];
	for(int i = 0; i < temp.size(); i++)
	    toReturn[i] = temp.get(i);
	return toReturn;
    }

    public boolean alreadyEntered(String league,String team) {
	for(int i = 0; i < yourTeams.size(); i++)
	    if(yourTeams.get(i).getLeague().equals(league) && yourTeams.get(i).getTeamName().equals(team))
		return true;
	return false;
    }

    public Preference getNewPreference(String leagueP,String teamP) {
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
		builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
		    public void onClick(DialogInterface dialog,int id) {
			yourTeams.remove(getIndexOfTeam(league, team));
			addTeamCategory.removePreference(preference);
		    }
		});
		builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
		    public void onClick(DialogInterface dialog,int id) {
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

    public int getIndexOfTeam(String league,String name) {
	for(int i = 0; i < yourTeams.size(); i++)
	    if(yourTeams.get(i).getLeague().equals(league) && yourTeams.get(i).getTeamName().equals(name))
		return i;
	return -1;
    }

    public void showAddTeamPopup() {
	final CharSequence[] leagues = getResources().getTextArray(R.array.leagues);
	final Context context = this;

	AlertDialog.Builder builder = new AlertDialog.Builder(context);
	builder.setTitle("Select your league:");
	builder.setItems(leagues, new DialogInterface.OnClickListener() {
	    public void onClick(DialogInterface dialog,final int itemLeague) {
		final CharSequence[] teams = getTeamsFromLeague(leagues[itemLeague].toString());
		AlertDialog.Builder builder2 = new AlertDialog.Builder(context);
		builder2.setTitle("Select your team:");
		builder2.setItems(teams, new DialogInterface.OnClickListener() {
		    @Override
		    public void onClick(DialogInterface dialog,int itemTeam) {
			addNewTeam(getResources().getTextArray(R.array.leagues)[itemLeague].toString(),
				teams[itemTeam].toString());
		    }
		});
		AlertDialog alert2 = builder2.create();
		alert2.show();
	    }
	});
	AlertDialog alert = builder.create();
	alert.show();
    }

    public void toast(String desc) {
	Toast toast = Toast.makeText(getApplicationContext(), desc, Toast.LENGTH_LONG);
	toast.show();
    }
}
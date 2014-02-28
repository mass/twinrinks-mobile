package com.gigaStorm.twinRinks;

import java.util.ArrayList;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;
import android.preference.PreferenceCategory;
import android.text.InputType;
import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockPreferenceActivity;
import com.actionbarsherlock.view.MenuItem;

/**
 * <code>Activity_Settings</code> allows the user to change application settings and choose
 * preferences.
 * 
 * @author Andrew Mass
 * @see PreferenceActivity
 */
public class Activity_Settings extends SherlockPreferenceActivity {

  private PreferenceCategory addTeamCategory;

  private ArrayList<Model_Team> yourTeams;

  private Data_MemoryManager memoryManager;

  private Preference addNewTeamPref;

  private ActionBar actionBar;

  private Util util;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    // Adds the settings XML to the activity
    addPreferencesFromResource(R.xml.settings_main);

    getWindow().setBackgroundDrawableResource(android.R.color.black);

    actionBar = getSupportActionBar();
    actionBar.setHomeButtonEnabled(true);
    actionBar.setDisplayHomeAsUpEnabled(true);
    actionBar.setTitle("Settings");

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

    util = new Util(this);

    util = new Util(this);

    memoryManager = new Data_MemoryManager(this);
    yourTeams = memoryManager.getYourTeams();
    updatePreferencesFromTeams();

    final EditTextPreference autoLogInUsername = (EditTextPreference) findPreference("autoLogInUsername");
    final EditTextPreference autoLogInPassword = (EditTextPreference) findPreference("autoLogInPassword");

    autoLogInUsername.getEditText().setInputType(
        InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
    autoLogInPassword.getEditText().setInputType(
        InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);

    final CheckBoxPreference autoLogInCheckBox = (CheckBoxPreference) findPreference("autoLogInCheckbox");
    autoLogInCheckBox.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
      @Override
      public boolean onPreferenceChange(Preference preference, Object newValue) {
        if(autoLogInCheckBox.isChecked()) {
          autoLogInPassword.setEnabled(false);
          autoLogInUsername.setEnabled(false);
        }
        else {
          autoLogInPassword.setEnabled(true);
          autoLogInUsername.setEnabled(true);
        }
        return true;
      }
    });

    if(autoLogInCheckBox.isChecked() == false) {
      autoLogInPassword.setEnabled(false);
      autoLogInUsername.setEnabled(false);
    }
    else {
      autoLogInPassword.setEnabled(true);
      autoLogInUsername.setEnabled(true);
    }
  }

  @Override
  protected void onPause() {
    memoryManager.saveYourTeams(yourTeams);
    super.onPause();
  }

  public void updatePreferencesFromTeams() {
    addTeamCategory.removeAll();
    for(int i = 0; i < yourTeams.size(); i++) {
      addTeamCategory.addPreference(getNewPreference(yourTeams.get(i)));
    }

    addTeamCategory.addPreference(addNewTeamPref);
  }

  public void addNewTeam(Model_Team team) {
    if(!alreadyEntered(team)) {
      yourTeams.add(team);
      addTeamCategory.addPreference(getNewPreference(team));
    }
    else {
      util.toast("Team already entered");
    }
  }

  public boolean alreadyEntered(Model_Team team) {
    for(int i = 0; i < yourTeams.size(); i++) {
      if(yourTeams.get(i).getLeague().equals(team.getLeague())
          && yourTeams.get(i).getTeamName().equals(team.getTeamName())) {
        return true;
      }
    }
    return false;
  }

  public Preference getNewPreference(Model_Team team) {
    final Context context = this;
    final String league = team.getLeague();
    final String teamName = team.getTeamName();

    Preference newPref = new Preference(this);
    newPref.setTitle(league + "-" + teamName);
    newPref.setOrder(addTeamCategory.getPreferenceCount() - 1);

    newPref.setOnPreferenceClickListener(new OnPreferenceClickListener() {
      @Override
      public boolean onPreferenceClick(final Preference preference) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage("Remove this team?");
        builder.setCancelable(false);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
          @Override
          public void onClick(DialogInterface dialog, int id) {
            yourTeams.remove(getIndexOfTeam(league, teamName));
            addTeamCategory.removePreference(preference);
          }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
          @Override
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
    for(int i = 0; i < yourTeams.size(); i++) {
      if(yourTeams.get(i).getLeague().equals(league) && yourTeams.get(i).getTeamName().equals(name)) {
        return i;
      }
    }
    return -1;
  }

  public void showAddTeamPopup() {
    final Context context = this;
    final ArrayList<Model_Team> teams = memoryManager.getTeams();

    final CharSequence[] teamStrings = new CharSequence[teams.size()];
    for(int i = 0; i < teams.size(); i++)
      teamStrings[i] = teams.get(i).toString();

    AlertDialog.Builder builder = new AlertDialog.Builder(context);
    builder.setTitle("Select A Team:");
    builder.setItems(teamStrings, new DialogInterface.OnClickListener() {
      @Override
      public void onClick(DialogInterface dialog, final int item) {
        addNewTeam(teams.get(item));
      }
    });
    AlertDialog alert = builder.create();
    alert.show();
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch(item.getItemId()) {
      case android.R.id.home:
        startActivity(new Intent(this, Activity_Main.class));
        return true;

      default:
        return super.onOptionsItemSelected(item);
    }
  }
}

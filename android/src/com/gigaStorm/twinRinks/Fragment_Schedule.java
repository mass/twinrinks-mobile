package com.gigaStorm.twinRinks;

import java.util.ArrayList;
import java.util.Calendar;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.format.Time;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import com.actionbarsherlock.app.SherlockFragment;

/**
 * <code>Fragment_Schedule</code> allows the user to view game data in multiple
 * different ways.
 * 
 * @author Andrew Mass
 * @see Fragment
 */
public class Fragment_Schedule extends SherlockFragment {

  private Button btn_schedule_dataSelector;

  private Data_MemoryManager memoryManager;

  private View view;

  private ListView listView_schedule_main;

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    view = inflater.inflate(R.layout.layout_frag_schedule, container, false);

    memoryManager = new Data_MemoryManager(getActivity());

    listView_schedule_main = (ListView) view
        .findViewById(R.id.listView_schedule_main);

    btn_schedule_dataSelector = (Button) view
        .findViewById(R.id.btn_schedule_dataSelector);
    btn_schedule_dataSelector.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        showDataSelectionPopup();
      }
    });
    return view;
  }

  private void showDataSelectionPopup() {
    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
    builder.setTitle("Choose Schedule Data to View:");
    builder.setItems(R.array.spinner_scheduleDataSelector_entries,
        new DialogInterface.OnClickListener() {
          @Override
          public void onClick(DialogInterface dialog, int which) {
            switch(which) {
              case 0:
                showSelectFromAllTeamsPopup();
                return;
              case 1:
                showSelectFromYourTeamsPopup();
                return;
              case 2:
                showScheduleDataAllGames();
                return;
              case 3:
                showScheduleDataPlayoffs();
                return;
              case 4:
                showSelectDatePopup();
                return;
              case 5:
                Time current = new Time();
                current.setToNow();
                showScheduleDataDate(current.year, current.month,
                    current.monthDay);
                return;
              default:
                return;
            }
          }
        });
    AlertDialog alert = builder.create();
    alert.show();
  }

  private void showGames(ArrayList<Model_Game> games) {
    String[] values = new String[games.size()];
    for(int i = 0; i < values.length; i++) {
      values[i] = games.get(i).toString();
    }

    Data_ArrayAdapter adapter = new Data_ArrayAdapter(getActivity(), games,
        values);
    listView_schedule_main.setAdapter(adapter);
  }

  private void showSelectFromAllTeamsPopup() {
    final Context context = getActivity();
    final ArrayList<Model_Team> teams = memoryManager.getTeams();

    final CharSequence[] teamStrings = new CharSequence[teams.size()];
    for(int i = 0; i < teams.size(); i++) {
      teamStrings[i] = teams.get(i).toString();
    }

    AlertDialog.Builder builder = new AlertDialog.Builder(context);
    builder.setTitle("Select A Team:");
    builder.setItems(teamStrings, new DialogInterface.OnClickListener() {
      @Override
      public void onClick(DialogInterface dialog, final int item) {
        showScheduleData(teams.get(item));
      }
    });

    AlertDialog alert = builder.create();
    alert.show();
  }

  private void showSelectFromYourTeamsPopup() {
    final ArrayList<Model_Team> yourTeams = memoryManager.getUserTeams();

    if(yourTeams.size() < 1) {
      Util util = new Util(getActivity());
      util.toast("You have not selected any teams.");
      return;
    }

    if(yourTeams.size() == 1) {
      showScheduleData(yourTeams.get(0));
      return;
    }

    CharSequence[] items = new CharSequence[yourTeams.size()];
    for(int i = 0; i < yourTeams.size(); i++) {
      items[i] = yourTeams.get(i).toString();
    }

    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
    builder.setTitle("Select Your Team:");
    builder.setItems(items, new DialogInterface.OnClickListener() {
      @Override
      public void onClick(DialogInterface dialog, final int item) {
        showScheduleData(yourTeams.get(item));
      }
    });
    AlertDialog alert = builder.create();
    alert.show();
  }

  private void showSelectDatePopup() {
    Time current = new Time();
    current.setToNow();
    DatePickerDialog dialog = new DatePickerDialog(getActivity(),
        new OnDateSetListener() {
          @Override
          public void onDateSet(DatePicker view, int year, int monthOfYear,
              int dayOfMonth) {
            showScheduleDataDate(year, monthOfYear, dayOfMonth);
          }
        }, current.year, current.month, current.monthDay);
    dialog.show();
  }

  private void showScheduleData(Model_Team team) {
    ArrayList<Model_Game> games = memoryManager.getGames();
    ArrayList<Model_Game> gamesToAdd = new ArrayList<Model_Game>();

    for(Model_Game e: games) {
      if((e.getTeamA().equalsIgnoreCase(team.getTeamName()) || e.getTeamH()
          .equalsIgnoreCase(team.getTeamName()))
          && e.getLeague().equalsIgnoreCase(team.getLeague()) && !e.hasPassed()) {
        gamesToAdd.add(e);
      }
    }

    showGames(gamesToAdd);
  }

  private void showScheduleDataAllGames() {
    ArrayList<Model_Game> games = memoryManager.getGames();
    ArrayList<Model_Game> gamesToAdd = new ArrayList<Model_Game>();

    for(Model_Game e: games) {
      if(!e.hasPassed()) {
        gamesToAdd.add(e);
      }
    }

    showGames(gamesToAdd);
  }

  private void showScheduleDataPlayoffs() {
    ArrayList<Model_Game> games = memoryManager.getGames();
    ArrayList<Model_Game> gamesToAdd = new ArrayList<Model_Game>();

    for(Model_Game e: games) {
      if((e.getTeamA().equalsIgnoreCase("PLAYOFFS") || e.getTeamH()
          .equalsIgnoreCase("PLAYOFFS")) && !e.hasPassed()) {
        gamesToAdd.add(e);
      }
    }

    showGames(gamesToAdd);
  }

  private void showScheduleDataDate(int year, int month, int monthday) {
    ArrayList<Model_Game> games = memoryManager.getGames();
    ArrayList<Model_Game> gamesToAdd = new ArrayList<Model_Game>();

    for(Model_Game e: games) {
      if(e.getCal().get(Calendar.YEAR) == year
          && e.getCal().get(Calendar.MONTH) == month
          && e.getCal().get(Calendar.DAY_OF_MONTH) == monthday
          && !e.hasPassed()) {
        gamesToAdd.add(e);
      }
    }

    showGames(gamesToAdd);
  }
}

package com.gigaStorm.twinRinks;

import java.util.ArrayList;
import java.util.Calendar;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.format.Time;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import com.actionbarsherlock.app.SherlockFragment;
import com.gigaStorm.twinRinks.R;

public class Fragment_Schedule extends SherlockFragment {

    private Button btn_schedule_viewAnyTeam;
    private Button btn_schedule_viewTeam;
    private Button btn_schedule_viewAllGames;
    private Button btn_schedule_viewPlayoffs;
    private Button btn_schedule_viewToday;
    private Button btn_schedule_viewDate;
    private Data_MemoryManager memoryManager;
    private View view;
    private ListView listView_schedule_main;

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {
	view = inflater.inflate(R.layout.layout_frag_schedule, container, false);

	memoryManager = new Data_MemoryManager(getActivity());

	listView_schedule_main = (ListView) view.findViewById(R.id.listView_schedule_main);
	listView_schedule_main.setSelector(new ColorDrawable(Color.TRANSPARENT));
	listView_schedule_main.setDivider(null);
	listView_schedule_main.setDividerHeight(10);

	btn_schedule_viewAnyTeam = (Button) view.findViewById(R.id.btn_schedule_viewAll);
	btn_schedule_viewTeam = (Button) view.findViewById(R.id.btn_schedule_viewTeam);
	btn_schedule_viewAllGames = (Button) view.findViewById(R.id.btn_schedule_allGames);
	btn_schedule_viewPlayoffs = (Button) view.findViewById(R.id.btn_schedule_playoffs);
	btn_schedule_viewToday = (Button) view.findViewById(R.id.btn_schedule_viewToday);
	btn_schedule_viewDate = (Button) view.findViewById(R.id.btn_schedule_viewDate);

	btn_schedule_viewAnyTeam.setOnClickListener(new OnClickListener() {
	    public void onClick(View v) {
		showSelectFromAllTeamsPopup();
	    }
	});
	btn_schedule_viewTeam.setOnClickListener(new OnClickListener() {
	    public void onClick(View v) {
		showSelectFromYourTeamsPopup();
	    }
	});
	btn_schedule_viewAllGames.setOnClickListener(new OnClickListener() {
	    public void onClick(View v) {
		showScheduleDataAllGames();
	    }
	});
	btn_schedule_viewPlayoffs.setOnClickListener(new OnClickListener() {
	    public void onClick(View v) {
		showScheduleDataPlayoffs();
	    }
	});
	btn_schedule_viewToday.setOnClickListener(new OnClickListener() {
	    public void onClick(View v) {
		Time current = new Time();
		current.setToNow();
		showScheduleDataDate(current.year, current.month, current.monthDay);
	    }
	});
	btn_schedule_viewDate.setOnClickListener(new OnClickListener() {
	    public void onClick(View v) {
		showSelectDatePopup();
	    }
	});

	return view;
    }

    public void showSelectFromAllTeamsPopup() {
	final Context context = getActivity();
	final ArrayList<Model_Team> teams = memoryManager.getTeams();

	final CharSequence[] teamStrings = new CharSequence[teams.size()];
	for(int i = 0; i < teams.size(); i++)
	    teamStrings[i] = teams.get(i).toString();

	AlertDialog.Builder builder = new AlertDialog.Builder(context);
	builder.setTitle("Select A Team:");
	builder.setItems(teamStrings, new DialogInterface.OnClickListener() {
	    public void onClick(DialogInterface dialog,final int item) {
		showScheduleData(teams.get(item).getLeague(), teams.get(item).getTeamName());
	    }
	});
	AlertDialog alert = builder.create();
	alert.show();
    }

    public void showSelectFromYourTeamsPopup() {
	final ArrayList<Model_Team> yourTeams = memoryManager.getYourTeams();
	CharSequence[] items = new CharSequence[yourTeams.size()];
	for(int i = 0; i < yourTeams.size(); i++)
	    items[i] = yourTeams.get(i).toString();

	AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
	builder.setTitle("Select Your Team:");
	builder.setItems(items, new DialogInterface.OnClickListener() {
	    public void onClick(DialogInterface dialog,final int item) {
		showScheduleData(yourTeams.get(item).getLeague(), yourTeams.get(item).getTeamName());
	    }
	});
	AlertDialog alert = builder.create();
	alert.show();
    }

    public void showSelectDatePopup() {
	Time current = new Time();
	current.setToNow();
	DatePickerDialog dialog = new DatePickerDialog(getActivity(), new OnDateSetListener() {
	    public void onDateSet(DatePicker view,int year,int monthOfYear,int dayOfMonth) {
		showScheduleDataDate(year, monthOfYear, dayOfMonth);
	    }
	}, current.year, current.month, current.monthDay);
	dialog.show();
    }

    public void showScheduleData(String league,String team) {
	ArrayList<Model_Game> games = memoryManager.getGames();
	ArrayList<Model_Game> gamesToAdd = new ArrayList<Model_Game>();
	for(Model_Game e: games)
	    if((e.getTeamA().equalsIgnoreCase(team) || e.getTeamH().equalsIgnoreCase(team)) && e.getLeague().equalsIgnoreCase(league))
		if(!e.hasPassed())
		    gamesToAdd.add(e);

	String[] values = new String[gamesToAdd.size()];
	for(int i = 0; i < values.length; i++)
	    values[i] = gamesToAdd.get(i).toString();

	Data_ArrayAdapter adapter = new Data_ArrayAdapter(getActivity(), gamesToAdd, values);
	listView_schedule_main.setAdapter(adapter);
    }

    public void showScheduleDataAllGames() {
	ArrayList<Model_Game> games = memoryManager.getGames();
	ArrayList<Model_Game> gamesToAdd = new ArrayList<Model_Game>();
	for(Model_Game e: games)
	    if(!e.hasPassed())
		gamesToAdd.add(e);

	String[] values = new String[gamesToAdd.size()];
	for(int i = 0; i < values.length; i++)
	    values[i] = gamesToAdd.get(i).toString();

	Data_ArrayAdapter adapter = new Data_ArrayAdapter(getActivity(), gamesToAdd, values);
	listView_schedule_main.setAdapter(adapter);
    }

    public void showScheduleDataPlayoffs() {
	ArrayList<Model_Game> games = memoryManager.getGames();
	ArrayList<Model_Game> gamesToAdd = new ArrayList<Model_Game>();
	for(Model_Game e: games)
	    if((e.getTeamA().equalsIgnoreCase("PLAYOFFS") || e.getTeamH().equalsIgnoreCase("PLAYOFFS")))
		if(!e.hasPassed())
		    gamesToAdd.add(e);

	String[] values = new String[gamesToAdd.size()];
	for(int i = 0; i < values.length; i++)
	    values[i] = gamesToAdd.get(i).toString();

	Data_ArrayAdapter adapter = new Data_ArrayAdapter(getActivity(), gamesToAdd, values);
	listView_schedule_main.setAdapter(adapter);
    }

    public void showScheduleDataDate(int year,int month,int monthday) {
	ArrayList<Model_Game> games = memoryManager.getGames();
	ArrayList<Model_Game> gamesToAdd = new ArrayList<Model_Game>();
	for(Model_Game e: games)
	    if(e.getCalendarObject().get(Calendar.YEAR) == year && e.getCalendarObject().get(Calendar.MONTH) == month && e.getCalendarObject().get(Calendar.DAY_OF_MONTH) == monthday)
		if(!e.hasPassed())
		    gamesToAdd.add(e);

	String[] values = new String[gamesToAdd.size()];
	for(int i = 0; i < values.length; i++)
	    values[i] = gamesToAdd.get(i).toString();

	Data_ArrayAdapter adapter = new Data_ArrayAdapter(getActivity(), gamesToAdd, values);
	listView_schedule_main.setAdapter(adapter);
    }
}
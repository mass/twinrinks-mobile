package com.gigaStorm.twinRinks;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.ActionBar.Tab;
import com.actionbarsherlock.app.ActionBar.TabListener;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentTransaction;
import android.text.format.Time;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.Toast;


//THIS IS A TEST TO TEST ECLIPSE AND GIT'S ABILITY TO PLAY NICELY TOGETHER
public class MainActivity extends SherlockActivity implements TabListener {

    private ArrayList<Game> games = new ArrayList<Game>();
    private ArrayList<Team> yourTeams = new ArrayList<Team>();
    private ArrayList<Team> allTeams = new ArrayList<Team>();
    private DataFetchTask fetchTask;
    private ActionBar actionBar;
    private MemoryManager memoryManager;
    private Button btn_schedule_viewAnyTeam;
    private Button btn_schedule_viewTeam;
    private Button btn_schedule_viewAllGames;
    private Button btn_schedule_viewPlayoffs;
    private Button btn_schedule_viewToday;
    private Button btn_schedule_viewDate;
    private Button btn_upcoming_goToAddTeams;

    @Override
    public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);

	actionBar = getSupportActionBar();
	actionBar.setHomeButtonEnabled(false);
	actionBar.setDisplayShowTitleEnabled(false);

	createTabs();
	actionBar.selectTab(actionBar.getTabAt(Integer.parseInt(PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("defaultTab", "0"))));
    }

    @Override
    protected void onResume() {
	loadSavedData();
	showCorrectView();
	if(games.size() <= 0)
	    fetchData();
	super.onResume();
    }

    protected void onPause() {
	saveData();
	super.onPause();
    }

    protected void onDestroy() {
	super.onDestroy();
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
	savedInstanceState.putInt("tab", actionBar.getSelectedTab().getPosition());
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
	int tabLoc = savedInstanceState.getInt("tab");
	actionBar.selectTab(actionBar.getTabAt(tabLoc));
	showCorrectView();
    }

    public void showUpcomingData() {
	LinearLayout linearLayout_upcomingMain_games = (LinearLayout) findViewById(R.id.linearLayout_upcoming_games);
	linearLayout_upcomingMain_games.removeAllViews();

	btn_upcoming_goToAddTeams = (Button) findViewById(R.id.btn_upcoming_goToAddTeams);
	if(btn_upcoming_goToAddTeams != null)
	    btn_upcoming_goToAddTeams.setOnClickListener(new OnClickListener() {
		@Override
		public void onClick(View v) {
		    startActivity(new Intent(getApplicationContext(), SettingsActivity.class));
		}
	    });

	ArrayList<Game> gamesToAdd = new ArrayList<Game>();
	for(Team e: yourTeams)
	    for(Game e1: games)
		if((e1.getTeamA().equalsIgnoreCase(e.getTeamName()) || e1.getTeamH().equalsIgnoreCase(e.getTeamName())) && e1.getLeague().equalsIgnoreCase(e.getLeague()))
		    if(!e1.hasPassed())
			gamesToAdd.add(e1);
	gamesToAdd = getSortedGameArray(gamesToAdd);
	for(Game e2: gamesToAdd) {
	    GameDisplay gd = new GameDisplay(this);
	    gd.setGame(e2);
	    linearLayout_upcomingMain_games.addView(gd);
	}

	if(!yourTeams.isEmpty() && btn_upcoming_goToAddTeams != null)
	    ((LinearLayout) btn_upcoming_goToAddTeams.getParent()).removeViewAt(0);
    }

    public void createTabs() {
	actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
	String[] tabNames = {"Upcoming", "Schedule", "Sub Sign-in"};
	for(int i = 0; i < 3; i++) {
	    ActionBar.Tab tab = getSupportActionBar().newTab();
	    tab.setText(tabNames[i]);
	    tab.setTabListener(this);
	    getSupportActionBar().addTab(tab);
	}

    }

    public void loadSavedData() {
	memoryManager = new MemoryManager(this);
	yourTeams = memoryManager.getYourTeamsFromMemory();
	allTeams = memoryManager.getAllTeamsFromMemory();
	games = memoryManager.getGamesFromMemory();
    }

    public void saveData() {
	memoryManager.saveYourTeamsToMemory(yourTeams);
	memoryManager.saveAllTeamsToMemory(allTeams);
	memoryManager.saveGamesToMemory(games);
    }

    public void parseTeamsFromGames() {
	allTeams = new ArrayList<Team>();
	for(Game e: games) {
	    String l = e.getLeague();
	    String a = e.getTeamA();
	    String h = e.getTeamH();
	    if(!h.equalsIgnoreCase("PLAYOFF"))
		if(!hasTeam(l, a)) {
		    allTeams.add(new Team(l, a));
		    if(!hasTeam(l, h))
			allTeams.add(new Team(l, h));
		} else if(!hasTeam(l, h))
		    allTeams.add(new Team(l, h));
	}
    }

    public boolean hasTeam(String league,String team) {
	for(Team e: allTeams)
	    if(e.getLeague().equalsIgnoreCase(league) && e.getTeamName().equalsIgnoreCase(team))
		return true;
	return false;

    }

    private void fetchData() {
	fetchTask = new DataFetchTask(MainActivity.this);
	fetchTask.execute();
    }

    public void setScheduleTable(String[] data) {
	if(data == null) {
	    toast("Data Retrieval Failed. Try again");
	}
	games = getGameList(data);
	parseTeamsFromGames();
	if(actionBar.getSelectedTab().getPosition() == 0)
	    showUpcomingData();
	saveData();
    }

    private ArrayList<Game> getGameList(String[] data) {
	ArrayList<Game> list = new ArrayList<Game>();

	for(int i = 0; i < data.length; i++) {
	    String[] attrs = data[i].split(",");
	    if(attrs.length == 8) {
		list.add(new Game(attrs[0], attrs[2], attrs[3], attrs[4], attrs[6], attrs[7], attrs[5]));
	    }
	}
	return getSortedGameArray(list);
    }

    public ArrayList<Game> getSortedGameArray(ArrayList<Game> list) {
	@SuppressWarnings("unchecked") ArrayList<Game> temp = (ArrayList<Game>) list.clone();
	Collections.sort(temp, new Comparator<Game>() {
	    @Override
	    public int compare(Game lhs,Game rhs) {
		return Time.compare(lhs.getTimeObject(), rhs.getTimeObject());
	    }
	});
	return temp;
    }

    public void showSigninPage() {
	String autoLogInUsername = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("autoLogInUsername", "NullAndVoid");
	String autoLogInPassword = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("autoLogInPassword", "NullAndVoid");
	boolean autoLogInCheckbox = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getBoolean("autoLogInCheckbox", false);

	WebView webView = (WebView) this.findViewById(R.id.webView_subsigninmain_mainWebView);
	WebViewClient webViewClient = new WebViewClient();
	webViewClient.shouldOverrideUrlLoading(webView, "http://www.twinrinks.com/adulthockey/subs/subs_entry.html");
	webView.setWebViewClient(webViewClient);
	webView.getSettings().setBuiltInZoomControls(true);

	if(checkInternet()) {
	    if(autoLogInCheckbox) {
		webView.loadUrl("http://www.twinrinks.com/adulthockey/subs/subs_entry.php?subs_data1=" + autoLogInUsername + "&subs_data2=" + autoLogInPassword);
	    } else {
		webView.loadUrl("http://www.twinrinks.com/adulthockey/subs/subs_entry.html");
	    }
	} else {
	    webView.loadData("This application requires a valid internet connection to properly function.", "text/html", "utf-8");
	    AlertDialog.Builder builder = new AlertDialog.Builder(this);
	    builder.setCancelable(false);
	    builder.setTitle("No Network Connection");
	    builder.setPositiveButton("Retry", new DialogInterface.OnClickListener() {
		public void onClick(DialogInterface dialog,int which) {
		    showSigninPage();
		}
	    });
	    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
		public void onClick(DialogInterface dialog,int which) {}
	    });
	    AlertDialog alert = builder.create();
	    alert.show();
	}
    }

    public boolean checkInternet() {
	ConnectivityManager connec = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
	NetworkInfo wifi = connec.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
	NetworkInfo mobile = connec.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

	if(wifi != null && wifi.isConnected() || mobile != null & mobile.isConnected())
	    return true;

	return false;
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

    public void showScheduleData(String league,String team) {
	LinearLayout linearLayout_mainSchedule_games = (LinearLayout) findViewById(R.id.linearLayout_mainSchedule_games);
	linearLayout_mainSchedule_games.removeAllViews();
	for(Game e: games)
	    if((e.getTeamA().equalsIgnoreCase(team) || e.getTeamH().equalsIgnoreCase(team)) && e.getLeague().equalsIgnoreCase(league))
		if(!e.hasPassed()) {
		    GameDisplay gd = new GameDisplay(this);
		    gd.setGame(e);
		    linearLayout_mainSchedule_games.addView(gd);
		}
    }

    public void showScheduleDataAllGames() {
	LinearLayout linearLayout_mainSchedule_games = (LinearLayout) findViewById(R.id.linearLayout_mainSchedule_games);
	linearLayout_mainSchedule_games.removeAllViews();
	for(Game e: games) {
	    if(!e.hasPassed()) {
		GameDisplay gd = new GameDisplay(this);
		gd.setGame(e);
		linearLayout_mainSchedule_games.addView(gd);
	    }
	}
    }

    public void showScheduleDataPlayoffs() {
	LinearLayout linearLayout_mainSchedule_games = (LinearLayout) findViewById(R.id.linearLayout_mainSchedule_games);
	linearLayout_mainSchedule_games.removeAllViews();
	for(Game e: games)
	    if((e.getTeamA().equalsIgnoreCase("PLAYOFF") || e.getTeamH().equalsIgnoreCase("PLAYOFF")))
		if(!e.hasPassed()) {
		    GameDisplay gd = new GameDisplay(this);
		    gd.setGame(e);
		    linearLayout_mainSchedule_games.addView(gd);
		}
    }

    public void showScheduleDataDate(int year,int month,int monthday) {
	LinearLayout linearLayout_mainSchedule_games = (LinearLayout) findViewById(R.id.linearLayout_mainSchedule_games);
	linearLayout_mainSchedule_games.removeAllViews();
	for(Game e: games) {
	    if(e.getTimeObject().year == year && e.getTimeObject().month == month && e.getTimeObject().monthDay == monthday)
		if(!e.hasPassed()) {
		    GameDisplay gd = new GameDisplay(this);
		    gd.setGame(e);
		    linearLayout_mainSchedule_games.addView(gd);
		}
	}
    }

    public void showSelectDatePopup() {
	Time current = new Time();
	current.setToNow();
	DatePickerDialog dialog = new DatePickerDialog(this, new OnDateSetListener() {
	    public void onDateSet(DatePicker view,int year,int monthOfYear,int dayOfMonth) {
		showScheduleDataDate(year, monthOfYear, dayOfMonth);
	    }
	}, current.year, current.month, current.monthDay);
	dialog.show();
    }

    public void showSelectFromAllTeamsPopup() {
	final CharSequence[] leagues = getResources().getTextArray(R.array.leagues);
	final Context context = this;

	AlertDialog.Builder builder = new AlertDialog.Builder(context);
	builder.setTitle("Select league:");
	builder.setItems(leagues, new DialogInterface.OnClickListener() {
	    public void onClick(DialogInterface dialog,final int itemLeague) {
		final CharSequence[] teams = getTeamsFromLeague(leagues[itemLeague].toString());
		AlertDialog.Builder builder2 = new AlertDialog.Builder(context);
		builder2.setTitle("Select your team:");
		builder2.setItems(teams, new DialogInterface.OnClickListener() {
		    @Override
		    public void onClick(DialogInterface dialog,int itemTeam) {
			showScheduleData(leagues[itemLeague].toString(), teams[itemTeam].toString());
		    }
		});
		AlertDialog alert2 = builder2.create();
		alert2.show();
	    }
	});
	AlertDialog alert = builder.create();
	alert.show();
    }

    public void showSelectFromYourTeamsPopup() {
	CharSequence[] items = new CharSequence[yourTeams.size()];
	for(int i = 0; i < yourTeams.size(); i++)
	    items[i] = yourTeams.get(i).toString();

	AlertDialog.Builder builder = new AlertDialog.Builder(this);
	builder.setTitle("Select team:");
	builder.setItems(items, new DialogInterface.OnClickListener() {
	    public void onClick(DialogInterface dialog,final int item) {
		showScheduleData(yourTeams.get(item).getLeague(), yourTeams.get(item).getTeamName());
	    }
	});
	AlertDialog alert = builder.create();
	alert.show();
    }

    public void setToUpcoming() {
	setContentView(R.layout.activity_main_upcoming);
	showUpcomingData();
    }

    public void setToSchedule() {
	setContentView(R.layout.activity_main_schedule);

	btn_schedule_viewAnyTeam = (Button) findViewById(R.id.btn_schedule_viewAll);
	btn_schedule_viewTeam = (Button) findViewById(R.id.btn_schedule_viewTeam);
	btn_schedule_viewAllGames = (Button) findViewById(R.id.btn_schedule_allGames);
	btn_schedule_viewPlayoffs = (Button) findViewById(R.id.btn_schedule_playoffs);
	btn_schedule_viewToday = (Button) findViewById(R.id.btn_schedule_viewToday);
	btn_schedule_viewDate = (Button) findViewById(R.id.btn_schedule_viewDate);

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
    }

    public void setToSubSignin() {
	setContentView(R.layout.activity_main_subsignin);
	showSigninPage();
    }

    private void toast(Object desc) {
	Toast toast = Toast.makeText(getApplicationContext(), desc.toString(), Toast.LENGTH_LONG);
	toast.show();
    }

    public void showCorrectView() {
	if(actionBar.getSelectedTab().getPosition() == 0) {
	    setToUpcoming();
	    return;
	}

	if(actionBar.getSelectedTab().getPosition() == 1) {
	    setToSchedule();
	    return;
	}

	if(actionBar.getSelectedTab().getPosition() == 2) {
	    setToSubSignin();
	    return;
	}
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
	// Receives infalter object from actionbarsherlock
	MenuInflater inflater = getSupportMenuInflater();
	inflater.inflate(R.menu.menu_main, menu);
	return true;
    }

    // Handles touch events for menu items in actionbar or actionbar overflow
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
	// Intents to start activities of the app
	Intent startSettings = new Intent(this, SettingsActivity.class);
	Intent startHome = new Intent(this, MainActivity.class);

	switch(item.getItemId()) {
	    case android.R.id.home:
		startActivity(startHome);
		return true;

	    case R.id.refresh:
		fetchData();
		return true;

	    case R.id.help:
		startActivity(new Intent(this, HelpActivity.class));
		return true;

	    case R.id.shareMenu:
		Intent shareIntent = new Intent(Intent.ACTION_SEND);
		shareIntent.setType("text/plain");
		shareIntent.putExtra(Intent.EXTRA_TEXT, "Check out this Twin Rinks Adult Hockey Android app. http://goo.gl/ZeGxN");
		startActivity(Intent.createChooser(shareIntent, "Choose Application:"));
		return true;

	    case R.id.sendFeedback:
		Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:support.m@gigastormdevelopers.com"));
		startActivity(Intent.createChooser(intent, "Choose Application:"));
		return true;

	    case R.id.rate:
		Intent rateIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.gigaStorm.twinRinks"));
		startActivity(Intent.createChooser(rateIntent, "Choose Application:"));
		return true;

	    case R.id.settings:
		startActivity(startSettings);
		return true;

	    default:
		return super.onOptionsItemSelected(item);
	}
    }

    @Override
    public void onTabSelected(Tab tab,FragmentTransaction ft) {
	showCorrectView();
    }

    @Override
    public void onTabUnselected(Tab tab,FragmentTransaction ft) {

    }

    @Override
    public void onTabReselected(Tab tab,FragmentTransaction ft) {}
}

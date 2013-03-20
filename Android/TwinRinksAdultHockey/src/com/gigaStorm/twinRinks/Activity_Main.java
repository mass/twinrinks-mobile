package com.gigaStorm.twinRinks;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.ActionBar.Tab;
import com.actionbarsherlock.app.ActionBar.TabListener;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.gigaStorm.twinRinks.R;

// Main class that shows the main views and game data
public class Activity_Main extends SherlockFragmentActivity implements TabListener {

    private ActionBar actionBar;
    private Data_MemoryManager memoryManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.layout_main);

	actionBar = getSupportActionBar();
	actionBar.setHomeButtonEnabled(false);
	actionBar.setDisplayShowTitleEnabled(false);

	createTabs();
	actionBar.selectTab(actionBar.getTabAt(0));
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
	MenuInflater inflater = getSupportMenuInflater();
	inflater.inflate(R.menu.menu_main, menu);
	return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
	// Intents to start activities of the app
	Intent startSettings = new Intent(this, Activity_Settings.class);
	Intent startHome = new Intent(this, Activity_Main.class);

	switch(item.getItemId()) {
	    case android.R.id.home:
		startActivity(startHome);
		return true;

	    case R.id.refresh:
		memoryManager = new Data_MemoryManager(this);
		memoryManager.refreshData();
		return true;

	    case R.id.help:
		startActivity(new Intent(this, Activity_Help.class));
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

	    case R.id.addToCalendar:
		if(Build.VERSION.SDK_INT >= 14) {
		    Data_CalendarManager man = new Data_CalendarManager(this);
		    man.saveGamesToCalendar();
		} else {
		    Data_CalendarManagerCompat man = new Data_CalendarManagerCompat(this);
		    man.saveGamesToCalendar();
		}
		return true;

	    case R.id.settings:
		startActivity(startSettings);
		return true;

	    default:
		return super.onOptionsItemSelected(item);
	}
    }

    @Override
    public void onTabSelected(Tab tab,FragmentTransaction trans) {
	FragmentTransaction ft;
	if(actionBar.getSelectedTab().getPosition() == 0) {
	    ft = getSupportFragmentManager().beginTransaction();
	    Fragment_Upcoming newFrag = new Fragment_Upcoming();
	    ft.replace(R.id.main_framelayout_main, newFrag);
	    ft.commit();
	    return;
	}

	if(actionBar.getSelectedTab().getPosition() == 1) {
	    ft = getSupportFragmentManager().beginTransaction();
	    Fragment_Schedule newFrag = new Fragment_Schedule();
	    ft.replace(R.id.main_framelayout_main, newFrag);
	    ft.commit();
	    return;
	}

	if(actionBar.getSelectedTab().getPosition() == 2) {
	    ft = getSupportFragmentManager().beginTransaction();
	    Fragment_SignIn newFrag = new Fragment_SignIn();
	    ft.replace(R.id.main_framelayout_main, newFrag);
	    ft.commit();
	    return;
	}
    }

    @Override
    public void onTabUnselected(Tab tab,FragmentTransaction ft) {}

    @Override
    public void onTabReselected(Tab tab,FragmentTransaction ft) {}
}
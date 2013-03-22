package com.gigaStorm.twinRinks;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.ActionBar.Tab;
import com.actionbarsherlock.app.ActionBar.TabListener;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.gigaStorm.twinRinks.R;
import com.viewpagerindicator.TitlePageIndicator;

// Main class that shows the main views and game data
public class Activity_Main extends SherlockFragmentActivity implements TabListener {

    private ActionBar actionBar;
    private Data_MemoryManager memoryManager;
    private ViewPager viewPager;
    private PagerAdapter pagerAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.layout_main);

	actionBar = getSupportActionBar();
	actionBar.setHomeButtonEnabled(false);
	actionBar.setDisplayShowTitleEnabled(false);

	viewPager = (ViewPager) findViewById(R.id.viewPager_main_main);
	pagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
	viewPager.setAdapter(pagerAdapter);

	TitlePageIndicator titleIndicator = (TitlePageIndicator) findViewById(R.id.viewPagerIndicator);
	titleIndicator.setViewPager(viewPager);
	titleIndicator.setBackgroundColor(getResources().getColor(R.color.vpi__background_holo_dark));
    }

    @Override
    public void onBackPressed() {
	if(viewPager.getCurrentItem() == 0)
	    super.onBackPressed();
	else
	    viewPager.setCurrentItem(viewPager.getCurrentItem() - 1);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
	MenuInflater inflater = getSupportMenuInflater();
	inflater.inflate(R.menu.menu_main, menu);
	return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
	Intent startSettings = new Intent(this, Activity_SettingsCompat.class);
	Intent startHome = new Intent(this, Activity_Main.class);

	switch(item.getItemId()) {
	    case android.R.id.home:
		startActivity(startHome);
		return true;

	    case R.id.menu_refresh:
		memoryManager = new Data_MemoryManager(this);
		memoryManager.refreshData();
		return true;

	    case R.id.menu_about:
		startActivity(new Intent(this, Activity_About.class));
		return true;

	    case R.id.menu_addToCalendar:
		if(Build.VERSION.SDK_INT >= 14) {
		    Data_CalendarManager man = new Data_CalendarManager(this);
		    man.saveGamesToCalendar();
		} else {
		    Data_CalendarManagerCompat man = new Data_CalendarManagerCompat(this);
		    man.saveGamesToCalendar();
		}
		return true;

	    case R.id.menu_settings:
		startActivity(startSettings);
		return true;

	    default:
		return super.onOptionsItemSelected(item);
	}
    }

    @Override
    public void onTabSelected(Tab tab,FragmentTransaction trans) {
	viewPager.setCurrentItem(tab.getPosition(), true);
	Log.e("hi", "here");
    }

    @Override
    public void onTabUnselected(Tab tab,FragmentTransaction ft) {}

    @Override
    public void onTabReselected(Tab tab,FragmentTransaction ft) {}

    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
	public ScreenSlidePagerAdapter(FragmentManager fm) {
	    super(fm);
	}

	@Override
	public Fragment getItem(int position) {
	    if(position == 0)
		return new Fragment_Upcoming();
	    if(position == 1)
		return new Fragment_Schedule();
	    if(position == 2)
		return new Fragment_SignIn();
	    return new Fragment_Upcoming();
	}

	@Override
	public CharSequence getPageTitle(int position) {
	    if(position == 0)
		return "Upcoming";
	    if(position == 1)
		return "Schedule";
	    if(position == 2)
		return "Sub Sign-In";
	    return "Error";
	}

	@Override
	public int getCount() {
	    return 3;
	}
    }
}
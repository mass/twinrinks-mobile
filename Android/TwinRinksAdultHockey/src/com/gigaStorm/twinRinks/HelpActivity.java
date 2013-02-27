package com.gigaStorm.twinRinks;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;

// Activity that shows help info to the user
public class HelpActivity extends SherlockActivity {

    private ActionBar actionBar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.activity_help);

	actionBar = getSupportActionBar();
	actionBar.setHomeButtonEnabled(true);
	actionBar.setDisplayShowTitleEnabled(false);
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
		return true;

	    case R.id.help:
		startActivity(new Intent(this, HelpActivity.class));
		return true;

	    case R.id.shareMenu:
		Intent shareIntent = new Intent(Intent.ACTION_SEND);
		shareIntent.setType("text/plain");
		shareIntent.putExtra(Intent.EXTRA_TEXT,
			"Check out this Twin Rinks Adult Hockey Android app. http://goo.gl/ZeGxN");
		startActivity(Intent.createChooser(shareIntent, "Choose Application:"));
		return true;

	    case R.id.sendFeedback:
		Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:support.m@gigastormdevelopers.com"));
		startActivity(Intent.createChooser(intent, "Choose Application:"));
		return true;

	    case R.id.rate:
		Intent rateIntent = new Intent(Intent.ACTION_VIEW,
			Uri.parse("market://details?id=com.gigaStorm.twinRinks"));
		startActivity(Intent.createChooser(rateIntent, "Choose Application:"));
		return true;

	    case R.id.settings:
		startActivity(startSettings);
		return true;

	    default:
		return super.onOptionsItemSelected(item);
	}
    }
}
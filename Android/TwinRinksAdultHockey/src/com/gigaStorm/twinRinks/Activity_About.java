package com.gigaStorm.twinRinks;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.gigaStorm.twinRinks.R;

// Activity that shows help info to the user
public class Activity_About extends SherlockActivity {

    private ActionBar actionBar;
    private Button btn_about_rate;
    private Button btn_about_share;
    private Button btn_about_send;

    @Override
    public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.layout_about);

	actionBar = getSupportActionBar();
	actionBar.setDisplayHomeAsUpEnabled(true);
	actionBar.setDisplayShowTitleEnabled(false);

	btn_about_rate = (Button) findViewById(R.id.btn_about_rate);
	btn_about_rate.setOnClickListener(new OnClickListener() {
	    @Override
	    public void onClick(View v) {
		Intent rateIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.gigaStorm.twinRinks"));
		startActivity(Intent.createChooser(rateIntent, "Choose Application:"));
	    }
	});

	btn_about_share = (Button) findViewById(R.id.btn_about_share);
	btn_about_share.setOnClickListener(new OnClickListener() {
	    @Override
	    public void onClick(View v) {
		Intent shareIntent = new Intent(Intent.ACTION_SEND);
		shareIntent.setType("text/plain");
		shareIntent.putExtra(Intent.EXTRA_TEXT, "Check out this Twin Rinks Adult Hockey Android app. http://goo.gl/ZeGxN");
		startActivity(Intent.createChooser(shareIntent, "Choose Application:"));
	    }
	});

	btn_about_send = (Button) findViewById(R.id.btn_about_send);
	btn_about_send.setOnClickListener(new OnClickListener() {
	    @Override
	    public void onClick(View v) {
		Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:support.m@gigastormdevelopers.com"));
		startActivity(Intent.createChooser(intent, "Choose Application:"));
	    }
	});
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
	Intent startSettings = new Intent(this, Activity_SettingsCompat.class);
	Intent startHome = new Intent(this, Activity_Main.class);

	switch(item.getItemId()) {
	    case android.R.id.home:
		startActivity(startHome);
		return true;

	    case R.id.menu_refresh:
		return true;

	    case R.id.menu_settings:
		startActivity(startSettings);
		return true;

	    default:
		return super.onOptionsItemSelected(item);
	}
    }
}
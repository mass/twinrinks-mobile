package com.gigaStorm.twinRinks;

import java.util.ArrayList;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import com.actionbarsherlock.app.SherlockFragment;
import com.gigaStorm.twinRinks.R;

public class Fragment_Upcoming extends SherlockFragment {

    private Button btn_upcoming_goToAddTeams;
    private ArrayList<Model_Team> yourTeams;
    private ArrayList<Model_Game> games;
    private Data_MemoryManager memoryManager;
    private LinearLayout linearLayout_upcomingMain_games;

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {
	View view = inflater.inflate(R.layout.layout_frag_upcoming, container, false);

	memoryManager = new Data_MemoryManager(getActivity());
	yourTeams = memoryManager.getYourTeams();

	linearLayout_upcomingMain_games = (LinearLayout) view.findViewById(R.id.linearLayout_upcoming_games);
	linearLayout_upcomingMain_games.removeAllViews();

	btn_upcoming_goToAddTeams = (Button) view.findViewById(R.id.btn_upcoming_goToAddTeams);
	btn_upcoming_goToAddTeams.setOnClickListener(new OnClickListener() {
	    @Override
	    public void onClick(View v) {
		startActivity(new Intent(getActivity().getApplicationContext(), Activity_SettingsCompat.class));
	    }
	});

	if(yourTeams.size() > 0)
	    prepareGames();

	return view;
    }

    private void prepareGames() {
	games = memoryManager.getGames();

	ArrayList<Model_Game> gamesToAdd = new ArrayList<Model_Game>();
	for(Model_Game e1: games)
	    for(Model_Team e: yourTeams)
		if((e1.getTeamA().equalsIgnoreCase(e.getTeamName()) || e1.getTeamH().equalsIgnoreCase(e.getTeamName())) && e1.getLeague().equalsIgnoreCase(e.getLeague()))
		    if(!e1.hasPassed())
			gamesToAdd.add(e1);

	for(Model_Game e2: gamesToAdd) {
	    View_GameDisplay gd = new View_GameDisplay(getActivity());
	    gd.setGame(e2);
	    linearLayout_upcomingMain_games.addView(gd);
	}

	if(!yourTeams.isEmpty() && btn_upcoming_goToAddTeams != null)
	    ((LinearLayout) btn_upcoming_goToAddTeams.getParent()).removeViewAt(0);
    }
}
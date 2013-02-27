package com.gigaStorm.twinRinks;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import android.content.Context;
import android.widget.Toast;

// Class which handles the importing and exporting of data to and from the
// internal memory
public class MemoryManager {

    private Context context;
    private File dir;
    private File gameStorage;
    private File yourTeamStorage;
    private File allTeamStorage;

    // Default constructor for a new instance of MemoryManager
    public MemoryManager(Context context) {
	this.context = context;
	dir = context.getDir("TwinRinksAdultHockey", Context.MODE_PRIVATE);
	gameStorage = new File(dir, "GameStorage");
	yourTeamStorage = new File(dir, "YourTeamStorage");
	allTeamStorage = new File(dir, "AllTeamStorage");
    }

    public void saveYourTeamsToMemory(ArrayList<Team> teams) {
	try {
	    DataOutputStream outTeam = new DataOutputStream(new FileOutputStream(yourTeamStorage, false));

	    for(int i = 0; i < teams.size(); i++) {
		String temp = teams.get(i).getTeamKey() + ":";
		outTeam.write(temp.getBytes());
	    }
	    outTeam.close();
	} catch(IOException e) {
	    e.printStackTrace();
	    resetFiles();
	}
    }

    @SuppressWarnings("deprecation")
    public ArrayList<Team> getYourTeamsFromMemory() {
	ArrayList<Team> teams = new ArrayList<Team>();

	try {
	    DataInputStream inTeam = new DataInputStream(new FileInputStream(yourTeamStorage));

	    String temp = inTeam.readLine();
	    inTeam.close();

	    if(temp != null) {
		String[] teamsStrings = temp.split(":");

		// Creates a new profile using the key taken from the String
		// array, and adds it to profiles
		for(int i = 0; i < teamsStrings.length; i++) {
		    teams.add(new Team(teamsStrings[i]));
		}
	    }
	    return teams;
	} catch(IOException e) {
	    e.printStackTrace();
	    resetFiles();
	    return teams;
	}
    }

    public void saveAllTeamsToMemory(ArrayList<Team> teams) {
	try {
	    DataOutputStream outTeam = new DataOutputStream(new FileOutputStream(allTeamStorage, false));

	    for(int i = 0; i < teams.size(); i++) {
		String temp = teams.get(i).getTeamKey() + ":";
		outTeam.write(temp.getBytes());
	    }
	    outTeam.close();
	} catch(IOException e) {
	    e.printStackTrace();
	    resetFiles();
	}
    }

    @SuppressWarnings("deprecation")
    public ArrayList<Team> getAllTeamsFromMemory() {
	ArrayList<Team> teams = new ArrayList<Team>();

	try {
	    DataInputStream inTeam = new DataInputStream(new FileInputStream(allTeamStorage));

	    String temp = inTeam.readLine();
	    inTeam.close();

	    if(temp != null) {
		String[] teamsStrings = temp.split(":");

		for(int i = 0; i < teamsStrings.length; i++) {
		    teams.add(new Team(teamsStrings[i]));
		}
	    }
	    return teams;
	} catch(IOException e) {
	    e.printStackTrace();
	    resetFiles();
	    return teams;
	}
    }

    public void saveGamesToMemory(ArrayList<Game> games) {
	try {
	    DataOutputStream outGame = new DataOutputStream(new FileOutputStream(gameStorage, false));

	    for(int i = 0; i < games.size(); i++) {
		String temp = games.get(i).getGameKey() + "::";
		outGame.write(temp.getBytes());
	    }
	    outGame.close();
	} catch(IOException e) {
	    e.printStackTrace();
	    resetFiles();
	}
    }

    @SuppressWarnings("deprecation")
    public ArrayList<Game> getGamesFromMemory() {
	ArrayList<Game> games = new ArrayList<Game>();

	try {
	    DataInputStream inGame = new DataInputStream(new FileInputStream(gameStorage));

	    String temp = inGame.readLine();
	    inGame.close();

	    if(temp != null) {
		String[] gamesStrings = temp.split("::");

		for(int i = 0; i < gamesStrings.length; i++) {
		    games.add(new Game(gamesStrings[i]));
		}
	    }
	    return games;
	} catch(IOException e) {
	    e.printStackTrace();
	    resetFiles();
	    return games;
	}
    }

    public void resetFiles() {
	gameStorage.delete();
	yourTeamStorage.delete();
	allTeamStorage.delete();
	gameStorage = null;
	yourTeamStorage = null;
	allTeamStorage = null;

	gameStorage = new File(dir, "GameStorage");
	yourTeamStorage = new File(dir, "YourTeamStorage");
	allTeamStorage = new File(dir, "AllTeamStorage");

	saveDefaultValues();
    }

    public void saveDefaultValues() {
	try {
	    DataOutputStream outGame = new DataOutputStream(new FileOutputStream(gameStorage, false));
	    DataOutputStream outYourTeam = new DataOutputStream(new FileOutputStream(yourTeamStorage, false));
	    DataOutputStream outAllTeam = new DataOutputStream(new FileOutputStream(allTeamStorage, false));

	    outYourTeam.close();
	    outAllTeam.close();
	    outGame.close();

	} catch(IOException e) {
	    e.printStackTrace();
	    toast("Failed");
	    resetFiles();
	}
    }

    // Shows the string as a message on the screen
    public void toast(String desc) {
	Toast toast = Toast.makeText(context, desc, Toast.LENGTH_LONG);
	toast.show();
    }
}
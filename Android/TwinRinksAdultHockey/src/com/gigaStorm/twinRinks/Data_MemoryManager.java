package com.gigaStorm.twinRinks;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import android.content.Context;
import android.widget.Toast;

// Class which handles the importing and exporting of data to and from the
// internal memory
public class Data_MemoryManager {

    private Context context;
    private File dir;
    private File gameStorage;
    private File yourTeamStorage;
    private File allTeamStorage;

    // Default constructor for a new instance of MemoryManager
    public Data_MemoryManager(Context context) {
	this.context = context;
	dir = context.getDir("TwinRinksAdultHockey", Context.MODE_PRIVATE);
	gameStorage = new File(dir, "GameStorage");
	yourTeamStorage = new File(dir, "YourTeamStorage");
	allTeamStorage = new File(dir, "AllTeamStorage");
    }

    public void refreshData() {
	Data_FetchTask fetchTask = new Data_FetchTask(this);
	fetchTask.execute();
    }

    public void setScheduleTable(String[] data) {
	if(data == null)
	    toast("Data Retrieval Failed. Try again");

	ArrayList<Model_Game> games = getGameList(data);
	saveTeams(parseTeamsFromGames(games));
	saveGames(games);
    }

    private ArrayList<Model_Game> getGameList(String[] data) {
	ArrayList<Model_Game> list = new ArrayList<Model_Game>();

	for(int i = 0; i < data.length; i++) {
	    String[] attrs = data[i].split(",");
	    if(attrs.length == 8)
		list.add(new Model_Game(attrs[0], attrs[2], attrs[3], attrs[4], attrs[6], attrs[7], attrs[5]));
	}
	sortGames(list);
	return list;
    }

    public ArrayList<Model_Team> parseTeamsFromGames(ArrayList<Model_Game> games) {
	ArrayList<Model_Team> allTeams = new ArrayList<Model_Team>();
	for(Model_Game e: games) {
	    String l = e.getLeague();
	    String a = e.getTeamA();
	    String h = e.getTeamH();
	    if(!h.equalsIgnoreCase("PLAYOFF"))
		if(!hasTeam(l, a)) {
		    allTeams.add(new Model_Team(l, a));
		    if(!hasTeam(l, h))
			allTeams.add(new Model_Team(l, h));
		} else if(!hasTeam(l, h))
		    allTeams.add(new Model_Team(l, h));
	}
	return allTeams;
    }

    public void sortGames(ArrayList<Model_Game> list) {
	Collections.sort(list, new Comparator<Model_Game>() {
	    @Override
	    public int compare(Model_Game lhs,Model_Game rhs) {
		Date date = lhs.getCalendarObject().getTime();
		return date.compareTo(rhs.getCalendarObject().getTime());
	    }
	});
    }

//    public CharSequence[] getTeamsFromLeague(String league) {
//	ArrayList<String> temp = new ArrayList<String>();
//	for(Team e: allTeams)
//	    if(e.getLeague().equalsIgnoreCase(league))
//		temp.add(e.getTeamName());
//
//	CharSequence[] toReturn = new CharSequence[temp.size()];
//	for(int i = 0; i < temp.size(); i++)
//	    toReturn[i] = temp.get(i);
//	return toReturn;
//    }

    public boolean hasTeam(String league,String team) {
	for(Model_Team e: getTeams())
	    if(e.getLeague().equalsIgnoreCase(league) && e.getTeamName().equalsIgnoreCase(team))
		return true;
	return false;
    }

    public void saveYourTeams(ArrayList<Model_Team> teams) {
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
    public ArrayList<Model_Team> getYourTeams() {
	ArrayList<Model_Team> teams = new ArrayList<Model_Team>();

	try {
	    DataInputStream inTeam = new DataInputStream(new FileInputStream(yourTeamStorage));

	    String temp = inTeam.readLine();
	    inTeam.close();

	    if(temp != null) {
		String[] teamsStrings = temp.split(":");

		// Creates a new profile using the key taken from the String
		// array, and adds it to profiles
		for(int i = 0; i < teamsStrings.length; i++) {
		    teams.add(new Model_Team(teamsStrings[i]));
		}
	    }
	    return teams;
	} catch(IOException e) {
	    e.printStackTrace();
	    resetFiles();
	    return teams;
	}
    }

    public void saveTeams(ArrayList<Model_Team> teams) {
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
    public ArrayList<Model_Team> getTeams() {
	ArrayList<Model_Team> teams = new ArrayList<Model_Team>();

	try {
	    DataInputStream inTeam = new DataInputStream(new FileInputStream(allTeamStorage));

	    String temp = inTeam.readLine();
	    inTeam.close();

	    if(temp != null) {
		String[] teamsStrings = temp.split(":");

		for(int i = 0; i < teamsStrings.length; i++) {
		    teams.add(new Model_Team(teamsStrings[i]));
		}
	    }
	    return teams;
	} catch(IOException e) {
	    e.printStackTrace();
	    resetFiles();
	    return teams;
	}
    }

    public void saveGames(ArrayList<Model_Game> games) {
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
    public ArrayList<Model_Game> getGames() {
	ArrayList<Model_Game> games = new ArrayList<Model_Game>();

	try {
	    DataInputStream inGame = new DataInputStream(new FileInputStream(gameStorage));

	    String temp = inGame.readLine();
	    inGame.close();

	    if(temp != null) {
		String[] gamesStrings = temp.split("::");

		for(int i = 0; i < gamesStrings.length; i++) {
		    games.add(new Model_Game(gamesStrings[i]));
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

    public Context getContext() {
	return this.context;
    }

    // Shows the string as a message on the screen
    public void toast(String desc) {
	Toast toast = Toast.makeText(context, desc, Toast.LENGTH_LONG);
	toast.show();
    }
}
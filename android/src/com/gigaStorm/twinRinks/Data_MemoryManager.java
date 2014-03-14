package com.gigaStorm.twinRinks;

import java.util.ArrayList;
import android.content.Context;

/**
 * <code>Data_MemoryManager</code> loads, saves, and manages application data.
 * 
 * @author Andrew Mass
 * @author Boris Dubinsky
 */
public class Data_MemoryManager {

  private Context context;

  private Util util;

  public Data_MemoryManager(Context context) {
    this.context = context;
    this.util = new Util(context);
  }

  public void refreshData() {
    if(util.checkInternet()) {
      Data_FetchTask fetchTask = new Data_FetchTask(this, context);
      fetchTask.execute();
    }
    else {
      util.toast("No Internet Connection Found");
    }
  }

  public void saveUserTeams(ArrayList<Model_Team> teams) {
    Data_DbHelper db = new Data_DbHelper(context);

    db.deleteAllUserTeamRecords();
    for(Model_Team mtrec: teams) {
      db.insertUserTeam(mtrec);
    }

    db.close();
  }

  public ArrayList<Model_Team> getUserTeams() {
    ArrayList<Model_Team> userTeams = null;
    Data_DbHelper db = new Data_DbHelper(context);

    userTeams = (ArrayList<Model_Team>) db.getAllUserTeams();
    if(userTeams == null) {
      userTeams = new ArrayList<Model_Team>();
    }

    db.close();

    return userTeams;
  }

  public ArrayList<Model_Team> getTeams() {
    ArrayList<Model_Team> teams = null;
    Data_DbHelper db = new Data_DbHelper(context);

    teams = (ArrayList<Model_Team>) db.getAllTeams();
    if(teams == null) {
      teams = new ArrayList<Model_Team>();
    }

    db.close();

    return teams;
  }

  public ArrayList<Model_Game> getGames() {
    ArrayList<Model_Game> games = null;
    Data_DbHelper db = new Data_DbHelper(context);

    games = (ArrayList<Model_Game>) db.getAllGames();
    if(games == null) {
      games = new ArrayList<Model_Game>();
    }

    db.close();

    return games;
  }
}

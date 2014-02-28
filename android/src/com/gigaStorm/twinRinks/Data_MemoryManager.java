package com.gigaStorm.twinRinks;

import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import android.content.Context;

/**
 * <code>Data_MemoryManager</code> loads, saves, and manages application data.
 * 
 * @author Andrew Mass
 */
public class Data_MemoryManager {

  private Context context;

  private File dir;

  private File gameStorage;

  private File yourTeamStorage;

  private File allTeamStorage;

  private Util util;

  public Data_MemoryManager(Context context) {
    this.context = context;
    this.util = new Util(context);

    dir = context.getDir("TwinRinksAdultHockey", Context.MODE_PRIVATE);
    gameStorage = new File(dir, "GameStorage");
    yourTeamStorage = new File(dir, "YourTeamStorage");
    allTeamStorage = new File(dir, "AllTeamStorage");
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

  public void setScheduleTable(String[] data) {
    if(data == null) {
      util.err("Data Retrieval Failed");
    }
    else {
      ArrayList<Model_Game> games = getGameList(data);
      saveTeams(sortTeams(parseTeamsFromGames(games)));
      sortGames(games);
      saveGames(games);
    }
  }

  private ArrayList<Model_Game> getGameList(String[] data) {
    ArrayList<Model_Game> list = new ArrayList<Model_Game>();

    for(int i = 0; i < data.length; i++) {
      String[] attrs = data[i].split(",");
      if(attrs.length == 8) {
        list.add(new Model_Game(attrs[0], attrs[2], attrs[3], attrs[4],
            attrs[6], attrs[7], attrs[5]));
      }
    }
    return list;
  }

  public ArrayList<Model_Team> parseTeamsFromGames(ArrayList<Model_Game> games) {
    ArrayList<Model_Team> allTeams = new ArrayList<Model_Team>();
    for(Model_Game e: games) {
      if(!hasTeam(allTeams, e.getLeague(), e.getTeamA())) {
        allTeams.add(new Model_Team(e.getLeague(), e.getTeamA()));
      }
      if(!hasTeam(allTeams, e.getLeague(), e.getTeamH())) {
        allTeams.add(new Model_Team(e.getLeague(), e.getTeamH()));
      }
    }
    return allTeams;
  }

  public ArrayList<Model_Team> sortTeams(ArrayList<Model_Team> teams) {
    Collections.sort(teams, new Comparator<Model_Team>() {
      @Override
      public int compare(Model_Team lhs, Model_Team rhs) {
        int leagueCompLeft;
        String tempLeagueLeft = lhs.getLeague();
        if(tempLeagueLeft.equals("Platinum")) {
          leagueCompLeft = 0;
        }
        else {
          if(tempLeagueLeft.equals("Gold")) {
            leagueCompLeft = 1;
          }
          else {
            if(tempLeagueLeft.equals("Silver")) {
              leagueCompLeft = 2;
            }
            else {
              if(tempLeagueLeft.equals("Bronze")) {
                leagueCompLeft = 3;
              }
              else {
                if(tempLeagueLeft.equals("Leisure")) {
                  leagueCompLeft = 4;
                }
                else {
                  leagueCompLeft = 10;
                }
              }
            }
          }
        }

        int leagueCompRight;
        String tempLeagueRight = rhs.getLeague();
        if(tempLeagueRight.equals("Platinum")) {
          leagueCompRight = 0;
        }
        else {
          if(tempLeagueRight.equals("Gold")) {
            leagueCompRight = 1;
          }
          else {
            if(tempLeagueRight.equals("Silver")) {
              leagueCompRight = 2;
            }
            else {
              if(tempLeagueRight.equals("Bronze")) {
                leagueCompRight = 3;
              }
              else {
                if(tempLeagueRight.equals("Leisure")) {
                  leagueCompRight = 4;
                }
                else {
                  leagueCompRight = 10;
                }
              }
            }
          }
        }

        if(leagueCompLeft > leagueCompRight) {
          return 1;
        }
        else {
          if(leagueCompLeft < leagueCompRight) {
            return -1;
          }
          else {
            if(lhs.getTeamName().equals("PLAYOFFS")) {
              return 1;
            }
            if(rhs.getTeamName().equals("PLAYOFFS")) {
              return 1;
            }
            return lhs.getTeamName().compareTo(rhs.getTeamName());
          }
        }
      }
    });
    return teams;
  }

  private void sortGames(ArrayList<Model_Game> list) {
    Collections.sort(list, new Comparator<Model_Game>() {
      @Override
      public int compare(Model_Game lhs, Model_Game rhs) {
        Date date = lhs.getCalendarObject().getTime();
        return date.compareTo(rhs.getCalendarObject().getTime());
      }
    });
  }

  public boolean
      hasTeam(ArrayList<Model_Team> teams, String league, String team) {
    for(Model_Team e: teams) {
      if(e.getLeague().equalsIgnoreCase(league)
          && e.getTeamName().equalsIgnoreCase(team)) {
        return true;
      }
    }
    return false;
  }

  public void saveYourTeams(ArrayList<Model_Team> teams) {
    try {
      ObjectOutputStream outFile = new ObjectOutputStream(new FileOutputStream(
          yourTeamStorage, false));
      outFile.writeObject(teams);
      outFile.close();
    }
    catch(IOException e) {
      resetFiles();
    }
  }

  @SuppressWarnings("unchecked")
  public ArrayList<Model_Team> getYourTeams() {
    ArrayList<Model_Team> yourTeams = null;
    try {
      ObjectInputStream inFile = new ObjectInputStream(new FileInputStream(
          yourTeamStorage));
      Object obj = inFile.readObject();
      yourTeams = (ArrayList<Model_Team>) obj;
      inFile.close();
    }
    catch(EOFException e) {}
    catch(IOException e1) {
      resetFiles();
    }
    catch(ClassNotFoundException e2) {
      resetFiles();
    }

    if(yourTeams == null) {
      yourTeams = new ArrayList<Model_Team>();
    }

    return yourTeams;
  }

  public void saveTeams(ArrayList<Model_Team> teams) {
    try {
      ObjectOutputStream outFile = new ObjectOutputStream(new FileOutputStream(
          allTeamStorage, false));
      outFile.writeObject(teams);
      outFile.close();
    }
    catch(IOException e) {
      e.printStackTrace();
      resetFiles();
    }
  }

  public ArrayList<Model_Team> getTeams() {
    ArrayList<Model_Team> teams = null;
    try {
      ObjectInputStream inFile = new ObjectInputStream(new FileInputStream(
          allTeamStorage));
      Object obj = inFile.readObject();
      teams = (ArrayList<Model_Team>) obj;
      inFile.close();
    }
    catch(Exception e) {
      resetFiles();
    }

    if(teams == null) {
      teams = new ArrayList<Model_Team>();
    }
    return teams;
  }

  public void saveGames(ArrayList<Model_Game> games) {
    try {
      ObjectOutputStream outFile = new ObjectOutputStream(new FileOutputStream(
          gameStorage, false));
      outFile.writeObject(games);
      outFile.close();
    }
    catch(IOException e) {
      e.printStackTrace();
      resetFiles();
    }
  }

  public ArrayList<Model_Game> getGames() {
    ArrayList<Model_Game> games = null;
    try {
      ObjectInputStream inFile = new ObjectInputStream(new FileInputStream(
          gameStorage));
      games = (ArrayList<Model_Game>) inFile.readObject();
      inFile.close();
    }
    catch(Exception e) {
      resetFiles();
    }

    if(games == null) {
      games = new ArrayList<Model_Game>();
    }
    return games;
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
      DataOutputStream outGame = new DataOutputStream(new FileOutputStream(
          gameStorage, false));
      DataOutputStream outYourTeam = new DataOutputStream(new FileOutputStream(
          yourTeamStorage, false));
      DataOutputStream outAllTeam = new DataOutputStream(new FileOutputStream(
          allTeamStorage, false));

      outYourTeam.close();
      outAllTeam.close();
      outGame.close();

    }
    catch(IOException e) {
      util.err("Failed");
    }
  }
}

package com.gigaStorm.twinRinks;

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

  private Util util;

  public Data_MemoryManager(Context context) {
    // database needs the context, so save it for db use
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

  public void setScheduleTable(String[] data) {
    if(data == null) {
      util.err("Data Retrieval Failed");
    }
    else {
      ArrayList<Model_Game> games = getGameList(data);
      // Not sure if need to sort the stuff anymore since db does it
      // already,
      // but I'll keep it for now.
      sortTeams(parseTeamsFromGames(games));
      sortGames(games);
    }
  }

  private ArrayList<Model_Game> getGameList(String[] data) {
    ArrayList<Model_Game> list = new ArrayList<Model_Game>();

    DbHelper db = new DbHelper(context);
    list = (ArrayList<Model_Game>) db.getAllGames();
    return list;
  }

  public ArrayList<Model_Team> parseTeamsFromGames(ArrayList<Model_Game> games) {

    ArrayList<Model_Team> allTeams = new ArrayList<Model_Team>();
    DbHelper db = new DbHelper(context);

    for(Model_Game e: games) {
      if(!hasTeam(allTeams, e.getLeague(), e.getTeamA())) {
        Model_Team mt = new Model_Team(e.getLeague(), e.getTeamA());
        db.insertRecord(mt);
        allTeams.add(mt);
      }

      if(!hasTeam(allTeams, e.getLeague(), e.getTeamH())) {
        Model_Team mt = new Model_Team(e.getLeague(), e.getTeamH());
        db.insertRecord(mt);
        allTeams.add(mt);
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
    DbHelper db = new DbHelper(context);
    db.deleteAllMyRecords(teams.get(0));
    for(Model_Team mtrec: teams) {
      db.insertMyRecord(mtrec);
    }
  }

  public ArrayList<Model_Team> getYourTeams() {
    ArrayList<Model_Team> yourTeams = null;
    // get teams from db
    DbHelper db = new DbHelper(context);
    yourTeams = (ArrayList<Model_Team>) db.getAllMyTeams();
    if(yourTeams == null) {
      yourTeams = new ArrayList<Model_Team>();
    }

    return yourTeams;
  }

  public ArrayList<Model_Team> getTeams() {
    ArrayList<Model_Team> teams = null;
    DbHelper db = new DbHelper(context);
    teams = (ArrayList<Model_Team>) db.getAllTeams();
    if(teams == null) {
      teams = new ArrayList<Model_Team>();
    }
    return teams;
  }

  public ArrayList<Model_Game> getGames() {
    ArrayList<Model_Game> games = null;
    DbHelper db = new DbHelper(context);
    games = (ArrayList<Model_Game>) db.getAllGames();
    if(games == null) {
      games = new ArrayList<Model_Game>();
    }
    return games;
  }

  public void resetFiles() {
    // saveDefaultValues();
  }

  public Context getContext() {
    return this.context;
  }
}

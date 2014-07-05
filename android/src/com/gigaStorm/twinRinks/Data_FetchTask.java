package com.gigaStorm.twinRinks;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.AsyncTask;

/**
 * <code>Data_FetchTask</code> handles the downloading of game data from the web
 * server.
 * 
 * @author Andrew Mass
 * @author Boris Dubinsky
 * @see AsyncTask
 */
public class Data_FetchTask extends AsyncTask<Void, Integer, Void> {

  private Data_DbHelper dbHelper;

  private ProgressDialog progressDialog;

  private Context parentContext;

  public Data_FetchTask(Data_MemoryManager parent, Context context) {
    super();
    parentContext = context;
    dbHelper = new Data_DbHelper(parentContext);
  }

  @Override
  protected void onPreExecute() {
    super.onPreExecute();
    progressDialog = new ProgressDialog(parentContext);
    progressDialog.setTitle("Fetching Game Data...");
    progressDialog.setMessage("Please Wait...");
    progressDialog.setIndeterminate(true);

    OnClickListener listener = new OnClickListener() {
      @Override
      public void onClick(DialogInterface dialog, int which) {
        dialog.dismiss();
      }
    };

    progressDialog.setButton(ProgressDialog.BUTTON_NEUTRAL, "Cancel", listener);
    progressDialog.show();
  }

  private void
      getLeagueSchedule(String leagueUrl, String league, int leaguePos) {
    Model_Game mg = new Model_Game();
    mg.setBeginTime("");
    mg.setDate("");
    mg.setEndTime("");
    mg.setRink("");
    mg.setTeamA("");
    mg.setTeamH("");

    Logger.i("Data_FetchTask", "Getting data from Twinrinks...");
    String htmlString = "http://twinrinks.com/" + leagueUrl;
    Document doc = null;
    try {
      doc = Jsoup.connect(htmlString).get();
      Elements elems = doc.select("span[style]");

      for(Element src: elems) {

        String line = src.text();
        if(line.length() > 70) {

          String lineTmp = line.trim().replaceAll("\\s+", " ");
          // clean up the line. Remove multiple spaces
          lineTmp = line.trim().replaceAll("\\s+", " ");

          mg.setDate(lineTmp.substring(0, lineTmp.indexOf(' ')));
          lineTmp = lineTmp.substring(lineTmp.indexOf(' ') + 1,
              lineTmp.length());

          mg.setWeekDay(lineTmp.substring(0, lineTmp.indexOf(' ')));
          lineTmp = lineTmp.substring(lineTmp.indexOf(' ') + 1,
              lineTmp.length());

          mg.setRink(lineTmp.substring(0, lineTmp.indexOf(' ')));
          lineTmp = lineTmp.substring(lineTmp.indexOf(' ') + 1,
              lineTmp.length());

          mg.setBeginTime(lineTmp.substring(0, lineTmp.indexOf(' ')));
          if(mg.getBeginTime().length() > 6) {
            // remove first character, which '*'
            mg.setBeginTime(mg.getBeginTime().substring(1,
                mg.getBeginTime().length()));
          }
          lineTmp = lineTmp.substring(lineTmp.indexOf(' ') + 1,
              lineTmp.length());

          mg.setEndTime(lineTmp.substring(0, lineTmp.indexOf(' ')));
          lineTmp = lineTmp.substring(lineTmp.indexOf(' ') + 1,
              lineTmp.length());

          for(int i = 0; i < 3; i++) {
            if(i == leaguePos) {
              mg.setLeague(lineTmp.substring(0, lineTmp.indexOf(' ')));
            }
            else {
              lineTmp = lineTmp.substring(lineTmp.indexOf(' ') + 1,
                  lineTmp.length());
            }
          }
          lineTmp = lineTmp.substring(lineTmp.indexOf(' ') + 1,
              lineTmp.length());

          // During playoff, the team name is followed by "(n)" indicating final standing
          String team = lineTmp.substring(0, lineTmp.indexOf(' '));
          int	indx = team.indexOf('(');
          if (indx > 0) {
        	  team = team.substring(0, team.indexOf('('));
          }
          mg.setTeamH(team.toUpperCase());
          
          lineTmp = lineTmp.substring(lineTmp.indexOf(' ') + 1,
              lineTmp.length());
          if(mg.getTeamH().contains("FINALS") || mg.getTeamH().contains("SEMI")) {
            mg.setTeamH("PLAYOFFS");
          }
          
       // During playoff, the team name is followed by "(n)" indicating final standing
          int sp_loc = lineTmp.indexOf(' ');
          if (sp_loc > 0) {
        	  // The is a second team, but it has some text after the team name
        	  team = lineTmp.substring(0, lineTmp.indexOf(' '));
          } else {
        	  // This is a second team. Make sure it's not play off time
        	  team = lineTmp;
          }
    	  if (team.indexOf('(') > 0)
    		  team = team.substring(0, team.indexOf('('));
    	  mg.setTeamA(team.toUpperCase());
    	  if ( mg.getTeamA().contains ("WIN") || mg.getTeamH().contains ("WIN") ||
    			  mg.getTeamA().contains("FINALS") || mg.getTeamA().contains("SEMI"))
    		  continue;
    	  mg.setTeamA(team.toUpperCase());
          mg.generateCalendarObject();

          /*
           * Check if the date Jan 1st. According to Gary Pivar (owner), there
           * will never be a game on that day because it's a customer
           * appreciation date. TODO: Add that day to every team in every league
           * as customer appreciation.
           */
          String moDay = mg.getDate().substring(0, 5);
          if(moDay.compareTo("01/01") != 0) {
            mg.generateCalendarObject();
            dbHelper.insertGame(mg);
          }
        }
      }
    }
    catch(Exception e) {
      Logger.i("getLeagueSchedule():",
          "Problem getting schedule: " + e.getMessage());
    }

    // Extract all the unique team names
    List<Model_Team> mtTmp = dbHelper.getTeamsFromLeague(league);
    for(Model_Team t: mtTmp) {
      t.setLeague(league);
      dbHelper.insertTeam(t);
    }
  }

  @Override
  protected Void doInBackground(Void... params) {
    // Remove database before we re-populate it.
    // NOTE: My records are not automatically deleted.
    dbHelper.deleteAllGameRecords();
    dbHelper.deleteAllTeamRecords();

    Logger.i("Data_FetchTask", "Getting Leasure data from Twinrinks...");
    getLeagueSchedule("recl/leisure%20schedule.htm", "Leisure", 1);

    Logger.i("Data_FetchTask", "Getting Bronze data from Twinrinks...");
    getLeagueSchedule("recb/bronze%20schedule.htm", "Bronze", 2);

    Logger.i("Data_FetchTask", "Getting Silver data from Twinrinks...");
    getLeagueSchedule("recs/silver%20schedule.htm", "Silver", 2);

    Logger.i("Data_FetchTask", "Getting Gold data from Twinrinks...");
    getLeagueSchedule("recg/gold.schedule.htm", "Gold", 2);

    Logger.i("Data_FetchTask", "Getting Platinum data from Twinrinks...");
    getLeagueSchedule("recp/platinum_sched.htm", "Platinum", 2);

    /*
     * Remove all game records, sort them by date, then add them back into the
     * database. This ensures that the game at the top of any list in the app
     * will be the next game chronologically.
     */
    Data_MemoryManager memoryManager = new Data_MemoryManager(parentContext);
    ArrayList<Model_Game> games = memoryManager.getGames();
    dbHelper.deleteAllGameRecords();

    Collections.sort(games, new Comparator<Model_Game>() {
      @Override
      public int compare(Model_Game a, Model_Game b) {
        if(a.getCal().equals(b.getCal())) {
          return 0;
        }
        return (a.getCal().before(b.getCal())) ? -1 : 1;
      }
    });

    for(Model_Game g: games) {
      dbHelper.insertGame(g);
    }

    return null;
  }

  @Override
  protected void onPostExecute(Void result) {
    dbHelper.close();
    progressDialog.dismiss();
    super.onPostExecute(result);
  }
}

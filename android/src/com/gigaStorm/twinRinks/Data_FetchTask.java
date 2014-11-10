package com.gigaStorm.twinRinks;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.jsoup.Connection;
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
	mg.setDate("00/00/0000");
	mg.setEndTime("");
	mg.setRink("");
	mg.setTeamA("");
	mg.setTeamH("");
	
	Logger.i("Data_FetchTask", "Getting data from Twinrinks...");
	String htmlString =  leagueUrl;
	Document doc = null;
	try {
		Connection.Response res = Jsoup.connect(htmlString).timeout(5000)
				.ignoreHttpErrors(true).followRedirects(true).execute();
			
		doc = res.parse();
		Element table = doc.select("table").get(0);
		Elements rows = table.select("tr");
/*
 * Here is the example of the line that needs parsing:
 * <tr id="tab_0,Leisure_Red,Leisure_Kelly"><td>10/11/2014</td><td>SA</td><td>Blue</td><td>09:10P</td><td>Leisure</td><td>Red</td><td>Kelly</td>
 */
		for (int i = 1; i < rows.size(); i++) { //first row is the col names so skip it.
		    String	tmpStr;
		    
			Element row = rows.get(i);
		    Elements cols = row.select("td");
			
			// get Date
		    tmpStr = cols.get(0).text();
		    if (!tmpStr.isEmpty())
		    	mg.setDate(tmpStr);
			
			// Get Weekday
		    tmpStr = cols.get(1).text();
		    if (!tmpStr.isEmpty())
		    	mg.setWeekDay(tmpStr);
			
			// Get Rink
		    tmpStr = cols.get(2).text();
		    if (!tmpStr.isEmpty())
		    	mg.setRink(tmpStr);

			// Get begin time
		    tmpStr = cols.get(3).text();
		    if (!tmpStr.isEmpty())
		    	mg.setBeginTime(tmpStr);

		    tmpStr = cols.get(4).text();
		    if (!tmpStr.isEmpty())
		    	mg.setLeague(tmpStr);

			// There is no end time
			// mg.setBeginTime(cols.get(4).text());
 
			// During playoff, the team name is followed by "(n)" indicating 
			// final standing
		    
		    String team = cols.get(5).text();
			int	indx = team.indexOf('(');
			if (indx > 0) {
				team = team.substring(0, team.indexOf('('));
			}
	
			if(mg.getTeamH().contains("FINALS") || 
				mg.getTeamH().contains("SEMI")) {
					mg.setTeamH("PLAYOFFS");
			} else
					mg.setTeamH(team.toUpperCase());
	  
			// During playoff, the team name is followed by "(n)" indicating 
			// final standing
			team = cols.get(6).text();
			indx = team.indexOf('(');
			if (indx > 0) {
				team = team.substring(0, team.indexOf('('));
			}
	
			if(mg.getTeamA().contains("FINALS") || 
				mg.getTeamA().contains("SEMI")) {
					mg.setTeamA("PLAYOFFS");
			} else
					mg.setTeamA(team.toUpperCase());
	  
			mg.generateCalendarObject();
	
		  /*
		   * Check if the date Jan 1st. According to Gary Pivar (owner), there
		   * will never be a game on that day because it's a customer
		   * appreciation date. TODO: Add that day to every team in every league
		   * as customer appreciation.
		   */
			if (!mg.getDate().equals("00/00/0000")) {
				String moDay = mg.getDate().substring(0, 5);
				if(moDay.compareTo("01/01") != 0 &&
					moDay.compareTo("1/1") != 0) {
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
    getLeagueSchedule("http://twinrinks.com/recl/leisure%20schedule.php", "Leisure", 1);

    Logger.i("Data_FetchTask", "Getting Bronze data from Twinrinks...");
    getLeagueSchedule("http://twinrinks.com/recb/bronze%20schedule.php", "Bronze", 2);

    Logger.i("Data_FetchTask", "Getting Silver data from Twinrinks...");
    getLeagueSchedule("http://twinrinks.com/recs/silver%20schedule.php", "Silver", 2);

    Logger.i("Data_FetchTask", "Getting Gold data from Twinrinks...");
    getLeagueSchedule("http://twinrinks.com/recg/gold%20schedule.php", "Gold", 2);

    Logger.i("Data_FetchTask", "Getting Platinum data from Twinrinks...");
    getLeagueSchedule("http://twinrinks.com/recp/platinum%20schedule.php", "Platinum", 2);

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

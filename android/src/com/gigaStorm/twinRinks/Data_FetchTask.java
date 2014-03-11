package com.gigaStorm.twinRinks;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.AsyncTask;

/**
 * <code>Data_FetchTask</code> handles the downloading of game data from the web server.
 * 
 * @author Andrew Mass
 * @see AsyncTask
 */
public class Data_FetchTask extends AsyncTask<Void, Integer, Void> {
  ProgressDialog progress;

  Data_MemoryManager parent;
  private DbHelper db;

  Map<String, String> urlMap;

  public Data_FetchTask(Data_MemoryManager p) {
    super();
    parent = p;
    db = new DbHelper(p.getContext());
  }

  @Override
  protected void onPreExecute() {
    super.onPreExecute();
    progress = new ProgressDialog(parent.getContext());
    progress.setTitle("Fetching Game Data...");
    progress.setMessage("Please Wait...");
    progress.setIndeterminate(true);

    OnClickListener listener = new OnClickListener() {
      @Override
      public void onClick(DialogInterface dialog, int which) {
        dialog.dismiss();
      }
    };

    progress.setButton(ProgressDialog.BUTTON_NEUTRAL, "Cancel", listener);
    progress.show();
  }

  private void getLeagueSchedule(String league, Model_Game mg, int leaguePos) {
    mg.setBeginTime("");
    mg.setDate("");
    mg.setEndTime("");
    mg.setRink("");
    mg.setTeamA("");
    mg.setTeamH("");

    Logger.i("Data_FetchTask", "Getting data from Twinrinks...");
    String htmlString = "http://twinrinks.com/" + league;
    Document doc = null;
    try {
      doc = Jsoup.connect(htmlString).get();
      Elements elems = doc.select("span[style]");
      Logger.i("Data_FetchTask", "Number of elements: " + elems.size());

      for(Element src: elems) {
        String line = src.text();
        if(line.length() > 60) {
          String lineTmp = line.trim().replaceAll("\\s+", " ");
          Logger.i("Data_FetchTask", "Data: " + src.text());
          // clean up the line. Remove multiple spaces
          lineTmp = line.trim().replaceAll("\\s+", " ");
          mg.setDate(lineTmp.substring(0, lineTmp.indexOf(' ')));
          lineTmp = lineTmp.substring(lineTmp.indexOf(' ') + 1, lineTmp.length());
          mg.setWeekDay(lineTmp.substring(0, lineTmp.indexOf(' ')));
          lineTmp = lineTmp.substring(lineTmp.indexOf(' ') + 1, lineTmp.length());
          mg.setRink(lineTmp.substring(0, lineTmp.indexOf(' ')));
          lineTmp = lineTmp.substring(lineTmp.indexOf(' ') + 1, lineTmp.length());
          mg.setBeginTime(lineTmp.substring(0, lineTmp.indexOf(' ')));
          if(mg.getBeginTime().length() > 6) {
            // remove first character, which '*'
            mg.setBeginTime(mg.getBeginTime().substring(1, mg.getBeginTime().length()));
          }
          lineTmp = lineTmp.substring(lineTmp.indexOf(' ') + 1, lineTmp.length());
          mg.setEndTime(lineTmp.substring(0, lineTmp.indexOf(' ')));
          lineTmp = lineTmp.substring(lineTmp.indexOf(' ') + 1, lineTmp.length());
          for(int i = 0; i < 3; i++) {
            if(i == leaguePos) {
              mg.setLeague(lineTmp.substring(0, lineTmp.indexOf(' ')));
            }
            else {
              lineTmp = lineTmp.substring(lineTmp.indexOf(' ') + 1, lineTmp.length());
            }
          }
          lineTmp = lineTmp.substring(lineTmp.indexOf(' ') + 1, lineTmp.length());
          mg.setTeamH(lineTmp.substring(0, lineTmp.indexOf(' ')));
          lineTmp = lineTmp.substring(lineTmp.indexOf(' ') + 1, lineTmp.length());
          mg.setTeamA(lineTmp.substring(0, lineTmp.length()));

          mg.generateCalendarObject();

          // Check if the date Jan 1st. According to Gary Pivar
          // (owner), there will
          // never be a game on that day because it's a customer
          // appreciation date.
          // TODO: add that day to every team in every league as
          // customer appreciation

          String moDay = mg.getDate().substring(0, 5);
          if(moDay.compareTo("01/01") != 0) {
            mg.generateCalendarObject();
            db.insertRecord(mg);
          }
        }
      }
    }
    catch(IOException e) {
      Logger.i("getLeagueSchedule():", "Problem getting schedule: " + e.getMessage());
    }
    // Extract all the unique team names
    List<Model_Team> mtTmp = db.getTeamfromGame("WHERE league = '" + mg.getLeague() + "' ");
    for(Model_Team t: mtTmp) {
      t.setLeague(mg.getLeague());
      db.insertRecord(t);
    }
  }

  @Override
  protected Void doInBackground(Void... params) {

    Model_Game mg = new Model_Game();
    Model_Team mt = new Model_Team();
    // Remove database before we re-populate it
    // NOTE: My records are not automatically deleted
    db.deleteAllRecords(mg);
    db.deleteAllRecords(mt);

    Logger.i("Data_FetchTask", "Getting Leasure data from Twinrinks...");
    getLeagueSchedule("recl/leisure%20schedule.htm", mg, 1);

    Logger.i("Data_FetchTask", "Getting Bronze data from Twinrinks...");
    getLeagueSchedule("recb/bronze%20schedule.htm", mg, 2);

    Logger.i("Data_FetchTask", "Getting Silver data from Twinrinks...");
    getLeagueSchedule("recs/silver%20schedule.htm", mg, 2);

    Logger.i("Data_FetchTask", "Getting Gold data from Twinrinks...");
    getLeagueSchedule("recg/gold.schedule.htm", mg, 2);

    Logger.i("Data_FetchTask", "Getting Platinum data from Twinrinks...");
    getLeagueSchedule("recp/platinum_sched.htm", mg, 2);

    // parent.setScheduleTable(data);
    return null;
  }

  @Override
  protected void onPostExecute(Void result) {
    progress.dismiss();
    super.onPostExecute(result);
  }
}

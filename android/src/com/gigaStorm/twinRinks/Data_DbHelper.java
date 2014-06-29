package com.gigaStorm.twinRinks;

import java.util.ArrayList;
import java.util.List;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * <code>Data_DbHelper</code> interfaces with the database and handles data
 * storage and retrieval.
 * 
 * @author Boris Dubinsky
 * @author Andrew Mass
 */
public class Data_DbHelper extends SQLiteOpenHelper {

  public static String TAG = "DbHelper";
  public static String SubTag;

  private static final String DATABASE_NAME = "twinrinks.db";
  private static final int DATABASE_VERSION = 1;

  private static final String GAMES_TABLE_NAME = "games";
  private static final String TEAMS_TABLE_NAME = "teams";
  private static final String USER_TEAMS_TABLE_NAME = "userTeams";

  private static final String ID_KEY = "_id";

  private static final String WEEKDAY_KEY = "weekDay";
  private static final String DATE_KEY = "date";
  private static final String BEGINTIME_KEY = "beginTime";
  private static final String ENDTIME_KEY = "endTime";
  private static final String RINK_KEY = "rink";
  private static final String TEAMH_KEY = "teamH";
  private static final String TEAMA_KEY = "teamA";
  private static final String LEAGUE_KEY = "league";
  private static final String TEAM_KEY = "team";

  public Data_DbHelper(Context context) {
    super(context, DATABASE_NAME, null, DATABASE_VERSION);
  }

  @Override
  public void onCreate(SQLiteDatabase db) {

    SubTag = "onCreate(): ";
    Logger.i(TAG, SubTag + "Creating twinrinks table");

    /*
     * Create games table if it doesn't already exist.
     */
    Cursor c = db.rawQuery(
        "SELECT name FROM sqlite_master WHERE type='table' AND name='"
            + GAMES_TABLE_NAME + "'", null);

    if(c.getCount() == 0) {
      db.execSQL("CREATE TABLE " + GAMES_TABLE_NAME + " (" + ID_KEY
          + " INTEGER PRIMARY KEY AUTOINCREMENT, " + DATE_KEY + " TEXT, "
          + WEEKDAY_KEY + " TEXT, " + BEGINTIME_KEY + " TEXT, " + ENDTIME_KEY
          + " DATE, " + RINK_KEY + " TEXT," + TEAMH_KEY + " TEXT," + TEAMA_KEY
          + " TEXT, " + LEAGUE_KEY + " TEXT );");
    }

    /*
     * Create teams table if it doesn't already exist.
     */
    c = db.rawQuery(
        "SELECT name FROM sqlite_master WHERE type='table' AND name='"
            + TEAMS_TABLE_NAME + "'", null);

    if(c.getCount() == 0) {
      db.execSQL("CREATE TABLE " + TEAMS_TABLE_NAME + " (" + ID_KEY
          + " INTEGER PRIMARY KEY AUTOINCREMENT, " + LEAGUE_KEY + " TEXT, "
          + TEAM_KEY + " TEXT );");
    }

    /*
     * Create user teams table if it doesn't already exits.
     */
    c = db.rawQuery(
        "SELECT name FROM sqlite_master WHERE type='table' AND name='"
            + USER_TEAMS_TABLE_NAME + "'", null);

    if(c.getCount() == 0) {
      db.execSQL("CREATE TABLE " + USER_TEAMS_TABLE_NAME + " (" + ID_KEY
          + " INTEGER PRIMARY KEY AUTOINCREMENT, " + LEAGUE_KEY + " TEXT, "
          + TEAM_KEY + " TEXT );");
    }
  }

  @Override
  public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    SubTag = "onUpgrade(): ";
  }

  public void insertGame(Model_Game mg) {
    SubTag = "insertGame(Game): ";

    try {

      SQLiteDatabase db = this.getWritableDatabase();

      ContentValues vals = new ContentValues();
      vals.put(DATE_KEY, mg.getDate());
      vals.put(WEEKDAY_KEY, mg.getWeekDay());
      vals.put(RINK_KEY, mg.getRink());
      vals.put(BEGINTIME_KEY, mg.getBeginTime());
      vals.put(ENDTIME_KEY, mg.getEndTime());
      vals.put(TEAMH_KEY, mg.getTeamH());
      vals.put(TEAMA_KEY, mg.getTeamA());
      vals.put(LEAGUE_KEY, mg.getLeague());
      db.insert(GAMES_TABLE_NAME, null, vals);
      db.close();
    }
    catch(Exception e) {
      Logger.e(TAG, SubTag + e.getMessage());
    }
  }

  public void insertTeam(Model_Team mt) {
    SubTag = "insertTeam(Team): ";

    try {

      SQLiteDatabase db = this.getWritableDatabase();

      ContentValues vals = new ContentValues();
      // Check if this team already in the table
      String selectQuery = "SELECT * FROM " + TEAMS_TABLE_NAME + " WHERE " 
    		  + TEAM_KEY + " = \"" + mt.getTeamName() + "\" AND "
    		  + LEAGUE_KEY + " = \"" + mt.getLeague()
    		  		+ "\";";
      Cursor cursor = db.rawQuery(selectQuery, null);

      // Looping through all rows and adding to list.
      if(cursor.getCount() == 0) {
    	  vals.put(TEAM_KEY, mt.getTeamName());
    	  vals.put(LEAGUE_KEY, mt.getLeague());
    	  db.insert(TEAMS_TABLE_NAME, null, vals);
      }
      Logger.e(TAG, SubTag + "Inserting team: " + mt.getLeague() + "-" + mt.getTeamName());
      db.close();
    }
    catch(Exception e) {
      Logger.e(TAG, SubTag + e.getMessage());
    }
  }

  public void insertUserTeam(Model_Team mt) {
    SubTag = "insertUserTeam(Team): ";
    try {

      SQLiteDatabase db = this.getWritableDatabase();

      ContentValues vals = new ContentValues();
      vals.put(TEAM_KEY, mt.getTeamName());
      vals.put(LEAGUE_KEY, mt.getLeague());
      db.insert(USER_TEAMS_TABLE_NAME, null, vals);
      db.close();

      Logger.i(TAG, SubTag + "Done inserting team: " + mt.getLeague() + "-" + mt.getTeamName());
    }
    catch(Exception e) {
      Logger.e(TAG, SubTag + e.getMessage());
    }
  }

  public List<Model_Team> getAllUserTeams() {
    SubTag = "getAllUserTeams(): ";

    List<Model_Team> dataList = new ArrayList<Model_Team>();
    try {
      String selectQuery = "SELECT * FROM " + USER_TEAMS_TABLE_NAME + ";";
      SQLiteDatabase db = this.getWritableDatabase();
      Cursor cursor = db.rawQuery(selectQuery, null);

      // Looping through all rows and adding to list.
      if(cursor.moveToFirst()) {
        do {
          Model_Team basicData = new Model_Team();
          basicData.setId(Integer.parseInt(cursor.getString(cursor
              .getColumnIndex(ID_KEY))));
          basicData.setLeague(cursor.getString(cursor
              .getColumnIndex(LEAGUE_KEY)));
          basicData.setTeamName(cursor.getString(cursor
              .getColumnIndex(TEAM_KEY)));

          // Adding basic data to list.
          dataList.add(basicData);
        }
        while(cursor.moveToNext());
        db.close();
      }
    }
    catch(Exception e) {
      Logger.e(TAG, SubTag + e.getMessage());
    }
    return dataList;
  }

  public List<Model_Team> getAllTeams() {
    SubTag = "getAllData(Team): ";

    List<Model_Team> dataList = new ArrayList<Model_Team>();
    try {
      String selectQuery = "SELECT * FROM " + TEAMS_TABLE_NAME + ";";
      SQLiteDatabase db = this.getWritableDatabase();
      Cursor cursor = db.rawQuery(selectQuery, null);

      // Looping through all rows and adding to list.
      if(cursor.moveToFirst()) {
        do {
          Model_Team basicData = new Model_Team();
          basicData.setId(Integer.parseInt(cursor.getString(cursor
              .getColumnIndex(ID_KEY))));
          basicData.setLeague(cursor.getString(cursor
              .getColumnIndex(LEAGUE_KEY)));
          basicData.setTeamName(cursor.getString(cursor
              .getColumnIndex(TEAM_KEY)));

          // Adding basic data to list.
          dataList.add(basicData);
        }
        while(cursor.moveToNext());
        db.close();
      }
    }
    catch(Exception e) {
      Logger.e(TAG, SubTag + e.getMessage());
    }
    return dataList;
  }

  public List<Model_Game> getAllGames() {
    SubTag = "getAllData(): ";

    List<Model_Game> dataList = new ArrayList<Model_Game>();
    try {
      String selectQuery = "SELECT * FROM " + GAMES_TABLE_NAME + ";";
      SQLiteDatabase db = this.getWritableDatabase();
      Cursor cursor = db.rawQuery(selectQuery, null);

      // Looping through all rows and adding to list.
      if(cursor.moveToFirst()) {
        do {
          Model_Game basicData = new Model_Game();
          basicData.setId(Integer.parseInt(cursor.getString(cursor
              .getColumnIndex(ID_KEY))));
          basicData.setDate(cursor.getString(cursor.getColumnIndex(DATE_KEY)));
          basicData.setWeekDay(cursor.getString(cursor
              .getColumnIndex(WEEKDAY_KEY)));
          basicData.setBeginTime(cursor.getString(cursor
              .getColumnIndex(BEGINTIME_KEY)));
          basicData.setEndTime(cursor.getString(cursor
              .getColumnIndex(ENDTIME_KEY)));
          basicData.setRink(cursor.getString(cursor.getColumnIndex(RINK_KEY)));
          basicData
              .setTeamA(cursor.getString(cursor.getColumnIndex(TEAMA_KEY)));
          basicData
              .setTeamH(cursor.getString(cursor.getColumnIndex(TEAMH_KEY)));
          basicData.setLeague(cursor.getString(cursor
              .getColumnIndex(LEAGUE_KEY)));

          basicData.setCal(basicData.generateCalendarObject());
          // Adding basic data to list.
          dataList.add(basicData);
        }
        while(cursor.moveToNext());
        db.close();
      }
    }
    catch(Exception e) {
      Logger.e(TAG, SubTag + e.getMessage());
    }
    return dataList;
  }

  public List<Model_Team> getTeamsFromLeague(String league) {
    SubTag = "getTeamsFromLeague(" + league + "): ";

    List<Model_Team> dataList = new ArrayList<Model_Team>();
    try {
      String selectQuery = "SELECT Distinct " + TEAMH_KEY + " FROM "
          + GAMES_TABLE_NAME + " WHERE league = '" + league + "' ";
      SQLiteDatabase db = this.getWritableDatabase();
      Cursor cursor = db.rawQuery(selectQuery, null);

      // Looping through all rows and adding to list.
      if(cursor.moveToFirst()) {
        do {
          Model_Team basicData = new Model_Team();
          basicData.setTeamName(cursor.getString(cursor
              .getColumnIndex(TEAMH_KEY)));
          basicData.setLeague(league);
          dataList.add(basicData);
        }
        while(cursor.moveToNext());
        
        // In case some teams are only Away team (ex: during playoff time)
        selectQuery = "SELECT Distinct " + TEAMA_KEY + " FROM "
                + GAMES_TABLE_NAME + " WHERE league = '" + league + "' ";
        cursor = db.rawQuery(selectQuery, null);

        // Looping through all rows and adding to list.
        if(cursor.moveToFirst()) {
        	do {
                Model_Team basicData = new Model_Team();
                basicData.setTeamName(cursor.getString(cursor
                    .getColumnIndex(TEAMA_KEY)));
                basicData.setLeague(league);
                if (!dataList.contains(basicData))
                	dataList.add(basicData);
              }
              while(cursor.moveToNext());
        }
        db.close();
      }
    }
    catch(Exception e) {
      Logger.e(TAG, SubTag + e.getMessage());
    }
    return dataList;
  }

  public void deleteAllGameRecords() {
    SubTag = "deleteAllGameRecords(): ";

    try {
      SQLiteDatabase db = this.getWritableDatabase();
      db.delete(GAMES_TABLE_NAME, null, null);
      db.close();
    }
    catch(Exception e) {
      Logger.e(TAG, SubTag + e.getMessage());
    }
  }

  public void deleteAllTeamRecords() {
    SubTag = "deleteAllTeamRecords(): ";
    try {
      SQLiteDatabase db = this.getWritableDatabase();
      db.delete(TEAMS_TABLE_NAME, null, null);
      db.close();
    }
    catch(Exception e) {
      Logger.e(TAG, SubTag + e.getMessage());
    }
  }

  public void deleteAllUserTeamRecords() {
    SubTag = "deleteAllUserTeamRecords(): ";

    try {
      SQLiteDatabase db = this.getWritableDatabase();
      db.delete(USER_TEAMS_TABLE_NAME, null, null);
      db.close();
    }
    catch(Exception e) {
      Logger.e(TAG, SubTag + e.getMessage());
    }
  }
}

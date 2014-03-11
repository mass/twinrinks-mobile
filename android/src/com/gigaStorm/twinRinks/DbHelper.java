
package com.gigaStorm.twinRinks;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * @author boris
 */
public class DbHelper extends SQLiteOpenHelper {

    public static String TAG = "DbHelper";
    public static String SubTag;

    private static final String DATABASE_NAME = "twinrinks.db";
    private static final int DATABASE_VERSION = 1;

    private static final String GAME_TABLE_NAME = "Model_Game";
    private static final String TEAM_TABLE_NAME = "Model_Team";
    private static final String MY_TEAMS_TABLE_NAME = "My_Teams";

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

    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * @param context
     * @param name
     * @param factory
     * @param version
     */
    public DbHelper(Context context, String name, CursorFactory factory,
            int version) {
        super(context, name, factory, version);
    }

    /**
     * @param context
     * @param name
     * @param factory
     * @param version
     * @param errorHandler
     */
    public DbHelper(Context context, String name, CursorFactory factory,
            int version, DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
    }

    /*
     * (non-Javadoc)
     * @see
     * android.database.sqlite.SQLiteOpenHelper#onCreate(android.database.sqlite
     * .SQLiteDatabase)
     */
    @Override
    public void onCreate(SQLiteDatabase db) {

        SubTag = "onCreate(): ";
        Logger.i(TAG, SubTag + "Creating twinrinks table");
        Cursor c = db.rawQuery(
                "SELECT name FROM sqlite_master WHERE type='table' AND name='"
                        + GAME_TABLE_NAME + "'", null);

        if (c.getCount() == 0) {
            db.execSQL("CREATE TABLE " + GAME_TABLE_NAME
                    + " (" + ID_KEY + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + DATE_KEY + " TEXT, " + WEEKDAY_KEY + " TEXT, " + BEGINTIME_KEY + " TEXT, "
                    + ENDTIME_KEY + " DATE, "
                    + RINK_KEY + " TEXT," + TEAMH_KEY + " TEXT," + TEAMA_KEY + " TEXT, "
                    + LEAGUE_KEY + " TEXT );");
        }

        c = db.rawQuery(
                "SELECT name FROM sqlite_master WHERE type='table' AND name='"
                        + TEAM_TABLE_NAME + "'", null);

        if (c.getCount() == 0) {
            db.execSQL("CREATE TABLE " + TEAM_TABLE_NAME
                    + " (" + ID_KEY + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + LEAGUE_KEY + " TEXT, " + TEAM_KEY + " TEXT );");
        }

        c = db.rawQuery(
                "SELECT name FROM sqlite_master WHERE type='table' AND name='"
                        + MY_TEAMS_TABLE_NAME + "'", null);

        if (c.getCount() == 0) {
            db.execSQL("CREATE TABLE " + MY_TEAMS_TABLE_NAME
                    + " (" + ID_KEY + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + LEAGUE_KEY + " TEXT, " + TEAM_KEY + " TEXT );");
        }
    }

    /*
     * (non-Javadoc)
     * @see
     * android.database.sqlite.SQLiteOpenHelper#onUpgrade(android.database.sqlite
     * .SQLiteDatabase, int, int)
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        SubTag = "onUpgrade(): ";
    }

    public void deleteTbl(Model_Game mg) {
        SubTag = "deleteTbl(Game): ";
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            String sql = "DROP TABLE IF EXISTS " + GAME_TABLE_NAME + ";";
            db.execSQL(sql);
            // Create empty table, so insert would not fail
            onCreate(db);
        } catch (Exception e) {
            Logger.e(TAG, SubTag + e.getMessage());
        }
    }

    public void deleteTbl(Model_Team mt) {
        SubTag = "deleteTbl(Team): ";
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            String sql = "DROP TABLE IF EXISTS " + TEAM_TABLE_NAME + ";";
            db.execSQL(sql);
            // Create empty table, so insert would not fail
            onCreate(db);
        } catch (Exception e) {
            Logger.e(TAG, SubTag + e.getMessage());
        }
    }

    public void deleteMyTbl(Model_Team mt) {
        SubTag = "deleteTbl(Team): ";
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            String sql = "DROP TABLE IF EXISTS " + MY_TEAMS_TABLE_NAME + ";";
            db.execSQL(sql);
            // Create empty table, so insert would not fail
            onCreate(db);
        } catch (Exception e) {
            Logger.e(TAG, SubTag + e.getMessage());
        }
    }

    public void insertRecord(Model_Game mg) {
        SubTag = "insert(Game): ";
        try {
            Logger.i(TAG, SubTag + "Inserting a record");

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
            db.insert(GAME_TABLE_NAME, null, vals);
            db.close();
            Logger.i(TAG, SubTag + "Done inserting a record");

        } catch (Exception e) {
            Logger.e(TAG, SubTag + e.getMessage());
        }
    }

    public void insertRecord(Model_Game mg, ContentValues values) {
        SubTag = "insert(Game): ";
        try {
            Logger.i(TAG, SubTag + "Inserting a record");

            SQLiteDatabase db = this.getWritableDatabase();

            db.insert(GAME_TABLE_NAME, null, values);
            db.close();
            Logger.i(TAG, SubTag + "Done inserting a record");

        } catch (Exception e) {
            Logger.e(TAG, SubTag + e.getMessage());
        }
    }

    public void insertRecord(Model_Team mt) {
        SubTag = "insert(Team): ";
        try {
            Logger.i(TAG, SubTag + "Inserting a record");

            SQLiteDatabase db = this.getWritableDatabase();

            ContentValues vals = new ContentValues();

            vals.put(TEAM_KEY, mt.getTeamName());
            vals.put(LEAGUE_KEY, mt.getLeague());
            db.insert(TEAM_TABLE_NAME, null, vals);
            db.close();

            Logger.i(TAG, SubTag + "Done inserting a record");

        } catch (Exception e) {
            Logger.e(TAG, SubTag + e.getMessage());
        }
    }

    public void insertMyRecord(Model_Team mt) {
        SubTag = "insert(Team): ";
        try {
            Logger.i(TAG, SubTag + "Inserting a record");

            SQLiteDatabase db = this.getWritableDatabase();

            ContentValues vals = new ContentValues();

            vals.put(TEAM_KEY, mt.getTeamName());
            vals.put(LEAGUE_KEY, mt.getLeague());
            db.insert(MY_TEAMS_TABLE_NAME, null, vals);
            db.close();

            Logger.i(TAG, SubTag + "Done inserting a record");

        } catch (Exception e) {
            Logger.e(TAG, SubTag + e.getMessage());
        }
    }

    public void insertRecord(Model_Team mt, ContentValues values) {
        SubTag = "insert(Team): ";
        try {
            Logger.i(TAG, SubTag + "Inserting a record");

            SQLiteDatabase db = this.getWritableDatabase();

            db.insert(TEAM_TABLE_NAME, null, values);
            db.close();
            Logger.i(TAG, SubTag + "Done inserting a record");

        } catch (Exception e) {
            Logger.e(TAG, SubTag + e.getMessage());
        }
    }

    public void insertMyRecord(Model_Team mt, ContentValues values) {
        SubTag = "insert(MyTeam): ";
        try {
            Logger.i(TAG, SubTag + "Inserting a record");

            SQLiteDatabase db = this.getWritableDatabase();

            db.insert(MY_TEAMS_TABLE_NAME, null, values);
            db.close();
            Logger.i(TAG, SubTag + "Done inserting a record");

        } catch (Exception e) {
            Logger.e(TAG, SubTag + e.getMessage());
        }
    }

    public List<Model_Team> getAllMyTeams() {
        List<Model_Team> dataList = new ArrayList<Model_Team>();
        SubTag = "getAllData(Team): ";
        try {
            String selectQuery = "SELECT * FROM " + MY_TEAMS_TABLE_NAME + ";";
            SQLiteDatabase db = this.getWritableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, null);

            // looping through all rows and adding to list
            if (cursor.moveToFirst()) {
                do {
                    Model_Team basicData = new Model_Team();
                    basicData.setId(Integer.parseInt(cursor.getString(cursor
                            .getColumnIndex(ID_KEY))));
                    basicData.setLeague(cursor.getString(cursor
                            .getColumnIndex(LEAGUE_KEY)));
                    basicData.setTeamName(cursor.getString(cursor
                            .getColumnIndex(TEAM_KEY)));

                    // Adding basic data to list
                    dataList.add(basicData);
                } while (cursor.moveToNext());
                db.close();
            }
        } catch (Exception e) {
            Logger.e(TAG, SubTag + e.getMessage());
        }
        return dataList;
    }

    public List<Model_Team> getAllTeams() {
        List<Model_Team> dataList = new ArrayList<Model_Team>();
        SubTag = "getAllData(Team): ";
        try {
            String selectQuery = "SELECT * FROM " + TEAM_TABLE_NAME + ";";
            SQLiteDatabase db = this.getWritableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, null);

            // looping through all rows and adding to list
            if (cursor.moveToFirst()) {
                do {
                    Model_Team basicData = new Model_Team();
                    basicData.setId(Integer.parseInt(cursor.getString(cursor
                            .getColumnIndex(ID_KEY))));
                    basicData.setLeague(cursor.getString(cursor
                            .getColumnIndex(LEAGUE_KEY)));
                    basicData.setTeamName(cursor.getString(cursor
                            .getColumnIndex(TEAM_KEY)));

                    // Adding basic data to list
                    dataList.add(basicData);
                } while (cursor.moveToNext());
                db.close();
            }
        } catch (Exception e) {
            Logger.e(TAG, SubTag + e.getMessage());
        }
        return dataList;
    }

    public List<Model_Game> getAllGames() {
        List<Model_Game> dataList = new ArrayList<Model_Game>();
        SubTag = "getAllData(): ";
        try {
            String selectQuery = "SELECT * FROM " + GAME_TABLE_NAME + ";";
            SQLiteDatabase db = this.getWritableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, null);

            // looping through all rows and adding to list
            if (cursor.moveToFirst()) {
                do {
                    Model_Game basicData = new Model_Game();
                    basicData.setId(Integer.parseInt(cursor.getString(cursor
                            .getColumnIndex(ID_KEY))));
                    basicData.setDate(cursor.getString(cursor
                            .getColumnIndex(DATE_KEY)));
                    basicData.setWeekDay(cursor.getString(cursor
                            .getColumnIndex(WEEKDAY_KEY)));
                    basicData.setBeginTime(cursor.getString(cursor
                            .getColumnIndex(BEGINTIME_KEY)));
                    basicData.setEndTime(cursor.getString(cursor
                            .getColumnIndex(ENDTIME_KEY)));
                    basicData.setRink(cursor.getString(cursor
                            .getColumnIndex(RINK_KEY)));
                    basicData.setTeamA(cursor.getString(cursor
                            .getColumnIndex(TEAMA_KEY)));
                    basicData.setTeamH(cursor.getString(cursor
                            .getColumnIndex(TEAMH_KEY)));
                    basicData.setLeague(cursor.getString(cursor
                            .getColumnIndex(LEAGUE_KEY)));

                    basicData.setCal(basicData.generateCalendarObject());
                    // Adding basic data to list
                    dataList.add(basicData);
                } while (cursor.moveToNext());
                db.close();
            }
        } catch (Exception e) {
            Logger.e(TAG, SubTag + e.getMessage());
        }
        return dataList;
    }

    public List<Model_Game> getGames(String sql) {
        List<Model_Game> dataList = new ArrayList<Model_Game>();
        SubTag = "getAllData(" + sql + "): ";
        try {
            String selectQuery = "SELECT * FROM " + GAME_TABLE_NAME + " "
                    + sql;
            SQLiteDatabase db = this.getWritableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, null);

            // looping through all rows and adding to list
            if (cursor.moveToFirst()) {
                do {
                    Model_Game basicData = new Model_Game();
                    basicData.setId(Integer.parseInt(cursor.getString(cursor
                            .getColumnIndex(ID_KEY))));
                    basicData.setDate(cursor.getString(cursor
                            .getColumnIndex(DATE_KEY)));
                    basicData.setWeekDay(cursor.getString(cursor
                            .getColumnIndex(WEEKDAY_KEY)));
                    basicData.setBeginTime(cursor.getString(cursor
                            .getColumnIndex(BEGINTIME_KEY)));
                    basicData.setEndTime(cursor.getString(cursor
                            .getColumnIndex(ENDTIME_KEY)));
                    basicData.setRink(cursor.getString(cursor
                            .getColumnIndex(RINK_KEY)));
                    basicData.setTeamH(cursor.getString(cursor
                            .getColumnIndex(TEAMH_KEY)));
                    basicData.setTeamA(cursor.getString(cursor
                            .getColumnIndex(TEAMA_KEY)));
                    basicData.setLeague(cursor.getString(cursor
                            .getColumnIndex(LEAGUE_KEY)));
                    basicData.setCal(basicData.generateCalendarObject());

                    // Adding basic data to list
                    dataList.add(basicData);
                } while (cursor.moveToNext());
                db.close();
            }
        } catch (Exception e) {
            Logger.e(TAG, SubTag + e.getMessage());
        }

        return dataList;
    }

    public List<Model_Team> getTeamfromGame(String sql) {
        List<Model_Team> dataList = new ArrayList<Model_Team>();
        SubTag = "getAllData(" + sql + "): ";
        try {
            String selectQuery = "SELECT Distinct " + TEAMH_KEY + " FROM " + GAME_TABLE_NAME + " "
                    + sql + ";";
            SQLiteDatabase db = this.getWritableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, null);

            // looping through all rows and adding to list
            if (cursor.moveToFirst()) {
                do {
                    Model_Team basicData = new Model_Team();
                    basicData.setTeamName(cursor.getString(cursor
                            .getColumnIndex(TEAMH_KEY)));
                    dataList.add(basicData);
                } while (cursor.moveToNext());
                db.close();
            }
        } catch (Exception e) {
            Logger.e(TAG, SubTag + e.getMessage());
        }
        return dataList;
    }

    public int getRecordCount(Model_Game mg) {
        SubTag = "getRecordCount(Game): ";
        int cnt = 0;
        try {
            String countQuery = "SELECT  * FROM " + GAME_TABLE_NAME + ";";
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.rawQuery(countQuery, null);
            cnt = cursor.getCount();
            cursor.close();
            db.close();
        } catch (Exception e) {
            Logger.e(TAG, SubTag + e.getMessage());
        }
        return cnt;
    }

    public int getRecordCount(Model_Team mt) {
        SubTag = "getRecordCount(Team): ";
        int cnt = 0;
        try {
            String countQuery = "SELECT  * FROM " + TEAM_TABLE_NAME + ";";
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.rawQuery(countQuery, null);
            cnt = cursor.getCount();
            cursor.close();
            db.close();
        } catch (Exception e) {
            Logger.e(TAG, SubTag + e.getMessage());
        }
        return cnt;
    }

    public int getMyRecordCount(Model_Team mt) {
        SubTag = "getRecordCount(Team): ";
        int cnt = 0;
        try {
            String countQuery = "SELECT  * FROM " + MY_TEAMS_TABLE_NAME + ";";
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.rawQuery(countQuery, null);
            cnt = cursor.getCount();
            cursor.close();
            db.close();
        } catch (Exception e) {
            Logger.e(TAG, SubTag + e.getMessage());
        }
        return cnt;
    }

    public void deleteAllRecords(Model_Game bd) {
        SubTag = "deleteAllRecords(Game): ";
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            db.delete(GAME_TABLE_NAME, null, null);
            db.close();
        } catch (Exception e) {
            Logger.e(TAG, SubTag + e.getMessage());
        }
    }

    public void deleteAllRecords(Model_Team bd) {
        SubTag = "deleteAllRecords(Team): ";
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            db.delete(TEAM_TABLE_NAME, null, null);
            db.close();
        } catch (Exception e) {
            Logger.e(TAG, SubTag + e.getMessage());
        }
    }

    public void deleteAllMyRecords(Model_Team bd) {
        SubTag = "deleteAllRecords(Team): ";
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            db.delete(MY_TEAMS_TABLE_NAME, null, null);
            db.close();
        } catch (Exception e) {
            Logger.e(TAG, SubTag + e.getMessage());
        }
    }

    public void deleteRecord(Model_Game bd) {
        SubTag = "deleteRecord(Game): ";
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            db.delete(GAME_TABLE_NAME, ID_KEY + " = ?",
                    new String[] {
                        String.valueOf(bd.getId())
                    });
            db.close();
        } catch (Exception e) {
            Logger.e(TAG, SubTag + e.getMessage());
        }
    }

    public void deleteRecord(Model_Team bd) {
        SubTag = "deleteGameRecord(Team): ";
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            db.delete(TEAM_TABLE_NAME, ID_KEY + " = ?",
                    new String[] {
                        String.valueOf(bd.getId())
                    });
            db.close();
        } catch (Exception e) {
            Logger.e(TAG, SubTag + e.getMessage());
        }
    }

    public void deleteMyRecord(Model_Team bd) {
        SubTag = "deleteGameRecord(Team): ";
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            db.delete(MY_TEAMS_TABLE_NAME, ID_KEY + " = ?",
                    new String[] {
                        String.valueOf(bd.getId())
                    });
            db.close();
        } catch (Exception e) {
            Logger.e(TAG, SubTag + e.getMessage());
        }
    }

    public void deleteRecord(int recId, Model_Game bd) {
        SubTag = "deleteRecord(recId,Game): ";
        if (recId < 0) {
            recId = bd.getId();
        }
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            db.delete(GAME_TABLE_NAME, ID_KEY + " = ?",
                    new String[] {
                        String.valueOf(recId)
                    });
            db.close();
        } catch (Exception e) {
            Logger.e(TAG, SubTag + e.getMessage());
        }
    }

    public void deleteRecord(int recId, Model_Team bd) {
        SubTag = "deleteRecord(recId,Team): ";
        if (recId < 0) {
            recId = bd.getId();
        }
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            db.delete(TEAM_TABLE_NAME, ID_KEY + " = ?",
                    new String[] {
                        String.valueOf(recId)
                    });
            db.close();
        } catch (Exception e) {
            Logger.e(TAG, SubTag + e.getMessage());
        }
    }

    public void deleteMyRecord(int recId, Model_Team bd) {
        SubTag = "deleteRecord(recId,Team): ";
        if (recId < 0) {
            recId = bd.getId();
        }
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            db.delete(MY_TEAMS_TABLE_NAME, ID_KEY + " = ?",
                    new String[] {
                        String.valueOf(recId)
                    });
            db.close();
        } catch (Exception e) {
            Logger.e(TAG, SubTag + e.getMessage());
        }
    }

    public int updateRecord(Model_Game bd) {
        SubTag = "updateGameRecord(Game): ";
        int rc;
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(ID_KEY, bd.getId());
        values.put(DATE_KEY, bd.getDate());
        values.put(WEEKDAY_KEY, bd.getWeekDay());
        values.put(BEGINTIME_KEY, bd.getBeginTime());
        values.put(ENDTIME_KEY, bd.getEndTime());
        values.put(RINK_KEY, bd.getRink());
        values.put(TEAMH_KEY, bd.getTeamH());
        values.put(TEAMA_KEY, bd.getTeamA());
        values.put(LEAGUE_KEY, bd.getLeague());

        // updating row
        rc = db.update(GAME_TABLE_NAME, values, ID_KEY + " = ?",
                new String[] {
                    String.valueOf(bd.getId())
                });
        db.close();
        return rc;
    }

    public int updateRecord(Model_Team bd) {
        SubTag = "updateTeamRecord(Team): ";
        int rc;
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(ID_KEY, bd.getId());
        values.put(TEAM_KEY, bd.getTeamKey());
        values.put(LEAGUE_KEY, bd.getLeague());

        // updating row
        rc = db.update(TEAM_TABLE_NAME, values, ID_KEY + " = ?",
                new String[] {
                    String.valueOf(bd.getId())
                });
        db.close();
        return rc;
    }

    public int updateMyRecord(Model_Team bd) {
        SubTag = "updateTeamRecord(MyTeam): ";
        int rc;
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(ID_KEY, bd.getId());
        values.put(TEAM_KEY, bd.getTeamKey());
        values.put(LEAGUE_KEY, bd.getLeague());

        // updating row
        rc = db.update(MY_TEAMS_TABLE_NAME, values, ID_KEY + " = ?",
                new String[] {
                    String.valueOf(bd.getId())
                });
        db.close();
        return rc;
    }
}

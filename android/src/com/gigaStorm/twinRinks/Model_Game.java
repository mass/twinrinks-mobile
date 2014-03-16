package com.gigaStorm.twinRinks;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * <code>Model_Game</code> represents a game.
 * 
 * @author Andrew Mass
 * @see Serializable
 */
public class Model_Game {

  private Integer id; // Used by database
  private String date;
  private String weekDay;
  private String beginTime;
  private String endTime;
  private String rink;
  private String teamH;
  private String teamA;
  private String league;
  private Calendar cal;

  public Model_Game() {}

  public Model_Game(String d, String r, String bt, String et, String th,
      String ta, String l) {
    date = d.replaceAll("\\s", "");
    beginTime = bt.replaceAll("\\s", "") + "M";
    endTime = et.replaceAll("\\s", "");
    rink = r.replaceAll("\\s", "");
    league = l.replaceAll("\\s", "");
    teamH = th.replaceAll("\\s", "");
    teamA = ta.replaceAll("\\s", "");
    cal = generateCalendarObject();
  }

  /**
   * Provides a more user-friendly string representing the date.
   * 
   * @return the formatted date string.
   */
  public String getFullDateString() {
    switch(this.cal.get(Calendar.DAY_OF_WEEK)) {
      case 1:
        return "Sunday, " + date + ", at " + beginTime;
      case 2:
        return "Monday, " + date + ", at " + beginTime;
      case 3:
        return "Tuesday, " + date + ", at " + beginTime;
      case 4:
        return "Wednesday, " + date + ", at " + beginTime;
      case 5:
        return "Thursday, " + date + ", at " + beginTime;
      case 6:
        return "Friday, " + date + ", at " + beginTime;
      case 7:
        return "Saturday, " + date + ", at " + beginTime;
      default:
        return "Failed";
    }
  }

  /**
   * Checks to see if the game is in the past.
   * <p>
   * Adds a two-hour grace-period so we can see games that have happened in the
   * past two hours.
   * </p>
   * 
   * @return boolean representing whether this game is in the past.
   */
  public boolean hasPassed() {
    Calendar now = Calendar.getInstance();
    now.add(Calendar.HOUR, -2);
    return this.cal.before(now);
  }

  // 05/31/13;08:55PM
  SimpleDateFormat format = new SimpleDateFormat("MM/dd/yy;hh:mmaa", Locale.US);

  /**
   * Outputs a <code>Calendar</code> object based on the game's date string
   * which gives the game's start time.
   * 
   * @return a <code>Calendar</code> object representing this game's start time.
   */
  public Calendar generateCalendarObject() {
    Calendar cal = Calendar.getInstance();

    SimpleDateFormat format = new SimpleDateFormat("MM/dd/yy;hh:mma", Locale.US);
    Date date = new Date();

    try {
      date = format.parse(this.date + ";" + this.beginTime + "M");
    }
    catch(ParseException e) {
      Logger.i("Model_Game", "generateCalendarObject(): " + e.getMessage());
      System.err.println("Failed to parse date string");
    }

    cal.setTime(date);
    return cal;
  }

  @Override
  public String toString() {
    return "League: " + league + "\nDate: " + date + "\nRink: " + rink
        + "\nBegin: " + beginTime + "\nEnd: " + endTime + "\nHome: " + teamH
        + "\nAway: " + teamA;
  }

  public String getDate() {
    return date;
  }

  public String getRink() {
    return rink;
  }

  public String getTeamH() {
    return teamH;
  }

  public String getTeamA() {
    return teamA;
  }

  public String getLeague() {
    return league;
  }

  public String getBeginTime() {
    return beginTime;
  }

  public void setBeginTime(String beginTime) {
    this.beginTime = beginTime;
  }

  public String getEndTime() {
    return endTime;
  }

  public void setEndTime(String endTime) {
    this.endTime = endTime;
  }

  public Calendar getCal() {
    return cal;
  }

  public void setCal(Calendar cal) {
    this.cal = cal;
  }

  public void setDate(String date) {
    this.date = date;
  }

  public void setRink(String rink) {
    this.rink = rink;
  }

  public void setTeamH(String teamH) {
    this.teamH = teamH;
  }

  public void setTeamA(String teamA) {
    this.teamA = teamA;
  }

  public void setLeague(String league) {
    this.league = league;
  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer idP) {
    id = idP;
  }

  public String getWeekDay() {
    return weekDay;
  }

  public void setWeekDay(String weekDay) {
    this.weekDay = weekDay;
  }
}

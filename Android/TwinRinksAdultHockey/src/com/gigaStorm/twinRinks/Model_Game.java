package com.gigaStorm.twinRinks;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

// Wrapper class for an object representing a game
public class Model_Game {
    private String date;
    private String beginTime;
    private String endTime;
    private String rink;
    private String teamH;
    private String teamA;
    private String league;
    private Calendar cal;

    public Model_Game(String backing) {
	String[] data = backing.split(";");
	date = data[0];
	beginTime = data[1];
	endTime = data[2];
	rink = data[3];
	teamH = data[4];
	teamA = data[5];
	league = data[6];
	cal = generateCalendarObject();
    }

    public Model_Game(String d,String r,String bt,String et,String th,String ta,String l) {
	// Clear whitespace
	date = d.replaceAll("\\s", "");
	beginTime = bt.replaceAll("\\s", "");
	endTime = et.replaceAll("\\s", "");
	rink = r.replaceAll("\\s", "");
	league = l.replaceAll("\\s", "");

	String temp = th.replaceAll("\\s", "");
	if(temp.equalsIgnoreCase("PLAYOFFS") || temp.equalsIgnoreCase("SEMI") || temp.equalsIgnoreCase("FINALS")) {
	    teamH = "PLAYOFF";
	    teamA = "GAME";
	} else {
	    teamH = th.replaceAll("\\s", "");
	    teamA = ta.replaceAll("\\s", "");
	}

	cal = generateCalendarObject();
    }

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
	}
	return "Failed";
    }

    public boolean hasPassed() {
	Calendar now = Calendar.getInstance();

	if(this.cal.getTimeInMillis() + 7200000 <= now.getTimeInMillis())
	    return true;
	return false;
    }

    private Calendar generateCalendarObject() {
	Calendar cal = Calendar.getInstance();

	// 05/31/13;08:55PM
	SimpleDateFormat format = new SimpleDateFormat("MM/dd/yy;hh:mma", Locale.US);
	Date date = new Date();
	try {
	    date = format.parse(this.date + ";" + this.beginTime + "M");
	} catch(ParseException e) {
	    e.printStackTrace();
	}
	cal.setTime(date);
	return cal;
    }

    public Calendar getCalendarObject() {
	return this.cal;
    }

    public String getGameKey() {
	return date + ";" + beginTime + ";" + endTime + ";" + rink + ";" + teamH + ";" + teamA + ";" + league;
    }

    public String toString() {
	return "League: " + league + "\nDate: " + date + "\nRink: " + rink + "\nBegin: " + beginTime + "\nEnd: " + endTime + "\nHome: " + teamH + "\nAway: " + teamA;
    }

    public String getDate() {
	return date;
    }

    public String getBeginTime() {
	return beginTime;
    }

    public String getEndTime() {
	return endTime;
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
}
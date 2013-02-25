package com.gigaStorm.twinRinks;

import android.text.format.Time;

public class Game {
    private String date;
    private String beginTime;
    private String endTime;
    private String rink;
    private String teamH;
    private String teamA;
    private String league;

    public Game(String backing) {
	String[] data = backing.split(";");
	date = data[0];
	beginTime = data[1];
	endTime = data[2];
	rink = data[3];
	teamH = data[4];
	teamA = data[5];
	league = data[6];
    }

    public Game(String d,String r,String bt,String et,String th,String ta,String l) {
	date = d.replaceAll("\\s", "");
	beginTime = bt.replaceAll("\\s", "");
	endTime = et.replaceAll("\\s", "");
	rink = r.replaceAll("\\s", "");
	league = l.replaceAll("\\s", "");

	
	
	String temp = th.replaceAll("\\s","");
	if(temp.equalsIgnoreCase("PLAYOFFS") || temp.equalsIgnoreCase("SEMI") || temp.equalsIgnoreCase("FINALS")) {
	    teamH = "PLAYOFF";
	    teamA = "GAME";
	} else {
	    teamH = th.replaceAll("\\s", "");
	    teamA = ta.replaceAll("\\s", "");
	}
    }

    public String getFullDateString() {
	switch(getTimeObject().weekDay) {
	    case 1:
		return "Monday, " + date + ", at " + beginTime;
	    case 2:
		return "Tuesday, " + date + ", at " + beginTime;
	    case 3:
		return "Wednesday, " + date + ", at " + beginTime;
	    case 4:
		return "Thursday, " + date + ", at " + beginTime;
	    case 5:
		return "Friday, " + date + ", at " + beginTime;
	    case 6:
		return "Saturday, " + date + ", at " + beginTime;
	    case 0:
		return "Sunday, " + date + ", at " + beginTime;
	}
	return "Failed";
    }

    public boolean hasPassed() {
	Time currentTime = new Time();
	currentTime.setToNow();

	if(this.getTimeObject().toMillis(false) + 7200000 <= currentTime.toMillis(false))
	    return true;
	return false;
    }

    public Time getTimeObject() {
	String[] data = date.split("/");
	String[] data2 = beginTime.substring(0, 5).split(":");
	Time time = new Time();

	// time.setToNow();
	time.set(0, Integer.parseInt(data2[1]), Integer.parseInt(data2[0]) + 12, Integer.parseInt(data[1]), Integer.parseInt(data[0]) - 1, 2000 + Integer.parseInt(data[2]));
	time.normalize(false);
	return time;
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

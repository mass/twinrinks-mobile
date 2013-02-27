package com.gigaStorm.twinRinks;

// Wrapper class for an object representing a team
public class Team {

    private String league;
    private String teamName;

    public Team(String l,String n) {
	league = l;
	teamName = n;
    }

    public Team(String backing) {
	String[] data = backing.split(";");
	league = data[0];
	teamName = data[1];
    }

    public String getTeamKey() {
	return league + ";" + teamName;
    }

    public String getLeague() {
	return league;
    }

    public String getTeamName() {
	return teamName;
    }

    public String toString() {
	return league + "-" + teamName;
    }
}
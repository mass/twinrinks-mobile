
package com.gigaStorm.twinRinks;

import java.io.Serializable;

//Wrapper class for an object representing a team
public class Model_Team implements Serializable {
    private static final long serialVersionUID = 8308588163969826346L;

    private String league;

    private String teamName;

    public Model_Team(String l, String n) {
        league = l;
        teamName = n;
    }

    public Model_Team(String backing) {
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

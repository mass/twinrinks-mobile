
package com.gigaStorm.twinRinks;

import java.io.Serializable;

/**
 * <code>Model_Team</code> represents a team.
 * 
 * @author Andrew Mass
 * @see Serializable
 */
public class Model_Team implements Serializable {
    private static final long serialVersionUID = 8308588163969826346L;

    private String league;

    private Integer Id;

    private String teamName;

    public Model_Team() {
    }

    public Model_Team(String l, String n) {
        league = l;
        teamName = n;
    }

    public Model_Team(String key) {
        String[] data = key.split(";");
        league = data[0];
        teamName = data[1];
    }

    public Integer getId() {
        return Id;
    }

    public void setId(Integer id) {
        Id = id;
    }

    public String getTeamKey() {
        return league + ";" + teamName;
    }

    public String getLeague() {
        return league;
    }

    public void setLeague(String league) {
        this.league = league;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    @Override
    public String toString() {
        return league + "-" + teamName;
    }
}

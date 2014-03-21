package com.gigaStorm.twinRinks;

import java.util.ArrayList;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import com.actionbarsherlock.app.SherlockFragment;

/**
 * <code>Fragment_Upcoming</code> shows the user's upcoming games in date-sorted
 * order.
 * 
 * @author Andrew Mass
 * @see Fragment
 */
public class Fragment_Upcoming extends SherlockFragment {

  private Button btn_upcoming_goToAddTeams;

  private ArrayList<Model_Team> yourTeams;

  private ArrayList<Model_Game> games;

  private Data_MemoryManager memoryManager;

  private ListView listView_upcoming_main;

  private View view;

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    view = inflater.inflate(R.layout.layout_frag_upcoming, container, false);

    memoryManager = new Data_MemoryManager(getActivity());
    games = memoryManager.getGames();

    /*
     * Refresh game data if there are no loaded games, or the latest game is in
     * the past.
     */
    if(games.size() <= 0 || games.get(games.size() - 1).hasPassed()) {
      memoryManager.refreshData();
      games = memoryManager.getGames();
    }

    btn_upcoming_goToAddTeams = (Button) view
        .findViewById(R.id.btn_upcoming_goToAddTeams);
    btn_upcoming_goToAddTeams.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        startActivity(new Intent(getActivity().getApplicationContext(),
            Activity_Settings.class));
      }
    });
    return view;
  }

  @Override
  public void onResume() {
    yourTeams = memoryManager.getUserTeams();
    if(yourTeams.size() > 0) {
      prepareGames();
    }
    super.onResume();
  }

  private void prepareGames() {
    ArrayList<Model_Game> gamesToAdd = new ArrayList<Model_Game>();
    for(Model_Game g: games) {
      for(Model_Team t: yourTeams) {
        if((g.getTeamA().equalsIgnoreCase(t.getTeamName()) || g.getTeamH()
            .equalsIgnoreCase(t.getTeamName()))
            && g.getLeague().equalsIgnoreCase(t.getLeague()) && !g.hasPassed()) {
          gamesToAdd.add(g);
        }
      }
    }

    String[] values = new String[gamesToAdd.size()];
    for(int i = 0; i < values.length; i++) {
      values[i] = gamesToAdd.get(i).toString();
    }

    Data_ArrayAdapter adapter = new Data_ArrayAdapter(getActivity(),
        gamesToAdd, values);
    listView_upcoming_main = (ListView) view
        .findViewById(R.id.listView_upcoming_main);
    listView_upcoming_main.setAdapter(adapter);

    if(!yourTeams.isEmpty() && btn_upcoming_goToAddTeams != null) {
      ((LinearLayout) btn_upcoming_goToAddTeams.getParent()).getChildAt(0)
          .setVisibility(View.GONE);
    }
  }
}

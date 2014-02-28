package com.gigaStorm.twinRinks;

import java.util.ArrayList;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
<<<<<<< HEAD
 * <code>Data_ArrayAdapter</code> creates views for the upcoming and schedule fragments.
=======
 * <code>Data_ArrayAdapter</code> creates views for the upcoming and schedule
 * fragments.
>>>>>>> 188a691... Fix data fetch bug.
 * 
 * @author Andrew Mass
 * @see ArrayAdapter
 */
public class Data_ArrayAdapter extends ArrayAdapter<String> {

  private Context context;

  private ArrayList<Model_Game> games;

  public Data_ArrayAdapter(Context context, ArrayList<Model_Game> games,
      String[] values) {
    super(context, R.id.listView_upcoming_main, values);
    this.context = context;
    this.games = games;
  }

  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
    LayoutInflater inflater = (LayoutInflater) context
        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    View rowView = inflater.inflate(R.layout.view_game, parent, false);

    Model_Game tempGame = games.get(position);

    String topText = tempGame.getTeamH() + " vs " + tempGame.getTeamA();
    String middleText = tempGame.getLeague() + " League - "
        + tempGame.getRink() + " Rink";
    String bottomText = tempGame.getFullDateString();

    if(tempGame.getTeamH().equals("PLAYOFFS")) {
      topText = "PLAYOFF GAME";
    }

<<<<<<< HEAD
    ((TextView) rowView.findViewById(R.id.textView_gameview_top)).setText(topText);
    ((TextView) rowView.findViewById(R.id.textView_gameview_middle)).setText(middleText);
    ((TextView) rowView.findViewById(R.id.textView_gameview_bottom)).setText(bottomText);
=======
    ((TextView) rowView.findViewById(R.id.textView_gameview_top))
        .setText(topText);
    ((TextView) rowView.findViewById(R.id.textView_gameview_middle))
        .setText(middleText);
    ((TextView) rowView.findViewById(R.id.textView_gameview_bottom))
        .setText(bottomText);
>>>>>>> 188a691... Fix data fetch bug.

    return rowView;
  }
}

package com.gigaStorm.twinRinks;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class GameDisplay extends RelativeLayout {
    private Context context;
    private TextView leagueView;
    private TextView awayView;
    private TextView rinkView;
    private TextView homeView;
    private TextView dateView;

    public GameDisplay(Context context) {
	super(context);
	this.context = context;
	init();
    }

    public GameDisplay(Context context,AttributeSet attrs) {
	super(context, attrs);
	this.context = context;
	init();
    }

    public GameDisplay(Context context,AttributeSet attrs,int default_style) {
	super(context, attrs, default_style);
	this.context = context;
	init();
    }

    private void init() {
	LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	layoutInflater.inflate(R.layout.gameview, this);
	leagueView = (TextView) this.findViewById(R.id.leagueView);
	dateView = (TextView) this.findViewById(R.id.dateView);
	awayView = (TextView) this.findViewById(R.id.awayView);
	rinkView = (TextView) this.findViewById(R.id.rinkView);
	homeView = (TextView) this.findViewById(R.id.homeView);
    }

    public void setGame(Game g) {
	leagueView.setText(g.getLeague());
	dateView.setText(g.getFullDateString());
	awayView.setText(g.getTeamA());
	rinkView.setText(g.getRink() + " Rink");
	homeView.setText(g.getTeamH());
    }
}

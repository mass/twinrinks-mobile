package com.gigaStorm.twinRinks;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Map;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.AsyncTask;

public class DataFetchTask extends AsyncTask<Void,Integer,String[]> {
    Dialog progress;
    MainActivity parent;
    Map<String,String> urlMap;

    public DataFetchTask(MainActivity p) {
	super();
	parent = p;
    }

    @Override
    protected void onPreExecute() {
	super.onPreExecute();
	progress = ProgressDialog.show(parent, "Fetching Game Data...", "Please Wait...", true);
    }

    @Override
    protected String[] doInBackground(Void... params) {
	String[] data;

	try {
	    URL textURL = new URL("http://www.avp42.com/zzz/bar.txt");

	    BufferedReader bufferReader = new BufferedReader(new InputStreamReader(textURL.openStream()));
	    String StringBuffer;
	    String stringText = "";
	    while((StringBuffer = bufferReader.readLine()) != null) {
		stringText += StringBuffer;
	    }
	    bufferReader.close();

	    data = stringText.split(";");
	} catch(IOException e) {
	    return null;
	}
	return data;
    }

    @Override
    protected void onPostExecute(String[] result) {
	super.onPostExecute(result);
	progress.dismiss();
	parent.setScheduleTable(result);
    }
}

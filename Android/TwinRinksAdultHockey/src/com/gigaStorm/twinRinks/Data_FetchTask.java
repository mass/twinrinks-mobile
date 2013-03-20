package com.gigaStorm.twinRinks;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Map;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.AsyncTask;

// Class which handles the downloading of data from the web server
public class Data_FetchTask extends AsyncTask<Void,Integer,String[]> {
    Dialog progress;
    Data_MemoryManager parent;
    Map<String,String> urlMap;

    public Data_FetchTask(Data_MemoryManager p) {
	super();
	parent = p;
    }

    // Called first
    @Override
    protected void onPreExecute() {
	super.onPreExecute();
	progress = ProgressDialog.show(parent.getContext(), "Fetching Game Data...", "Please Wait...", true);
    }

    // Called second
    @Override
    protected String[] doInBackground(Void... params) {
	String[] data;

	try {
	    URL textURL = new URL("http://themasster12.github.com/twinrinks-mobile/ScheduleData.txt");

	    BufferedReader bufferReader = new BufferedReader(new InputStreamReader(textURL.openStream()));
	    String StringBuffer;
	    String stringText = "";

	    while((StringBuffer = bufferReader.readLine()) != null) {
		stringText += StringBuffer;
	    }
	    bufferReader.close();

	    data = stringText.split(";");
	} catch(IOException e) {
	    e.printStackTrace();
	    return null;
	}
	return data;
    }

    // Called last
    @Override
    protected void onPostExecute(String[] result) {
	super.onPostExecute(result);
	parent.setScheduleTable(result);
	progress.dismiss();
	
    }
}

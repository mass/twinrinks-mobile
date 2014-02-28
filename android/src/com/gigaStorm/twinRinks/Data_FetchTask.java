package com.gigaStorm.twinRinks;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.AsyncTask;

/**
 * <code>Data_FetchTask</code> handles the downloading of game data from the web
 * server.
 * 
 * @author Andrew Mass
 * @see AsyncTask
 */
public class Data_FetchTask extends AsyncTask<Void, Integer, Void> {

  private ProgressDialog progress;

  private Data_MemoryManager parent;

  private Context parentContext;

  private String[] fetchData;

  public Data_FetchTask(Data_MemoryManager p, Context context) {
    super();
    parent = p;
    parentContext = context;
  }

  @Override
  protected void onPreExecute() {
    super.onPreExecute();
    progress = new ProgressDialog(parentContext);
    progress.setTitle("Fetching Game Data...");
    progress.setMessage("Please Wait...");
    progress.setIndeterminate(true);

    OnClickListener listener = new OnClickListener() {
      @Override
      public void onClick(DialogInterface dialog, int which) {
        dialog.dismiss();
      }
    };

    progress.setButton(ProgressDialog.BUTTON_NEUTRAL, "Cancel", listener);
    progress.show();
  }

  @Override
  protected Void doInBackground(Void... params) {

    try {
      URL textURL = new URL(parentContext.getResources().getString(
          R.string.data_url));
      BufferedReader bufferReader = new BufferedReader(new InputStreamReader(
          textURL.openStream()));
      String StringBuffer;
      String stringText = "";

      while((StringBuffer = bufferReader.readLine()) != null)
        stringText += StringBuffer;

      bufferReader.close();
      fetchData = stringText.split(";");
    }
    catch(IOException e) {
      fetchData = null;
    }

    return null;
  }

  @Override
  protected void onPostExecute(Void result) {
    parent.setScheduleTable(fetchData);
    progress.dismiss();
    super.onPostExecute(result);
  }
}

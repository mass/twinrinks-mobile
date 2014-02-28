package com.gigaStorm.twinRinks;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Map;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.AsyncTask;

// Class which handles the downloading of data from the web server
public class Data_FetchTask extends AsyncTask<Void, Integer, Void> {
  ProgressDialog progress;

  Data_MemoryManager parent;

  Map<String, String> urlMap;

  public Data_FetchTask(Data_MemoryManager p) {
    super();
    parent = p;
  }

  @Override
  protected void onPreExecute() {
    super.onPreExecute();
    progress = new ProgressDialog(parent.getContext());
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
    String[] data;

    try {
      URL textURL = new URL("raw.github.com/mass/twinrinks-mobile/master/ScheduleData.txt");
      BufferedReader bufferReader = new BufferedReader(new InputStreamReader(textURL.openStream()));
      String StringBuffer;
      String stringText = "";

      while((StringBuffer = bufferReader.readLine()) != null)
        stringText += StringBuffer;

      bufferReader.close();
      data = stringText.split(";");
    }
    catch(IOException e) {
      e.printStackTrace();
      data = null;
    }

    parent.setScheduleTable(data);
    return null;
  }

  @Override
  protected void onPostExecute(Void result) {
    progress.dismiss();
    super.onPostExecute(result);
  }
}

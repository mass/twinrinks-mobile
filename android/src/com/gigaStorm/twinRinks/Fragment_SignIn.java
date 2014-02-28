package com.gigaStorm.twinRinks;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import com.actionbarsherlock.app.SherlockFragment;

// Fragment which takes the user to the sub sign-in system
public class Fragment_SignIn extends SherlockFragment {

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.layout_frag_signin, container, false);

    String autoLogInUsername = PreferenceManager.getDefaultSharedPreferences(
        getActivity().getApplicationContext()).getString("autoLogInUsername", "Null");
    String autoLogInPassword = PreferenceManager.getDefaultSharedPreferences(
        getActivity().getApplicationContext()).getString("autoLogInPassword", "Null");
    boolean autoLogInCheckbox = PreferenceManager.getDefaultSharedPreferences(
        getActivity().getApplicationContext()).getBoolean("autoLogInCheckbox", false);

    WebView webView = (WebView) view.findViewById(R.id.webView_subsigninmain_mainWebView);
    WebViewClient webViewClient = new WebViewClient();
    webViewClient.shouldOverrideUrlLoading(webView,
        "http://www.twinrinks.com/adulthockey/subs/subs_entry.html");
    webView.setWebViewClient(webViewClient);
    webView.getSettings().setBuiltInZoomControls(true);

    if(checkInternet()) {
      if(autoLogInCheckbox)
        webView.loadUrl("http://www.twinrinks.com/adulthockey/subs/subs_entry.php?subs_data1="
            + autoLogInUsername + "&subs_data2=" + autoLogInPassword);
      else
        webView.loadUrl("http://www.twinrinks.com/adulthockey/subs/subs_entry.html");
    }
    else
      webView.loadData(
          "This application requires a valid internet connection to properly function.",
          "text/html", "utf-8");
    return view;
  }

  public boolean checkInternet() {
    ConnectivityManager connec = (ConnectivityManager) getActivity().getSystemService(
        Context.CONNECTIVITY_SERVICE);
    NetworkInfo wifi = connec.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
    NetworkInfo mobile = connec.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

    if(wifi != null && wifi.isConnected() || mobile != null & mobile.isConnected())
      return true;

    return false;
  }
}

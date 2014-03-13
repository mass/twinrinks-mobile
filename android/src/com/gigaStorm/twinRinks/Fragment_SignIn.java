package com.gigaStorm.twinRinks;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import com.actionbarsherlock.app.SherlockFragment;

/**
 * <code>Fragment_SignIn</code> takes the user to the online sub sign-in system.
 * 
 * @author Andrew Mass
 * @see Fragment
 */
public class Fragment_SignIn extends SherlockFragment {

  private Util util;

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.layout_frag_signin, container, false);

    util = new Util(getActivity());

    String autoLogInUsername = PreferenceManager.getDefaultSharedPreferences(
        getActivity().getApplicationContext()).getString("autoLogInUsername",
        "Null");
    String autoLogInPassword = PreferenceManager.getDefaultSharedPreferences(
        getActivity().getApplicationContext()).getString("autoLogInPassword",
        "Null");
    boolean autoLogInCheckbox = PreferenceManager.getDefaultSharedPreferences(
        getActivity().getApplicationContext()).getBoolean("autoLogInCheckbox",
        false);

    WebView webView = (WebView) view
        .findViewById(R.id.webView_subsigninmain_mainWebView);
    WebViewClient webViewClient = new WebViewClient();
    webViewClient.shouldOverrideUrlLoading(webView,
        "http://www.twinrinks.com/adulthockey/subs/subs_entry.html");
    webView.setWebViewClient(webViewClient);
    webView.getSettings().setBuiltInZoomControls(true);

    if(util.checkInternet()) {
      if(autoLogInCheckbox) {
        webView
            .loadUrl("http://www.twinrinks.com/adulthockey/subs/subs_entry.php?subs_data1="
                + autoLogInUsername + "&subs_data2=" + autoLogInPassword);
      }
      else {
        webView
            .loadUrl("http://www.twinrinks.com/adulthockey/subs/subs_entry.html");
      }
    }
    else {
      webView
          .loadData(
              "This application requires a valid internet connection to properly function.",
              "text/html", "utf-8");
    }
    return view;
  }
}

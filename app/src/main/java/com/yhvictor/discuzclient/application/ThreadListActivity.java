package com.yhvictor.discuzclient.application;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.yhvictor.discuzclient.R;
import com.yhvictor.discuzclient.setting.SettingsFragment;
import com.yhvictor.discuzclient.threadlist.ThreadListFragment;
import com.yhvictor.discuzclient.webview.WebViewFragment;

public class ThreadListActivity extends AppCompatActivity {

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    DiscuzClientApplication.appComponent.inject(this);
    super.onCreate(savedInstanceState);

    setContentView(R.layout.activity_threadlist);
    getSupportActionBar().hide();

    getSupportFragmentManager()
        .beginTransaction()
        .replace(R.id.fragment, new SettingsFragment())
        .commit();
  }

  public void navigateToRecentThreadList() {
    getSupportFragmentManager()
        .beginTransaction()
        .replace(R.id.fragment, new ThreadListFragment())
        .addToBackStack(null)
        .commit();
  }

  public void navigateToWebView(Uri uri) {
    WebViewFragment webViewFragment = new WebViewFragment();
    webViewFragment.loadUrl(uri.toString());

    getSupportFragmentManager()
        .beginTransaction()
        .replace(R.id.fragment, webViewFragment)
        .addToBackStack(null)
        .commit();
  }
}

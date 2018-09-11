package com.yhvictor.discuzclient.application;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.inputmethod.InputMethodManager;

import com.yhvictor.discuzclient.R;
import com.yhvictor.discuzclient.setting.SettingsFragment;
import com.yhvictor.discuzclient.threadlist.ThreadListFragment;
import com.yhvictor.discuzclient.webview.WebViewFragment;

/**
 * App entry activity.
 *
 * <p>TODO(yh_victor): handle incoming urls.
 */
public class FragmentActivity extends AppCompatActivity {

  private Snackbar snackbar;

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    DiscuzClientApplication.appComponent.inject(this);
    super.onCreate(savedInstanceState);

    setContentView(R.layout.activity_threadlist);
    getSupportActionBar().hide();

    snackbar = Snackbar.make(findViewById(android.R.id.content), "", Snackbar.LENGTH_LONG);

    getSupportFragmentManager()
        .beginTransaction()
        .replace(R.id.fragment, new SettingsFragment())
        .commit();
  }

  @Override
  protected void onDestroy() {
    snackbar = null;
    super.onDestroy();
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

  public void snackbar(String text) {
    snackbar.setText(text);
    snackbar.show();
  }

  public void hideKeyboard() {
    InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
    imm.hideSoftInputFromWindow(
        findViewById(android.R.id.content).getRootView().getWindowToken(), 0);
  }
}

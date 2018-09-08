package com.yhvictor.discuzclient;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.google.common.base.Function;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.yhvictor.discuzclient.application.DiscuzClientApplication;
import com.yhvictor.discuzclient.debug.Logger;
import com.yhvictor.discuzclient.discuzapi.DiscuzApi;
import com.yhvictor.discuzclient.discuzapi.data.DiscuzThread;
import com.yhvictor.discuzclient.discuzapi.data.ForumDisplay;
import com.yhvictor.discuzclient.discuzapi.data.RecentThreadList;
import com.yhvictor.discuzclient.webview.WebViewActivity;

import java.util.List;

import javax.inject.Inject;

public class MainActivity extends AppCompatActivity {
  @Inject
  DiscuzApi discuzApi;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    DiscuzClientApplication.appComponent.inject(this);

    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
  }

  public void onButtonClick(View view) {
    Intent intent = new Intent();
    intent.setAction(Intent.ACTION_VIEW);
    intent.setClassName(this.getPackageName(), WebViewActivity.class.getCanonicalName());
    intent.setDataAndType(Uri.parse("http://95.179.161.95/"), "text/html");
    startActivity(intent);
  }

  public void onButton2Click(View view) {
    ListenableFuture<List<DiscuzThread>> threads =
        Futures.transform(discuzApi.listRecentThread(2, 0, 100), RecentThreadList::getThreads);

    Futures.addCallback(
        threads,
        new FutureCallback<List<DiscuzThread>>() {
          @Override
          public void onSuccess(List<DiscuzThread> result) {
            for (DiscuzThread thread : result) {
              Logger.d(thread.author + ": " + thread.subject);
            }
          }

          @Override
          public void onFailure(Throwable t) {
            Log.i("yh_victor", "ERROR", t);
          }
        });
  }

  private void forumDisplay() {
    Futures.transform(
        discuzApi.forumDisplay(2, 1),
        (Function<ForumDisplay, Object>)
            forumDisplay -> {
              for (DiscuzThread thread : forumDisplay.getThreads()) {
                Log.i("yhvictor", thread.author + ": " + thread.subject);
              }
              return null;
            });
  }
}

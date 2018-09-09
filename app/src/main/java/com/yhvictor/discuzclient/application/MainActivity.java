package com.yhvictor.discuzclient.application;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.google.common.util.concurrent.FluentFuture;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.MoreExecutors;
import com.yhvictor.discuzclient.R;
import com.yhvictor.discuzclient.debug.Logger;
import com.yhvictor.discuzclient.discuzapi.DiscuzApi;
import com.yhvictor.discuzclient.util.json.Json;
import com.yhvictor.discuzclient.util.json.JsonUtil;

import java.util.List;

import javax.inject.Inject;

public class MainActivity extends AppCompatActivity {
  @Inject DiscuzApi discuzApi;

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
    FluentFuture.from(discuzApi.listRecentThread(2, 0, 100))
        .transformAsync(JsonUtil::transform, MoreExecutors.directExecutor())
        .transform(json -> json.optArray("Variables", "data"), MoreExecutors.directExecutor())
        .addCallback(
            new FutureCallback<List<Json>>() {
              @Override
              public void onSuccess(List<Json> result) {
                for (Json thread : result) {
                  Logger.d(thread.optString("author") + ": " + thread.optString("subject"));
                }
              }

              @Override
              public void onFailure(Throwable t) {
                Log.i("yh_victor", "ERROR", t);
              }
            },
            MoreExecutors.directExecutor());
  }
}

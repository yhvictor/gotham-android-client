package com.yhvictor.discuzclient.application;

import android.app.Application;

public class DiscuzClientApplication extends Application {
  public static AppComponent appComponent;

  @Override
  public void onCreate() {
    super.onCreate();

    appComponent = DaggerAppComponent.builder().application(this).build();
  }
}

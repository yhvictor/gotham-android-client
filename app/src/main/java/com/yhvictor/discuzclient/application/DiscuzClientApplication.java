package com.yhvictor.discuzclient.application;

import android.app.Application;

/**
 * Application entry for whole app.
 *
 * <p>TODO(yh_victor): consider using activity / fragmnet injection.
 */
public class DiscuzClientApplication extends Application {
  public static AppComponent appComponent;

  @Override
  public void onCreate() {
    super.onCreate();

    appComponent = DaggerAppComponent.builder().application(this).build();
  }
}

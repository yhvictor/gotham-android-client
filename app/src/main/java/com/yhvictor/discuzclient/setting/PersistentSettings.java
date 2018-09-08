package com.yhvictor.discuzclient.setting;

import android.app.Application;
import android.content.SharedPreferences;

import javax.inject.Inject;

import static android.content.Context.MODE_PRIVATE;

public class PersistentSettings {
  private final SharedPreferences sharedPreferences;

  @Inject
  PersistentSettings(Application application) {
    sharedPreferences = application.getSharedPreferences("settings", MODE_PRIVATE);
    setUsername("yh_victor");
    setPassword("EGquahuelpawyi0");
  }

  public String getUsername() {
    return sharedPreferences.getString("username", "");
  }

  public void setUsername(String username) {
    sharedPreferences.edit().putString("username", username).apply();
  }

  public String getPassword() {
    return sharedPreferences.getString("password", "");
  }

  public void setPassword(String password) {
    sharedPreferences.edit().putString("password", password).apply();
  }
}

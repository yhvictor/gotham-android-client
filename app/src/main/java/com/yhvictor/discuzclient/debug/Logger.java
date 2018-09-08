package com.yhvictor.discuzclient.debug;

import android.util.Log;

public class Logger {
  public static final Logger logger = new Logger();

  private Logger() {
  }

  public static void d(Object log) {
    Log.d("yh_victor", log == null ? "[NULL]" : log.toString());
  }

  public static void d(String log, Throwable e) {
    Log.v("yh_victor", log, e);
  }
}

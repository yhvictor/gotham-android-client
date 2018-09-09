package com.yhvictor.discuzclient.debug;

import android.util.Log;

import com.google.common.base.Function;

public class Logger {

  private Logger() {}

  public static void d(Object log) {
    Log.d("yh_victor", log == null ? "[NULL]" : log.toString());
  }

  public static void d(String log, Throwable e) {
    Log.v("yh_victor", log, e);
  }

  public static <T> Function<T, T> echo(Injectable<T> injectable) {
    return input -> {
      injectable.inject(input);
      return input;
    };
  }

  public interface Injectable<T> {
    void inject(T object);
  }
}

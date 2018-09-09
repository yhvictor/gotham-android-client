package com.yhvictor.discuzclient.util.concurrency;

import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;

import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;

import java.util.List;
import java.util.concurrent.AbstractExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class CommonExecutors {
  private static final ListeningExecutorService POOL_EXECUTOR =
      MoreExecutors.listeningDecorator(Executors.newCachedThreadPool());
  private static final Handler UI_HANDLER = new Handler(Looper.getMainLooper());
  private static final ListeningExecutorService UI_EXECUTOR =
      MoreExecutors.listeningDecorator(
          new AbstractExecutorService() {

            @Override
            public void execute(@NonNull Runnable command) {
              UI_HANDLER.post(command);
            }

            @Override
            public void shutdown() {
              throw new RuntimeException();
            }

            @NonNull
            @Override
            public List<Runnable> shutdownNow() {
              throw new RuntimeException();
            }

            @Override
            public boolean isShutdown() {
              throw new RuntimeException();
            }

            @Override
            public boolean isTerminated() {
              throw new RuntimeException();
            }

            @Override
            public boolean awaitTermination(long timeout, @NonNull TimeUnit unit) {
              throw new RuntimeException();
            }
          });

  private CommonExecutors() {}

  public static ListeningExecutorService poolExecutor() {
    return POOL_EXECUTOR;
  }

  public static ListeningExecutorService uiExecutor() {
    return UI_EXECUTOR;
  }
}

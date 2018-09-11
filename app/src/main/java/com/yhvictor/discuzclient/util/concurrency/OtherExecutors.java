package com.yhvictor.discuzclient.util.concurrency;

import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;

import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;

import java.util.List;
import java.util.concurrent.AbstractExecutorService;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/** Provides other executors like ui_thread and background pool. */
public class OtherExecutors {

  // TODO(yh_victor): no thread switch if still in pool.
  /** Background pool executor */
  private static final ListeningExecutorService POOL_EXECUTOR =
      MoreExecutors.listeningDecorator(
          new ThreadPoolExecutor(
              2, Integer.MAX_VALUE, 30L, TimeUnit.SECONDS, new SynchronousQueue<>()));

  /** UI executor */
  private static final ListeningExecutorService UI_EXECUTOR =
      MoreExecutors.listeningDecorator(
          new AbstractExecutorService() {
            private Handler uiHandler = new Handler(Looper.getMainLooper());

            @Override
            public void execute(@NonNull Runnable command) {
              if (Looper.myLooper() == Looper.getMainLooper()) {
                command.run();
              } else {
                uiHandler.post(command);
              }
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

  private OtherExecutors() {}

  public static ListeningExecutorService poolExecutor() {
    return POOL_EXECUTOR;
  }

  public static ListeningExecutorService uiExecutor() {
    return UI_EXECUTOR;
  }
}

package com.yhvictor.discuzclient.util.concurrency;

import com.google.common.util.concurrent.ListeningExecutorService;

import java.util.concurrent.Executor;

import dagger.Module;
import dagger.Provides;

@Module
public class ConcurrencyModule {

  @Provides
  Executor executor() {
    return OtherExecutors.poolExecutor();
  }

  @Provides
  ListeningExecutorService listeningExecutorService() {
    return OtherExecutors.poolExecutor();
  }
}

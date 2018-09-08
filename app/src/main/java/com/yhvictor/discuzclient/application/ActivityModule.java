package com.yhvictor.discuzclient.application;

import com.yhvictor.discuzclient.MainActivity;
import com.yhvictor.discuzclient.annotation.ActivityScope;
import com.yhvictor.discuzclient.threadlist.ThreadListActivity;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public interface ActivityModule {

  @ActivityScope
  @ContributesAndroidInjector
  MainActivity mainActivity();

  @ActivityScope
  @ContributesAndroidInjector
  ThreadListActivity threadListActivity();
}

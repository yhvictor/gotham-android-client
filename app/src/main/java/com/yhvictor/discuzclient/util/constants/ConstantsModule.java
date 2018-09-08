package com.yhvictor.discuzclient.util.constants;

import com.yhvictor.discuzclient.annotation.HostName;

import dagger.Module;
import dagger.Provides;

@Module
public class ConstantsModule {

  @Provides
  @HostName
  public String hostName() {
    return "bbs.bbuhot.com";
  }
}

package com.yhvictor.discuzclient.discuzapi;

import com.yhvictor.discuzclient.annotation.HostName;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.guava.GuavaCallAdapterFactory;

@Module
public class DiscuzApiModule {

  @Provides
  @Singleton
  DiscuzApi discuzApi(@HostName String hostName, OkHttpClient okHttpClient) {
    Retrofit retrofit =
        new Retrofit.Builder()
            .baseUrl("http://" + hostName + "/api/mobile/")
            .client(okHttpClient)
            // .addConverterFactory(ScalarsConverterFactory.create())
            .addCallAdapterFactory(GuavaCallAdapterFactory.create())
            .build();

    return retrofit.create(DiscuzApi.class);
  }
}

package com.yhvictor.discuzclient.discuzapi;

import com.yhvictor.discuzclient.constants.Constants;

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
  DiscuzApi discuzApi(Constants constants, OkHttpClient okHttpClient) {
    Retrofit retrofit =
        new Retrofit.Builder()
            .baseUrl(constants.host())
            .client(okHttpClient)
            .addCallAdapterFactory(GuavaCallAdapterFactory.create())
            .build();

    return retrofit.create(DiscuzApi.class);
  }
}

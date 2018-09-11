package com.yhvictor.discuzclient.util.net;

import android.app.Application;
import android.webkit.CookieManager;
import android.webkit.WebSettings;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.Request;

@Module
public class OkHttpClientModule {

  private static final int CACHE_SIZE = 10 * 1024 * 1024; // 10MB

  // TODO(yh_victor): consider move this inside a injectable class.
  @Provides
  @Singleton
  OkHttpClient okHttpClient(Application application, WebViewCookieHandler webViewCookieHandler) {
    String userAgent = WebSettings.getDefaultUserAgent(application);
    return new OkHttpClient.Builder()
        .addInterceptor(
            chain -> {
              Request request =
                  chain.request().newBuilder().header("User-Agent", userAgent).build();
              return chain.proceed(request);
            })
        .cookieJar(webViewCookieHandler)
        .cache(new Cache(application.getCacheDir(), CACHE_SIZE))
        .build();
  }

  @Provides
  CookieManager cookieManager() {
    return CookieManager.getInstance();
  }
}

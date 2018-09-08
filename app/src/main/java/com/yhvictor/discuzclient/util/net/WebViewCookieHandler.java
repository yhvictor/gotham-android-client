package com.yhvictor.discuzclient.util.net;

import android.support.annotation.NonNull;
import android.webkit.CookieManager;

import com.yhvictor.discuzclient.debug.Logger;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;

/**
 * Cookie jar to sync cookie between web view & ok http client.
 */
class WebViewCookieHandler implements CookieJar {
  private final CookieManager webViewCookieManager;

  @Inject
  WebViewCookieHandler(CookieManager webViewCookieManager) {
    this.webViewCookieManager = webViewCookieManager;
  }

  @Override
  public void saveFromResponse(@NonNull HttpUrl url, @NonNull List<Cookie> cookies) {
    String urlString = url.toString();

    for (Cookie cookie : cookies) {
      Logger.d("Saving cookie: " + cookie);
      webViewCookieManager.setCookie(urlString, cookie.toString());
    }

    Logger.d("Saving cookie url: " + urlString);
  }

  @Override
  public List<Cookie> loadForRequest(@NonNull HttpUrl url) {
    String cookiesString = webViewCookieManager.getCookie(url.toString());

    Logger.d("Got cookies: " + cookiesString);
    Logger.d("Got cookie url:" + url);

    if (cookiesString != null && !cookiesString.isEmpty()) {
      // We can split on the ';' char as the cookie manager only returns cookies
      // that match the url and haven't expired, so the cookie attributes aren't included
      String[] cookieHeaders = cookiesString.split(";");
      List<Cookie> cookies = new ArrayList<>(cookieHeaders.length);

      for (String header : cookieHeaders) {
        cookies.add(Cookie.parse(url, header));
      }
      return cookies;
    }

    return Collections.emptyList();
  }
}

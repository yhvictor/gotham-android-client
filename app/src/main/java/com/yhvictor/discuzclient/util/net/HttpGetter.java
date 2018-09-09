package com.yhvictor.discuzclient.util.net;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.MoreExecutors;
import com.google.common.util.concurrent.SettableFuture;

import java.io.IOException;

import javax.inject.Inject;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class HttpGetter {
  private final OkHttpClient okHttpClient;

  @Inject
  public HttpGetter(OkHttpClient okHttpClient) {
    this.okHttpClient = okHttpClient;
  }

  public ListenableFuture<Bitmap> getBitmap(
      @Nullable BitmapFactory.Options options, String url, HeaderPair... headerPairs) {

    return Futures.transform(
        getResponseBody(url, headerPairs),
        (ResponseBody responseBody) ->
            BitmapFactory.decodeStream(responseBody.byteStream(), null, options),
        MoreExecutors.directExecutor());
  }

  public ListenableFuture<ResponseBody> getResponseBody(String url, HeaderPair... headerPairs) {
    Request.Builder builder = new Request.Builder().url(url);
    for (HeaderPair headerPair : headerPairs) {
      builder.addHeader(headerPair.key, headerPair.value);
    }
    Request request = builder.build();

    return getResponseBody(request);
  }

  public ListenableFuture<ResponseBody> getResponseBody(Request request) {
    return Futures.transformAsync(
        enqueue(request),
        response -> {
          if (response.isSuccessful()) {
            return Futures.immediateFuture(response.body());
          } else {
            throw new IOException(response.code() + ": " + response.message());
          }
        },
        MoreExecutors.directExecutor());
  }

  private ListenableFuture<Response> enqueue(Request request) {
    SettableFuture<Response> responseFuture = SettableFuture.create();

    Call call = okHttpClient.newCall(request);
    call.enqueue(
        new Callback() {
          @Override
          public void onFailure(@NonNull Call call, @NonNull IOException e) {
            responseFuture.setException(new IOException(e));
          }

          @Override
          public void onResponse(@NonNull Call call, @NonNull Response response) {
            responseFuture.set(response);
          }
        });

    responseFuture.addListener(
        () -> {
          if (responseFuture.isCancelled()) {
            call.cancel();
          }
        },
        MoreExecutors.directExecutor());

    return responseFuture;
  }

  public static class HeaderPair {
    private final String key;
    private final String value;

    public HeaderPair(String key, String value) {
      this.key = key;
      this.value = value;
    }
  }
}

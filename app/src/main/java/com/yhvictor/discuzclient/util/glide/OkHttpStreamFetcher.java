package com.yhvictor.discuzclient.util.glide;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.bumptech.glide.Priority;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.HttpException;
import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.data.DataFetcher;
import com.bumptech.glide.load.model.ModelLoader;
import com.bumptech.glide.load.model.ModelLoaderFactory;
import com.bumptech.glide.load.model.MultiModelLoaderFactory;
import com.bumptech.glide.signature.ObjectKey;

import java.io.IOException;
import java.io.InputStream;

import javax.inject.Inject;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

class OkHttpStreamFetcher implements DataFetcher<InputStream> {
  private final OkHttpModelLoader factory;
  private final Request request;

  private OkHttpStreamFetcher(OkHttpModelLoader factory, Request request) {
    this.factory = factory;
    this.request = request;
  }

  @Override
  public void loadData(
      @NonNull Priority priority, @NonNull DataCallback<? super InputStream> callback) {
    factory
        .okHttpClient
        .newCall(request)
        .enqueue(
            new Callback() {
              @Override
              public void onFailure(@NonNull Call call, @NonNull IOException e) {
                callback.onLoadFailed(e);
              }

              @Override
              public void onResponse(@NonNull Call call, @NonNull Response response) {
                if (response.isSuccessful()) {
                  callback.onDataReady(response.body().byteStream());
                } else {
                  callback.onLoadFailed(new HttpException(response.message(), response.code()));
                }
              }
            });
  }

  @Override
  public void cleanup() {
    // TODO: decide we need to close stream or not.
  }

  @Override
  public void cancel() {}

  @NonNull
  @Override
  public Class<InputStream> getDataClass() {
    return InputStream.class;
  }

  @NonNull
  @Override
  public DataSource getDataSource() {
    return DataSource.REMOTE;
  }

  static class OkHttpModelLoader implements ModelLoader<Request, InputStream> {
    private final OkHttpClient okHttpClient;

    @Inject
    OkHttpModelLoader(OkHttpClient okHttpClient) {
      this.okHttpClient = okHttpClient;
    }

    @Nullable
    @Override
    public LoadData<InputStream> buildLoadData(
        @NonNull Request request, int width, int height, @NonNull Options options) {
      return new LoadData<>(new ObjectKey(request), new OkHttpStreamFetcher(this, request));
    }

    @Override
    public boolean handles(@NonNull Request request) {
      return true;
    }

    ModelLoaderFactory<Request, InputStream> getModelLoaderFactory() {
      return new ModelLoaderFactory<Request, InputStream>() {

        @NonNull
        @Override
        public ModelLoader<Request, InputStream> build(
            @NonNull MultiModelLoaderFactory multiFactory) {
          return OkHttpModelLoader.this;
        }

        @Override
        public void teardown() {}
      };
    }
  }
}

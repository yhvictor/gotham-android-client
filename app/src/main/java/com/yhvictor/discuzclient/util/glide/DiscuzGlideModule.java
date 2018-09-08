package com.yhvictor.discuzclient.util.glide;

import android.content.Context;
import android.support.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Registry;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.module.AppGlideModule;
import com.yhvictor.discuzclient.application.DiscuzClientApplication;

import java.io.InputStream;

import javax.inject.Inject;

import okhttp3.Request;

@GlideModule
public final class DiscuzGlideModule extends AppGlideModule {
  @Inject
  OkHttpStreamFetcher.OkHttpModelLoader factory;

  @Override
  public void registerComponents(@NonNull Context context, @NonNull Glide glide, Registry registry) {
    DiscuzClientApplication.appComponent.inject(this);

    registry.append(Request.class, InputStream.class, factory.getModelLoaderFactory());
  }

  @Override
  public boolean isManifestParsingEnabled() {
    return false;
  }
}

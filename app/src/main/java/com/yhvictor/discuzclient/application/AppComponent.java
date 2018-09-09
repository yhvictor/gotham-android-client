package com.yhvictor.discuzclient.application;

import android.app.Application;

import com.yhvictor.discuzclient.constants.ConstantsModule;
import com.yhvictor.discuzclient.discuzapi.DiscuzApiModule;
import com.yhvictor.discuzclient.setting.SettingsFragment;
import com.yhvictor.discuzclient.threadlist.ThreadListFragment;
import com.yhvictor.discuzclient.util.concurrency.ConcurrencyModule;
import com.yhvictor.discuzclient.util.glide.DiscuzGlideModule;
import com.yhvictor.discuzclient.util.net.OkHttpClientModule;
import com.yhvictor.discuzclient.webview.WebViewFragment;

import javax.inject.Singleton;

import dagger.Component;
import dagger.android.AndroidInjectionModule;

@Singleton
@Component(
    modules = {
      AndroidInjectionModule.class,
      ConcurrencyModule.class,
      ConstantsModule.class,
      DiscuzApiModule.class,
      OkHttpClientModule.class,
    },
    dependencies = {
      Application.class,
    })
public interface AppComponent {

  Application application();

  void inject(DiscuzGlideModule discuzGlideModule);

  void inject(SettingsFragment threadListFragment);

  void inject(ThreadListFragment threadListFragment);

  void inject(WebViewFragment webViewFragment);

  void inject(ThreadListActivity threadListActivity);

  void inject(MainActivity mainActivity);

  @Component.Builder
  interface Builder {
    Builder application(Application application);

    AppComponent build();
  }
}

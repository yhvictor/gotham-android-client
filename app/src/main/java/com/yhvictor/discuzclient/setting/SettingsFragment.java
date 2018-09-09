package com.yhvictor.discuzclient.setting;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.CookieManager;
import android.widget.EditText;
import android.widget.ImageView;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.MoreExecutors;
import com.yhvictor.discuzclient.R;
import com.yhvictor.discuzclient.annotation.HostName;
import com.yhvictor.discuzclient.application.DiscuzClientApplication;
import com.yhvictor.discuzclient.application.ThreadListActivity;
import com.yhvictor.discuzclient.debug.Logger;
import com.yhvictor.discuzclient.discuzapi.DiscuzApi;
import com.yhvictor.discuzclient.discuzapi.data.LoginInfo;
import com.yhvictor.discuzclient.util.glide.GlideApp;
import com.yhvictor.discuzclient.util.net.HttpGetter;

import javax.inject.Inject;

import okhttp3.Request;

public class SettingsFragment extends Fragment implements View.OnClickListener {
  @HostName @Inject String hostName;
  @Inject HttpGetter httpGetter;
  @Inject DiscuzApi discuzApi;
  @Inject PersistentSettings persistentSettings;

  ImageView imageView;
  EditText editText;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    DiscuzClientApplication.appComponent.inject(this);
    super.onCreate(savedInstanceState);
  }

  @Override
  public View onCreateView(
      @NonNull LayoutInflater inflater,
      @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_settings, container, false);
    view.findViewById(R.id.refresh_login_button).setOnClickListener(this);
    view.findViewById(R.id.list_thread_button).setOnClickListener(this);
    view.findViewById(R.id.hash_image).setOnClickListener(this);
    view.findViewById(R.id.clear_cookie).setOnClickListener(this);

    imageView = view.findViewById(R.id.hash_image);
    editText = view.findViewById(R.id.hash_image_code);

    refreshImage();

    return view;
  }

  @Override
  public void onDestroyView() {
    imageView = null;
    editText = null;
    super.onDestroyView();
  }

  @Override
  public void onClick(View view) {
    switch (view.getId()) {
      case R.id.clear_cookie:
        CookieManager.getInstance().removeAllCookies(value -> Logger.d("All cookies removed"));
        break;
      case R.id.hash_image:
        refreshImage();
        break;
      case R.id.refresh_login_button:
        onRefreshLoginClick();
        break;
      case R.id.list_thread_button:
        ((ThreadListActivity) getActivity()).navigateToRecentThreadList();
        break;
      default:
        break;
    }
  }

  private void refreshImage() {
    Request request =
        new Request.Builder()
            .url("http://" + hostName + "/misc.php?mod=seccode&update=12345&idhash=S199&mobile=2")
            .addHeader(
                "Referer", "http://" + hostName + "/member.php?mod=logging&action=login&mobile=2")
            .build();
    GlideApp.with(imageView)
        .load(request)
        .placeholder(R.drawable.ic_loading)
        .skipMemoryCache(true)
        .diskCacheStrategy(DiskCacheStrategy.NONE)
        .into(imageView);
  }

  private void onRefreshLoginClick() {
    ListenableFuture<LoginInfo> loginInfoListenableFuture =
        discuzApi.login(
            persistentSettings.getUsername(),
            persistentSettings.getPassword(),
            editText.getText().toString());

    Futures.addCallback(
        loginInfoListenableFuture,
        new FutureCallback<LoginInfo>() {
          @Override
          public void onSuccess(@NonNull LoginInfo result) {
            Logger.d(result.version);
            Logger.d(result.message);
            Logger.d(result.variables);
          }

          @Override
          public void onFailure(@NonNull Throwable t) {
            Logger.d("Error !", new Throwable(t));
          }
        },
        MoreExecutors.directExecutor());
  }
}

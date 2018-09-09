package com.yhvictor.discuzclient.setting;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.common.util.concurrent.FluentFuture;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.MoreExecutors;
import com.yhvictor.discuzclient.R;
import com.yhvictor.discuzclient.annotation.HostName;
import com.yhvictor.discuzclient.application.DiscuzClientApplication;
import com.yhvictor.discuzclient.application.FragmentActivity;
import com.yhvictor.discuzclient.discuzapi.DiscuzApi;
import com.yhvictor.discuzclient.util.concurrency.CommonExecutors;
import com.yhvictor.discuzclient.util.glide.GlideApp;
import com.yhvictor.discuzclient.util.json.Json;
import com.yhvictor.discuzclient.util.json.JsonUtil;
import com.yhvictor.discuzclient.util.net.HttpGetter;
import com.yhvictor.discuzclient.util.net.WebViewCookieHandler;

import javax.inject.Inject;

import okhttp3.Request;

public class SettingsFragment extends Fragment implements View.OnClickListener {
  @HostName @Inject String hostName;
  @Inject HttpGetter httpGetter;
  @Inject DiscuzApi discuzApi;
  @Inject WebViewCookieHandler webViewCookieHandler;
  @Inject PersistentSettings persistentSettings;

  ImageView imageView;
  EditText usernameEdit;
  EditText passwordEdit;
  EditText secCodeEdit;

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
    usernameEdit = view.findViewById(R.id.username);
    passwordEdit = view.findViewById(R.id.password);
    secCodeEdit = view.findViewById(R.id.hash_image_code);

    // TODO: use data binding.
    usernameEdit.setText(persistentSettings.getUsername());
    passwordEdit.setText(persistentSettings.getPassword());
    refreshImage();

    return view;
  }

  @Override
  public void onDestroyView() {
    imageView = null;
    usernameEdit = null;
    passwordEdit = null;
    secCodeEdit = null;
    super.onDestroyView();
  }

  @Override
  public void onClick(View view) {
    switch (view.getId()) {
      case R.id.clear_cookie:
        webViewCookieHandler.removeAllCookies();
        break;
      case R.id.hash_image:
        refreshImage();
        break;
      case R.id.refresh_login_button:
        onRefreshLoginClick();
        break;
      case R.id.list_thread_button:
        ((FragmentActivity) getActivity()).navigateToRecentThreadList();
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
    persistentSettings.setUsername(usernameEdit.getText().toString());
    persistentSettings.setPassword(passwordEdit.getText().toString());
    ((FragmentActivity) getActivity()).hideKeyboard();

    FluentFuture.from(
            discuzApi.login(
                persistentSettings.getUsername(),
                persistentSettings.getPassword(),
                secCodeEdit.getText().toString()))
        .transformAsync(JsonUtil::transform, MoreExecutors.directExecutor())
        .addCallback(
            new FutureCallback<Json>() {
              @Override
              public void onSuccess(Json result) {
                // TODO: better handle activity lifecycle.
                // TODO: better handle message.
                ((FragmentActivity) getActivity()).snackbar(result.optString("Message", "messagestr"));
              }

              @Override
              public void onFailure(@NonNull Throwable t) {
                ((FragmentActivity) getActivity()).snackbar("Network error!");
              }
            },
            CommonExecutors.uiExecutor());
  }
}

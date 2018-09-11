package com.yhvictor.discuzclient.threadlist;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.common.util.concurrent.FluentFuture;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.MoreExecutors;
import com.yhvictor.discuzclient.R;
import com.yhvictor.discuzclient.application.DiscuzClientApplication;
import com.yhvictor.discuzclient.application.FragmentActivity;
import com.yhvictor.discuzclient.constants.Constants;
import com.yhvictor.discuzclient.debug.Logger;
import com.yhvictor.discuzclient.discuzapi.DiscuzApi;
import com.yhvictor.discuzclient.util.json.Json;
import com.yhvictor.discuzclient.util.json.JsonUtil;

import javax.inject.Inject;

public class ThreadListFragment extends Fragment
    implements AdapterView.OnItemClickListener, SwipeRefreshLayout.OnRefreshListener {
  @Inject Constants constants;
  @Inject DiscuzApi discuzApi;
  @Inject ThreadListArrayAdapter adapter;

  SwipeRefreshLayout swipeRefreshLayout;
  ListView listView;

  boolean hasInitLoaded = false;

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
    swipeRefreshLayout =
        (SwipeRefreshLayout) inflater.inflate(R.layout.fragment_threadlist, container, false);
    listView = swipeRefreshLayout.findViewById(R.id.list_view);

    listView.setOnItemClickListener(this);
    listView.setAdapter(adapter);

    swipeRefreshLayout.setOnRefreshListener(this);

    if (!hasInitLoaded) {
      swipeRefreshLayout.setRefreshing(true);
      this.onRefresh();
      hasInitLoaded = true;
    }

    return swipeRefreshLayout;
  }

  @Override
  public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
    String tid = adapter.getItem(position).tid();
    navigateToWebView(tid);
  }

  private void natigateToThreadView(String tid) {
    FluentFuture.from(discuzApi.getThreadInfo(tid))
        .transformAsync(JsonUtil::transform, MoreExecutors.directExecutor())
        .transform(json -> json.optSubJson("Variables"), MoreExecutors.directExecutor())
        .transform(Logger.echo(Logger::d), MoreExecutors.directExecutor())
        .addCallback(
            new FutureCallback<Json>() {
              @Override
              public void onSuccess(Json result) {}

              @Override
              public void onFailure(Throwable t) {}
            },
            MoreExecutors.directExecutor());
  }

  private void navigateToWebView(String tid) {
    Uri uri =
        new Uri.Builder()
            .scheme("http")
            .authority(constants.authority())
            .path("forum.php")
            .appendQueryParameter("mod", "viewthread")
            .appendQueryParameter("tid", tid)
            .build();

    ((FragmentActivity) getActivity()).navigateToWebView(uri);
  }

  @Override
  public void onRefresh() {
    adapter.refreshList(() -> swipeRefreshLayout.setRefreshing(false));
  }

  @Override
  public void onDestroyView() {
    swipeRefreshLayout = null;
    listView = null;

    super.onDestroyView();
  }
}

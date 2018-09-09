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

import com.yhvictor.discuzclient.R;
import com.yhvictor.discuzclient.annotation.HostName;
import com.yhvictor.discuzclient.application.DiscuzClientApplication;
import com.yhvictor.discuzclient.application.ThreadListActivity;
import com.yhvictor.discuzclient.discuzapi.DiscuzApi;

import javax.inject.Inject;

public class ThreadListFragment extends Fragment
    implements AdapterView.OnItemClickListener, SwipeRefreshLayout.OnRefreshListener {
  @Inject DiscuzApi discuzApi;
  @Inject ThreadListArrayAdapter adapter;
  @HostName @Inject String hostName;

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
    Uri uri =
        new Uri.Builder()
            .scheme("http")
            .authority(hostName)
            .path("forum.php")
            .appendQueryParameter("mod", "viewthread")
            .appendQueryParameter("tid", adapter.getItem(position).tid)
            .build();

    ((ThreadListActivity) getActivity()).navigateToWebView(uri);
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

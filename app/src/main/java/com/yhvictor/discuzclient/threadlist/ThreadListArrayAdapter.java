package com.yhvictor.discuzclient.threadlist;

import android.app.Application;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.yhvictor.discuzclient.R;
import com.yhvictor.discuzclient.debug.Logger;
import com.yhvictor.discuzclient.discuzapi.DiscuzApi;
import com.yhvictor.discuzclient.discuzapi.data.DiscuzThread;
import com.yhvictor.discuzclient.discuzapi.data.RecentThreadList;
import com.yhvictor.discuzclient.util.concurrency.CommonExecutors;

import javax.inject.Inject;

public class ThreadListArrayAdapter extends ArrayAdapter<DiscuzThread> {

  private final DiscuzApi discuzApi;
  private final LayoutInflater inflater;
  private final int resourceId;

  @Inject
  public ThreadListArrayAdapter(Application context, DiscuzApi discuzApi) {
    super(context, R.layout.view_threadlist);
    this.discuzApi = discuzApi;
    this.inflater = LayoutInflater.from(context);
    this.resourceId = R.layout.view_threadlist;
  }

  @Override
  public @NonNull
  View getView(
      int position, @Nullable View convertView, @NonNull ViewGroup parent) {
    return createViewFromResource(getItem(position), convertView, parent);
  }

  private @NonNull
  View createViewFromResource(
      DiscuzThread thread, @Nullable View convertView, @NonNull ViewGroup parent) {
    final View view;

    if (convertView == null) {
      view = inflater.inflate(resourceId, parent, false);
    } else {
      view = convertView;
    }

    TextView threadName = view.findViewById(R.id.thread_name);
    TextView author = view.findViewById(R.id.thread_author);

    threadName.setText(thread.subject);
    author.setText(thread.author);

    return view;
  }

  public void refreshList(Runnable runnable) {
    Futures.addCallback(
        discuzApi.listRecentThread(2, 0, 100),
        new FutureCallback<RecentThreadList>() {
          @Override
          public void onSuccess(RecentThreadList result) {
            CommonExecutors.uiExecutor().submit(() -> {
              clear();
              addAll(result.getThreads());
              runnable.run();
            });
          }

          @Override
          public void onFailure(@NonNull Throwable t) {
            Logger.d("Error!", t);
          }
        });
  }
}

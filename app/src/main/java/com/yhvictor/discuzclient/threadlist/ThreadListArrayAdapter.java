package com.yhvictor.discuzclient.threadlist;

import android.app.Application;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.MoreExecutors;
import com.yhvictor.discuzclient.R;
import com.yhvictor.discuzclient.annotation.HostName;
import com.yhvictor.discuzclient.debug.Logger;
import com.yhvictor.discuzclient.discuzapi.DiscuzApi;
import com.yhvictor.discuzclient.discuzapi.data.DiscuzThread;
import com.yhvictor.discuzclient.discuzapi.data.RecentThreadList;
import com.yhvictor.discuzclient.util.concurrency.CommonExecutors;
import com.yhvictor.discuzclient.util.glide.GlideApp;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import okhttp3.Request;

public class ThreadListArrayAdapter extends BaseAdapter {
  private final String hostName;
  private final DiscuzApi discuzApi;
  private final LayoutInflater inflater;

  private final List<DiscuzThread> discuzThreads = Collections.synchronizedList(new ArrayList<>());

  @Inject
  public ThreadListArrayAdapter(
      @HostName String hostName, Application context, DiscuzApi discuzApi) {
    this.hostName = hostName;
    this.discuzApi = discuzApi;
    this.inflater = LayoutInflater.from(context);
  }

  @Override
  public int getCount() {
    return discuzThreads.size();
  }

  @Override
  public DiscuzThread getItem(int position) {
    return discuzThreads.get(position);
  }

  @Override
  public long getItemId(int position) {
    return position;
  }

  @Override
  public @NonNull View getView(
      int position, @Nullable View convertView, @NonNull ViewGroup parent) {
    return createViewFromResource(getItem(position), convertView, parent);
  }

  private @NonNull View createViewFromResource(
      DiscuzThread thread, @Nullable View convertView, @NonNull ViewGroup parent) {
    final View view;

    if (convertView == null) {
      view = inflater.inflate(R.layout.layout_threadlist, parent, false);
    } else {
      view = convertView;
    }

    TextView threadName = view.findViewById(R.id.thread_name);
    TextView author = view.findViewById(R.id.author);
    TextView threadCreationTime = view.findViewById(R.id.thread_creation_time);
    ImageView imageView = view.findViewById(R.id.author_image);

    Request request =
        new Request.Builder()
            .url("https://" + hostName + "/uc_server/avatar.php?size=small&uid=" + thread.authorId)
            .build();

    threadName.setText(thread.subject);
    author.setText(thread.author);
    threadCreationTime.setText(Html.fromHtml(thread.dateline));
    GlideApp.with(view).load(request).into(imageView);

    return view;
  }

  public void refreshList(Runnable runnable) {
    Futures.addCallback(
        discuzApi.listRecentThread(2, 0, 100),
        new FutureCallback<RecentThreadList>() {
          @Override
          public void onSuccess(@NonNull RecentThreadList result) {
            CommonExecutors.uiExecutor()
                .submit(
                    () -> {
                      discuzThreads.clear();
                      discuzThreads.addAll(result.getThreads());
                      notifyDataSetChanged();
                      runnable.run();
                    });
          }

          @Override
          public void onFailure(@NonNull Throwable t) {
            Logger.d("Error!", t);
          }
        },
        MoreExecutors.directExecutor());
  }
}

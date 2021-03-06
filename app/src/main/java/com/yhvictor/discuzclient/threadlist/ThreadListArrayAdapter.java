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

import com.google.common.collect.Lists;
import com.google.common.util.concurrent.FluentFuture;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.MoreExecutors;
import com.yhvictor.discuzclient.R;
import com.yhvictor.discuzclient.constants.Constants;
import com.yhvictor.discuzclient.debug.Logger;
import com.yhvictor.discuzclient.discuzapi.DiscuzApi;
import com.yhvictor.discuzclient.util.concurrency.OtherExecutors;
import com.yhvictor.discuzclient.util.glide.GlideApp;
import com.yhvictor.discuzclient.util.json.JsonUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import okhttp3.Request;

/**
 * Adapter to show thread list.
 */
public class ThreadListArrayAdapter extends BaseAdapter {
  private final Constants constants;
  private final DiscuzApi discuzApi;
  private final LayoutInflater inflater;

  private final List<ThreadModel> discuzThreads = Collections.synchronizedList(new ArrayList<>());

  @Inject
  public ThreadListArrayAdapter(
      Constants constants, Application context, DiscuzApi discuzApi) {
    this.constants = constants;
    this.discuzApi = discuzApi;
    this.inflater = LayoutInflater.from(context);
  }

  @Override
  public int getCount() {
    return discuzThreads.size();
  }

  @Override
  public ThreadModel getItem(int position) {
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
      ThreadModel thread, @Nullable View convertView, @NonNull ViewGroup parent) {
    final View view;

    if (convertView == null) {
      view = inflater.inflate(R.layout.layout_thread_list, parent, false);
    } else {
      view = convertView;
    }

    TextView threadName = view.findViewById(R.id.subject);
    TextView author = view.findViewById(R.id.author);
    TextView threadCreationTime = view.findViewById(R.id.thread_creation_time);
    ImageView imageView = view.findViewById(R.id.author_image);

    Request request =
        new Request.Builder()
            .url(constants.host() + "/uc_server/avatar.php?size=small&uid=" + thread.authorId())
            .build();

    threadName.setText(thread.subject());
    author.setText(thread.author());
    threadCreationTime.setText(Html.fromHtml(thread.dateline()));
    GlideApp.with(view).load(request).into(imageView);

    return view;
  }

  // TODO(yh_victor): move this out of this class.
  public void refreshList(Runnable runnable) {
    FluentFuture.from(discuzApi.listRecentThread(2, 0, 100))
        .transformAsync(JsonUtil::transform, MoreExecutors.directExecutor())
        .transform(json -> json.optArray("Variables", "data"), MoreExecutors.directExecutor())
        .transform(
            jsonList -> Lists.transform(jsonList, ThreadModel::new), MoreExecutors.directExecutor())
        .addCallback(
            new FutureCallback<List<ThreadModel>>() {
              @Override
              public void onSuccess(List<ThreadModel> result) {
                Logger.d("thread count: " + result.size());

                discuzThreads.clear();
                discuzThreads.addAll(result);
                notifyDataSetChanged();
                runnable.run();
              }

              @Override
              public void onFailure(Throwable t) {
                Logger.d("Error!", t);
                runnable.run();
              }
            },
            OtherExecutors.uiExecutor());
  }
}

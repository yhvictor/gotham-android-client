package com.yhvictor.discuzclient.discuzapi;

import com.google.common.util.concurrent.ListenableFuture;

import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * APIs those connect to discuz bbs server.
 *
 * <p>TODO(yh_victor): consider get rid of retrofit2.
 */
public interface DiscuzApi {
  @GET("/api/mobile/index.php?version=4&module=login&loginsubmit=yes")
  ListenableFuture<ResponseBody> login(
      @Query("username") String username,
      @Query("password") String password,
      @Query("seccodeverify") String hashCode);

  @GET("/api/mobile/index.php?version=4&module=forumdisplay")
  ListenableFuture<ResponseBody> forumDisplay(@Query("fid") int fid, @Query("page") int page);

  @GET("/api/mobile/index.php?version=4&module=newthreads")
  ListenableFuture<ResponseBody> listRecentThread(
      @Query("fids") int fid, @Query("start") int start, @Query("limit") int limit);

  @GET("/api/mobile/index.php?version=4&module=viewthread")
  ListenableFuture<ResponseBody> getThreadInfo(@Query("tid") String tid);
}

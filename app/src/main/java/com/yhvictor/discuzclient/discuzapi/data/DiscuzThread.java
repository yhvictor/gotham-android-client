package com.yhvictor.discuzclient.discuzapi.data;

import com.google.gson.annotations.SerializedName;

import proguard.annotation.KeepName;

public class DiscuzThread {
  @KeepName public String tid;

  @KeepName public String author;

  @SerializedName("authorid")
  public String authorId;

  @KeepName public String subject;

  @KeepName public String dateline;

  @SerializedName("lastpost")
  public String lastPost;

  @SerializedName("lastposter")
  public String lastPoster;

  @KeepName public String views;

  @KeepName public String replies;

  @SerializedName("dbdateline")
  public int dbDateLine;

  @SerializedName("dblastpost")
  public int dbLastPost;

  /*
  * Do not use the reply here. it's useless.

  @SerializedName("reply")
  public List<JsonElement> replies;
  */
}

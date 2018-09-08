package com.yhvictor.discuzclient.discuzapi.data;

import com.google.gson.annotations.SerializedName;

public class DiscuzThread {
  public String tid;

  public String author;

  @SerializedName("authorid")
  public String authorId;

  public String subject;

  @SerializedName("lastposter")
  public String lastPoster;

  public String views;

  public String replies;

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

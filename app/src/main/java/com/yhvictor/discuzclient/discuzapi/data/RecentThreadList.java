package com.yhvictor.discuzclient.discuzapi.data;

import com.google.gson.annotations.SerializedName;

import java.util.Collections;
import java.util.List;

public class RecentThreadList {
  @SerializedName("Version")
  public String version;

  @SerializedName("Variables")
  public Variables variables;

  public List<DiscuzThread> getThreads() {
    return variables == null || variables.discuzThreads == null
        ? Collections.emptyList()
        : variables.discuzThreads;
  }

  public static class Variables {
    @SerializedName("data")
    public List<DiscuzThread> discuzThreads;
  }
}

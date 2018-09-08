package com.yhvictor.discuzclient.discuzapi.data;

import com.google.gson.JsonElement;
import com.google.gson.annotations.SerializedName;

public class LoginInfo {
  @SerializedName("Version")
  public String version;

  @SerializedName("Variables")
  public JsonElement variables;

  @SerializedName("Message")
  public JsonElement message;
}

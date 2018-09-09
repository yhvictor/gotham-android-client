package com.yhvictor.discuzclient.threadlist;

import com.yhvictor.discuzclient.util.json.Json;

class ThreadData {

  private final Json json;

  ThreadData(Json json) {
    this.json = json;
  }

  public String tid() {
    return json.optString("tid");
  }

  public String authorId() {
    return json.optString("authorid");
  }

  public String subject() {
    return json.optString("subject");
  }

  public String author() {
    return json.optString("author");
  }

  public String dateline() {
    return json.optString("dateline");
  }
}

package com.yhvictor.discuzclient.util.json;

import android.util.JsonReader;
import android.util.JsonToken;

import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;

/** Util methods for {@link Json}. */
public class JsonUtil {
  private JsonUtil() {}

  public static ListenableFuture<Json> transform(ResponseBody responseBody) throws IOException {
    return Futures.immediateFuture(fromResponseBody(responseBody));
  }

  public static Json fromResponseBody(ResponseBody responseBody) throws IOException {
    try (JsonReader jsonReader = new JsonReader(responseBody.charStream())) {
      return toJson(jsonReader);
    }
  }

  private static Json toJson(JsonReader jsonReader) throws IOException {
    Map<String, Object> map = new HashMap<>();
    jsonReader.beginObject();
    while (jsonReader.hasNext()) {
      String key = jsonReader.nextName();
      Json json = nextJson(jsonReader);
      if (json != null) {
        map.put(key, json);
      }
    }
    jsonReader.endObject();
    return new Json(map);
  }

  private static Json toJsonArray(JsonReader jsonReader) throws IOException {
    List<Json> list = new ArrayList<>();
    jsonReader.beginArray();
    while (jsonReader.hasNext()) {
      Json json = nextJson(jsonReader);
      if (json != null) {
        list.add(json);
      }
    }
    jsonReader.endArray();
    return new Json(list);
  }

  private static Json nextJson(JsonReader jsonReader) throws IOException {
    JsonToken token = jsonReader.peek();
    switch (token) {
      case BEGIN_OBJECT:
        return toJson(jsonReader);
      case BEGIN_ARRAY:
        return toJsonArray(jsonReader);
      case STRING:
      case BOOLEAN:
      case NUMBER:
        return toJsonString(jsonReader);
      default:
        jsonReader.skipValue();
        return null;
    }
  }

  private static Json toJsonString(JsonReader jsonReader) throws IOException {
    return new Json(jsonReader.nextString());
  }
}

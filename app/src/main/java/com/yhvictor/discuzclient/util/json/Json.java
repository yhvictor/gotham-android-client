package com.yhvictor.discuzclient.util.json;

import android.support.annotation.Nullable;

import com.google.common.base.Joiner;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Represent a Json object.
 *
 * <p>TODO(yh_victor): handle number & boolean.
 */
@SuppressWarnings("unchecked")
public class Json {
  // Internal value which could Map<String,Json>, List<Json> or String.
  Object value;

  Json(Object value) {
    this.value = value;
  }

  @Nullable
  public Json optSubJson(Object... keys) {
    Json ret = this;
    for (Object key : keys) {
      if (key instanceof String) {
        if (ret.value instanceof Map) {
          ret = ((Map<String, Json>) ret.value).get(key);
        } else {
          ret = null;
        }
      } else if (key instanceof Integer) {
        if (ret.value instanceof List) {
          int intKey = (int) key;
          if (intKey > ((List) ret.value).size()) {
            ret = null;
          } else {
            ret = ((List<Json>) ret.value).get(intKey);
          }
        } else {
          ret = null;
        }
      }
      if (ret == null) {
        return null;
      }
    }
    return ret;
  }

  public List<Json> optArray(Object... keys) {
    Json ret = optSubJson(keys);

    if (ret.value instanceof List) {
      return (List<Json>) ret.value;
    } else {
      return Collections.emptyList();
    }
  }

  public String optString(Object... keys) {
    Json ret = optSubJson(keys);
    if (ret.value instanceof String) {
      return (String) ret.value;
    } else {
      return "";
    }
  }

  @Override
  public String toString() {
    if (value instanceof String) {
      return String.format("\"%s\"", value);
    } else if (value instanceof Map) {
      return String.format("{%s}", Joiner.on(", ").join(((Map<String, Json>) value).entrySet()));
    } else if (value instanceof List) {
      return String.format("[%s]", Joiner.on(", ").join((List<Json>) value));
    } else {
      return "{ERROR!}";
    }
  }
}

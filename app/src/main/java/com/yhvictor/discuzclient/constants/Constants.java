package com.yhvictor.discuzclient.constants;

import javax.inject.Inject;

/** Holds most of the constants for this project. */
public class Constants {

  @Inject
  Constants() {}

  public String authority() {
    return "bbs.bbuhot.com";
  }

  public String host() {
    return "http://" + authority();
  }
}

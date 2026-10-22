package com.qaautomation.config;

import java.util.ResourceBundle;

public final class TestConfig {

  private static final ResourceBundle CONFIG = ResourceBundle.getBundle("config");

  private TestConfig() {
  }

  public static String getSauceDemoUsername() {
    return get("saucedemo.username");
  }

  public static String getSauceDemoPassword() {
    return get("saucedemo.password");
  }

  public static String getRestfulBookerBaseUrl() {
    return get("restfulbooker.baseUrl");
  }

  private static String get(String key) {
    return System.getProperty(key, CONFIG.getString(key));
  }
}

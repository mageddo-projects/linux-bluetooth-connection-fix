package com.mageddo.commons.lang;

import lombok.SneakyThrows;

public class Threads {
  @SneakyThrows
  public static void sleep(int millis) {
    Thread.sleep(millis);
  }
}

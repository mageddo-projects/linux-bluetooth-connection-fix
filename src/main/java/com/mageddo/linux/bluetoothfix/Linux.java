package com.mageddo.linux.bluetoothfix;

import com.sun.security.auth.module.UnixSystem;

public class Linux {

  private Linux() {
  }

  public static long findUserId() {
    return new UnixSystem().getUid();
  }

  public static boolean runningAsRoot() {
    return findUserId() == 0;
  }
}

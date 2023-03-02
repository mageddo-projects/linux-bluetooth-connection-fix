package com.mageddo.linux.bluetoothfix;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Main {
  public static void main(String[] args) {

    if (!Linux.runningAsRoot()) {
      log.warn("you need to run as root due to restart bluetooth service, exiting...");
      System.exit(1);
    }
    new BluetoothConnector().connect("94:DB:56:F5:78:41");
  }

}

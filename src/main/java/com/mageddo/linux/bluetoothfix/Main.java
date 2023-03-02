package com.mageddo.linux.bluetoothfix;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Main {
  public static void main(String[] args) {
    if(args.length == 0){
      log.warn("pass the bluetooth device id which you want to connect to, ex: \"94:CC:56:E5:72:85\"");
      System.exit(1);
    }
    new BluetoothConnector().connect(args[0]);
  }

}

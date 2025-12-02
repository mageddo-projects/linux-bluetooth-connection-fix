package com.mageddo.linux.bluetoothfix;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ConReqMapper {
  public static ConReq of(String[] args) {
    if (args.length == 0) {
      log.warn("pass the bluetooth device id which you want to connect to, ex: \"94:CC:56:E5:72:85\"");
      System.exit(1);
    }
    if (args.length == 1) {
      return ConReq
        .builder()
        .deviceId(args[0])
        .build();
    }
    return ConReq
      .builder()
      .deviceId(args[0])
      .controllerId(args[1])
      .build();
  }
}

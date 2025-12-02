package com.mageddo.linux.bluetoothfix;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Main {
  public static void main(String[] args) {
    final ConReq req = ConReqMapper.of(args);
    new BluetoothConnector(new BluetoothCommandRunner()).connect(req);
  }

}

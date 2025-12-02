package com.mageddo.linux.bluetoothfix;

public class ConnectOccurrenceMapper {
  public static BluetoothConnector.Occurrence of(String str) {
    if (str.contains("Attempting to connect") && str.endsWith("[bluetooth]#")) {
      return BluetoothConnector.Occurrence.NO_ERROR;
    }
    throw new IllegalStateException(str);
  }
}

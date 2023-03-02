package com.mageddo.bluetoothfix;


import com.mageddo.commons.exec.CommandLines;

public class OccurrenceParser {
  public static BluetoothConnector.Occurrence parse(CommandLines.Result result) {
    final String out = result.getOutAsString();
    if (out.contains("br-connection-busy")) {
      return BluetoothConnector.Occurrence.ERROR_CONNECTION_BUSY;
    } else {
      return null;
    }
  }
}

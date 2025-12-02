package com.mageddo.linux.bluetoothfix;


import com.mageddo.commons.exec.CommandLines;
import com.mageddo.linux.bluetoothfix.BluetoothConnector.Occurrence;

public class OccurrenceParser {
  public static Occurrence parse(CommandLines.Result result) {
    final String out = result.getOutAsString();
    if (out.contains("br-connection-busy")) {
      return Occurrence.ERROR_CONNECTION_BUSY;
//    } else if (out.contains("not available")) {
//      return Occurrence.DEVICE_NOT_AVAILABLE;
    } else {
      throw new IllegalStateException(String.format("unknown result: %s", result.getOutAsString()));
    }
  }
}

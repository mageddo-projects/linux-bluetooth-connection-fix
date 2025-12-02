package com.mageddo.linux.bluetoothfix;

public class ConnectCommandMapper {
  public static String of(ConReq req, int timeoutSecs) {
    if (req.hasNotControllerId()) {
      return String.format("bluetoothctl --timeout %d connect %s", timeoutSecs, req.deviceId());
    }
    return String.format(
      "/bin/bash -c \"echo -e 'select %s\nconnect %s' | bluetoothctl --timeout %d\"",
      req.controllerId(), req.deviceId(), timeoutSecs
    );
  }
}

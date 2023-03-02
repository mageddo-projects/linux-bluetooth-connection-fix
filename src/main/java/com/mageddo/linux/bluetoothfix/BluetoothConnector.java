package com.mageddo.linux.bluetoothfix;


import com.mageddo.commons.exec.CommandLines;
import com.mageddo.commons.exec.ExecutionValidationFailedException;
import com.mageddo.commons.lang.Threads;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.exec.CommandLine;
import org.apache.commons.lang3.time.StopWatch;

@Slf4j
public class BluetoothConnector {
  public static final boolean PRINT_OUT = false;
  public static final int BLUETOOTH_POWER_ON_DELAY = 1000;
  private final int timeoutSecs = 10;

  public void connect(String deviceId) {

    if (this.isSoundDeviceConfigured(deviceId)) {
      log.info("status=bluetooth-device-already-configured-and-working, deviceId={}", deviceId);
      return;
    }

    final StopWatch stopWatch = StopWatch.createStarted();
    Occurrence status = null;
    do {

      stopWatch.split();

      if (status != null) {
        switch (status) {
          case CONNECTED_BUT_SOUND_NOT_CONFIGURED:
          case ERROR_CONNECTION_BUSY:
            this.disconnect(deviceId);
            break;
        }
      }

      this.restartService();
      status = this.connect0(deviceId);

      log.debug(
        "status=tried, occurrence={}, time={}",
        status, stopWatch.getTime() - stopWatch.getSplitTime()
      );
      Threads.sleep(1000);

    } while (status != Occurrence.CONNECTED);
    log.debug(
      "status=successfullyConnected!, device={}, totalTime={}",
      deviceId, stopWatch.getTime()
    );
  }

  boolean disconnect(String deviceId) {
    try {
      final CommandLines.Result result = CommandLines.exec(
          "bluetoothctl --timeout %d disconnect %s", timeoutSecs, deviceId
        )
        .checkExecution();
      log.debug("status=disconnected, {}", result.toString(PRINT_OUT));
      return true;
    } catch (ExecutionValidationFailedException e) {
      log.debug("status=failedToDisconnect, {}", e.result()
        .toString(PRINT_OUT));
      return false;
    }
  }

  CommandLines.Result restartService() {
    final CommandLine cmd = new CommandLine("/bin/sh")
      .addArguments(new String[]{
        "-c",
        "systemctl restart bluetooth.service",
      }, false);
    final CommandLines.Result result = CommandLines.exec(cmd)
      .checkExecution();
    log.debug("status=restarted, {}", result.toString(PRINT_OUT));
    Threads.sleep(BLUETOOTH_POWER_ON_DELAY); // wait some time to bluetooth power on
    return result;
  }

  boolean isConnected(String deviceId) {
    final CommandLines.Result result = CommandLines.exec(
        "bluetoothctl info %s", deviceId
      )
      .checkExecution();
    final String out = result.getOutAsString();
    if (out.contains("Connected: yes")) {
      return true;
    } else if (out.contains("Connected: no")) {
      return false;
    } else {
      throw new IllegalStateException(String.format("cant check if it's connected: %s", out));
    }
  }

  Occurrence connect0(String deviceId) {
    try {
      log.debug("status=tryConnecting, device={}", deviceId);
      final CommandLines.Result result = CommandLines
        .exec(
          "bluetoothctl --timeout %d connect %s", timeoutSecs, deviceId
        )
        .checkExecution();
      final BluetoothConnector.Occurrence occurrence = OccurrenceParser.parse(result);
      if (occurrence != null) {
        return occurrence;
      }
      final Occurrence occur = this.connectionOccurrenceCheck(deviceId);
      log.debug("status=done, occurrence={}", occur);
      return occur;
    } catch (ExecutionValidationFailedException e) {
      return OccurrenceParser.parse(e.result());
    }
  }

  Occurrence connectionOccurrenceCheck(String deviceId) {
    final boolean connected = this.isConnected(deviceId);
    if (connected) {
      if (this.isSoundDeviceConfigured(deviceId)) {
        return Occurrence.CONNECTED;
      }
      return Occurrence.CONNECTED_BUT_SOUND_NOT_CONFIGURED;
    } else {
      return Occurrence.DISCONNECTED;
    }
  }

  /**
   * A device like the following must be displayed when bluetooth audio is working
   * bluez_sink.94_DB_56_F5_78_41.a2dp_sink
   */
  boolean isSoundDeviceConfigured(String deviceId) {
    final String audioSinkId = String.format(
      "bluez_sink.%s.a2dp_sink", deviceId.replaceAll(":", "_")
    );
    final CommandLine cmd = new CommandLine("/bin/sh")
      .addArguments(new String[]{"-c", "pactl list | grep 'Sink'"}, false);

    final CommandLines.Result result = CommandLines.exec(cmd)
      .checkExecution();

    final boolean found = result
      .getOutAsString()
      .contains(audioSinkId);

    log.debug("found={}, {}", found, result.toString(PRINT_OUT));
    return found;

  }

  public enum Occurrence {
    ERROR_CONNECTION_BUSY,
    CONNECTED,
    DISCONNECTED,
    ERROR_UNKNOWN,
    CONNECTED_BUT_SOUND_NOT_CONFIGURED;
  }

}

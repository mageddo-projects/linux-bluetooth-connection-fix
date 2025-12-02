package com.mageddo.linux.bluetoothfix;


import javax.swing.JOptionPane;
import javax.swing.JPasswordField;

import com.mageddo.commons.exec.CommandLines;
import com.mageddo.commons.lang.Threads;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.lang3.time.StopWatch;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class BluetoothConnector {

  private final BluetoothCommandRunner runner;

  public static final boolean PRINT_OUT = false;
  public static final int BLUETOOTH_POWER_ON_DELAY = 1000;
  private final int timeoutSecs = 6;
  private char[] sudoPassword;

  public void connect(ConReq conReq) {

    if (this.isSoundDeviceConfigured(conReq.deviceId())) {
      log.info("status=bluetooth-device-already-configured-and-working, conReq={}", conReq);
      return;
    }

    final var stopWatch = StopWatch.createStarted();
    Occurrence status = null;
    do {

      stopWatch.split();

      if (status != null) {
        switch (status) {
          case CONNECTED_BUT_SOUND_NOT_CONFIGURED:
          case ERROR_CONNECTION_BUSY:
            this.disconnect(conReq);
            this.waitBluetoothCommandComplete();
            break;
        }
      }

      this.restartService();

      status = this.connect0(conReq);

      log.info(
          "status=tried, occurrence={}, time={}",
          status, stopWatch.getTime() - stopWatch.getSplitTime()
      );

    } while (status != Occurrence.CONNECTED);
    log.info(
        "status=successfullyConnected!, device={}, totalTime={}",
        conReq, stopWatch.getTime()
    );
  }

  boolean disconnect(ConReq req) {
    final var msg = this.runner.exec(
        this.timeoutSecs,
        String.format("select %s", req.controllerId()),
        String.format("disconnect %s", req.deviceId())
    );
    log.info("msg={}", msg);
    return true;
  }

  CommandLines.Result restartService() {
    askForPassword();
    final var result = this.restartService0();
    Threads.sleep(BLUETOOTH_POWER_ON_DELAY);
    return result;
  }

  private CommandLines.Result restartService0() {
    final var cmd = new CommandLine("/bin/sh")
        .addArguments(new String[]{
                "-c",
                String.format(
                    "echo %s | /usr/bin/sudo -S systemctl restart bluetooth.service",
                    new String(this.sudoPassword)
                )
            }, false
        );
    final var result = CommandLines.exec(cmd)
        .checkExecution();
    log.debug("status=restarted, {}", result.toString(PRINT_OUT));
    return result;
  }

  private void askForPassword() {
    log.warn("systemctl will ask you for root password to restart bluetooth service ...");
    if (this.sudoPassword == null) {
      while (true) {
        JPasswordField pf = new JPasswordField();
        final int res = JOptionPane.showConfirmDialog(null, pf, "Enter Password",
            JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE
        );
        if (res == JOptionPane.OK_OPTION) {
          this.sudoPassword = pf.getPassword();
          break;
        }
      }
    }
  }

  boolean isConnected(ConReq req) {
    final var msg = this.runner.exec(
        this.timeoutSecs,
        String.format("select %s", req.controllerId()),
        String.format("info %s", req.deviceId())
    );
    if (msg.contains("Connected: yes")) {
      return true;
    } else if (msg.contains("Connected: no")) {
      return false;
    } else {
      throw new IllegalStateException(String.format("cant check if it's connected: %s", msg));
    }
  }

  Occurrence connect0(ConReq req) {
    final var resStr = this.runner.exec(
        this.timeoutSecs,
        String.format("select %s", req.controllerId()),
        String.format("connect %s", req.deviceId())
    );
    this.waitBluetoothCommandComplete();
    final var occur = this.connectionOccurrenceCheck(req);
    if (occur != Occurrence.CONNECTED) {
      log.info("status=notConnected, occurrence={}, msg={}", occur, resStr);
    }
    return occur;
  }

  private void waitBluetoothCommandComplete() {
    Threads.sleep(2_000);
  }

  Occurrence connectionOccurrenceCheck(ConReq req) {
    final boolean connected = this.isConnected(req);
    if (connected) {
      if (this.isSoundDeviceConfigured(req.deviceId())) {
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

    log.info("found={}, {}", found, result.toString(PRINT_OUT));
    return found;

  }

  public enum Occurrence {
    ERROR_CONNECTION_BUSY,
    DEVICE_NOT_AVAILABLE,
    CONNECTED,
    DISCONNECTED,
    ERROR_UNKNOWN,
    CONNECTED_BUT_SOUND_NOT_CONFIGURED,
    NO_ERROR;
  }

}

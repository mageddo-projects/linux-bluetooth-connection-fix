package com.mageddo.linux.bluetoothfix;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.PumpStreamHandler;
import org.apache.commons.lang3.Validate;

public class BluetoothCommandRunner {

  public String exec(int timeoutSecs, String... commands) {
    final var str = String.join("\n", commands);
    final var executor = new DefaultExecutor();
    final var out = new ByteArrayOutputStream();
    final var streamHandler = new PumpStreamHandler(out);
    executor.setStreamHandler(streamHandler);

    final var cmd = new CommandLine("/bin/bash");
    cmd.addArgument("-c");
    cmd.addArgument(
        String.format(
            "echo -e '%s' | bluetoothctl --timeout %d", str, timeoutSecs
//            "echo -e 'select %s\nconnect %s' | bluetoothctl --timeout %d",
//            req.controllerId(), req.deviceId(), timeoutSecs
        ),
        false
    );
    try {
      final int exitCode = executor.execute(cmd);
      Validate.isTrue(exitCode == 0, "Unexpected exit code %s", exitCode);
      return out.toString();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public void exec(CommandLine cmd) {
    final DefaultExecutor executor = new DefaultExecutor();
    final ByteArrayOutputStream out = new ByteArrayOutputStream();
    final PumpStreamHandler streamHandler = new PumpStreamHandler(out);
    executor.setStreamHandler(streamHandler);
    try {
      final int exitCode = executor.execute(cmd);
      Validate.isTrue(exitCode == 0, "Unexpected exit code %s", exitCode);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}

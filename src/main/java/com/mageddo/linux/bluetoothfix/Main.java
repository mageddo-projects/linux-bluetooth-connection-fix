package com.mageddo.linux.bluetoothfix;

import lombok.extern.slf4j.Slf4j;

import javax.swing.*;

@Slf4j
public class Main {
  public static void main(String[] args) {
    new BluetoothConnector().connect("94:DB:56:F5:78:41", askForPassword());
  }

  static String askForPassword() {
    return JOptionPane.showInputDialog(
        null, "Sudo password to restart bluetooth service",
        "Sudo Password", JOptionPane.QUESTION_MESSAGE
    );
  }

}

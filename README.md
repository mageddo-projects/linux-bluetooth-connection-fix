# linux-bluetooth-connection-fix
Automated process to connect bluetooth devices on Linux despite the error `hci0: command <code> tx timeout`. 
Related errors

```bash
Bluetooth: hci0: command 0x0c24 tx timeout
Bluetooth: hci0: command 0x0c52 tx timeout
Bluetooth: hci0: command 0x0c1a tx timeout
```

# Running it 

Download the [latest release][1] then run the command below: 

```bash
$ java -jar linux-bluetooth-connection-fix.jar 94:CC:56:E5:72:85
[main] INFO com.mageddo.linux.bluetoothfix.BluetoothConnector - found=false, code=0, out=null
[main] WARN com.mageddo.linux.bluetoothfix.BluetoothConnector - systemctl will ask you for root password to restart bluetooth service ...
[main] INFO com.mageddo.linux.bluetoothfix.BluetoothConnector - status=restarted, code=0, out=null
[main] INFO com.mageddo.linux.bluetoothfix.BluetoothConnector - status=tryConnecting, device=94:DB:56:F5:78:41
[main] INFO com.mageddo.linux.bluetoothfix.BluetoothConnector - found=true, code=0, out=null
[main] INFO com.mageddo.linux.bluetoothfix.BluetoothConnector - status=done, occurrence=CONNECTED
[main] INFO com.mageddo.linux.bluetoothfix.BluetoothConnector - status=tried, occurrence=CONNECTED, time=18218
[main] INFO com.mageddo.linux.bluetoothfix.BluetoothConnector - status=successfullyConnected!, device=94:CC:56:E5:72:85, totalTime=19218
```

# Requirements

* Linux
* JRE 7+

# How it works
After buy a new bluetooth usb dongle, I noticed it was very difficult to make it connect to my headphones, also
noticed if I restart the bluetooth service and try to connect sometimes it will work at some moment, so what I did 
was jut automate this process. 

About the bluetooth issue root cause:

I wasn't able to find a real fix for the bluetooth problem, looks like it doesn't even exist, all people advise to buy a new
hardware at the end, then I made this program as a workaround.

Related issues 

* https://bbs.archlinux.org/viewtopic.php?id=198718
* https://bbs.archlinux.org/viewtopic.php?id=270693
* https://bbs.archlinux.org/viewtopic.php?id=195886&p=2
* https://unix.stackexchange.com/questions/581974/alpine-linux-failed-to-start-discovery-org-bluez-error-inprogress

# Compiling from source

```bash
$ ./gradlew build shadowJar
```

[1]: https://github.com/mageddo-projects/linux-bluetooth-connection-fix/releases

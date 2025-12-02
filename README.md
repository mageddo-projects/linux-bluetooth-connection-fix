# linux-bluetooth-connection-fix
Automated process to connect bluetooth devices on Linux despite the error `hci0: command <code> tx timeout`. 
Related errors

```bash
Bluetooth: hci0: command 0x0c24 tx timeout
Bluetooth: hci0: command 0x0c52 tx timeout
Bluetooth: hci0: command 0x0c1a tx timeout
```

## Running it 

Usage

```bash
$ java -jar linux-bluetooth-connection-fix.jar <deviceId> <controllerId>
[main] INFO com.mageddo.linux.bluetoothfix.BluetoothConnector - found=false, code=0, out=null
[main] WARN com.mageddo.linux.bluetoothfix.BluetoothConnector - systemctl will ask you for root password to restart bluetooth service ...
[main] INFO com.mageddo.linux.bluetoothfix.BluetoothConnector - found=true, code=0, out=null
[main] INFO com.mageddo.linux.bluetoothfix.BluetoothConnector - status=tried, occurrence=CONNECTED, time=10158
[main] INFO com.mageddo.linux.bluetoothfix.BluetoothConnector - status=successfullyConnected!, device=ConReq(deviceId=94:CC:56:E5:72:85, controllerId=00:F0:83:56:F2:03), totalTime=10158
```

More Details below:

### Install Deps

Download the [latest release][1]

Install `bluetoothctl`
```
# sudo apt install bluez
$ bluetoothctl --version
bluetoothctl: 5.64
```

### Pair the Phone
Pair the phone as you commonly do, make sure to know what was the controller id used (see `Discover uour Controller ID`) 

### Discover your Controller ID aka Bluetooth Dongle ID

Discover your controller id, generally you will have only one, I have two, so you need to make sure to identify 
the one you paired with your headphone with, if not sure, unpair and pair again. `00:F0:83:56:F2:03` is mine. 

```bash
bluetoothctl list
Controller 00:2A:7D:CA:71:0B bluetooth4_ [default]
Controller 00:F0:83:56:F2:03 bluetooth5 
```

### Discover your Device ID / Phone ID

Discover your device ID, for example `94:CC:56:E5:72:85` is my headphone:
```bash
$ bluetoothctl
$ select 00:F0:83:56:F2:03
$ devices 
Device 94:CC:56:E5:72:85 WH-1000XM4
```


## Requirements

* Linux
* JRE 11+

## How it works
After buy a new bluetooth usb dongle, I noticed it was very difficult to make it connect to my headphones, also
noticed if I restart the bluetooth service and try to connect sometimes it will work at some moment, so what I did 
was jut automate this process. 

About the bluetooth issue root cause:

I wasn't able to find a real fix for the bluetooth problem, looks like it doesn't exist yet,
all people advise to buy a new hardware at the end, then I made this program as a workaround.

Related issues 
* https://bbs.archlinux.org/viewtopic.php?id=198718
* https://bbs.archlinux.org/viewtopic.php?id=270693
* https://bbs.archlinux.org/viewtopic.php?id=195886&p=2
* https://unix.stackexchange.com/questions/581974/alpine-linux-failed-to-start-discovery-org-bluez-error-inprogress

## Compiling from source

```bash
$ ./gradlew build shadowJar
```

[1]: https://github.com/mageddo-projects/linux-bluetooth-connection-fix/releases

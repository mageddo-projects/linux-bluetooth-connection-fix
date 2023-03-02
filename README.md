# linux-bluetooth-connection-fix
Tries  to connect bluetooth devices on Linux despite error `hci0: command 0x0c24 tx timeout`

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
* JRE 8+

# Compiling from source

```bash
$ ./gradlew build shadowJar
```

[1]: https://github.com/mageddo-projects/linux-bluetooth-connection-fix/releases

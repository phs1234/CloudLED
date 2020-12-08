package com.devreport.cloud.main.fragment.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.widget.Toast;

import java.io.IOException;
import java.io.OutputStream;
import java.util.UUID;

public class BluetoothService {
    private static final String TAG = "BluetoothService";
    private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");     //임의의 숫자라도 괜찮다

    private static String macAddress = "98:D3:71:FD:5F:AF";

    private static BluetoothAdapter bluetoothAdapter;
    private static BluetoothDevice bluetoothDevice;
    private static BluetoothSocket bluetoothSocket;
    private static OutputStream outputStream;

    public static boolean Connect(String macAddress, Context context) {
        try {
            bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            BluetoothService.macAddress = macAddress;
            bluetoothDevice = bluetoothAdapter.getRemoteDevice(macAddress);
            bluetoothSocket = bluetoothDevice.createInsecureRfcommSocketToServiceRecord(MY_UUID);

            bluetoothSocket.connect();

            outputStream = bluetoothSocket.getOutputStream();

            Toast.makeText(context, "연결 성공", Toast.LENGTH_SHORT).show();

            return true;
        } catch (IOException e) {
            e.printStackTrace();

            Toast.makeText(context, "연결 실패", Toast.LENGTH_SHORT).show();

            return false;
        }
    }

    public static boolean writeData(String data) {
        try {
            outputStream.write(data.getBytes());

            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }

    public static boolean writeData(int red, int green, int blue) {
        try {
            String data = red + "," + green + "," + blue + "@";
            outputStream.write(data.getBytes());

            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }
}

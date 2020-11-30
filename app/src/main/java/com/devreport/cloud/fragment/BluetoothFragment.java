package com.devreport.cloud.fragment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import com.firebase.cloud.R;

import java.util.ArrayList;
import java.util.Set;

public class BluetoothFragment extends Fragment {
    public static final int REQUEST_ENABLE_BT = 101;
    public View mView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mView = layoutInflater.inflate(R.layout.fragment_bluetooth, null);

        RecyclerView pairedRecyclerView = mView.findViewById(R.id.pairedRecyclerView);

        pairedRecyclerView.setHasFixedSize(true);

        //블루투스 기능 활성화
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        if (!bluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }

        // 페어링된 기기확인
        BluetoothDevice[] bluetoothDeviceArray = (BluetoothDevice[])bluetoothAdapter.getBondedDevices().toArray();


    }
}

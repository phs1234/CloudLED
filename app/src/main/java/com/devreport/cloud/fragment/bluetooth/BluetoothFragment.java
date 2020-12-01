package com.devreport.cloud.fragment.bluetooth;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.cloud.R;

public class BluetoothFragment extends Fragment {
    public static final String TAG = "BluetoothFragment";

    public static final int REQUEST_ENABLE_BT = 101;
    public View mView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mView = layoutInflater.inflate(R.layout.fragment_bluetooth, null);

        RecyclerView pairedRecyclerView = mView.findViewById(R.id.pairedRecyclerView);
        RecyclerView searchedRecyclerVIew = mView.findViewById(R.id.searchedRecyclerView);

        pairedRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        pairedRecyclerView.addItemDecoration(new DividerItemDecoration(pairedRecyclerView.getContext(), DividerItemDecoration.VERTICAL));

        // 퍼포먼스를 향상되게 해준다
        pairedRecyclerView.setHasFixedSize(true);
        searchedRecyclerVIew.setHasFixedSize(true);

        // 블루투스 기능 활성화
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        if (!bluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }

        // 페어링된 기기확인
        BluetoothDevice[] bluetoothDevices = bluetoothAdapter.getBondedDevices().toArray(new BluetoothDevice[0]);

        BluetoothViewAdapter viewAdapter = new BluetoothViewAdapter(bluetoothDevices);
        pairedRecyclerView.setAdapter(viewAdapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return mView;
    }
}

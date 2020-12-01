package com.devreport.cloud.fragment.bluetooth;

import android.bluetooth.BluetoothDevice;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.firebase.cloud.R;

public class BluetoothViewAdapter extends RecyclerView.Adapter<BluetoothViewAdapter.BluetoothDeviceHolder> {
    public static final String TAG = "BluetoothViewAdapter";

    private BluetoothDevice[] bluetoothDevices;
    
    public static class BluetoothDeviceHolder extends RecyclerView.ViewHolder {
        public TextView nameTextView;
        public TextView addressTextView;
        
        public BluetoothDeviceHolder (View view) {
            super(view);
            
            this.nameTextView = view.findViewById(R.id.nameTexrView);
            this.addressTextView = view.findViewById(R.id.adressTextView);
        }
    }

    public BluetoothViewAdapter(BluetoothDevice[] bluetoothDevices) {
        this.bluetoothDevices = bluetoothDevices;
    }

    @Override
    public BluetoothDeviceHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.holder_bluetooth, parent, false);

        return new BluetoothDeviceHolder(view);
    }

    @Override
    public void onBindViewHolder(BluetoothDeviceHolder holder, int pos) {
        Log.d(TAG, bluetoothDevices[pos].getName());

        holder.nameTextView.setText(bluetoothDevices[pos].getName());
        holder.addressTextView.setText(bluetoothDevices[pos].getAddress());
    }

    @Override
    public int getItemCount() {
        return bluetoothDevices.length;
    }
}

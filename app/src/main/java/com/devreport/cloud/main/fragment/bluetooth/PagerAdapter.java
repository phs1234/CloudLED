package com.devreport.cloud.main.fragment.bluetooth;

import android.bluetooth.BluetoothDevice;

import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class PagerAdapter extends FragmentStateAdapter {
    private BluetoothDevice[] bluetoothDevices;

    public PagerAdapter(Fragment fragment, BluetoothDevice[] bluetoothDevices) {
        super(fragment);

        this.bluetoothDevices = bluetoothDevices;
    }

    @Override
    public Fragment createFragment(int pos) {
        return new PagerFragment(bluetoothDevices, pos);
    }

    @Override
    public int getItemCount() {
        return bluetoothDevices.length / 4 + 1;
    }
}

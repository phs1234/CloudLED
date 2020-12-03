package com.devreport.cloud.fragment.bluetooth;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.firebase.cloud.R;

public class PagerFragment extends Fragment {
    private BluetoothDevice[] bluetoothDevices;
    private int page;

    private View view;

    public PagerFragment (BluetoothDevice[] bluetoothDevices, int page) {
        this.bluetoothDevices = bluetoothDevices;
        this.page = page;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        view = layoutInflater.inflate(R.layout.fragment_bluetooth_pager, null);

        LinearLayout linearLayout = view.findViewById(R.id.LinearLayout);

        for (int index = 0; index < 4; index++) {
            if (4 * page + index < bluetoothDevices.length) {
                View view = layoutInflater.inflate(R.layout.holder_bluetooth, null);

                TextView nameTextView = view.findViewById(R.id.nameTextView);
                TextView addressTextView = view.findViewById(R.id.addressTextView);

                nameTextView.setText(bluetoothDevices[4 * page + index].getName());
                addressTextView.setText(bluetoothDevices[4 * page + index].getAddress());

                linearLayout.addView(view);
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return view;
    }
}

package com.devreport.cloud.fragment.bluetooth;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.cloud.R;
import com.tbuonomo.viewpagerdotsindicator.DotsIndicator;
import com.tbuonomo.viewpagerdotsindicator.SpringDotsIndicator;

public class BluetoothFragment extends Fragment {
    public static final String TAG = "BluetoothFragment";

    private static final int REQUEST_ENABLE_BT = 101;

    private View mView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mView = layoutInflater.inflate(R.layout.fragment_bluetooth, null);

        ViewPager2 pairingViewPager = mView.findViewById(R.id.pairingViewPager);
        DotsIndicator indicator = mView.findViewById(R.id.DotsIndicator);

        // 블루투스 기능 활성화
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        if (!bluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }

        // 페어링된 기기확인
        BluetoothDevice[] bluetoothDevices = bluetoothAdapter.getBondedDevices().toArray(new BluetoothDevice[0]);

        PagerAdapter pagerAdapter= new PagerAdapter(this, bluetoothDevices);
        pairingViewPager.setAdapter(pagerAdapter);

        indicator.setViewPager2(pairingViewPager);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return mView;
    }
}

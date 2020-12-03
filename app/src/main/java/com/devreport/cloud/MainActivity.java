package com.devreport.cloud;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;

import com.devreport.cloud.fragment.bluetooth.BluetoothService;
import com.firebase.cloud.R;

public class MainActivity extends AppCompatActivity {

    private ViewPager2 mViewpager;
    private FragmentStateAdapter mPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mViewpager = findViewById(R.id.viewPager);
        mPagerAdapter = new PagerAdapter(this);
        mViewpager.setAdapter(mPagerAdapter);

        //BluetoothService.getInstance().writeData("Hello world");


    }
}

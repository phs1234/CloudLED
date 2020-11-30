package com.devreport.cloud;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.devreport.cloud.fragment.ColorPickerFragment;
import com.devreport.cloud.fragment.WeatherFragment;

public class PagerAdapter extends FragmentStateAdapter {
    public PagerAdapter(FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new WeatherFragment();
            case 1:
                return new ColorPickerFragment();
        }
        return null;
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}

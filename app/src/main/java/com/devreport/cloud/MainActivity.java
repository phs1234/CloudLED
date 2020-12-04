package com.devreport.cloud;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;

import com.firebase.cloud.R;

public class MainActivity extends AppCompatActivity {

    private static ViewPager2 viewpager;

    public static void moveToMainPage(int page) {
        if (viewpager != null)
            viewpager.setCurrentItem(page);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewpager = findViewById(R.id.viewPager);

        FragmentStateAdapter pagerAdapter = new PagerAdapter(this);
        viewpager.setAdapter(pagerAdapter);
    }


}

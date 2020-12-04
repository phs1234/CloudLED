package com.devreport.cloud.fragment;

import android.content.Context;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.Debug;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.devreport.cloud.fragment.bluetooth.BluetoothService;
import com.firebase.cloud.R;
import com.larswerkman.holocolorpicker.ColorPicker;
import com.larswerkman.holocolorpicker.SVBar;

public class ColorPickerFragment extends Fragment {
    public View mView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mView = layoutInflater.inflate(R.layout.fragment_colorpicker, null);

        final ColorPicker colorPicker = mView.findViewById(R.id.ColorPicker);
        final TextView RGBTextView = mView.findViewById(R.id.RGBTextView);
        final ImageView ovalImageView = mView.findViewById(R.id.OvalImageView);
        final SVBar SVBar = mView.findViewById(R.id.SVBar);

        colorPicker.addSVBar(SVBar);

        colorPicker.setOnColorChangedListener(new ColorPicker.OnColorChangedListener() {
            @Override
            public void onColorChanged(int color) {
                int red = (color >> 16) & 0xff;
                int blue = (color >> 8) & 0xff;
                int green = color & 0xff;

                RGBTextView.setText("#" + Integer.toHexString(red).toUpperCase()
                                        + Integer.toHexString(blue).toUpperCase()
                                        + Integer.toHexString(green).toUpperCase());

                ovalImageView.setBackgroundColor(color);
            }
        });

        colorPicker.setOnColorSelectedListener(new ColorPicker.OnColorSelectedListener() {
            @Override
            public void onColorSelected(int color) {
                int red = (color >> 16) & 0xff;
                int blue = (color >> 8) & 0xff;
                int green = color & 0xff;

                BluetoothService.writeData(red, green, blue);
            }
        });



        int red = (colorPicker.getColor() >> 16) & 0xff;
        int blue = (colorPicker.getColor() >> 8) & 0xff;
        int green = colorPicker.getColor() & 0xff;

        RGBTextView.setText("#" + red + blue + green);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return mView;
    }
}

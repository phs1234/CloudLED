package com.devreport.cloud.fragment.weather;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.firebase.cloud.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WeatherFragment extends Fragment {
    public static final String TAG = "WeatherFragment";

    public View view;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = layoutInflater.inflate(R.layout.fragment_weather, null);

        WeatherRetrofitService.getCurrentWeather(1835847, new Callback<WeatherAllData>() {
            @Override
            public void onResponse(Call<WeatherAllData> call, Response<WeatherAllData> response) {
                WeatherAllData result = response.body();


                
                String message = "구름 : " + result.getCloudData().getDensity() + "\n"
                                + "온도 : " + result.getDetailData().getTemp() + "\n"
                                + "습기 : " + result.getDetailData().getHumidity() + "\n"
                                + "기압 : " + result.getDetailData().getPressure() + "\n"
                                + "바람 : " + result.getWindData().getSpeed() + "\n";

                Log.d(TAG, message);
            }

            @Override
            public void onFailure(Call<WeatherAllData> call, Throwable t) {
                Log.d(TAG, "실패!" + t.getMessage());
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return view;
    }
}

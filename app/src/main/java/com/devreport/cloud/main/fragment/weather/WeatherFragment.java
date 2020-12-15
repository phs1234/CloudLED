package com.devreport.cloud.main.fragment.weather;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.devreport.cloud.main.fragment.bluetooth.BluetoothService;
import com.devreport.cloud.map.MapActivity;
import com.firebase.cloud.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class WeatherFragment extends Fragment {
    private static final String TAG = "WeatherFragment";

    private static final String APPID = "dacdd3ff23cc19ea41e9107e3006a07e";
    private static final int MAP_REQUEST_CODE = 101;

    private View view;

    private TextView cityNameTextView;

    private ImageView cloudImageView;
    private TextView cloudTextView, cloudSubTextView;

    private TextView  tempTextView, humidityTextView, pressureTextView, windTextView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 뷰 관련 객체 선언
        LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = layoutInflater.inflate(R.layout.fragment_weather, null);

        LinearLayout mapButton = view.findViewById(R.id.MapButton);

        cityNameTextView = view.findViewById(R.id.CityNameTextView);

        cloudImageView = view.findViewById(R.id.CloudImageView);
        cloudTextView = view.findViewById(R.id.CloudTextView);
        cloudSubTextView = view.findViewById(R.id.CloudSubTextView);

        tempTextView = view.findViewById(R.id.TempTextView);
        humidityTextView = view.findViewById(R.id.HumidityTextView);
        pressureTextView = view.findViewById(R.id.PressureTextView);
        windTextView = view.findViewById(R.id.WindTextView);

        mapButton.setOnClickListener(view -> {
            Intent intent = new Intent(getContext(), MapActivity.class);
            startActivityForResult(intent, MAP_REQUEST_CODE);
        });

        // 날씨 서울로 세팅
        setCurrentWeather(1835847);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == MAP_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                cityNameTextView.setText(intent.getExtras().getString("name"));

                setCurrentWeather(intent.getExtras().getInt("id"));
            }
        }
    }

    // API 에서 불러와서 화면에 보여주기 까지
    private void setCurrentWeather (int id) {
        WeatherInterface weatherInterface = new Retrofit.Builder()
                .baseUrl("https://api.openweathermap.org/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(WeatherInterface.class);

        Call<WeatherAllData> call =  weatherInterface.getCurrentWeather(id, APPID);
        call.enqueue(new Callback<WeatherAllData>() {
            @Override
            public void onResponse(Call<WeatherAllData> call, Response<WeatherAllData> response) {
                WeatherAllData result = response.body();

                // 구름 이미지 및 텍스트 변경
                if (result.getCloudData().getDensity() >= 60) {
                    cloudImageView.setImageResource(R.drawable.icon_cloud_cloudy);

                    cloudTextView.setText("Cloudy Day");
                    cloudSubTextView.setText("산책하기 좋은 날씨네요!");
                } else if (result.getCloudData().getDensity() >= 30) {
                    cloudImageView.setImageResource(R.drawable.icon_cloud_calm);

                    cloudTextView.setText("Calm Day");
                    cloudSubTextView.setText("아메리카노가 땡기네요!");
                } else {
                    cloudImageView.setImageResource(R.drawable.icon_cloud_clear);

                    cloudTextView.setText("Clear Day");
                    cloudSubTextView.setText("뭘 해도 좋을 날씨예요!");
                }

                // 세부사항 바꾸기
                int temp = (int)(result.getDetailData().getTemp() - 273);

                tempTextView.setText(temp + " °C");
                humidityTextView.setText(result.getDetailData().getHumidity() + " %");
                pressureTextView.setText(result.getDetailData().getPressure() + " inHg");
                windTextView.setText(result.getWindData().getSpeed() + " mph");

                // 온도에 따라 구름 LED에 색깔데이터 보내기
                if (temp >= 28) {
                    BluetoothService.writeData(255, 128, 0);
                } else if (temp >= 18) {
                    BluetoothService.writeData(128, 255, 0);
                } else if (temp >= 10) {
                    BluetoothService.writeData(0, 128, 128);
                } else if (temp >= 0) {
                    BluetoothService.writeData(0, 128, 255);
                } else {
                    BluetoothService.writeData(0, 0, 255);
                }
            }

            @Override
            public void onFailure(Call<WeatherAllData> call, Throwable t) {
                Log.d(TAG, "실패!" + t.getMessage());
            }
        });
    }

    // API 에서 날씨 데이터를 가져와서 CallBack 에 넘김
    public void setCurrentWeather (int id, Callback callback) {
        WeatherInterface weatherInterface = new Retrofit.Builder()
                .baseUrl("https://api.openweathermap.org/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(WeatherInterface.class);

        Call<WeatherAllData> call =  weatherInterface.getCurrentWeather(id, APPID);
        call.enqueue(callback);
    }
}

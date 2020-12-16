package com.devreport.cloud.main.fragment.weather;

import android.animation.AnimatorInflater;
import android.animation.ArgbEvaluator;
import android.animation.TimeAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Debug;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.devreport.cloud.main.MainActivity;
import com.devreport.cloud.main.PagerAdapter;
import com.devreport.cloud.main.fragment.ColorPickerFragment;
import com.devreport.cloud.main.fragment.bluetooth.BluetoothService;
import com.devreport.cloud.map.MapActivity;
import com.firebase.cloud.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class WeatherFragment extends Fragment {
    private static final String TAG = "WeatherFragment";

    private static WeatherFragment instance;

    private static final String APPID = "dacdd3ff23cc19ea41e9107e3006a07e";
    private static final int MAP_REQUEST_CODE = 101;

    private View view;

    private TextView cityNameTextView;

    private ImageView cloudImageView;
    private TextView cloudTextView, cloudSubTextView;

    private TextView  tempTextView, humidityTextView, pressureTextView, windTextView;

    private GradientDrawable bgGradient;

    private ImageView dessert1ImageView, dessert2ImageView, dessert3ImageView;
    private ValueAnimator[] animators = new ValueAnimator[10];

    public static WeatherFragment getInstance() {
        if(instance == null) {
            instance = new WeatherFragment();
        }

        return instance;
    }

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

        bgGradient = (GradientDrawable) view.findViewById(R.id.WeatherLayout).getBackground();

        dessert1ImageView = view.findViewById(R.id.Dessert1ImageView);
        dessert2ImageView = view.findViewById(R.id.Dessert2ImageView);
        dessert3ImageView = view.findViewById(R.id.Dessert3ImageView);

        // 맵 버튼 클릭리스너 등록
        mapButton.setOnClickListener(view -> {
            Intent intent = new Intent(getContext(), MapActivity.class);
            startActivityForResult(intent, MAP_REQUEST_CODE);
        });

        // 처음에 태그 데이터가 없기 때문에 지정해줘야 한다.
        dessert1ImageView.setTag(Color.parseColor("#884500"));
        dessert2ImageView.setTag(Color.parseColor("#B97738"));
        dessert3ImageView.setTag(Color.parseColor("#CC915C"));
        Log.d(TAG, "origin : " + Color.parseColor("#884500"));

        // 날씨 서울로 세팅
        setCurrentWeather(1835847);

        // 애니메이터 객체 초기화
        for (int i = 0; i < animators.length; i++) {
            animators[i] = new ValueAnimator();
        }
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

        // 파싱 시작과
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

                // 온도에 따라 배경바꾸고 구름 LED에 데이터 보내기
                if (temp >= 30) {
                    changeGradientColor(bgGradient, "#F58563", "#792020", 500);

                    changeImageColor(dessert1ImageView, "#4A130A", 500);
                    changeImageColor(dessert2ImageView, "#76271D", 500);
                    changeImageColor(dessert3ImageView, "#943535", 500);

                    BluetoothService.writeData(255, 128, 0);
                } else if (temp >= 20) {
                    changeGradientColor(bgGradient, "#F0B07C", "#DE733C", 500);

                    changeImageColor(dessert1ImageView, "#884500", 500);
                    changeImageColor(dessert2ImageView, "#B97738", 500);
                    changeImageColor(dessert3ImageView, "#CC915C", 500);

                    BluetoothService.writeData(128, 255, 0);
                } else if (temp >= 10) {
                    changeGradientColor(bgGradient, "#81CF5E", "#26803C", 500);

                    changeImageColor(dessert1ImageView, "#0A4A25", 500);
                    changeImageColor(dessert2ImageView, "#1D7624", 500);
                    changeImageColor(dessert3ImageView, "#569435", 500);

                    BluetoothService.writeData(0, 128, 128);
                } else if (temp >= 0) {
                    changeGradientColor(bgGradient, "#63B2F5", "#322079", 500);

                    changeImageColor(dessert1ImageView, "#0A194A", 500);
                    changeImageColor(dessert2ImageView, "#1D2E76", 500);
                    changeImageColor(dessert3ImageView, "#354694", 500);

                    BluetoothService.writeData(0, 128, 255);
                } else {
                    changeGradientColor(bgGradient, "#B063F5", "#322079", 500);
  //                  changeGradientColor(ColorPickerFragment.getInstance().getBgGradient(), "#401388", "#000000", 500);

                    changeImageColor(dessert1ImageView, "#401388", 500);
                    changeImageColor(dessert2ImageView, "#5F24B2", 500);
                    changeImageColor(dessert3ImageView, "#7C3493", 500);

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

    // 함수가 끝나면 ValueAnimation 이 사라지기 때문에 클래스 변수로 등록해둔 animator 중에서 돌아가고 있지 않은 애니메이터를 찾아야 한다.
    private void changeImageColor (ImageView imageView, String colorTo, long duration) {
        for (ValueAnimator animator : animators) {
            if (!animator.isRunning()) {
                int colorFrom = (int) imageView.getTag();

                Log.d(TAG, "sent : " + colorFrom);

                animator = ValueAnimator.ofObject(new ArgbEvaluator(), colorFrom, Color.parseColor(colorTo));
                animator.setDuration(duration);

                animator.addUpdateListener(animatorRun -> {
                    imageView.setColorFilter((int) animatorRun.getAnimatedValue(), PorterDuff.Mode.SRC_IN);
                    imageView.setTag(animatorRun.getAnimatedValue());
                });

                animator.start();
            }
        }
    }

    private void changeGradientColor (GradientDrawable gradient, String topToString, String bottomToString, long duration) {
        for (ValueAnimator animator : animators) {
            if (!animator.isRunning()) {
                final int topFrom = gradient.getColors()[0];
                final int bottomFrom = gradient.getColors()[1];

                final int topTo = Color.parseColor(topToString);
                final int bottomTo = Color.parseColor(bottomToString);

                final ArgbEvaluator evaluator = new ArgbEvaluator();

                animator = TimeAnimator.ofFloat(0.0f, 1.0f);
                animator.setDuration(duration);

                animator.addUpdateListener(valueAnimator -> {
                    Float fraction = valueAnimator.getAnimatedFraction();
                    int topColor = (int) evaluator.evaluate(fraction, topFrom, topTo);
                    int bottomColor = (int) evaluator.evaluate(fraction, bottomFrom, bottomTo);

                    int[] colorArray = {topColor, bottomColor};

                    gradient.setColors(colorArray);
                });

                animator.start();
            }
        }
    }
}

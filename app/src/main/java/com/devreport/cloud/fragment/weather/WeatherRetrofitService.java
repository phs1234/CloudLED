package com.devreport.cloud.fragment.weather;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class WeatherRetrofitService {
    private static final String appId = "dacdd3ff23cc19ea41e9107e3006a07e";

    public static void getCurrentWeather (int id, Callback callback) {
         WeatherInterface weatherInterface = new Retrofit.Builder()
                .baseUrl("https://api.openweathermap.org/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(WeatherInterface.class);

        Call<WeatherAllData> call =  weatherInterface.getCurrentWeather(id, appId);
        call.enqueue(callback);
    }
}

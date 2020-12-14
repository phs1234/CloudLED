package com.devreport.cloud.map;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.firebase.cloud.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {
    private static final String TAG = "MapActivity";

    private TextView nameTextView, latLngTextView, descTextView;
    private Button setButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.MapFragment);
        mapFragment.getMapAsync(this);

        nameTextView = findViewById(R.id.NameTextView);
        latLngTextView = findViewById(R.id.LatLngTextView);
        descTextView = findViewById(R.id.DescTextView);

        setButton = findViewById(R.id.setButton);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        try {
            // 맵 스타일 지정
            googleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.map_style));

            // 마커 표시하기
            InputStream inputStream = getAssets().open("map_city.json");

            int fileSize = inputStream.available();

            byte[] buffer = new byte[fileSize];
            inputStream.read(buffer);
            inputStream.close();

            JSONArray cityArray = new JSONArray(new String(buffer, "UTF-8"));

            for (int i = 0; i < cityArray.length(); i++) {
                JSONObject city = cityArray.getJSONObject(i);
                JSONObject cityCoord = city.getJSONObject("coord");

                MarkerOptions markerOptions = new MarkerOptions();
                MapMarkerInfo mapMarkerInfo = new MapMarkerInfo(city.getInt("id"),
                                                                city.getString("name"),
                                                                cityCoord.getDouble("lat"),
                                                                cityCoord.getDouble("lon"),
                                                                "");

                markerOptions.position(new LatLng(mapMarkerInfo.getLat(), mapMarkerInfo.getLon()));
                googleMap.addMarker(markerOptions).setTag(mapMarkerInfo);
            }

            googleMap.setOnMarkerClickListener(this);
        } catch (IOException exception) {

        } catch (JSONException exception) {
            exception.printStackTrace();
        }
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        MapMarkerInfo mapMarkerInfo = (MapMarkerInfo) marker.getTag();


        nameTextView.setText(mapMarkerInfo.getName());
        latLngTextView.setText("Lat " + String.format("%.2f", mapMarkerInfo.getLat()) + "\t" + "Lon " + String.format("%.2f", mapMarkerInfo.getLon()));
        descTextView.setText(mapMarkerInfo.getDesc());

        setButton.setOnClickListener(view -> {
            Intent intent = new Intent();

            intent.putExtra("id", mapMarkerInfo.getId());
            intent.putExtra("name", mapMarkerInfo.getName());

            setResult(RESULT_OK, intent);
            finish();
        });

        return true;
    }
}

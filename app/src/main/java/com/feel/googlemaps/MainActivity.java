package com.feel.googlemaps;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback, View.OnClickListener {
    private GoogleMap mMap;
    private ImageView img_search;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        reConnectedWidget();

        Log.e("GoogleMaps","Success");

    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng Location = new LatLng(37.363348, 127.114821);
        mMap.addMarker(new MarkerOptions().position(Location).title("Marker in Location"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(Location));
        CameraUpdate zoom = CameraUpdateFactory.zoomTo(15);
        mMap.animateCamera(zoom);   // moveCamera 는 바로 변경하지만,
        // animateCamera() 는 근거리에선 부드럽게 변경합니다


        /**
         * Zoom level 0 1:20088000.56607700 meters
         * Zoom level 1 1:10044000.28303850 meters
         * Zoom level 2 1:5022000.14151925 meters
         * Zoom level 3 1:2511000.07075963 meters
         * Zoom level 4 1:1255500.03537981 meters
         * Zoom level 5 1:627750.01768991 meters
         * Zoom level 6 1:313875.00884495 meters
         * Zoom level 7 1:156937.50442248 meters
         * Zoom level 8 1:78468.75221124 meters
         * Zoom level 9 1:39234.37610562 meters
         * Zoom level 10 1:19617.18805281 meters
         * Zoom level 11 1:9808.59402640 meters
         * Zoom level 12 1:4909.29701320 meters
         * Zoom level 13 1:2452.14850660 meters
         * Zoom level 14 1:1226.07425330 meters
         * Zoom level 15 1:613.03712665 meters
         * Zoom level 16 1:306.51856332 meters
         * Zoom level 17 1:153.25928166 meters
         * Zoom level 18 1:76.62964083 meters
         * Zoom level 19 1:38.31482042 meters
         */
    }
    @Override
    public void onClick(View view)
    {
        int id = view.getId();
        switch (id){
            case R.id.img_search:
                Toast.makeText(this,"Search to Location",Toast.LENGTH_SHORT).show();
                break;
        }
    }

    public void reConnectedWidget()
    {
        img_search = (ImageView) findViewById(R.id.img_search);
        img_search.setOnClickListener(this);
        permissionCheck();

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


    }

    public void permissionCheck()
    {

    }
}
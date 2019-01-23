package com.feel.googlemaps;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.security.Permission;
import java.text.DecimalFormat;
import java.util.List;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback, View.OnClickListener, GoogleMap.OnMapLongClickListener, GoogleMap.OnMapClickListener {
    private Context mContext;
    private GoogleMap mMap;
    private Geocoder mGeocoder;
    private ImageView img_search;
    private Marker myMarker;
    private LatLng mStart_Location;
    private Location mPointing_Locationn;
    private GoogleApiClient mGoogleApiClient;

    private FusedLocationProviderClient mFusedLocationProviderClient;
    private LocationCallback mLocationCallback;
    private Location mCurrentLocation;
    private LocationRequest mLocationRequest;
    private boolean mLocationPermissionGranted = false;

    private EditText medt_distance;


    private float mDistance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        reConnectedWidget();

        Log.e("GoogleMaps", "Success");

    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if(!mLocationPermissionGranted) {
            getLocationPermission();
        }
        // Add a marker in Sydney and move the camera
        mStart_Location = new LatLng(37.363348, 127.114821);
        mPointing_Locationn.setLatitude(mStart_Location.latitude); // 시작 위치 위도
        mPointing_Locationn.setLongitude(mStart_Location.longitude); // 시작 위치 경도

        mMap.addMarker(new MarkerOptions().position(mStart_Location)
                .title("Marker in Location")
                .snippet("위도 : " + mStart_Location.latitude + " 경도 : " + mStart_Location.longitude)); // 마커를 추가한다.

//        LatLng Current_Location = new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude());
//        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(Current_Location, 15));// 현재위치로 카메라를 움직인다.
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mStart_Location, 15));// moveCamera 는 바로 변경하지만,
//        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(mStart_Location,15));   // animateCamera() 는 근거리에선 부드럽게 변경합니다
        mMap.setOnMapClickListener(MainActivity.this); // 지도 클릭 이벤트
        mMap.setOnMapLongClickListener(MainActivity.this); // 지도 롱클릭 이벤트


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
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {

        }
    }

    @Override
    public void onMapClick(LatLng latLng) {
        Toast.makeText(this, "Map Click\n위도 : " + latLng.latitude + "\n경도 : " + latLng.longitude, Toast.LENGTH_SHORT).show();
        if (myMarker == null) {

        } else {
//            myMarker = mMap.addMarker(new MarkerOptions()
//            .position(latLng));
        }

    }

    @Override
    public void onMapLongClick(LatLng latLng) {
//        Toast.makeText(this,"Map Long Click\n위도 : " +latLng.latitude+ "\n경도 : "+latLng.longitude,Toast.LENGTH_SHORT).show();
        List<Address> address = null;

        try {
            address = mGeocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
            Log.e("GeocoderTest", String.valueOf(address));

        } catch (IOException e) {

        }
        if (myMarker == null) {
            myMarker = mMap.addMarker(new MarkerOptions()
                    .position(latLng)
                    .title(address.get(0).getAddressLine(0))
                    .snippet("위도 : " + latLng.latitude + "\n경도 : " + latLng.longitude));
        } else {
            // Marker already exists, just update it's position
            myMarker.setTitle(address.get(0).getAddressLine(0));
            myMarker.showInfoWindow(); // 마커의 타이틀과 스니펫을 업데이트한다.
            myMarker.setPosition(latLng);
        }
        // Marker was not set yet. Add marker:
        Location Endpoint_lat = new Location("");
        Endpoint_lat.setLongitude(latLng.longitude);
        Endpoint_lat.setLatitude(latLng.latitude);

        /**
         * 두지점 사이의 거리를 구한다.
         */
        mDistance = mPointing_Locationn.distanceTo(Endpoint_lat) / 1000;
        if (mDistance < 1) {
            mDistance = mPointing_Locationn.distanceTo(Endpoint_lat);
            medt_distance.setText(new DecimalFormat("###,###").format(mDistance) + " m");
        } else {
            medt_distance.setText(new DecimalFormat("###,###.#").format(mDistance) + " km");
        }
    }

    public void reConnectedWidget() {
        mContext = this;
        mGeocoder = new Geocoder(mContext); // 주소값을 얻어 오기위한 변수

        medt_distance = (EditText) findViewById(R.id.edt_distance);
        mPointing_Locationn = new Location("Pointing_Location");


        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(mContext); // 현재 내위치를 얻어 오기 위한 변수
        CreateLocationCallback();
        CreateLocationRequest();
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode,permissions,grantResults);
        mLocationPermissionGranted = false;
        if(requestCode == 1)
        {
            mLocationPermissionGranted = true;
        }
        UpdateLocationUI();
    }
    private void CreateLocationCallback()
    {
        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                mCurrentLocation = locationResult.getLastLocation();


                LatLng Current_Location = new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude());
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(Current_Location, 15));// moveCamera 는 바로 변경하지만,

                UpdateLocationUI();
            }
        };
    }
    @SuppressWarnings("MissingPermission")
    private void UpdateLocationUI()
    {
        if(mMap == null) return;
        if(mLocationPermissionGranted) {
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(true);
        }
        else
        {
            mMap.setMyLocationEnabled(false);
            mMap.getUiSettings().setMyLocationButtonEnabled(false);
        }

    }
    private void CreateLocationRequest()
    {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(100);
        mLocationRequest.setFastestInterval(100);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY); // 배터리 소모량이 높고 정확도가 높다.
    }
    @SuppressWarnings("MissingPermission")
    private void getLocationPermission()
    {

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this,new String[] {Manifest.permission.ACCESS_FINE_LOCATION},1);
            getCurrentLocation();
        }
        else
        {
            mLocationPermissionGranted = true;
        }
    }
    @SuppressWarnings("MissingPermission")
    private void getCurrentLocation()
    {
        if(mLocationPermissionGranted)
        {
            mFusedLocationProviderClient.requestLocationUpdates(mLocationRequest,mLocationCallback, Looper.myLooper());
            UpdateLocationUI();
        }
    }

    /**
     * 콜백을 제거 해준다.
     */
    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        if(mFusedLocationProviderClient != null) {
            mFusedLocationProviderClient.removeLocationUpdates(mLocationCallback)
                    .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            mFusedLocationProviderClient = null;
                        }
                    });
        }
    }
    public void permissionCheck() {

    }
}
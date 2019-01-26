package com.feel.googlemaps;


import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
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
import java.security.MessageDigest;
import java.text.DecimalFormat;
import java.util.List;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback, View.OnClickListener, GoogleMap.OnMapClickListener {
    private Context mContext;
    private GoogleMap mMap;
    private Geocoder mGeocoder;
    private ImageView img_search;
    private Marker myMarker;

    private GoogleApiClient mGoogleApiClient;
    private FusedLocationProviderClient mFusedLocationProviderClient;

    private LatLng mDestination_LatLng; // 목적지 위도 경도
    private Location mDestination_Locationn; // 목적지 위치
    private Location m_Current_Location; // 현재 위치
    private LocationCallback mLocationCallback;
    private LocationRequest mLocationRequest;

    private boolean mLocationPermissionGranted = false;
    private EditText medt_distance;

    private float mDistance;

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // region Class Override Function
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        reConnectedWidget();
//        getAppKeyHash(); //get Hash Key

    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e("Current_Location","onActivityResult : "+data.toString());
        if ((resultCode == Activity.RESULT_OK) && (requestCode == 1000)) {
//            GpsManager.getInstance().startLocationUpdates();
        }
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        /**
         * 현재위치를 받아오는 콜백을 제거 해준다.
         */
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


    @Override
    public void onMapReady(GoogleMap googleMap) {
        List<Address> address = null;
        mMap = googleMap;
        if(!mLocationPermissionGranted) {
            permissionCheck();
//            getLocationPermission();
        }
        // Add a marker in Sydney and move the camera
        LatLng startLocation = mDestination_LatLng;
        try {
            address = mGeocoder.getFromLocation(startLocation.latitude, startLocation.longitude, 1);
        }
        catch (IOException e)
        {
            Log.e("Exception_Error","IOException Geocoder : "+e.toString());
        }

        mDestination_Locationn.setLatitude(startLocation.latitude); // 시작 위치 위도
        mDestination_Locationn.setLongitude(startLocation.longitude); // 시작 위치 경도

        mMap.addMarker(new MarkerOptions().position(startLocation)
                .title("A Destination Point")
                .snippet(address.get(0).getAddressLine(0)))// 위도 경도를 가지고 주소를 가져온다.
                .showInfoWindow(); // 말풍선 스니펫창이 떠있도록 설정한다.

//        LatLng Current_Location = new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude());
//        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(Current_Location, 15));// 현재위치로 카메라를 움직인다.

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(startLocation, 17));// moveCamera 는 바로 변경하지만,
//        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(mstartLocation,15));   // animateCamera() 는 근거리에선 부드럽게 변경합니다
        mMap.setOnMapClickListener(MainActivity.this); // 지도 클릭 이벤트

        getCurrentLocation();


        //region Zoom Level
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
        //endregion
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

    /**
     * requestPermissions으로 부터 권한요청 결과를 받는다.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode,permissions,grantResults);
        mLocationPermissionGranted = false;
        if(requestCode == ConstantDefine.REQUEST_CODE_PERMISSION)
        {
            mLocationPermissionGranted = true;
        }
        UpdateLocationUI();
    }

    // endregion
    ////////////////////////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // region Initialize
    public void reConnectedWidget() {
        mContext = this;
        mGeocoder = new Geocoder(mContext); // 주소값을 얻어 오기위한 변수
        mDestination_LatLng = new LatLng(37.363348, 127.114821); // 디폴트 위치 설정
        medt_distance = (EditText) findViewById(R.id.edt_distance);
        mDestination_Locationn = new Location("Pointing_Location");
        permissionCheck(); // 퍼미션 체크를 한다.

        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(mContext); // 현재 내위치를 얻어 오기 위한 변수
        CreateLocationCallback();
        CreateLocationRequest();
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

//        Handler handler = new Handler();
//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                if(mMap != null && m_Current_Location != null) {
//                    LatLng Current_LatLng = new LatLng(m_Current_Location.getLatitude(),m_Current_Location.getLongitude());
//                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(Current_LatLng, 17));// 목적지 위치로 카메라를 설정한다.
//                }
//            }},700);

    }
    private void getAppKeyHash() {
        try {
            PackageInfo info = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md;
                md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String something = new String(Base64.encode(md.digest(), 0));
                Log.e("Hash_key", something);
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            Log.e("Hash_key","Function = "+ e.toString());
        }
    }


    // endregion
    ////////////////////////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // region Function
    public void getDistanceDeference(Location location)
    {
        /**
         * @mDestination_Locationn 목적지 위도,경도 위치
         */
        mDistance = mDestination_Locationn.distanceTo(location) / 1000;
        if (mDistance < 1) { // 가까울 경우 미터 단위로
            mDistance = mDestination_Locationn.distanceTo(location);
            medt_distance.setText(new DecimalFormat("###,###").format(mDistance) + " m");
        } else { // 멀경우 킬로미터 단위로
            medt_distance.setText(new DecimalFormat("###,###.#").format(mDistance) + " km");
        }
    }
    // endregion
    ////////////////////////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // region Current Position Callback
    private void CreateLocationCallback()
    {
        Log.e("Current_Location","Location Callback : "+String.valueOf(mLocationPermissionGranted));
        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                m_Current_Location = locationResult.getLastLocation();
//                Location currentLocation = locationResult.getLastLocation();
                Log.e("Current_Location","Location Result : "+locationResult.getLastLocation().toString());

                LatLng Current_Location = new LatLng(m_Current_Location.getLatitude(), m_Current_Location.getLongitude());

                /**
                 * Zoom 크기가 커질수록 확대된다.
                 */
                mMap.animateCamera(CameraUpdateFactory.newLatLng(Current_Location));
//                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(Current_Location, 17));// moveCamera 는 바로 변경하지만, animationmove는 부드럽게 변경한다.
                /**
                 * 두지점 사이의 거리를 구한다.
                 */
                getDistanceDeference(m_Current_Location); //현재 위치의 위도,경도 정보를 파라미터로 넘긴다.
                UpdateLocationUI();
        }
        };
    }

    @SuppressWarnings("MissingPermission")
    private void UpdateLocationUI()
    {

        if(mMap == null) return;
        Log.e("Current_Location","Location Permission : "+String.valueOf(mLocationPermissionGranted));
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
        /**
         * @setInteval : 위치가 update되는 주기
         * @setFastestInterval : 위치 획득 후 update되는 주기
         *
         *         Priority는 4가지의 설정값이 있다.
         * @PRIORITY_HIGH_ACCURACY : 배터리소모를 고려하지 않으며 정확도를 최우선으로 고려
         * @PRIORITY_LOW_POWER : 저전력을 고려하며 정확도가 떨어짐
         * @PRIORITY_NO_POWER : 추가적인 배터리 소모없이 위치정보 획득
         * @PRIORITY_BALANCED_POWER_ACCURACY : 전력과 정확도의 밸런스를 고려. 정확도 다소 높음
         * setSmallestDisplacement : 최소 거리 이동시 갱신 가능.
         */
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(100); // 위치가 업데이틑 되는 주기
        mLocationRequest.setFastestInterval(100); // 위치획득후 업데이트되는 주기
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY); // 배터리 소모량이 높고 정확도가 높다.
        mLocationRequest.setSmallestDisplacement(100); // 최소거리이동시 갱신

    }

    @SuppressWarnings("MissingPermission")
    private void getCurrentLocation()
    {

        if(mLocationPermissionGranted)
        {
            Log.e("Current_Location","Location Permission : "+String.valueOf("현재위치를 업데이트 합니다."));
            mFusedLocationProviderClient.requestLocationUpdates(mLocationRequest,mLocationCallback, Looper.myLooper());
            UpdateLocationUI();
        }
    }
    //end region
    ////////////////////////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // region Permission
    @SuppressWarnings("MissingPermission")
    private void getLocationPermission() {

    }
    public void permissionCheck()
    {
        Log.e("Current_Location","Location Permission : "+String.valueOf(mLocationPermissionGranted));
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            Log.e("Current_Location","Location Permission : "+String.valueOf("권한을요청합니다."));
            ActivityCompat.requestPermissions(this,new String[] {Manifest.permission.ACCESS_FINE_LOCATION},ConstantDefine.REQUEST_CODE_PERMISSION);
            getCurrentLocation();
        }
        else
        {
            Log.e("Current_Location","Location Permission : "+String.valueOf("권한이 이미 허용되어있습니다."));
            mLocationPermissionGranted = true;

        }

    }
    //end region
    ////////////////////////////////////////////////////////////////////////////////////////////////

}
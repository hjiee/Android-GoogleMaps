package com.feel.googlemaps;

<<<<<<< HEAD
import android.Manifest;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Build;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
=======
import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
>>>>>>> 7794b33b0c1799918ea77d2e20a4007ab4429f16
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

<<<<<<< HEAD
import java.security.MessageDigest;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback, View.OnClickListener {
=======
import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback, View.OnClickListener,GoogleMap.OnMapLongClickListener, GoogleMap.OnMapClickListener {
>>>>>>> 7794b33b0c1799918ea77d2e20a4007ab4429f16
    private GoogleMap mMap;
    private ImageView img_search;
    private Marker myMarker;
    private LatLng mStart_Location;
    private Location mPointing_Locationn;
    private float mDistance;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        reConnectedWidget();
//        getAppKeyHash(); //get Hash Key
        Log.e("GoogleMaps","Success");

    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        mStart_Location = new LatLng(37.363348, 127.114821);
        mPointing_Locationn.setLatitude(mStart_Location.latitude); // 시작 위치 위도
        mPointing_Locationn.setLongitude(mStart_Location.longitude); // 시작 위치 경도

        mMap.addMarker(new MarkerOptions().position(mStart_Location).title("Marker in Location")); // 마커를 추가한다.
        mMap.moveCamera(CameraUpdateFactory.newLatLng(mStart_Location));// moveCamera 는 바로 변경하지만,
        mMap.animateCamera(CameraUpdateFactory.zoomTo(15));     // animateCamera() 는 근거리에선 부드럽게 변경합니다
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
    public void onClick(View view)
    {
        int id = view.getId();
        switch (id){
            case R.id.img_search:
                Toast.makeText(this,"Search to Location",Toast.LENGTH_SHORT).show();
                break;
        }
    }

    @Override
    public void onMapClick(LatLng latLng) {
        Toast.makeText(this,"Map Click\n위도 : " +latLng.latitude+ "\n경도 : "+latLng.longitude,Toast.LENGTH_SHORT).show();
        if (myMarker == null) {

        }
        else {
//            myMarker = mMap.addMarker(new MarkerOptions()
//            .position(latLng));
        }

    }
    @Override
    public void onMapLongClick(LatLng latLng) {
//        Toast.makeText(this,"Map Long Click\n위도 : " +latLng.latitude+ "\n경도 : "+latLng.longitude,Toast.LENGTH_SHORT).show();

            // First check if myMarker is null
            if (myMarker == null) {

                // Marker was not set yet. Add marker:
                myMarker = mMap.addMarker(new MarkerOptions()
                        .position(latLng)
                        .title("Your Start Position")
                        .snippet("위도 : " +latLng.latitude+ "\n경도 : "+latLng.longitude));


            } else {

                // Marker already exists, just update it's position

                myMarker.setPosition(latLng);

            }
        Location lat = new Location("");
        lat.setLongitude(latLng.longitude);
        lat.setLatitude(latLng.latitude);

        mDistance = mPointing_Locationn.distanceTo(lat)/1000 ;
        if(mDistance < 1) {
            mDistance = mPointing_Locationn.distanceTo(lat) / 10;
            Toast.makeText(this,"거리 : " + new DecimalFormat("###,###.##").format(mDistance) + " M",Toast.LENGTH_SHORT).show();
        }
        else
            Toast.makeText(this,"거리 : " + new DecimalFormat("###,###.##").format(mDistance) + " KM",Toast.LENGTH_SHORT).show();


        }

    public void reConnectedWidget()
    {
        img_search = (ImageView) findViewById(R.id.img_search);
        img_search.setOnClickListener(this);
        mPointing_Locationn = new Location("Pointing_Location");
        permissionCheck();

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);



    }
    public void Distans()
    {

    }
    public void permissionCheck()
    {

    }
<<<<<<< HEAD
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
=======


>>>>>>> 7794b33b0c1799918ea77d2e20a4007ab4429f16
}
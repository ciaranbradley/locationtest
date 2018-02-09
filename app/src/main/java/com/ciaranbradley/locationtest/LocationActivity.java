package com.ciaranbradley.locationtest;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

public class LocationActivity extends AppCompatActivity {

    private final String TAG = LocationActivity.class.getSimpleName();

    private static final int REQUEST_LOCATION_PERMISSION_CODE = 1;

    private LocationListener mGpsListener = new MyLocationListener();

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        Log.v(TAG, "onRequestPermissionResult called");
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults[0] == PackageManager.PERMISSION_GRANTED
                && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
            bindLocationListener();
        } else {
            Toast.makeText(this, "Requires Location access", Toast.LENGTH_LONG).show();
        }
    }

    private void bindLocationListener() {
        Log.v(TAG, "bindLocationListener called");
        BoundLocationManager.bindLocationListenerIn(this, mGpsListener, getApplicationContext());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.v(TAG, "onCreate called");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[] {Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_LOCATION_PERMISSION_CODE);
        } else {
            bindLocationListener();
        }
    }



    private class MyLocationListener implements LocationListener {

        private final String TAG = MyLocationListener.class.getSimpleName();

        @Override
        public void onLocationChanged(Location location) {
            Log.v(TAG, "onLocationChanged called");
            TextView textView = findViewById(R.id.location);
            textView.setText(location.getLatitude() + ", " + location.getLongitude());
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {
            Log.v(TAG, "onStatusChanged called");
        }

        @Override
        public void onProviderEnabled(String provider) {
            Log.v(TAG, "onProviderEnabled called");
            Toast.makeText(LocationActivity.this,
                    "Provider enabled: " + provider, Toast.LENGTH_SHORT).show();

        }

        @Override
        public void onProviderDisabled(String s) {
            Log.v(TAG, "onProviderDisabled called");
        }
    }
}

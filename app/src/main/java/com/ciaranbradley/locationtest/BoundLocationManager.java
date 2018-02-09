package com.ciaranbradley.locationtest;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.OnLifecycleEvent;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.util.Log;

/**
 * Created by macuser on 08/02/2018.
 */

public class BoundLocationManager {

   public static void bindLocationListenerIn(LifecycleOwner lifecycleOwner,
                                              LocationListener listener, Context context) {
        Log.v("BoundLocationManager", "bindLocationListenerIn called");
        new BoundLocationListener(lifecycleOwner, listener, context);

    }

    @SuppressWarnings("MissingPermission")
    static class BoundLocationListener implements LifecycleObserver {

        private final String TAG = BoundLocationListener.class.getSimpleName();


        private final Context mContext;
        private LocationManager mLocationManager;
        private final LocationListener mListener;

        public BoundLocationListener(LifecycleOwner lifecycleOwner,
                                     LocationListener listener, Context context) {
            mContext = context;
            mListener = listener;
            lifecycleOwner.getLifecycle().addObserver(this);
        }

        @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
        void addLocationListener() {
            // Note: Use the Fused Location Provider from Google Play Services instead.
            // https://developers.google.com/android/reference/com/google/android/gms/location/FusedLocationProviderApi
            Log.v(TAG, "addLocationListener called");
            mLocationManager =
                    (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
            mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, mListener);
            Log.d(TAG, "Listener added");

            Location lastLocation = mLocationManager.getLastKnownLocation(
                    LocationManager.GPS_PROVIDER);

            if (lastLocation != null) {
                mListener.onLocationChanged(lastLocation);
            } else {
                Log.v(TAG, "last location is null");
            }

        }

        @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
        void removeLocationListener() {
            Log.v(TAG, "removeLocationListener called");
            if(mLocationManager == null) {
                return;
            }
            mLocationManager.removeUpdates(mListener);
            mLocationManager = null;
            Log.d(TAG, "Listener removed");
        }
    }

}

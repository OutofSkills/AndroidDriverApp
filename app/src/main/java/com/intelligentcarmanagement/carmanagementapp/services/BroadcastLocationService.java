package com.intelligentcarmanagement.carmanagementapp.services;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.intelligentcarmanagement.carmanagementapp.api.users.responses.IUpdateLocation;
import com.intelligentcarmanagement.carmanagementapp.repositories.users.IUsersRepository;
import com.intelligentcarmanagement.carmanagementapp.repositories.users.UsersRepository;
import com.intelligentcarmanagement.carmanagementapp.utils.SessionManager;

public class BroadcastLocationService extends Service {

    private static final String TAG = "BroadcastService";
    private static final long BROADCAST_RATE = 20000; // 20 seconds

    private Context context = this;
    private Handler handler = null;
    public static Runnable runnable = null;

    private IUsersRepository mUsersRepository;

    private FusedLocationProviderClient fusedLocationProviderClient;
    private LocationRequest locationRequest;
    private LatLng currentLatLng = null;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        // Create fused location requests
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        locationRequest = LocationRequest.create();
        locationRequest.setSmallestDisplacement(10);
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(5000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        mUsersRepository = new UsersRepository();

        handler = new Handler();
        runnable = () -> {
            Log.d(TAG, "onCreate: Service is still running");
            updateLocation();
            handler.postDelayed(runnable, BROADCAST_RATE);
        };

        handler.postDelayed(runnable, 15000);
    }

    private void updateLocation() {
        String token = new SessionManager(context).getUserData().get(SessionManager.KEY_JWT_TOKEN);
        String id = new SessionManager(context).getUserData().get(SessionManager.KEY_ID);
        mUsersRepository.updateLocation(token, Integer.parseInt(id), String.valueOf(currentLatLng.latitude),
                String.valueOf(currentLatLng.longitude), new IUpdateLocation() {
                    @Override
                    public void onFailure(Throwable t) {
                        Log.d(TAG, "onFailure: update location " + t.getMessage());
                    }

                    @Override
                    public void onResponse() {
                        Log.d(TAG, "onResponse: success update.");
                    }
                });
    }

    @Override
    public void onDestroy() {
        /* IF YOU WANT THIS SERVICE KILLED WITH THE APP THEN UNCOMMENT THE FOLLOWING LINE */
        //handler.removeCallbacks(runnable);
        stopLocationUpdates();
        Toast.makeText(this, "Service stopped", Toast.LENGTH_LONG).show();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(this, "Service started by user.", Toast.LENGTH_LONG).show();
        startLocationUpdates();
        return startId;
    }

    LocationCallback locationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            super.onLocationResult(locationResult);
            Log.d(TAG, "onLocationResult: " + locationResult.getLastLocation());
            currentLatLng = new LatLng(locationResult.getLastLocation().getLatitude(), locationResult.getLastLocation().getLongitude());
        }
    };

    @SuppressLint("MissingPermission")
    private void startLocationUpdates() {
        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper());
    }

    private void stopLocationUpdates() {
        fusedLocationProviderClient.removeLocationUpdates(locationCallback);
    }
}
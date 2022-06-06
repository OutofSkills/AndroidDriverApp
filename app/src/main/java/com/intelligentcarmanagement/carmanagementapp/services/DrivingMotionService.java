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
import com.intelligentcarmanagement.carmanagementapp.database.DatabaseHelper;
import com.intelligentcarmanagement.carmanagementapp.models.DrivingBehaviorEvent;
import com.intelligentcarmanagement.carmanagementapp.models.utils.Motion;
import com.intelligentcarmanagement.carmanagementapp.utils.DataCollector;
import com.intelligentcarmanagement.carmanagementapp.utils.DrivingBehaviorProcessor;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class DrivingMotionService extends Service {

    private static final String TAG = "DrivingMotionService";
    private static final long PROCESS_RATE = 10000; // 10 seconds window size

    private Context context = this;
    private Handler handler = null;
    public static Runnable runnable = null;

    // Location client
    private FusedLocationProviderClient fusedLocationProviderClient;
    private LocationRequest locationRequest;
    private LatLng currentLatLng = null;
    private LatLng lastLatLng = null;

    // Motion sensors service
    private DataCollector mDataCollector;

    /*
    *   Motion data processor
    *   will take a 5D array and return a 1D array with 2 values (results)
    *   which are representing the result
     */
    private DrivingBehaviorProcessor mDrivingBehaviorProcessor;

    // Database service
    private DatabaseHelper mDbHelper;

    // Save the data in a queue so we can
    // so we can take the needed amount of instances in
    // a correct order
    private Queue<Motion> mDataQueue = new LinkedList<>();

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        // Create the motion collector instance
        mDataCollector = new DataCollector(context);
        // Create the instance for the data processor
        mDrivingBehaviorProcessor = new DrivingBehaviorProcessor(context);
        // Create the instance for the db helper
        mDbHelper = new DatabaseHelper(context);
        // Create fused location requests
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        locationRequest = LocationRequest.create();
        //locationRequest.setSmallestDisplacement(10);
        locationRequest.setInterval(1000);
        locationRequest.setFastestInterval(500);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        handler = new Handler();
        runnable = () -> {
            Log.d(TAG, "onCreate: Service is still running");
            // TODO: Handle location update
            if(currentLatLng != null)
            {
                if(currentLatLng != lastLatLng) {

                    // Process and save the motion data
                    List<Motion> collectedData = mDataCollector.getCollectedDataList();
                    mDataQueue.addAll(collectedData);

                    int temp = mDataQueue.size() / 20;
                    if(temp > 0)
                    {
                        ArrayList<Motion> tempList = new ArrayList<>();
                        for(int i = 0; i < temp * 20; i++)
                        {
                            tempList.add(mDataQueue.poll());
                        }

                        // Get the result for the collected data
                        DrivingBehaviorEvent event = mDrivingBehaviorProcessor.doInference(tempList);
                        // Save the result in database
                        mDbHelper.InsertResult(event);
                    }


                    Log.d(TAG, "onCreate: Data collected: " + collectedData.size());
                    mDataCollector.clearCollectedDataList();

                    // Update last location
                    lastLatLng = currentLatLng;
                }
                else
                {
                    // If the device is not moving, thank we don't need the collected data
                    mDataCollector.clearCollectedDataList();
                }
            }
            handler.postDelayed(runnable, PROCESS_RATE);
        };
        handler.postDelayed(runnable, PROCESS_RATE);
    }

    @Override
    public void onDestroy() {
        /* IF YOU WANT THIS SERVICE KILLED WITH THE APP THEN UNCOMMENT THE FOLLOWING LINE */
        //handler.removeCallbacks(runnable);
        stopLocationUpdates();
        mDataCollector.unregister();
        Toast.makeText(this, "Service stopped", Toast.LENGTH_LONG).show();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(this, "Service started by user.", Toast.LENGTH_LONG).show();
        startLocationUpdates();
        mDataCollector.register();

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

package com.intelligentcarmanagement.carmanagementapp.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.intelligentcarmanagement.carmanagementapp.R;
import com.intelligentcarmanagement.carmanagementapp.databinding.ActivityNavigationBinding;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class NavigationActivity extends DrawerBaseActivity implements OnMapReadyCallback {

    private static final String TAG = "NavigationActivity";
    private static final int MY_PERMISSIONS_REQUEST_LOCATION = 1;
    // Binding
    private ActivityNavigationBinding navigationBinding;
    // Maps controls
    private GoogleMap mMap;
    private FloatingActionButton locateMeFab;

    // GPS Permissions
    private boolean isPermissionGranted;

    // Google maps fused location
    private FusedLocationProviderClient locationProviderClient;
    private LocationManager mLocationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        navigationBinding = ActivityNavigationBinding.inflate(getLayoutInflater());
        setContentView(navigationBinding.getRoot());
        super.allocateActivityTitle("Navigation");

        // Bind views to controls
        locateMeFab = findViewById(R.id.find_me_button);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        // Initialize the gps client
        locationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        requestLocationPermission();

        // Set Event Listeners
        setEventListeners();
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     *
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.getUiSettings().setZoomGesturesEnabled(true);

        try{
            boolean success = googleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(NavigationActivity.this, R.raw.maps_style));
            if(!success)
                Log.e("MapsError", "Style parsing error.");
        }catch (Resources.NotFoundException e)
        {
            Log.e("MapsError", e.getMessage());
        }
    }

    private void setEventListeners()
    {
        locateMeFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getCurrentLocation();
                Log.d(TAG, "Click");
            }
        });
    }

    // Check if the GPS location permissions are granted
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // Forward results to EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @AfterPermissionGranted(MY_PERMISSIONS_REQUEST_LOCATION)
    public void requestLocationPermission() {
        String[] perms = {Manifest.permission.ACCESS_FINE_LOCATION};
        if(EasyPermissions.hasPermissions(this, perms)) {
            Toast.makeText(this, "Permission already granted", Toast.LENGTH_SHORT).show();
        }
        else {
            EasyPermissions.requestPermissions(this, "Please grant the location permission", MY_PERMISSIONS_REQUEST_LOCATION, perms);
        }
    }

    @SuppressLint("MissingPermission")
    private void getCurrentLocation()
    {
        Log.d(TAG, "Permission granted");
        // Initialize task location
        Task<Location> task = locationProviderClient.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    LatLng currentLocation = new LatLng(location.getLatitude(), location.getLongitude());
                    Log.d(TAG, "Location: " + location.getLatitude() + " " + location.getLongitude());
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 10));
                }else
                {
                    startGPS();
                }
            }
        });
    }

    public void startGPS() {

        mLocationManager = (LocationManager)this.getSystemService(LOCATION_SERVICE);
        if(!mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER))
        {
            showLocationSettingsAlert();
        }
    }

    public void showLocationSettingsAlert() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);

        // Setting Dialog Title
        alertDialog.setTitle("Location permission required.");

        // Setting Dialog Message
        alertDialog.setMessage("Please enable the location service to be able to continue.");

        // On pressing Settings button
        alertDialog.setPositiveButton(
                "Enable",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(
                                Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(intent);
                    }
                });

        alertDialog.show();
    }

}
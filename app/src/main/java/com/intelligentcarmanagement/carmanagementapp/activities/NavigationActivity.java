package com.intelligentcarmanagement.carmanagementapp.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Point;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.google.android.material.snackbar.Snackbar;
import com.google.maps.DirectionsApi;
import com.google.maps.DirectionsApiRequest;
import com.google.maps.GeoApiContext;
import com.google.maps.model.DirectionsLeg;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.DirectionsRoute;
import com.google.maps.model.DirectionsStep;
import com.google.maps.model.EncodedPolyline;
import com.intelligentcarmanagement.carmanagementapp.R;
import com.intelligentcarmanagement.carmanagementapp.utils.EndRideDialog;
import com.intelligentcarmanagement.carmanagementapp.utils.HaversineAlgorithm;
import com.intelligentcarmanagement.carmanagementapp.viewmodels.NavigationViewModel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class NavigationActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMapLongClickListener, GoogleMap.OnMarkerDragListener {

    private static final String TAG = "NavigationActivity";
    private GoogleMap mMap;
    private Geocoder geocoder;
    private final int ACCESS_LOCATION_REQUEST_CODE = 10001;

    FusedLocationProviderClient fusedLocationProviderClient;
    LocationRequest locationRequest;
    LocationManager mLocationManager;

    Marker userLocationMarker;
    Circle userLocationAccuracyCircle;

    // Maps polyline route
    private Polyline polyline;

    // Current location
    private LatLng currentLatLng = null;
    private LatLng targetLatLng = null;

    // Loader
    private CircularProgressIndicator mProgressIndicator;

    // Navigation panel data
    private TextView navigationTargetAddress, navigationTargetName, navigationTargetDistance;
    private TextView rideClientName;
    private Button startRideButton, endRideButton;

    private int rideId = 0;
    private NavigationViewModel viewModel = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);

        // Bind controls
        navigationTargetAddress = findViewById(R.id.ride_navigation_target_address);
        navigationTargetName = findViewById(R.id.ride_navigation_target_name);
        navigationTargetDistance = findViewById(R.id.ride_navigation_distance);
        rideClientName = findViewById(R.id.ride_navigation_client_name);
        startRideButton = findViewById(R.id.navigation_start_ride_button);
        endRideButton = findViewById(R.id.navigation_end_ride_button);
        mProgressIndicator = findViewById(R.id.navigation_progress_indicator);

        // Initialize the view model
        viewModel = new ViewModelProvider(this).get(NavigationViewModel.class);

        // Get intent extras
        Intent intent = getIntent();
        rideId = intent.getIntExtra("rideId", 0);

        // Fetch the given ride details
        viewModel.fetchRide(rideId);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        mapFragment.getMapAsync(this);
        geocoder = new Geocoder(this);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        // Set-up GPS
        locationRequest = LocationRequest.create();
        locationRequest.setSmallestDisplacement(10);
        locationRequest.setInterval(100);
        locationRequest.setFastestInterval(100);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        // Set event listeners
        setEventListeners();
    }

    private void setEventListeners() {
        // Observe ride object change
        viewModel.getRide().observe(NavigationActivity.this, ride -> {
            if(ride != null)
            {
                String rideState = ride.getRideState().getName();
                if(rideState.equals("ASSIGNED"))
                {
                    targetLatLng = new LatLng(Double.parseDouble(ride.getPickUpLat()), Double.parseDouble(ride.getPickUpPlaceLong()));
                    updateNavigationPanels(ride.getPickUpPlaceName(), ride.getPickUpPlaceAddress(),
                            ride.getClient().getFirstName() + " " + ride.getClient().getLastName());

                    // Enable "Start" button
                    startRideButton.setVisibility(View.VISIBLE);
                    startRideButton.setEnabled(true);
                    endRideButton.setVisibility(View.GONE);
                }
                else if(rideState.equals("STARTED"))
                {
                    targetLatLng = new LatLng(Double.parseDouble(ride.getDestinationPlaceLat()), Double.parseDouble(ride.getDestinationPlaceLong()));
                    updateNavigationPanels(ride.getDestinationPlaceName(), ride.getDestinationPlaceAddress(),
                            ride.getClient().getFirstName() + " " + ride.getClient().getLastName());

                    // Enable "End" button
                    startRideButton.setVisibility(View.GONE);
                    endRideButton.setVisibility(View.VISIBLE);
                }
                else {
                    // TODO: Handle possible error
                    endRideButton.setEnabled(false);
                    updateNavigationPanels("-", "-", "-");
                }
            }
            else
            {
                updateNavigationPanels("-", "-", "-");

                // Handle the error
                displayRetryBottomDialog();
            }
        });

        // Start ride button action
        startRideButton.setOnClickListener(view -> viewModel.startRide());

        // End ride button action
        endRideButton.setOnClickListener(view -> viewModel.endRide());

        viewModel.getStartRideState().observe(NavigationActivity.this, requestState -> {
            switch (requestState)
            {
                case ERROR:
                    mProgressIndicator.setVisibility(View.GONE);
                    break;
                case SUCCESS:
                    mProgressIndicator.setVisibility(View.GONE);
                    Toast.makeText(NavigationActivity.this, "Ride started successfully.", Toast.LENGTH_SHORT).show();
                    break;
                case START:
                    mProgressIndicator.setVisibility(View.VISIBLE);
                    mProgressIndicator.bringToFront();
                    break;
            }
        });

        viewModel.getEndRideState().observe(NavigationActivity.this, requestState -> {
            switch (requestState)
            {
                case ERROR:
                    mProgressIndicator.setVisibility(View.GONE);
                    break;
                case SUCCESS:
                    mProgressIndicator.setVisibility(View.GONE);
                    EndRideDialog dialog = new EndRideDialog();
                    dialog.showDialog(NavigationActivity.this, viewModel);
                    break;
                case START:
                    mProgressIndicator.setVisibility(View.VISIBLE);
                    mProgressIndicator.bringToFront();
                    break;
            }
        });
    }

    private void displayRetryBottomDialog() {
        final Snackbar snackBar = Snackbar.make(findViewById(android.R.id.content), "Something went wrong.", Snackbar.LENGTH_INDEFINITE);

        snackBar.setAction("Retry", v -> {
            viewModel.fetchRide(rideId);
            snackBar.dismiss();
        });
        snackBar.show();
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        mMap.setOnMapLongClickListener(this);
        mMap.setOnMarkerDragListener(this);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            enableGPS();
        } else {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                //We can show user a dialog why this permission is necessary
                ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, ACCESS_LOCATION_REQUEST_CODE);
            } else  {
                ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, ACCESS_LOCATION_REQUEST_CODE);
            }

        }
    }

    LocationCallback locationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            super.onLocationResult(locationResult);
            Log.d(TAG, "onLocationResult: " + locationResult.getLastLocation());

            if (mMap != null) {
                if(targetLatLng != null){
                    drawRoute(new LatLng(locationResult.getLastLocation().getLatitude(), locationResult.getLastLocation().getLongitude()),
                            targetLatLng);
                }
                setUserLocationMarker(locationResult.getLastLocation());
            }
        }
    };

    private void setUserLocationMarker(Location location) {

        currentLatLng = new LatLng(location.getLatitude(), location.getLongitude());

        // Update target distance on the panel
        if(targetLatLng != null) {
            double distance = HaversineAlgorithm.HaversineInKM(currentLatLng.latitude, currentLatLng.longitude, targetLatLng.latitude, targetLatLng.longitude);
            updateNavigationPanels(String.format("%.2f", distance));
        }

        if (userLocationMarker == null) {
            //Create a new marker
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(currentLatLng);
            markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.redcar));
            markerOptions.rotation(location.getBearing());
            markerOptions.anchor((float) 0.5, (float) 0.5);
            markerOptions.flat(true);
            userLocationMarker = mMap.addMarker(markerOptions);
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 17));
        } else  {
            //use the previously created marker
            userLocationMarker.setPosition(currentLatLng);
            userLocationMarker.setRotation(location.getBearing());

            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(currentLatLng)
                    .tilt(60)
                    .zoom(17)
                    .bearing(location.getBearing())
                    .build();

            mMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

            updateCameraBearing(mMap, location.getBearing());
        }

        if (userLocationAccuracyCircle == null) {
            CircleOptions circleOptions = new CircleOptions();
            circleOptions.center(currentLatLng);
            circleOptions.strokeWidth(4);
            circleOptions.strokeColor(Color.argb(255, 255, 0, 0));
            circleOptions.fillColor(Color.argb(32, 255, 0, 0));
            circleOptions.radius(location.getAccuracy());
            userLocationAccuracyCircle = mMap.addCircle(circleOptions);
        } else {
            userLocationAccuracyCircle.setCenter(currentLatLng);
            userLocationAccuracyCircle.setRadius(location.getAccuracy());
        }
    }

    // Draw a route between the 2 given places
    private void drawRoute(LatLng pickUp, LatLng destination)
    {
        //Define list to get all lat and lng for the route
        List<LatLng> path = new ArrayList();

        //Setup Directions API context
        GeoApiContext context = new GeoApiContext.Builder()
                .apiKey(getResources().getString(R.string.google_maps_key))
                .build();

        // Execute a direction request
        DirectionsApiRequest req = DirectionsApi.getDirections(context, pickUp.latitude + "," + pickUp.longitude,
                destination.latitude + "," + destination.longitude);
        try {
            DirectionsResult res = req.await();

            //Loop through legs and steps to get encoded polylines of each step
            if (res.routes != null && res.routes.length > 0) {
                DirectionsRoute route = res.routes[0];

                if (route.legs !=null) {
                    for(int i=0; i<route.legs.length; i++) {
                        DirectionsLeg leg = route.legs[i];
                        if (leg.steps != null) {
                            for (int j=0; j<leg.steps.length;j++){
                                DirectionsStep step = leg.steps[j];
                                if (step.steps != null && step.steps.length >0) {
                                    for (int k=0; k<step.steps.length;k++){
                                        DirectionsStep step1 = step.steps[k];
                                        EncodedPolyline points1 = step1.polyline;
                                        if (points1 != null) {
                                            //Decode polyline and add points to list of route coordinates
                                            List<com.google.maps.model.LatLng> coords1 = points1.decodePath();
                                            for (com.google.maps.model.LatLng coord1 : coords1) {
                                                path.add(new LatLng(coord1.lat, coord1.lng));
                                            }
                                        }
                                    }
                                } else {
                                    EncodedPolyline points = step.polyline;
                                    if (points != null) {
                                        //Decode polyline and add points to list of route coordinates
                                        List<com.google.maps.model.LatLng> coords = points.decodePath();
                                        for (com.google.maps.model.LatLng coord : coords) {
                                            path.add(new LatLng(coord.lat, coord.lng));
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } catch(Exception ex) {
            Log.e(TAG, "Fail: " + ex.getMessage());
        }

        //Draw the polyline
        if (path.size() > 0) {
            // First clear the existing route
            if(polyline != null) polyline.remove();
            // Create a new route for the provided pick-up and destination
            PolylineOptions opts = new PolylineOptions().addAll(path).color(Color.BLUE).width(7);
            polyline = mMap.addPolyline(opts);

            // Fit the points
            //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(getCenterOfPoints(path), 7));
        }
    }

    @SuppressLint("MissingPermission")
    private void startLocationUpdates() {
        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper());
    }

    private void stopLocationUpdates() {
        fusedLocationProviderClient.removeLocationUpdates(locationCallback);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            startLocationUpdates();
        } else {
            // you need to request permissions...
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        stopLocationUpdates();
    }

    @SuppressLint("MissingPermission")
    private void enableUserLocation() {
        mMap.setMyLocationEnabled(true);
    }

    @SuppressLint("MissingPermission")
    private void zoomToUserLocation() {
        Task<Location> locationTask = fusedLocationProviderClient.getLastLocation();
        locationTask.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 12));
//                mMap.addMarker(new MarkerOptions().position(latLng));
            }
        });
    }

    @Override
    public void onMapLongClick(LatLng latLng) {
        Log.d(TAG, "onMapLongClick: " + latLng.toString());
        try {
            List<Address> addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
            if (addresses.size() > 0) {
                Address address = addresses.get(0);
                String streetAddress = address.getAddressLine(0);
                mMap.addMarker(new MarkerOptions()
                        .position(latLng)
                        .title(streetAddress)
                        .draggable(true)
                );
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onMarkerDragStart(Marker marker) {
        Log.d(TAG, "onMarkerDragStart: ");
    }

    @Override
    public void onMarkerDrag(Marker marker) {
        Log.d(TAG, "onMarkerDrag: ");
    }

    @Override
    public void onMarkerDragEnd(Marker marker) {
        Log.d(TAG, "onMarkerDragEnd: ");
        LatLng latLng = marker.getPosition();
        try {
            List<Address> addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
            if (addresses.size() > 0) {
                Address address = addresses.get(0);
                String streetAddress = address.getAddressLine(0);
                marker.setTitle(streetAddress);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == ACCESS_LOCATION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                enableUserLocation();
                zoomToUserLocation();
            } else {
                //We can show a dialog that permission is not granted...
            }
        }
    }

    public void enableGPS() {

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

    private void updateCameraBearing(GoogleMap googleMap, float bearing) {
        if ( googleMap == null) return;

        LatLng mapCenter = mMap.getCameraPosition().target;
        Projection projection = mMap.getProjection();
        Point centerPoint = projection.toScreenLocation(mapCenter);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int displayHeight = displayMetrics.heightPixels;

        centerPoint.y = centerPoint.y - (int) (displayHeight / 6.0);  // move center down for approx 22%

        LatLng newCenterPoint = projection.fromScreenLocation(centerPoint);

//        CameraPosition camPos = CameraPosition
//                .builder()
//                .target(newCenterPoint)
//                .bearing(bearing)
//                .zoom(17)
//                .build();

        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(newCenterPoint, 17));
    }

    private void updateNavigationPanels(String targetName, String targetAddress, String clientName)
    {
        navigationTargetAddress.setText(targetAddress);
        navigationTargetName.setText(targetName);
        rideClientName.setText(clientName);
    }

    private void updateNavigationPanels(String targetDistance)
    {
        navigationTargetDistance.setText(targetDistance + "Km");
    }
}
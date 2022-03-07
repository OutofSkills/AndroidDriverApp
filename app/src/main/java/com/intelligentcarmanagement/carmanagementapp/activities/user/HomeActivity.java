package com.intelligentcarmanagement.carmanagementapp.activities.user;

import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.AutocompletePrediction;
import com.google.android.libraries.places.api.model.AutocompleteSessionToken;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.RectangularBounds;
import com.google.android.libraries.places.api.model.TypeFilter;
import com.google.android.libraries.places.api.net.FetchPlaceRequest;
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.maps.android.SphericalUtil;
import com.intelligentcarmanagement.carmanagementapp.R;
import com.intelligentcarmanagement.carmanagementapp.activities.DrawerBaseActivity;
import com.intelligentcarmanagement.carmanagementapp.adapters.PlacesRecyclerViewAdapter;
import com.intelligentcarmanagement.carmanagementapp.databinding.ActivityHomeBinding;
import com.intelligentcarmanagement.carmanagementapp.services.GPSTrackerService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import kotlin.jvm.Synchronized;

public class HomeActivity extends DrawerBaseActivity implements OnMapReadyCallback {

    private static final String TAG = "HomeActivity";

    private ActivityHomeBinding binding;
    FrameLayout sheet;
    BottomSheetBehavior bottomSheetBehavior;
    EditText pickUpEditText, destinationEditText;

    // GPS sensor used to retrieve current location
    GPSTrackerService gpsTracker;

    String apiKey;
    private GoogleMap mMap;
    PlacesClient placesClient;
    StringBuilder mResult;
    LatLng currentLocation;

    // Autocomplete recycler view
    RecyclerView recyclerView;
    PlacesRecyclerViewAdapter adapter;

    // Autocomplete predictions data
    ArrayList<String> mCityNames = new ArrayList<>();
    ArrayList<String> mRegionNames = new ArrayList<>();
    ArrayList<String> mDistances = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        allocateActivityTitle("Home");

        currentLocation = getCurrentLocation();

        // setup for the bottom sheet
        sheet = findViewById(R.id.bottomSheet);
        bottomSheetBehavior = BottomSheetBehavior.from(sheet);

        pickUpEditText = findViewById(R.id.pickUpTextView);
        destinationEditText = findViewById(R.id.dropTextView);

        recyclerView = findViewById(R.id.placesRecyclerView);

        pickUpEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    if(bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_COLLAPSED)
                        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                }
                else
                {
                    if(!pickUpEditText.isFocused())
                        if(!pickUpEditText.getText().toString().matches("") && !destinationEditText.getText().toString().matches(""))
                        {
                            // Both text boxes are filled
                            // so we can do ride request
                            // and close the bottom sheet
                            if(bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
                                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                                // TODO make the request here
                            }
                        }
                }
            }
        });

        pickUpEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_COLLAPSED)
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            }
        });

        destinationEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    if(bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_COLLAPSED)
                    {
                        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                    }
                }
                else
                {
                    if(!pickUpEditText.isFocused())
                        if(!pickUpEditText.getText().toString().matches("") && !destinationEditText.getText().toString().matches(""))
                        {
                            // Both text boxes are filled
                            // so we can do ride request
                            // and close the bottom sheet
                            if(bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
                                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                                // TODO make the request here
                            }
                        }
                }
            }
        });

        destinationEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_COLLAPSED)
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            }
        });

        // Autocomplete setup
        apiKey = getResources().getString(R.string.google_maps_key);

        if (!Places.isInitialized()) {
            Places.initialize(getApplicationContext(), apiKey);
        }

        // Create a new Places client instance.
        placesClient = Places.createClient(this);

        pickUpEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                predictPlaces(s.toString());
            }
        });

        destinationEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                predictPlaces(s.toString());
            }
        });


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    private void initRecyclerView()
    {
        adapter = new PlacesRecyclerViewAdapter(this, mCityNames, mRegionNames, mDistances, pickUpEditText, destinationEditText);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private LatLng getCurrentLocation()
    {
        // Request gps permissions
        try {
            if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ) {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 101);
            }
        } catch (Exception e){
            e.printStackTrace();
        }

        // Get the current latitude and longitude
        gpsTracker = new GPSTrackerService(this);
        LatLng latLng = null;
        if(gpsTracker.canGetLocation()) {
            latLng = new LatLng(gpsTracker.getLatitude(), gpsTracker.getLongitude());
            gpsTracker.stopUsingGPS();
        }else{
            gpsTracker.showSettingsAlert();
        }

        return latLng;
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

        mMap.getUiSettings().setZoomGesturesEnabled(true);
        try{
            boolean success = googleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(HomeActivity.this, R.raw.maps_style));
            if(!success)
                Log.e("MapsError", "Style parsing error.");
        }catch (Resources.NotFoundException e)
        {
            Log.e("MapsError", e.getMessage());
        }

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }

    // Fetch a place details specified in the placeFields
    @Synchronized
    private void fetchPlacesDetails(List<AutocompletePrediction> predictions)
    {
        // Specify the fields to return.
        List<Place.Field> placeFields = Arrays.asList(Place.Field.LAT_LNG);

        for (AutocompletePrediction p: predictions)
        {
            // Construct a request object, passing the place ID and fields array.
            FetchPlaceRequest request = FetchPlaceRequest.newInstance(p.getPlaceId(), placeFields);

            // Fetch places details
            placesClient.fetchPlace(request).addOnSuccessListener((response) -> {
                Place place = response.getPlace();
                Log.i(TAG, "Place found: " + place.getName() + " " + place.getLatLng());
                // Save the details
                Log.i(TAG, "Place distance: " + calculateDistance(currentLocation, place.getLatLng()));
                mDistances.add(String.format("%.2f", calculateDistance(currentLocation, place.getLatLng())) + " Km");
                mCityNames.add(p.getPrimaryText(null).toString());
                mRegionNames.add(p.getSecondaryText(null).toString());

                if(mCityNames.size() == predictions.size()) {
                    // Pass the data to the recycler view
                    initRecyclerView();
                }
            }).addOnFailureListener((exception) -> {
                if (exception instanceof ApiException) {
                    ApiException apiException = (ApiException) exception;
                    int statusCode = apiException.getStatusCode();
                    // Handle error with given status code.
                    Log.e(TAG, "Place could not be fetched: " + exception.getMessage());
                }
            });

        }

    }

    // Calculate the distance between 2 points
    private double calculateDistance(LatLng point1, LatLng point2){
        double p = 0.017453292519943295;
        double a = 0.5 - Math.cos((point2.latitude - point1.latitude) * p)/2 +
                Math.cos(point1.latitude * p) * Math.cos(point2.latitude * p) *
                        (1 - Math.cos((point2.longitude - point1.longitude) * p))/2;
        return 12742 * Math.asin(Math.sqrt(a));
    }

    // Autocomplete the recycler view by using Places API
    private void predictPlaces(String searchString)
    {
        // Reset the places containers
        mCityNames = new ArrayList<>();
        mRegionNames = new ArrayList<>();
        mDistances = new ArrayList<>();

        // Create a new token for the autocomplete session. Pass this to FindAutocompletePredictionsRequest,
        // and once again when the user makes a selection (for example when calling fetchPlace()).
        AutocompleteSessionToken token = AutocompleteSessionToken.newInstance();

        // Set the current location if it was not yet
        if(currentLocation == null) {
            currentLocation = getCurrentLocation();
        }
        Log.d(TAG, "Current Location: "+ currentLocation.toString());
        // in meter
        double radius = 300.0;
        // optional: to get distance to circle radius, not the edge
        double distance = radius * Math.sqrt(2.0);

        //LatLng center = new LatLng(47.376232, 28.380365);
        LatLng center = currentLocation;

        LatLng ne = SphericalUtil.computeOffset(center, distance, 45.0);
        LatLng sw = SphericalUtil.computeOffset(center, distance, 225.0);

        // Create a RectangularBounds object.
        RectangularBounds bounds = RectangularBounds.newInstance(sw, ne);

        // Use the builder to create a FindAutocompletePredictionsRequest.
        FindAutocompletePredictionsRequest request = FindAutocompletePredictionsRequest.builder()
                // Set the current location to get the distance
                .setOrigin(currentLocation)
                // Call either setLocationBias() OR setLocationRestriction().
                .setLocationBias(bounds)
                //.setLocationRestriction(bounds)
                .setCountry("md")//Moldova
                .setTypeFilter(TypeFilter.ADDRESS)
                .setSessionToken(token)
                .setQuery(searchString.toString())
                .build();

        placesClient.findAutocompletePredictions(request).addOnSuccessListener(response -> {
           // for (AutocompletePrediction prediction : response.getAutocompletePredictions()) {
                // Fill the recycler view containers with results
                //mCityNames.add(prediction.getPrimaryText(null).toString());
                //mRegionNames.add(prediction.getSecondaryText(null).toString());
                // String.valueOf((prediction.getDistanceMeters()/1000.0f))
                //mDistances.add("10 km");
                fetchPlacesDetails(response.getAutocompletePredictions());
                //Log.d(TAG, String.valueOf(prediction.getDistanceMeters()));
            //}
            ////
        }).addOnFailureListener((exception) -> {
            if (exception instanceof ApiException) {
                ApiException apiException = (ApiException) exception;
                Log.e(TAG, "Place not found: " + apiException.getStatusCode());
            }
        });
    }
}
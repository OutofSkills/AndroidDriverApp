package com.intelligentcarmanagement.carmanagementapp.activities.user;

import android.content.res.Resources;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Toast;

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
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.AutocompletePrediction;
import com.google.android.libraries.places.api.model.AutocompleteSessionToken;
import com.google.android.libraries.places.api.model.RectangularBounds;
import com.google.android.libraries.places.api.model.TypeFilter;
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.maps.android.SphericalUtil;
import com.intelligentcarmanagement.carmanagementapp.R;
import com.intelligentcarmanagement.carmanagementapp.activities.DrawerBaseActivity;
import com.intelligentcarmanagement.carmanagementapp.adapters.PlacesRecyclerViewAdapter;
import com.intelligentcarmanagement.carmanagementapp.databinding.ActivityHomeBinding;

import java.util.ArrayList;

public class HomeActivity extends DrawerBaseActivity implements OnMapReadyCallback {

    private static final String TAG = "HomeActivity";

    private ActivityHomeBinding binding;
    FrameLayout sheet;
    BottomSheetBehavior bottomSheetBehavior;
    EditText pickUpEditText, destinationEditText;

    String apiKey;
    private GoogleMap mMap;
    PlacesClient placesClient;
    StringBuilder mResult;

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

        // setup for the bottom sheet
        sheet = findViewById(R.id.bottomSheet);
        bottomSheetBehavior = BottomSheetBehavior.from(sheet);

        pickUpEditText = findViewById(R.id.pickUpTextView);
        destinationEditText = findViewById(R.id.dropTextView);

        pickUpEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    if(bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_COLLAPSED)
                        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
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

                        initTestAutocompleteData();
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
        apiKey = getString(R.string.google_maps_key);

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
                Log.d(TAG, s.toString());
            }
        });


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    private void initTestAutocompleteData()
    {
        mCityNames.add("Chisinau");
        mRegionNames.add("Chisinau");
        mDistances.add("10 km");

        mCityNames.add("Calarasi");
        mRegionNames.add("Calarasi");
        mDistances.add("12 km");

        mCityNames.add("Calarasi");
        mRegionNames.add("Calarasi");
        mDistances.add("12 km");

        mCityNames.add("Calarasi");
        mRegionNames.add("Calarasi");
        mDistances.add("12 km");

        mCityNames.add("Calarasi");
        mRegionNames.add("Calarasi");
        mDistances.add("12 km");

        mCityNames.add("Calarasi");
        mRegionNames.add("Calarasi");
        mDistances.add("12 km");

        mCityNames.add("Calarasi");
        mRegionNames.add("Calarasi");
        mDistances.add("12 km");

        mCityNames.add("Calarasi");
        mRegionNames.add("Calarasi");
        mDistances.add("12 km");

        mCityNames.add("Calarasi");
        mRegionNames.add("Calarasi");
        mDistances.add("12 km");

        initRecyclerView();
    }

    private void initRecyclerView()
    {
        RecyclerView recyclerView = findViewById(R.id.placesRecyclerView);
        PlacesRecyclerViewAdapter adapter = new PlacesRecyclerViewAdapter(this, mCityNames, mRegionNames, mDistances);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
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

    private void predictPlaces(String searchString)
    {
        // Create a new token for the autocomplete session. Pass this to FindAutocompletePredictionsRequest,
        // and once again when the user makes a selection (for example when calling fetchPlace()).
        AutocompleteSessionToken token = AutocompleteSessionToken.newInstance();

        // in meter
        double radius = 30.0;
        // optional: to get distance to circle radius, not the edge
        double distance = radius * Math.sqrt(2.0);

        LatLng center = new LatLng(47.376232, 28.380365);

        LatLng ne = SphericalUtil.computeOffset(center, distance, 45.0);
        LatLng sw = SphericalUtil.computeOffset(center, distance, 225.0);

        // Create a RectangularBounds object.
        RectangularBounds bounds = RectangularBounds.newInstance(sw, ne);

        // Use the builder to create a FindAutocompletePredictionsRequest.
        FindAutocompletePredictionsRequest request = FindAutocompletePredictionsRequest.builder()
                // Call either setLocationBias() OR setLocationRestriction().
                .setLocationBias(bounds)
                //.setLocationRestriction(bounds)
                .setCountry("md")//Moldova
                .setTypeFilter(TypeFilter.ADDRESS)
                .setSessionToken(token)
                .setQuery(searchString.toString())
                .build();

        placesClient.findAutocompletePredictions(request).addOnSuccessListener(response -> {
            mResult = new StringBuilder();
            for (AutocompletePrediction prediction : response.getAutocompletePredictions()) {
                mResult.append(" ").append(prediction.getFullText(null) + "\n");
                Log.i(TAG, prediction.getPlaceId());
                Log.i(TAG, prediction.getPrimaryText(null).toString());
                Toast.makeText(HomeActivity.this, prediction.getPrimaryText(null) + "-" + prediction.getSecondaryText(null), Toast.LENGTH_SHORT).show();
            }
            //mSearchResult.setText(String.valueOf(mResult));
        }).addOnFailureListener((exception) -> {
            if (exception instanceof ApiException) {
                ApiException apiException = (ApiException) exception;
                Log.e(TAG, "Place not found: " + apiException.getStatusCode());
            }
        });
    }
}
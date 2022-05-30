package com.intelligentcarmanagement.carmanagementapp.activities;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.card.MaterialCardView;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.snackbar.Snackbar;
import com.intelligentcarmanagement.carmanagementapp.R;
import com.intelligentcarmanagement.carmanagementapp.adapters.DashboardRecyclerViewAdapter;
import com.intelligentcarmanagement.carmanagementapp.databinding.ActivityDashboardBinding;
import com.intelligentcarmanagement.carmanagementapp.models.Notification;
import com.intelligentcarmanagement.carmanagementapp.models.ride.Ride;
import com.intelligentcarmanagement.carmanagementapp.utils.ImageConverter;
import com.intelligentcarmanagement.carmanagementapp.utils.RequestState;
import com.intelligentcarmanagement.carmanagementapp.viewmodels.DashboardViewModel;

import java.util.ArrayList;

public class DashboardActivity extends DrawerBaseActivity {

    private static final String TAG = "DashboardActivity";
    ActivityDashboardBinding dashboardBinding;

    private SwipeRefreshLayout swipeRefreshLayout;

    // Ongoing ride card details
    private MaterialCardView ongoingRideCard, noOngoingRideCard;
    private ShapeableImageView clientAvatar;
    private TextView clientUsername, clientRating;

    // Ongoing ride control buttons
    private Button navigateToButton;
    private Button contactPhoneNumberButton;
    private Button contactChatButton;

    // Dashboard notifications recycler view
    RecyclerView recyclerView;
    DashboardRecyclerViewAdapter adapter;

    private TextView noAvailableNotificationsTextView;

    // View Model
    private DashboardViewModel mViewModel;
    private boolean isFetchingRide, isFetchingNotifications;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dashboardBinding = ActivityDashboardBinding.inflate(getLayoutInflater());
        setContentView(dashboardBinding.getRoot());
        allocateActivityTitle("Dashboard");

        // Bind views to its controls
        swipeRefreshLayout = findViewById(R.id.dashboard_refresh_layout);
        recyclerView = findViewById(R.id.notifications_recycler_view);
        noAvailableNotificationsTextView = findViewById(R.id.dashboard_no_available_notifications_text);
        navigateToButton = findViewById(R.id.dashboard_navigate_to_button);
        contactPhoneNumberButton = findViewById(R.id.ongoing_ride_contact_phone_number);
        contactChatButton = findViewById(R.id.ongoing_ride_contact_chat);
        ongoingRideCard = findViewById(R.id.dashboard_ongoing_ride);
        noOngoingRideCard = findViewById(R.id.dashboard_no_ongoing_ride);
        clientAvatar = findViewById(R.id.ongoing_ride_client_avatar);
        clientUsername = findViewById(R.id.ongoing_ride_client_username);
        clientRating = findViewById(R.id.ongoing_ride_client_rating);

        mViewModel = new ViewModelProvider(this).get(DashboardViewModel.class);

        // Set Event Listeners
        setEventListeners();

        mViewModel.fetchOngoingRide();
        mViewModel.fetchNotifications();
    }

    private void initRecyclerView(ArrayList<Notification> notifications)
    {
        adapter = new DashboardRecyclerViewAdapter(this, notifications);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void setEventListeners()
    {
        /* Set ongoing ride */
        mViewModel.getRide().observe(DashboardActivity.this, new Observer<Ride>() {
            @Override
            public void onChanged(Ride ride) {
                if(ride != null)
                {
                    ongoingRideCard.setVisibility(View.VISIBLE);
                    noOngoingRideCard.setVisibility(View.GONE);

                    String base64 = ride.getClient().getAvatar();
                    byte[] imageBytes = ImageConverter.convertBase64ToBytes(base64);
                    Bitmap bmp = ImageConverter.convertBytesToBitmap(imageBytes);

                    clientAvatar.setImageBitmap(bmp);
                    clientUsername.setText(ride.getClient().getEmail());
                    // TODO: set rating
                    clientRating.setText("0.0");

                    setRideActionButtons(ride);
                }
                else
                {
                    ongoingRideCard.setVisibility(View.GONE);
                    noOngoingRideCard.setVisibility(View.VISIBLE);
                }
            }
        });

        mViewModel.getRideState().observe(DashboardActivity.this, new Observer<RequestState>() {
            @Override
            public void onChanged(RequestState requestState) {
                Log.d(TAG, "onChanged: " + requestState);
                switch (requestState)
                {
                    case ERROR:
                        Snackbar.make(swipeRefreshLayout, "Couldn't connect to the server.", Snackbar.LENGTH_LONG)
                                .setAction(R.string.try_again, new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        fetchDashboardData();
                                    }
                                }).show();
                    case SUCCESS:
                        isFetchingRide = false;
                        if(!isFetchingNotifications)
                        {
                            swipeRefreshLayout.setRefreshing(false);
                        }
                        break;
                    case START:
                        isFetchingRide = true;
                        break;
                }
            }
        });

        mViewModel.getNotificationsState().observe(DashboardActivity.this, new Observer<RequestState>() {
            @Override
            public void onChanged(RequestState requestState) {
                Log.d(TAG, "onChanged: " + requestState);
                switch (requestState)
                {
                    case ERROR:
                    case SUCCESS:
                        isFetchingNotifications = false;
                        if(!isFetchingRide)
                        {
                            swipeRefreshLayout.setRefreshing(false);
                        }
                        break;
                    case START:
                        isFetchingNotifications = true;
                        break;
                }
            }
        });

        /* Set user notifications */
        mViewModel.getNotifications().observe(DashboardActivity.this, new Observer<ArrayList<Notification>>() {
            @Override
            public void onChanged(ArrayList<Notification> notifications) {
                if(notifications.size() == 0)
                    noAvailableNotificationsTextView.setVisibility(View.VISIBLE);
                initRecyclerView(notifications);
            }
        });

        /* On swipe refresh the content */
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                fetchDashboardData();
            }
        });
    }

    private void fetchDashboardData() {
        mViewModel.fetchOngoingRide();
        mViewModel.fetchNotifications();
    }

    private void setRideActionButtons(Ride ride) {
        contactPhoneNumberButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + ride.getClient().getPhoneNumber()));
                startActivity(intent);
            }
        });

        contactChatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(DashboardActivity.this, "Not implemented", Toast.LENGTH_SHORT).show();
            }
        });

        navigateToButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToOngoingRide(ride);
            }
        });
    }

    private void goToOngoingRide(Ride ride)
    {
        Intent intent = new Intent(DashboardActivity.this, NavigationActivity.class);

        /*
        * Put extra ride details like
        * pick-up location, destination, client name..
        */

        int rideId = ride.getId();
        intent.putExtra("rideId", rideId);

        startActivity(intent);
    }
}
package com.intelligentcarmanagement.carmanagementapp.activities.driver;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import com.intelligentcarmanagement.carmanagementapp.R;
import com.intelligentcarmanagement.carmanagementapp.activities.DrawerBaseActivity;
import com.intelligentcarmanagement.carmanagementapp.adapters.DashboardRecyclerViewAdapter;
import com.intelligentcarmanagement.carmanagementapp.adapters.HistoryRecyclerViewAdapter;
import com.intelligentcarmanagement.carmanagementapp.databinding.ActivityDashboardBinding;
import com.intelligentcarmanagement.carmanagementapp.models.Notification;
import com.intelligentcarmanagement.carmanagementapp.models.Ride;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class DashboardActivity extends DrawerBaseActivity {

    ActivityDashboardBinding dashboardBinding;

    // Dashboard notifications recycler view
    RecyclerView recyclerView;
    DashboardRecyclerViewAdapter adapter;

    // Notifications
    private ArrayList<Notification> notifications = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dashboardBinding = ActivityDashboardBinding.inflate(getLayoutInflater());
        setContentView(dashboardBinding.getRoot());
        allocateActivityTitle("Dashboard");

        // Manage notifications
        recyclerView = findViewById(R.id.notifications_recycler_view);

        seedNotifcationsData();
    }

    private void initRecyclerView()
    {
        adapter = new DashboardRecyclerViewAdapter(this, notifications);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void seedNotifcationsData()
    {
        Bitmap btm = BitmapFactory.decodeResource(getResources(), R.drawable.profile_pic);

        notifications.add(new Notification("Ride completed", "Reached destination.",
                new GregorianCalendar(2014, Calendar.FEBRUARY, 11).getTime(), btm));

        notifications.add(new Notification("Ride started", "Reached pick-up dest...",
                new GregorianCalendar(2014, Calendar.FEBRUARY, 11).getTime(), btm));

        notifications.add(new Notification("Ride request", "John is requesting a ...",
                new GregorianCalendar(2014, Calendar.FEBRUARY, 11).getTime(), btm));

        initRecyclerView();
    }
}
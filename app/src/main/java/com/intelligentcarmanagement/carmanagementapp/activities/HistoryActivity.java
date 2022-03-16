package com.intelligentcarmanagement.carmanagementapp.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.intelligentcarmanagement.carmanagementapp.R;
import com.intelligentcarmanagement.carmanagementapp.activities.DrawerBaseActivity;
import com.intelligentcarmanagement.carmanagementapp.adapters.HistoryRecyclerViewAdapter;
import com.intelligentcarmanagement.carmanagementapp.databinding.ActivityHistoryBinding;
import com.intelligentcarmanagement.carmanagementapp.models.Ride;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class HistoryActivity extends DrawerBaseActivity {
    ActivityHistoryBinding activityHistoryBinding;

    // History recycler view
    RecyclerView recyclerView;
    HistoryRecyclerViewAdapter adapter;

    // History rides
    ArrayList<Ride> historyRides = new ArrayList<Ride>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityHistoryBinding = ActivityHistoryBinding.inflate(getLayoutInflater());
        setContentView(activityHistoryBinding.getRoot());
        allocateActivityTitle("History");

        recyclerView = findViewById(R.id.historyRecyclerView);

        seedHistoryData();
    }

    private void initRecyclerView()
    {
        adapter = new HistoryRecyclerViewAdapter(this, historyRides);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void seedHistoryData()
    {
        historyRides.add(new Ride(1, "Craiova", "Bucuresti",
                new GregorianCalendar(2014, Calendar.FEBRUARY, 11).getTime(), 24.54));

        historyRides.add(new Ride(1, "Craiova", "Bucuresti",
                new GregorianCalendar(2014, Calendar.FEBRUARY, 11).getTime(), 24.54));

        historyRides.add(new Ride(1, "Craiova", "Bucuresti",
                new GregorianCalendar(2014, Calendar.FEBRUARY, 11).getTime(), 24.54));

        initRecyclerView();
    }
}
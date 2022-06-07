package com.intelligentcarmanagement.carmanagementapp.activities;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.intelligentcarmanagement.carmanagementapp.R;
import com.intelligentcarmanagement.carmanagementapp.adapters.HistoryRecyclerViewAdapter;
import com.intelligentcarmanagement.carmanagementapp.databinding.ActivityHistoryBinding;
import com.intelligentcarmanagement.carmanagementapp.models.ride.Ride;
import com.intelligentcarmanagement.carmanagementapp.utils.RequestState;
import com.intelligentcarmanagement.carmanagementapp.viewmodels.HistoryViewModel;

import java.util.ArrayList;

public class HistoryActivity extends DrawerBaseActivity {
    private static final String TAG = "HistoryActivity";
    ActivityHistoryBinding activityHistoryBinding;

    private SwipeRefreshLayout refreshLayout;
    // History recycler view
    RecyclerView recyclerView;
    HistoryRecyclerViewAdapter adapter;

    private CircularProgressIndicator mProgressIndicator;
    private HistoryViewModel mHistoryViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityHistoryBinding = ActivityHistoryBinding.inflate(getLayoutInflater());
        setContentView(activityHistoryBinding.getRoot());
        allocateActivityTitle("History");

        refreshLayout = findViewById(R.id.history_refresh_layout);
        recyclerView = findViewById(R.id.historyRecyclerView);
        mProgressIndicator = findViewById(R.id.history_progress_indicator);

        mHistoryViewModel = new ViewModelProvider(this).get(HistoryViewModel.class);

        setEventListeners();

        mHistoryViewModel.getHistory();
    }

    private void setEventListeners() {
        mHistoryViewModel.getRides().observe(HistoryActivity.this, rides -> initRecyclerView(rides));

        mHistoryViewModel.getProcessingState().observe(HistoryActivity.this, state -> {
            switch (state){
                case ERROR:
                    break;
                case SUCCESS:
                    Log.d(TAG, "onChanged: done");
                    mProgressIndicator.setVisibility(View.GONE);
                    break;
                case START:
                    Log.d(TAG, "onChanged: start");
                    mProgressIndicator.setVisibility(View.VISIBLE);
                    mProgressIndicator.bringToFront();
                    break;
            }
        });

        refreshLayout.setOnRefreshListener(() -> {
            refreshLayout.setRefreshing(false);
            mHistoryViewModel.getHistory();
        });
    }

    private void initRecyclerView(ArrayList<Ride> rides)
    {
        adapter = new HistoryRecyclerViewAdapter(this, rides);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}
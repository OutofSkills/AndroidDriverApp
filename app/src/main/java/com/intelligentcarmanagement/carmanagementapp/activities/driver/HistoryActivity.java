package com.intelligentcarmanagement.carmanagementapp.activities.driver;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.intelligentcarmanagement.carmanagementapp.R;
import com.intelligentcarmanagement.carmanagementapp.activities.DrawerBaseActivity;
import com.intelligentcarmanagement.carmanagementapp.databinding.ActivityHistoryBinding;
import com.intelligentcarmanagement.carmanagementapp.databinding.ActivityHomeBinding;

public class HistoryActivity extends DrawerBaseActivity {
    ActivityHistoryBinding activityHistoryBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityHistoryBinding = ActivityHistoryBinding.inflate(getLayoutInflater());
        setContentView(activityHistoryBinding.getRoot());
        allocateActivityTitle("History");
    }
}
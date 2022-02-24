package com.intelligentcarmanagement.carmanagementapp.activities.driver;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.intelligentcarmanagement.carmanagementapp.activities.DrawerBaseActivity;
import com.intelligentcarmanagement.carmanagementapp.databinding.ActivityHomeBinding;

public class HomeActivity extends DrawerBaseActivity {

    ActivityHomeBinding activityHomeBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityHomeBinding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(activityHomeBinding.getRoot());
        allocateActivityTitle("Home");
    }
}
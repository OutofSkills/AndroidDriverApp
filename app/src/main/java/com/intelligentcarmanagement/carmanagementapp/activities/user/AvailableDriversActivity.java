package com.intelligentcarmanagement.carmanagementapp.activities.user;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.intelligentcarmanagement.carmanagementapp.R;
import com.intelligentcarmanagement.carmanagementapp.activities.DrawerBaseActivity;
import com.intelligentcarmanagement.carmanagementapp.databinding.ActivityAvailableDriversBinding;
import com.intelligentcarmanagement.carmanagementapp.databinding.ActivityProfileBinding;

public class AvailableDriversActivity extends DrawerBaseActivity {

    ActivityAvailableDriversBinding availableDriversBinding;

    Button availableDriversBackButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        availableDriversBinding = ActivityAvailableDriversBinding.inflate(getLayoutInflater());
        setContentView(availableDriversBinding.getRoot());
        allocateActivityTitle("Available Drivers");

        availableDriversBackButton = findViewById(R.id.availableDriversBackButton);
        availableDriversBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
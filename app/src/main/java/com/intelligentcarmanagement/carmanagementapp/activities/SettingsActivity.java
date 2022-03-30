package com.intelligentcarmanagement.carmanagementapp.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import com.intelligentcarmanagement.carmanagementapp.R;
import com.intelligentcarmanagement.carmanagementapp.databinding.ActivitySettingsBinding;

public class SettingsActivity extends DrawerBaseActivity {

    private ActivitySettingsBinding binding;

    // Edit profile settings redirect
    private RelativeLayout profileContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySettingsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        allocateActivityTitle("Settings");

        // Create preferences fragment
        if(findViewById(R.id.settings_preferences_container) != null)
        {
            if(savedInstanceState == null){
                getSupportFragmentManager()
                        .beginTransaction()
                        .add(R.id.settings_preferences_container, SettingsFragment.class, null)
                        .commit();

            }
        }

        // Bind controls to objects
        profileContainer = findViewById(R.id.settings_user_card_container);

        // Set event listeners
        setEventListeners();
    }

    private void setEventListeners()
    {
        profileContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SettingsActivity.this, ProfileSettingsActivity.class);
                startActivity(intent);
            }
        });
    }
}
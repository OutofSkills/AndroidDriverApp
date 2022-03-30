package com.intelligentcarmanagement.carmanagementapp.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.intelligentcarmanagement.carmanagementapp.R;

public class ProfileSettingsActivity extends AppCompatActivity {

    // Back button
    private ImageView editEmail, editPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_settings);

        // Bind controls to objects
        editEmail = findViewById(R.id.profile_settings_change_email);
        editPassword = findViewById(R.id.profile_settings_change_password);

        // toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.profile_settings_toolbar);
        setSupportActionBar(toolbar);

        // add back arrow to toolbar
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle("Profile Settings");
        }

        // Set event listeners
        setEventListeners();
    }

    private void setEventListeners()
    {
        // Redirect to change email
        editEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ProfileSettingsActivity.this, ChangeEmailActivity.class));
            }
        });
        // Redirect to change password
        editPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ProfileSettingsActivity.this, ChangePasswordActivity.class));
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
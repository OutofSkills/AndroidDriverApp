package com.intelligentcarmanagement.carmanagementapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.intelligentcarmanagement.carmanagementapp.activities.LoginActivity;

public class RegisterActivity extends AppCompatActivity {

    Button loginButtonRedirect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        loginButtonRedirect = findViewById(R.id.registerLoginRedirect);

        loginButtonRedirect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            }
        });
    }
}
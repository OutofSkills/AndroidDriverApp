package com.intelligentcarmanagement.carmanagementapp.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.intelligentcarmanagement.carmanagementapp.R;
import com.intelligentcarmanagement.carmanagementapp.utils.LoginState;
import com.intelligentcarmanagement.carmanagementapp.viewmodels.LoginViewModel;

public class LoginActivity extends AppCompatActivity {
    // Activity Tag
    private static final String TAG = "LoginActivity";
    // View model
    private LoginViewModel mViewModel;

    // Login and register buttons
    private Button loginRedirectRegister, loginButton;

    // Login progress
    private CircularProgressIndicator progressIndicator;

    // Login email and password labels
    private EditText emailEditText, passwordEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Get the view model
        mViewModel = ViewModelProviders.of(this).get(LoginViewModel.class);

        loginRedirectRegister = findViewById(R.id.loginRedirectRegister);
        loginButton = findViewById(R.id.loginButton);
        progressIndicator = findViewById(R.id.login_progress_indicator);
        emailEditText = findViewById(R.id.login_email);
        passwordEditText = findViewById(R.id.login_password);

        loginRedirectRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: Set a link to the Become a Driver FORM
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mViewModel.login(emailEditText.getText().toString(), passwordEditText.getText().toString());
            }
        });

        mViewModel.getLoginState().observe(this, new Observer<LoginState>() {
            @Override
            public void onChanged(LoginState state) {
                Log.d(TAG, "Login state: " + state);
                switch (state) {
                    case START:
                        progressIndicator.setVisibility(View.VISIBLE);
                        break;
                    case SUCCESS:
                        mViewModel.getLoginResult().observe(LoginActivity.this, new Observer<String>() {
                            @Override
                            public void onChanged(String s) {
                                progressIndicator.setVisibility(View.GONE);
                                Intent intent = new Intent(LoginActivity.this, DashboardActivity.class);
                                intent.putExtra("email", s);
                                startActivity(intent);
                                finish();
                            }
                        });
                        break;
                    case ERROR:
                        Log.d(TAG, "Login error state");
                        progressIndicator.setVisibility(View.GONE);
                        break;
                }
            }
        });

        mViewModel.getLoginError().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                Log.d(TAG, "Login error: " + s);
            }
        });
    }
}
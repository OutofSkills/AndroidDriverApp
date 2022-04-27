package com.intelligentcarmanagement.carmanagementapp.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.messaging.FirebaseMessaging;
import com.intelligentcarmanagement.carmanagementapp.R;
import com.intelligentcarmanagement.carmanagementapp.utils.LoginState;
import com.intelligentcarmanagement.carmanagementapp.utils.ValidationTextWatcher;
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

    // Login email and password edit texts
    private TextInputEditText emailEditText, passwordEditText;
    // Login email and password layouts
    private TextInputLayout emailEditTextLayout, passwordEditTextLayout;

    // Login text error
    private TextView loginError;

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
        emailEditTextLayout = findViewById(R.id.login_email_layout);
        passwordEditText = findViewById(R.id.login_password);
        passwordEditTextLayout = findViewById(R.id.login_password_layout);
        loginError = findViewById(R.id.login_error_message);

        // Text changed listeners for validation
        emailEditText.addTextChangedListener(new ValidationTextWatcher(emailEditText, emailEditTextLayout));
        passwordEditText.addTextChangedListener(new ValidationTextWatcher(passwordEditText, passwordEditTextLayout));


        // Testing---
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "Fetching FCM registration token failed", task.getException());
                            return;
                        }

                        // Get new FCM registration token
                        String token = task.getResult();
                        Log.d(TAG, token);
                    }
                });


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
                        loginButton.setEnabled(false);
                        progressIndicator.setVisibility(View.VISIBLE);
                        break;
                    case SUCCESS:
                        progressIndicator.setVisibility(View.GONE);
                        Intent intent = new Intent(LoginActivity.this, DashboardActivity.class);
                        startActivity(intent);
                        finish();
                        break;
                    case ERROR:
                        Log.d(TAG, "Login error state");
                        progressIndicator.setVisibility(View.GONE);
                        loginButton.setEnabled(true);
                        break;
                    default:
                    loginButton.setEnabled(true);
                    break;
                }
            }
        });

        mViewModel.getLoginError().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                Log.d(TAG, "Login error: " + s);
                loginError.setVisibility(View.VISIBLE);
                loginError.setText(s);
            }
        });
    }
}
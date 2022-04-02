package com.intelligentcarmanagement.carmanagementapp.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.intelligentcarmanagement.carmanagementapp.R;
import com.intelligentcarmanagement.carmanagementapp.viewmodels.LoginViewModel;

public class LoginActivity extends AppCompatActivity {
    // Activity Tag
    private static final String TAG = "LoginActivity";
    // View model
    private LoginViewModel mViewModel = new LoginViewModel();

    Button loginRedirectRegister;
    Button loginButton;

    // Login email and password labels
    private EditText emailEditText, passwordEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginRedirectRegister = findViewById(R.id.loginRedirectRegister);
        loginButton = findViewById(R.id.loginButton);
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


                //startActivity(new Intent(LoginActivity.this, RetrofitTestActivity.class));
            }
        });

        mViewModel.getLoginResult().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                Log.d(TAG, "Login state: " + s);
            }
        });
    }
}
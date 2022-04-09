package com.intelligentcarmanagement.carmanagementapp.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.intelligentcarmanagement.carmanagementapp.R;
import com.intelligentcarmanagement.carmanagementapp.adapters.DashboardRecyclerViewAdapter;
import com.intelligentcarmanagement.carmanagementapp.adapters.ErrorsRecyclerViewAdapter;
import com.intelligentcarmanagement.carmanagementapp.utils.ValidationTextWatcher;
import com.intelligentcarmanagement.carmanagementapp.viewmodels.ChangePasswordViewModel;

public class ChangePasswordActivity extends AppCompatActivity {

    private static final String TAG = "ChangePasswordActivity";
    ChangePasswordViewModel mViewModel;

    // Password edit texts
    private TextInputEditText currentPasswordEditText, newPasswordEditText, confirmPasswordEditText;
    // Password layouts
    private TextInputLayout currentPasswordLayout, newPasswordLayout, confirmPasswordLayout;
    // Submit floating button
    FloatingActionButton submitButton;

    // Errors recycler view
    private RecyclerView recyclerView;
    private ErrorsRecyclerViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        // toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.change_password_toolbar);
        setSupportActionBar(toolbar);

        // add back arrow to toolbar
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle("Change Password");
        }

        // Bind objects to controls
        currentPasswordEditText = findViewById(R.id.current_password);
        newPasswordEditText = findViewById(R.id.new_password);
        confirmPasswordEditText = findViewById(R.id.confirm_new_password);
        currentPasswordLayout = findViewById(R.id.current_password_label);
        newPasswordLayout = findViewById(R.id.new_password_label);
        confirmPasswordLayout = findViewById(R.id.confirm_new_password_label);
        recyclerView = findViewById(R.id.change_password_errors_recycler_view);

        submitButton = findViewById(R.id.submit_change_password);

        // Init view model
        mViewModel = ViewModelProviders.of(this).get(ChangePasswordViewModel.class);

        // Text changed listeners for validation
        currentPasswordEditText.addTextChangedListener(new ValidationTextWatcher(currentPasswordEditText, currentPasswordLayout));
        newPasswordEditText.addTextChangedListener(new ValidationTextWatcher(newPasswordEditText, newPasswordLayout));
        confirmPasswordEditText.addTextChangedListener(new ValidationTextWatcher(confirmPasswordEditText, confirmPasswordLayout));

        // Set event listeners
        setEventListeners();
    }

    private void setEventListeners()
    {
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mViewModel.changePassword(currentPasswordEditText.getText().toString(),
                        newPasswordEditText.getText().toString(),
                        confirmPasswordEditText.getText().toString()
                );
            }
        });

        mViewModel.getStateMutableLiveData().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                Log.d(TAG, "onChanged: " + aBoolean);
                if(aBoolean == false)
                {
                    mViewModel.getErrorsMutableLiveData().observe(ChangePasswordActivity.this, new Observer<String[]>() {
                        @Override
                        public void onChanged(String[] strings) {
                            adapter = new ErrorsRecyclerViewAdapter(ChangePasswordActivity.this, strings);
                            recyclerView.setAdapter(adapter);
                            recyclerView.setLayoutManager(new LinearLayoutManager(ChangePasswordActivity.this));

                            if(strings.length == 0) {
                                Toast.makeText(ChangePasswordActivity.this, "Password successfully changed.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
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
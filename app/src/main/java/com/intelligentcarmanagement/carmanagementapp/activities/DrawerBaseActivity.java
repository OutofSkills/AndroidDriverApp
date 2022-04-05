package com.intelligentcarmanagement.carmanagementapp.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.navigation.NavigationView;
import com.intelligentcarmanagement.carmanagementapp.R;
import com.intelligentcarmanagement.carmanagementapp.models.User;
import com.intelligentcarmanagement.carmanagementapp.viewmodels.DrawerViewModel;
import com.intelligentcarmanagement.carmanagementapp.viewmodels.LoginViewModel;

public class DrawerBaseActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "DrawerBaseActivity";
    private DrawerViewModel viewModel;

    private ShapeableImageView userAvatar;
    private TextView userEmail;

    DrawerLayout drawerLayout;

    @Override
    public void setContentView(View view) {
        drawerLayout = (DrawerLayout) getLayoutInflater().inflate(R.layout.activity_drawer_base, null);
        FrameLayout container = drawerLayout.findViewById(R.id.activityContainter);
        container.addView(view);
        super.setContentView(drawerLayout);

        Toolbar toolbar = drawerLayout.findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);

        NavigationView navigationView = drawerLayout.findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.menu_drawer_open, R.string.menu_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        View headerView = navigationView.getHeaderView(0);
        LinearLayout linearLayout = headerView.findViewById(R.id.header_linear_layout_container);
        CardView imageCard = linearLayout.findViewById(R.id.myCardView);

        userAvatar = headerView.findViewById(R.id.drawer_user_avatar);
        userEmail = headerView.findViewById(R.id.drawer_user_email);

        viewModel = ViewModelProviders.of(this).get(DrawerViewModel.class);

        // Get intent parameters
        // Get the authenticated user
        Intent intent = getIntent();
        String email = intent.getStringExtra("email");
        setDrawerUserData(email);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        drawerLayout.closeDrawer(GravityCompat.START);
        switch(item.getItemId())
        {
            case R.id.nav_history:
                startActivity(new Intent(this, HistoryActivity.class));
                overridePendingTransition(0, 0);
                break;
            case R.id.nav_home:
                startActivity(new Intent(this, HomeActivity.class));
                overridePendingTransition(0, 0);
                break;
            case R.id.nav_dashboard:
                startActivity(new Intent(this, DashboardActivity.class));
                overridePendingTransition(0, 0);
                break;
            case R.id.nav_profile:
                startActivity(new Intent(this, ProfileActivity.class));
                overridePendingTransition(0, 0);
                break;
            case R.id.nav_sign_out:
                viewModel.logout();
                startActivity(new Intent(this, LoginActivity.class));
                overridePendingTransition(0, 0);
                break;
            case R.id.nav_model_test:
                startActivity(new Intent(this, MotionSensorsActivity.class));
                overridePendingTransition(0, 0);
                break;
            case R.id.nav_settings:
                startActivity(new Intent(this, SettingsActivity.class));
                overridePendingTransition(0, 0);
                break;
        }
        return false;
    }

    protected void allocateActivityTitle(String titleString)
    {
        if(getSupportActionBar() != null)
        {
            getSupportActionBar().setTitle(titleString);
        }
    }

    // Get the current authenticated user's data
    // and set it on the drawer
    private void setDrawerUserData(String email) {
        viewModel.fetchUser(email);
        viewModel.getUserLiveData().observe(this, new Observer<User>() {
            @Override
            public void onChanged(User user) {
                userEmail.setText(user.getEmail());

                // If the base64 string that represents the image
                // is empty then set a default image
                if(user.getAvatar().matches(""))
                    userAvatar.setImageDrawable(getResources().getDrawable(R.drawable.no_image));
            }
        });
    }
}
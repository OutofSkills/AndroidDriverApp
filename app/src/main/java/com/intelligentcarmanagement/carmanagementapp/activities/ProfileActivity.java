package com.intelligentcarmanagement.carmanagementapp.activities;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.imageview.ShapeableImageView;
import com.intelligentcarmanagement.carmanagementapp.R;
import com.intelligentcarmanagement.carmanagementapp.databinding.ActivityHistoryBinding;
import com.intelligentcarmanagement.carmanagementapp.databinding.ActivityProfileBinding;

public class ProfileActivity extends DrawerBaseActivity {
    ActivityProfileBinding activityProfileBinding;
    // Activity launcher
    ActivityResultLauncher<Intent> activityResultLaunch = null;

    // Gallery intent request code
    private static final int RESULT_LOAD_IMAGE = 563;

    // Profile picture
    private ShapeableImageView profilePicture;

    // Image buttons
    private ImageView enableEditButton, chooseImageButton;

    // Details text views
    private TextView profileFirstName, profileLastName, profilePhoneNumber;
    // Details text views bottom bars
    private View profileFirstNameBar, profileLastNameBar, profilePhoneNumberBar;
    // Edit details edit text views
    private EditText profileFirstNameEdit, profileLastNameEdit, profilePhoneNumberEdit;
    // Save floating action button
    private FloatingActionButton saveProfileChangesButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Setting up the drawer
        activityProfileBinding = ActivityProfileBinding.inflate(getLayoutInflater());
        setContentView(activityProfileBinding.getRoot());
        allocateActivityTitle("Profile");

        // Initiate activity launcher
        if(activityResultLaunch == null)
            initActivityLauncher();

        // Bind views with objects
        enableEditButton = findViewById(R.id.profile_enable_edit);
        chooseImageButton = findViewById(R.id.profile_choose_picture);
        // Profile text views
        profileFirstName = findViewById(R.id.profileFirstName);
        profileLastName = findViewById(R.id.profileLastName);
        profilePhoneNumber = findViewById(R.id.profilePhoneNumber);
        // Profile bottom bars
        profileFirstNameBar = findViewById(R.id.profile_first_name_bar);
        profileLastNameBar = findViewById(R.id.profile_last_name_bar);
        profilePhoneNumberBar = findViewById(R.id.profile_phone_number_bar);
        // Profile edit texts
        profileFirstNameEdit = findViewById(R.id.profile_edit_first_name);
        profileLastNameEdit = findViewById(R.id.profile_edit_last_name);
        profilePhoneNumberEdit = findViewById(R.id.profile_edit_phone_number);
        // Floating button
        saveProfileChangesButton = findViewById(R.id.profile_floating_save_button);
        // Profile pic
        profilePicture = findViewById(R.id.profilePic);

        // Set event listeners
        setEventListeners();
    }

    private void setEventListeners()
    {
        // Edit button
        enableEditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                enableProfileEditViews(true);
                enableProfileTextViews(false);
            }
        });

        // Save button
        saveProfileChangesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                enableProfileEditViews(false);
                enableProfileTextViews(true);
                Toast.makeText(ProfileActivity.this, "Saved", Toast.LENGTH_SHORT).show();
            }
        });

        // Open gallery to choose a new image
        chooseImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGallery();
            }
        });
    }

    private void enableProfileEditViews(boolean show)
    {
        int viewVisibility = show ? View.VISIBLE : View.GONE;
        // Image button
        chooseImageButton.setVisibility(viewVisibility);
        // Edit texts
        profileFirstNameEdit.setVisibility(viewVisibility);
        profileLastNameEdit.setVisibility(viewVisibility);
        profilePhoneNumberEdit.setVisibility(viewVisibility);
        // Save button
        saveProfileChangesButton.setVisibility(viewVisibility);
    }

    private void enableProfileTextViews(boolean show)
    {
        int viewVisibility = show ? View.VISIBLE : View.GONE;
        // Disable edit image button
        enableEditButton.setVisibility(viewVisibility);
        // Text Views
        profileFirstName.setVisibility(viewVisibility);
        profileLastName.setVisibility(viewVisibility);
        profilePhoneNumber.setVisibility(viewVisibility);
        // Bottom views bars
        profileFirstNameBar.setVisibility(viewVisibility);
        profileLastNameBar.setVisibility(viewVisibility);
        profilePhoneNumberBar.setVisibility(viewVisibility);
    }

    // Create an intent to open gallery window
    private void openGallery() {
        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        setResult(RESULT_LOAD_IMAGE, gallery);
        activityResultLaunch.launch(gallery);
    }

    // Init the activity launcher and set the callbacks
    private void initActivityLauncher()
    {
        activityResultLaunch = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == AppCompatActivity.RESULT_OK){
                        Uri imageUri = result.getData().getData();
                        setProfileImage(imageUri);
                    }
                }
            });
    }

    // Set the image control's content
    private void setProfileImage(Uri profileImageURI)
    {
        // Load the image in the register view
        profilePicture.setImageURI(profileImageURI);
    }
}
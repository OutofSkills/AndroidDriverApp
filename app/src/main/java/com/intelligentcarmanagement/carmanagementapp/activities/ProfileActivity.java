package com.intelligentcarmanagement.carmanagementapp.activities;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.imageview.ShapeableImageView;
import com.intelligentcarmanagement.carmanagementapp.R;
import com.intelligentcarmanagement.carmanagementapp.databinding.ActivityProfileBinding;
import com.intelligentcarmanagement.carmanagementapp.models.User;
import com.intelligentcarmanagement.carmanagementapp.utils.ImageConverter;
import com.intelligentcarmanagement.carmanagementapp.viewmodels.ProfileViewModel;

public class ProfileActivity extends DrawerBaseActivity {
    private static final String TAG = "ProfileActivity";
    // Binding
    ActivityProfileBinding activityProfileBinding;
    // Activity launcher
    ActivityResultLauncher<Intent> activityResultLaunch = null;

    // View Model
    private ProfileViewModel profileViewModel;

    // Gallery intent request code
    private static final int RESULT_LOAD_IMAGE = 563;
    // Profile picture
    private ShapeableImageView profilePicture;
    // Image buttons
    private ImageView enableEditButton, chooseImageButton;
    // Details text views
    private TextView profileFirstName, profileLastName, profilePhoneNumber, profileAddressText, profileAddressLabel, profileEmail, profileRidesNumber, profileRating;
    // Details text views bottom bars
    private View profileFirstNameBar, profileLastNameBar, profilePhoneNumberBar;
    // Edit details edit text views
    private EditText profileFirstNameEdit, profileLastNameEdit, profilePhoneNumberEdit, profileAddressEdit;
    // Save floating action button
    private FloatingActionButton saveProfileChangesButton;
    // Linear layout container for address edit text
    private LinearLayout addressEditTextContainer;

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
        profileAddressLabel = findViewById(R.id.profile_edit_address_text);
        profileAddressText = findViewById(R.id.profileAddress);
        profileEmail = findViewById(R.id.profile_email);
        profileRating = findViewById(R.id.profile_rating);
        profileRidesNumber = findViewById(R.id.profile_rides_number);
        // Profile bottom bars
        profileFirstNameBar = findViewById(R.id.profile_first_name_bar);
        profileLastNameBar = findViewById(R.id.profile_last_name_bar);
        profilePhoneNumberBar = findViewById(R.id.profile_phone_number_bar);
        // Profile edit texts
        profileFirstNameEdit = findViewById(R.id.profile_edit_first_name);
        profileLastNameEdit = findViewById(R.id.profile_edit_last_name);
        profilePhoneNumberEdit = findViewById(R.id.profile_edit_phone_number);
        profileAddressEdit = findViewById(R.id.profile_edit_address);
        // Profile address container
        addressEditTextContainer = findViewById(R.id.profile_edit_address_container);
        // Floating button
        saveProfileChangesButton = findViewById(R.id.profile_floating_save_button);
        // Profile pic
        profilePicture = findViewById(R.id.profile_user_image);

        // Get the view model
        profileViewModel = ViewModelProviders.of(this).get(ProfileViewModel.class);
        profileViewModel.fetchUser();

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
                // Change the user's data here
                editUserProfile();
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

        // View model user data observer
        profileViewModel.getUserMutableLiveData().observe(this, new Observer<User>() {
            @Override
            public void onChanged(User user) {
                if(user == null) {
                    Log.d(TAG, "User was null.");
                }
                else{
                    loadProfileData(user);
                }
            }
        });

        // Updating state
        profileViewModel.getUpdatingMutableLiveData().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean state) {
                if(state == true)
                    Log.d(TAG, "State: editing");
                else {
                    Log.d(TAG, "State: done");
                    // Enable the text views and disable the edit texts
                    enableProfileEditViews(false);
                    enableProfileTextViews(true);

                    // Update the drawer avatar
                    User user = profileViewModel.getUserMutableLiveData().getValue();
                    byte[] imageBytes = ImageConverter.convertBase64ToBytes(user.getAvatar());
                    userAvatar.setImageBitmap(ImageConverter.convertBytesToBitmap(imageBytes));
                }
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
        addressEditTextContainer.setVisibility(viewVisibility);
        profileAddressEdit.setVisibility(viewVisibility);
        profileAddressLabel.setVisibility(viewVisibility);
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
        profileAddressText.setVisibility(viewVisibility);
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

    private void loadProfileData(User user)
    {
        try {
            // Text views
            if (user.getAvatar().matches("")) {
                profilePicture.setImageDrawable(getDrawable(R.drawable.no_image));
            } else {
                byte[] encodedImage = ImageConverter.convertBase64ToBytes(user.getAvatar());
                profilePicture.setImageBitmap(ImageConverter.convertBytesToBitmap(encodedImage));
            }
            profileAddressText.setText("Not specified yet");
            profileEmail.setText(user.getEmail());
            profileFirstName.setText(user.getFirstName());
            profileLastName.setText(user.getLastName());
            profilePhoneNumber.setText(user.getPhoneNumber());
            profileRating.setText(String.valueOf(user.getRating()) + "/10");
            profileRidesNumber.setText(String.valueOf(user.getDeservedClients()));

            // Edit texts
            profileAddressEdit.setText("Not specified yet");
            profileFirstNameEdit.setText(user.getFirstName());
            profileLastNameEdit.setText(user.getLastName());
            profilePhoneNumberEdit.setText(user.getPhoneNumber());
        }
        catch (Exception e)
        {
            Log.d(TAG, "loadProfileData: " + e.getMessage());
        }
    }

    private void editUserProfile()
    {
        // User to update
        User user = profileViewModel.getUserMutableLiveData().getValue();

        BitmapDrawable drawable = (BitmapDrawable) profilePicture.getDrawable();

        user.setAvatar(ImageConverter.convertBytesToBase64(ImageConverter.convertBitmapToBytes(drawable.getBitmap())));
        // TODO: Add address to the model
        //profileAddressTextEdit.getText();
        user.setFirstName(profileFirstNameEdit.getText().toString());
        user.setLastName(profileLastNameEdit.getText().toString());
        user.setPhoneNumber(profilePhoneNumberEdit.getText().toString());

        // Update the view model
        profileViewModel.updateUserMutableLiveData(user);
    }
}
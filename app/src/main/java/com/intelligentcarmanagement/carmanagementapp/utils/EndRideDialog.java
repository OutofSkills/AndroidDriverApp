package com.intelligentcarmanagement.carmanagementapp.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.Window;
import android.view.WindowId;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.lifecycle.ViewModel;

import com.google.android.material.imageview.ShapeableImageView;
import com.intelligentcarmanagement.carmanagementapp.R;
import com.intelligentcarmanagement.carmanagementapp.activities.HomeActivity;
import com.intelligentcarmanagement.carmanagementapp.models.ride.Ride;
import com.intelligentcarmanagement.carmanagementapp.viewmodels.NavigationViewModel;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class EndRideDialog {

    public void showDialog(Activity activity, NavigationViewModel viewModel){
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.layout_ride_completed);

        // Ride date
        TextView dateTextView = (TextView) dialog.findViewById(R.id.completed_ride_date);

        String pattern = "dd/MM/yyyy HH:mm:ss";
        DateFormat df = new SimpleDateFormat(pattern);
        String stringDate = df.format(viewModel.getRide().getValue().getPickUpTime());

        dateTextView.setText(stringDate);

        TextView incomeTextView = (TextView) dialog.findViewById(R.id.completed_ride_total_income);
        incomeTextView.setText(new StringBuilder().append('$').append(String.format("%.2f", +viewModel.getRide().getValue().getPrice())).toString());

        // Pick-up place
        TextView pickUpTextView = (TextView) dialog.findViewById(R.id.completed_ride_pick_up);
        pickUpTextView.setText(viewModel.getRide().getValue().getPickUpPlaceName());

        // Destination place
        TextView destinationTextView = (TextView) dialog.findViewById(R.id.completed_ride_destination);
        destinationTextView.setText(viewModel.getRide().getValue().getDestinationPlaceName());

        TextView distanceTextView = (TextView) dialog.findViewById(R.id.completed_ride_distance);

        // Process the distance
        double distance = HaversineAlgorithm.HaversineInKM(
                Double.valueOf(viewModel.getRide().getValue().getPickUpPlaceLat()),
                Double.valueOf(viewModel.getRide().getValue().getPickUpPlaceLong()),
                Double.valueOf(viewModel.getRide().getValue().getDestinationPlaceLat()),
                Double.valueOf(viewModel.getRide().getValue().getDestinationPlaceLong())
        );

        distanceTextView.setText(String.format("%.2fKm", distance));

        // Client avatar
        ShapeableImageView clientAvatar = dialog.findViewById(R.id.completed_ride_client_avatar);
        String base64String = viewModel.getRide().getValue().getClient().getAvatar();
        byte[] imageBytes = ImageConverter.convertBase64ToBytes(base64String);
        Bitmap bmp = ImageConverter.convertBytesToBitmap(imageBytes);
        clientAvatar.setImageBitmap(bmp);

        TextView clientRateName = dialog.findViewById(R.id.completed_ride_client_rate_name);
        clientRateName.setText("Would you like to rate " + viewModel.getRide().getValue().getClient().getLastName() + " ?");

        // Ride accuracy
        TextView rideAccuracyTextView = dialog.findViewById(R.id.completed_ride_accuracy);
        float accuracy = viewModel.getRideAccuracy();
        rideAccuracyTextView.setText(new StringBuilder().append(String.format("%.2f", accuracy)).append('%').toString());

        // Rating bar
        RatingBar ratingBar = dialog.findViewById(R.id.completed_ride_rating);

        Button dialogSubmitButton = (Button) dialog.findViewById(R.id.completed_ride_end_button);

        dialogSubmitButton.setOnClickListener(v -> {
            viewModel.rateRide(ratingBar.getRating());
            dialog.dismiss();
            activity.startActivity(new Intent(activity, HomeActivity.class));
            activity.finish();
        });

        Button dialogSkipButton = (Button) dialog.findViewById(R.id.completed_ride_skip_button);

        dialogSkipButton.setOnClickListener(v -> {
            dialog.dismiss();
            activity.startActivity(new Intent(activity, HomeActivity.class));
            activity.finish();
        });

        dialog.show();
    }
}

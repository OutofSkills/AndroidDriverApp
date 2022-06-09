package com.intelligentcarmanagement.carmanagementapp.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.imageview.ShapeableImageView;
import com.intelligentcarmanagement.carmanagementapp.R;
import com.intelligentcarmanagement.carmanagementapp.models.ride.Ride;
import com.intelligentcarmanagement.carmanagementapp.utils.ImageConverter;

import org.ocpsoft.prettytime.PrettyTime;

import java.util.ArrayList;
import java.util.Locale;

public class HistoryRecyclerViewAdapter extends RecyclerView.Adapter<HistoryRecyclerViewAdapter.ViewHolder>{
    private static final String TAG = "HistoryRecyclerViewAdapter";
    Context mContext;
    ArrayList<Ride> mRides;

    public HistoryRecyclerViewAdapter(Context context, ArrayList<Ride> rides) {
        this.mRides = rides;
        this.mContext = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_history_list_item, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        PrettyTime p = new PrettyTime();
        p.setLocale(Locale.ENGLISH);
        holder.rideDate.setText(p.format(mRides.get(position).getPickUpTime()));

        holder.ridePickUpAddress.setText(mRides.get(position).getPickUpPlaceName());
        holder.rideDestinationAddress.setText(mRides.get(position).getDestinationPlaceName());
        holder.rideDistance.setText(String.format("%.2f", mRides.get(position).getDistance()) + "km");
        holder.rideTime.setText(new StringBuilder().append((int) mRides.get(position).getAverageTime()).append(" min").toString());

        String price = String.format("%.2f", mRides.get(position).getPrice());
        holder.rideTotalMoney.setText(new StringBuilder().append("$").append(price).toString());
        holder.clientUsername.setText(mRides.get(position).getClient().getEmail());

        String string64 = mRides.get(position).getClient().getAvatar();
        byte[] imageBytes = ImageConverter.convertBase64ToBytes(string64);
        Bitmap bmp = ImageConverter.convertBytesToBitmap(imageBytes);
        holder.clientAvatar.setImageBitmap(bmp);

        String rating = String.format("%.1f", mRides.get(position).getClient().getRating());
        holder.clientRating.setText(rating);

        String accuracy = mRides.get(position).getReview() == null ? "0.00" : String.format("%.2f", mRides.get(position).getReview().getDrivingAccuracy());
        holder.rideAccuracy.setText(new StringBuilder().append(accuracy).append("%").toString());
    }

    @Override
    public int getItemCount() {
        return mRides.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView rideDate, ridePickUpAddress, rideDestinationAddress, rideDistance, rideTime, rideTotalMoney, rideAccuracy;
        TextView clientUsername, clientRating;
        ShapeableImageView clientAvatar;
        RelativeLayout parentLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            rideAccuracy = itemView.findViewById(R.id.history_ride_accuracy);
            rideDate = itemView.findViewById(R.id.history_ride_date);
            ridePickUpAddress = itemView.findViewById(R.id.history_ride_from);
            rideDestinationAddress = itemView.findViewById(R.id.history_ride_to);
            rideDistance = itemView.findViewById(R.id.history_ride_total_distance);
            rideTime = itemView.findViewById(R.id.history_ride_total_time);
            rideTotalMoney = itemView.findViewById(R.id.history_ride_total_money);
            clientAvatar = itemView.findViewById(R.id.history_ride_client_avatar);
            clientUsername = itemView.findViewById(R.id.history_ride_client_username);
            clientRating = itemView.findViewById(R.id.history_ride_client_rating);
            parentLayout = itemView.findViewById(R.id.historyItemParentLayout);
        }
    }
}

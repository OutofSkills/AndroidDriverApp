package com.intelligentcarmanagement.carmanagementapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.intelligentcarmanagement.carmanagementapp.R;
import com.intelligentcarmanagement.carmanagementapp.models.Ride;

import java.util.ArrayList;

public class HistoryRecyclerViewAdapter extends RecyclerView.Adapter<HistoryRecyclerViewAdapter.ViewHolder>{
    Context mContext;
    ArrayList<Ride> mRides = new ArrayList<>();

    public HistoryRecyclerViewAdapter(Context mContext, ArrayList<Ride> mRides) {
        this.mRides = mRides;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public HistoryRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_history_list_item, parent, false);
        HistoryRecyclerViewAdapter.ViewHolder holder = new HistoryRecyclerViewAdapter.ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.historyRideId.setText("#" + String.valueOf(mRides.get(position).getId()));
        holder.historyRideDate.setText(String.format("%1$tY-%1$tm-%1$td", mRides.get(position).getRideDate()));
        holder.historyRidePickUpCity.setText(mRides.get(position).getPickUpAddress());
        holder.historyRideDestinationCity.setText(mRides.get(position).getDestinationAddress());
        holder.historyRideDistance.setText(String.format("%.2f", mRides.get(position).getDistance()) + "km");
    }

    @Override
    public int getItemCount() {
        return mRides.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView historyRideId, historyRideDate, historyRidePickUpCity, historyRideDestinationCity, historyRideDistance;
        RelativeLayout parentLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            historyRideId = itemView.findViewById(R.id.historyRideId);
            historyRideDate = itemView.findViewById(R.id.historyRideDate);
            historyRidePickUpCity = itemView.findViewById(R.id.historyRidePickUp);
            historyRideDestinationCity = itemView.findViewById(R.id.historyRideDestination);
            historyRideDistance = itemView.findViewById(R.id.historyRideDistance);
            parentLayout = itemView.findViewById(R.id.historyItemParentLayout);
        }
    }
}

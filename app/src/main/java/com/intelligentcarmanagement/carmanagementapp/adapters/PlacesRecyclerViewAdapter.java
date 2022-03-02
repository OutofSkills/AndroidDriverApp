package com.intelligentcarmanagement.carmanagementapp.adapters;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.intelligentcarmanagement.carmanagementapp.R;

import java.util.ArrayList;
import java.util.zip.Inflater;

public class PlacesRecyclerViewAdapter extends RecyclerView.Adapter<PlacesRecyclerViewAdapter.ViewHolder> {

    ArrayList<String> mCityNames = new ArrayList<>();
    ArrayList<String> mRegionNames = new ArrayList<>();
    ArrayList<String> mDistances = new ArrayList<>();
    Context mContext;

    public PlacesRecyclerViewAdapter(Context mContext, ArrayList<String> mCityNames, ArrayList<String> mRegionNames, ArrayList<String> mDistances) {
        this.mCityNames = mCityNames;
        this.mRegionNames = mRegionNames;
        this.mDistances = mDistances;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_places_list_item, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.placeCityName.setText(mCityNames.get(position));
        holder.placeRegionName.setText(mRegionNames.get(position));
        holder.placeDistance.setText(mDistances.get(position));

        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(mContext, mCityNames.get(holder.getAbsoluteAdapterPosition()), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mCityNames.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView placeCityName, placeRegionName, placeDistance;
        RelativeLayout parentLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            placeCityName = itemView.findViewById(R.id.placeCityName);
            placeRegionName = itemView.findViewById(R.id.placeRegionName);
            placeDistance = itemView.findViewById(R.id.placeDistance);
            parentLayout = itemView.findViewById(R.id.place_item_parent_layout);
        }
    }
}

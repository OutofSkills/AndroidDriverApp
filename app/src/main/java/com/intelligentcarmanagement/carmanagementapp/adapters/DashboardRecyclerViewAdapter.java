package com.intelligentcarmanagement.carmanagementapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import org.ocpsoft.prettytime.PrettyTime;

import com.google.android.material.imageview.ShapeableImageView;
import com.intelligentcarmanagement.carmanagementapp.R;
import com.intelligentcarmanagement.carmanagementapp.models.Notification;
import com.intelligentcarmanagement.carmanagementapp.models.Ride;

import java.util.ArrayList;
import java.util.Locale;

public class DashboardRecyclerViewAdapter extends RecyclerView.Adapter<DashboardRecyclerViewAdapter.ViewHolder>{
    Context mContext;
    ArrayList<Notification> mNotifications = new ArrayList<>();

    public DashboardRecyclerViewAdapter(Context mContext, ArrayList<Notification> mNotifications) {
        this.mNotifications = mNotifications;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public DashboardRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_dashboard_notification_item, parent, false);
        DashboardRecyclerViewAdapter.ViewHolder holder = new DashboardRecyclerViewAdapter.ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull DashboardRecyclerViewAdapter.ViewHolder holder, int position) {
        holder.notificationIcon.setImageBitmap(mNotifications.get(position).getIcon());
        holder.notificationName.setText(mNotifications.get(position).getName());
        holder.notificationDescription.setText(mNotifications.get(position).getDescription());

        PrettyTime p = new PrettyTime();
        p.setLocale(Locale.ENGLISH);
        holder.notificationTimeAgo.setText(p.format(mNotifications.get(position).getDate()));
    }

    @Override
    public int getItemCount() {
        return mNotifications.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ShapeableImageView notificationIcon;
        TextView notificationName, notificationDescription, notificationTimeAgo;
        RelativeLayout parentLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            notificationIcon = itemView.findViewById(R.id.notification_item_image);
            notificationName = itemView.findViewById(R.id.notification_item_name);
            notificationDescription = itemView.findViewById(R.id.notification_item_description);
            notificationTimeAgo = itemView.findViewById(R.id.notification_item_time_ago);
            parentLayout = itemView.findViewById(R.id.historyItemParentLayout);
        }
    }
}

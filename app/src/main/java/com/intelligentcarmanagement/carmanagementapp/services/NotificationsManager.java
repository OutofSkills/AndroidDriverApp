package com.intelligentcarmanagement.carmanagementapp.services;

import static androidx.core.content.ContextCompat.getSystemService;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.intelligentcarmanagement.carmanagementapp.R;

public class NotificationsManager {

    private static final String CHANNEL_ID  = "Car_Management";
    private static final String CHANNEL_NAME  = "Driver Notification";
    private static final String CHANNEL_DESCRIPTION  = "Driver notifications channel.";

    private Context mContext;

    public NotificationsManager(Context context)
    {
        mContext = context;
        // Create notifications channel if needed
        createNotificationChannel();
    }

    public void displayNotification(String title, String text)
    {
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(mContext, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_outline_directions_car_24)
                .setContentTitle(title)
                .setContentText(text)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat mNotificationMngr = NotificationManagerCompat.from(mContext);
        mNotificationMngr.notify(1, mBuilder.build());
    }

    private void createNotificationChannel()
    {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
            mChannel.setDescription(CHANNEL_DESCRIPTION);
            NotificationManager manager = getSystemService(mContext, NotificationManager.class);
            manager.createNotificationChannel(mChannel);
        }
    }
}

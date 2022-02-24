package com.intelligentcarmanagement.carmanagementapp.services;

import android.content.Context;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.intelligentcarmanagement.carmanagementapp.database.DatabaseHelper;
import com.intelligentcarmanagement.carmanagementapp.models.DrivingEvent;
import com.intelligentcarmanagement.carmanagementapp.models.Motion;

import java.text.ParseException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class DrivingEventsService {
    private DatabaseHelper dbHelper;
    private MotionProcessorService motionProcessorService;

    public DrivingEventsService(){};

    public DrivingEventsService(Context context)
    {
        dbHelper = new DatabaseHelper(context);
        motionProcessorService = new MotionProcessorService(context);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void processEvents()
    {
        List<Motion> data = new ArrayList<>();
        try {
            data = dbHelper.GetMotionData(1);
        }catch (ParseException e){
            e.printStackTrace();
        }

        if(data != null)
        {
            float avgAccX = 0, avgAccY = 0, avgAccZ = 0;
            float avgGyroX = 0, avgGyroY = 0, avgGyroZ = 0;
            Date startTime, endTime;

            //initial timestamp will be the first instance time
            startTime = data.get(0).getTimestamp();
            for(int i = 0; i < data.size(); i++)
            {
                Motion motion = data.get(i);
                avgAccX += motion.getAccelerometerX();
                avgAccY += motion.getAccelerometerY();
                avgAccZ += motion.getAccelerometerZ();
                avgGyroX += motion.getGyroX();
                avgGyroY += motion.getGyroY();
                avgGyroZ += motion.getGyroZ();
                endTime = motion.getTimestamp();

                // if the diffrence between firsta data instance and currenct data instance is 15s
                // make the average and do the processing
                // k is used to store the number of processed instances
                int k = 0;
                if(getDateDiff(startTime, endTime, TimeUnit.SECONDS) >= 15)
                {
                    //processing data
                    DrivingEvent event  = motionProcessorService.doInference(new float[]{avgAccX/(i-k), avgAccY/(i-k), avgAccZ/(i-k),
                            avgGyroX/(i-k), avgGyroY/(i-k), avgGyroZ/(i-k)});

                    Log.d("Processed data", "Avg data: "+ avgAccX/(i-k) + " " + avgAccY/(i-k) + " " +
                            avgAccZ/(i-k) + " " + avgGyroX/(i-k) + " " + avgGyroY/(i-k) + " " + avgGyroZ/(i-k));
                    Log.d("Processed data", "Events: " + event.getSuddenAcceleration() + " "
                    + event.getSuddenLTurn() + " " + event.getSuddenRTurn() + " " + event.getSuddenBreak());


                    k = i;
                    startTime = endTime;
                }
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static long getDateDiff(Date date1, Date date2, TimeUnit timeUnit) {
        Duration d = Duration.between(date1.toInstant(), date2.toInstant());

        //long diffInMillies = date2.getTime() - date1.getTime();
        return d.toMillis()/1000;
    }
}

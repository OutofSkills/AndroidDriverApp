package com.intelligentcarmanagement.carmanagementapp.services;

import android.content.Context;
import android.util.Log;

import com.intelligentcarmanagement.carmanagementapp.database.DatabaseHelper;
import com.intelligentcarmanagement.carmanagementapp.models.Motion;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class MotionCollectorService {
    private Context context;
    private DatabaseHelper dbHelper;

    public MotionCollectorService(){};

    public MotionCollectorService(Context context)
    {
        this.context = context;
        dbHelper = new DatabaseHelper(context);
    }

    public boolean addMotionRecord(Motion motion)
    {
        boolean result = dbHelper.InsertMotion(motion);

        return result;
    }

    public void displayData()
    {
        List<Motion> motionList = new ArrayList<>();
        try {
            motionList = dbHelper.GetMotionData(1);
        } catch (ParseException e) {
            Log.d("DATA TEST: ", e.getMessage());
        }
        for(int i = 0; i < motionList.size(); i++)
            Log.d("MotionViewModel", "Dataset: " +
                    " acc X: " + motionList.get(i).getAccelerometerX() +
                    " acc Y: " + motionList.get(i).getAccelerometerY() +
                    " acc Z: " + motionList.get(i).getAccelerometerZ() +
                    " gyro X: " + motionList.get(i).getGyroX() +
                    " gyro Y: " + motionList.get(i).getGyroY() +
                    " gyro Z: " + motionList.get(i).getGyroZ() +
                    " timestamp: " + motionList.get(i).getTimestamp()
            );
    }
}

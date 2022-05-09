package com.intelligentcarmanagement.carmanagementapp.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.intelligentcarmanagement.carmanagementapp.models.utils.Motion;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    // database details
    private static final String DB_NAME = "carmng.db";
    private static final int DB_VERSION = 1;

    // Motion table details
    private static final String MOTION_TABLE = "Motion";
    private static final String COLUMN_MOTION_ID = "Id";
    private static final String COLUMN_ACC_X = "AccX";
    private static final String COLUMN_ACC_Y = "AccY";
    private static final String COLUMN_ACC_Z = "AccZ";
    private static final String COLUMN_GYRO_X = "GyroX";
    private static final String COLUMN_GYRO_Y = "GyroY";
    private static final String COLUMN_GYRO_Z = "GyroZ";
    private static final String COLUMN_CLASS = "Class";
    private static final String COLUMN_TIME_STAMP = "TimeStamp";

    public DatabaseHelper(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    // this is called the first time the database is accessed.
    @Override
    public void onCreate(SQLiteDatabase db) {
        String createMotionTable = "CREATE TABLE " + MOTION_TABLE + "("+ COLUMN_MOTION_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_ACC_X + " REAL, " + COLUMN_ACC_Y + " REAL, " + COLUMN_ACC_Z + " REAL, " +
                COLUMN_GYRO_X + " REAL, " + COLUMN_GYRO_Y + " REAL, " + COLUMN_GYRO_Z + " REAL, " +
                COLUMN_TIME_STAMP + " INTEGER, " + COLUMN_CLASS + " TEXT )";

        db.execSQL(createMotionTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {

    }

    public boolean InsertMotion(Motion motion)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues cv = new ContentValues();

        cv.put(COLUMN_ACC_X, motion.getAccelerometerX());
        cv.put(COLUMN_ACC_Y, motion.getAccelerometerY());
        cv.put(COLUMN_ACC_Z, motion.getAccelerometerZ());

        cv.put(COLUMN_GYRO_X, motion.getGyroX());
        cv.put(COLUMN_GYRO_Y, motion.getGyroY());
        cv.put(COLUMN_GYRO_Z, motion.getGyroZ());

        cv.put(COLUMN_CLASS, motion.getDrivingClass());

        long result = db.insert(MOTION_TABLE, null, cv);

        db.close();

        if(result == -1)
            return false;

        return true;
    }

    public List<Motion> GetMotionData(int rideId) throws ParseException {
        List<Motion> returnList = new ArrayList<>();

        String queryString = "SELECT* FROM " + MOTION_TABLE;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(queryString, null);

        if(cursor.moveToFirst())
        {
            // loop through the cursor (result set) and create a list of motion objects
            do {
                float acc_X = cursor.getFloat(1);
                float acc_Y = cursor.getFloat(2);
                float acc_Z = cursor.getFloat(3);
                float gyro_X = cursor.getFloat(4);
                float gyro_Y = cursor.getFloat(5);
                float gyro_Z = cursor.getFloat(6);
                long timestamp = cursor.getLong(7);
                String drivingClass = cursor.getString(8);

                Motion motion = new Motion(acc_X, acc_Y, acc_Z, gyro_X, gyro_Y, gyro_Z, drivingClass, timestamp);
                returnList.add(motion);
            }while (cursor.moveToNext());
        }
        else
        {
            // failure, we have nothing in the list
        }

        cursor.close();
        db.close();

        return returnList;
    }

    public void ClearMotion()
    {
        //Open the database
        SQLiteDatabase database = this.getWritableDatabase();

        //Execute sql query to remove from database
        //NOTE: When removing by String in SQL, value must be enclosed with ''
        database.execSQL("DELETE FROM " + MOTION_TABLE);

        //Close the database
        database.close();
    }
}

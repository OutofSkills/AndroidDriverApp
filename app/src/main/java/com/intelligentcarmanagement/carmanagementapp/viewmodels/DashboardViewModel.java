package com.intelligentcarmanagement.carmanagementapp.viewmodels;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.intelligentcarmanagement.carmanagementapp.api.notifications.responses.IGetNotifications;
import com.intelligentcarmanagement.carmanagementapp.api.rides.responses.IGetRide;
import com.intelligentcarmanagement.carmanagementapp.api.rides.responses.IGetRidesHistory;
import com.intelligentcarmanagement.carmanagementapp.models.notifications.Notification;
import com.intelligentcarmanagement.carmanagementapp.models.ride.Ride;
import com.intelligentcarmanagement.carmanagementapp.repositories.notifications.INotificationsRepository;
import com.intelligentcarmanagement.carmanagementapp.repositories.notifications.NotificationsRepository;
import com.intelligentcarmanagement.carmanagementapp.repositories.rides.IRidesRepository;
import com.intelligentcarmanagement.carmanagementapp.repositories.rides.RidesRepository;
import com.intelligentcarmanagement.carmanagementapp.services.SessionManager;
import com.intelligentcarmanagement.carmanagementapp.utils.RequestState;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class DashboardViewModel extends AndroidViewModel {
    private static final String TAG = "DashboardViewModel";
    private MutableLiveData<Ride> mOngoingRideLiveData = new MutableLiveData<Ride>();
    private MutableLiveData<ArrayList<Notification>> mNotificationsLiveData = new MutableLiveData<ArrayList<Notification>>();
    private MutableLiveData<RequestState> mRideRequestStateLiveData = new MutableLiveData<>();
    private MutableLiveData<RequestState> mNotificationsStateLiveData = new MutableLiveData<>();

    private MutableLiveData<Double> mEarningsMutableLiveData = new MutableLiveData<Double>();
    private MutableLiveData<Double> mDistanceMutableLiveData = new MutableLiveData<Double>();
    private MutableLiveData<Double> mAccuracyMutableLiveData = new MutableLiveData<Double>();
    private MutableLiveData<Integer> mRidesNumberMutableLiveData = new MutableLiveData<Integer>();
    private MutableLiveData<RequestState> mEarningsStateLiveData = new MutableLiveData<>();

    private SessionManager mSessionManager;
    private IRidesRepository mRidesRepository;
    private INotificationsRepository mNotificationsRepository;

    public DashboardViewModel(@NonNull Application application) {
        super(application);

        mSessionManager = new SessionManager(application);
        mRidesRepository = new RidesRepository();
        mNotificationsRepository = new NotificationsRepository();
    }

    public void fetchOngoingRide()
    {
        mRideRequestStateLiveData.setValue(RequestState.START);

        String jwtToken = mSessionManager.getUserData().get(SessionManager.KEY_JWT_TOKEN);
        String userId = mSessionManager.getUserData().get(SessionManager.KEY_ID);

        mRidesRepository.getOngoingRide(jwtToken, Integer.parseInt(userId), new IGetRide() {
            @Override
            public void onResponse(Ride ride) {
                mOngoingRideLiveData.postValue(ride);
                mRideRequestStateLiveData.setValue(RequestState.SUCCESS);
            }

            @Override
            public void onFailure(Throwable t) {
                mRideRequestStateLiveData.setValue(RequestState.ERROR);
                Log.d(TAG, "onFailure: " + t.getMessage());
            }
        });
    }

    public void fetchNotifications()
    {
        mNotificationsStateLiveData.setValue(RequestState.START);

        String jwtToken = mSessionManager.getUserData().get(SessionManager.KEY_JWT_TOKEN);
        String userId = mSessionManager.getUserData().get(SessionManager.KEY_ID);

        mNotificationsRepository.getNotifications(jwtToken, userId, new IGetNotifications() {
            @Override
            public void onResponse(ArrayList<Notification> notifications) {
                mNotificationsLiveData.postValue(notifications);
                mNotificationsStateLiveData.setValue(RequestState.SUCCESS);
            }

            @Override
            public void onFailure(Throwable throwable) {
                Log.d(TAG, "onFailure: " + throwable.getMessage());
                mNotificationsStateLiveData.setValue(RequestState.ERROR);
            }
        });
    }


    public void fetchHistory() {
        mEarningsStateLiveData.setValue(RequestState.START);

        // Get client's id
        String id = mSessionManager.getUserData().get(SessionManager.KEY_ID);
        String jwtToken = mSessionManager.getUserData().get(SessionManager.KEY_JWT_TOKEN);

        // Fetch the rides
        mRidesRepository.getRides(jwtToken, Integer.parseInt(id), new IGetRidesHistory() {
            @Override
            public void onResponse(ArrayList<Ride> historyRides) {
                double totalEarnings = 0.0;
                int ridesNumber = 0;
                double distance = 0.0;
                double accuracy = 0.0;

                Date yesterday = getYesterdayDate();
                for (Ride ride: historyRides) {
                    if(ride.getPickUpTime().after(yesterday))
                    {
                        totalEarnings += ride.getPrice();
                        distance += ride.getDistance();
                        ridesNumber++;

                        if(ride.getReview() != null)
                        {
                            Log.d(TAG, "onResponse: Accuracy " + ride.getReview().getDrivingAccuracy());
                            accuracy += ride.getReview().getDrivingAccuracy();
                        }
                    }
                }

                mEarningsMutableLiveData.setValue(totalEarnings);
                mRidesNumberMutableLiveData.setValue(ridesNumber);
                mDistanceMutableLiveData.setValue(distance);

                double averageAccuracy = ridesNumber == 0 ? 0 : accuracy/ridesNumber;
                mAccuracyMutableLiveData.setValue(averageAccuracy);
                mEarningsStateLiveData.setValue(RequestState.SUCCESS);
            }

            @Override
            public void onFailure(Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage() );
                t.printStackTrace();
                mEarningsStateLiveData.setValue(RequestState.ERROR);
            }
        });
    }

    private Date getYesterdayDate() {
        Date dt = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(dt);
        c.add(Calendar.DATE, -1);

        return c.getTime();
    }


    public LiveData<Ride> getRide()
    {
        return mOngoingRideLiveData;
    }

    public LiveData<ArrayList<Notification>> getNotifications()
    {
        return mNotificationsLiveData;
    }

    public LiveData<Double> getTodayEarnings()
    {
        return mEarningsMutableLiveData;
    }

    public LiveData<Integer> getTodayRides()
    {
        return mRidesNumberMutableLiveData;
    }

    public LiveData<Double> getTodayAccuracy()
    {
        return mAccuracyMutableLiveData;
    }

    public LiveData<Double> getTodayDistance()
    {
        return mDistanceMutableLiveData;
    }

    public LiveData<RequestState> getRideState()
    {
        return mRideRequestStateLiveData;
    }

    public LiveData<RequestState> getNotificationsState()
    {
        return mNotificationsStateLiveData;
    }
}

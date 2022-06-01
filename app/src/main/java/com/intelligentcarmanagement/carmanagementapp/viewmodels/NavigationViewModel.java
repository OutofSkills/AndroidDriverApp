package com.intelligentcarmanagement.carmanagementapp.viewmodels;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.intelligentcarmanagement.carmanagementapp.api.reviews.IRateClient;
import com.intelligentcarmanagement.carmanagementapp.api.reviews.IReviewRequests;
import com.intelligentcarmanagement.carmanagementapp.api.rides.responses.IGetRide;
import com.intelligentcarmanagement.carmanagementapp.models.ride.Ride;
import com.intelligentcarmanagement.carmanagementapp.repositories.reviews.IReviewsRepository;
import com.intelligentcarmanagement.carmanagementapp.repositories.reviews.ReviewsRepository;
import com.intelligentcarmanagement.carmanagementapp.repositories.rides.IRidesRepository;
import com.intelligentcarmanagement.carmanagementapp.repositories.rides.RidesRepository;
import com.intelligentcarmanagement.carmanagementapp.services.SessionManager;
import com.intelligentcarmanagement.carmanagementapp.utils.RequestState;

public class NavigationViewModel extends AndroidViewModel {
    private static final String TAG = "NavigationViewModel";
    private MutableLiveData<Ride> mOngoingRideLiveData = new MutableLiveData<Ride>();
    private MutableLiveData<RequestState> mRideRequestStateLiveData = new MutableLiveData<>();
    private MutableLiveData<RequestState> mStartRideStateLiveData = new MutableLiveData<>();
    private MutableLiveData<RequestState> mEndRideStateLiveData = new MutableLiveData<>();

    private SessionManager mSessionManager;
    private IRidesRepository mRidesRepository;
    private IReviewsRepository mReviewsRepository;

    public NavigationViewModel(@NonNull Application application) {
        super(application);

        mSessionManager = new SessionManager(application);
        mRidesRepository = new RidesRepository();
        mReviewsRepository = new ReviewsRepository();
    }

    public void fetchRide(int rideId)
    {
        mRideRequestStateLiveData.setValue(RequestState.START);
        String jwtToken = mSessionManager.getUserData().get(SessionManager.KEY_JWT_TOKEN);

        mRidesRepository.getRide(jwtToken, rideId, new IGetRide() {
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

    public void startRide()
    {
        mStartRideStateLiveData.setValue(RequestState.START);
        String jwtToken = mSessionManager.getUserData().get(SessionManager.KEY_JWT_TOKEN);
        int rideId = mOngoingRideLiveData.getValue().getId();

        mRidesRepository.startRide(jwtToken, rideId, new IGetRide() {
            @Override
            public void onResponse(Ride ride) {
                mOngoingRideLiveData.postValue(ride);
                mStartRideStateLiveData.setValue(RequestState.SUCCESS);
            }

            @Override
            public void onFailure(Throwable t) {
                mStartRideStateLiveData.setValue(RequestState.ERROR);
                Log.d(TAG, "onFailure: " + t.getMessage());
            }
        });
    }

    public void endRide()
    {
        mEndRideStateLiveData.setValue(RequestState.START);
        String jwtToken = mSessionManager.getUserData().get(SessionManager.KEY_JWT_TOKEN);
        int rideId = mOngoingRideLiveData.getValue().getId();

        mRidesRepository.endRide(jwtToken, rideId, new IGetRide() {
            @Override
            public void onResponse(Ride ride) {
                mOngoingRideLiveData.postValue(ride);
                mEndRideStateLiveData.setValue(RequestState.SUCCESS);
            }

            @Override
            public void onFailure(Throwable t) {
                mEndRideStateLiveData.setValue(RequestState.ERROR);
                Log.d(TAG, "onFailure: " + t.getMessage());
            }
        });
    }

    public void rateRide(double rating)
    {
        int rideId = mOngoingRideLiveData.getValue().getId();
        String jwtToken = mSessionManager.getUserData().get(SessionManager.KEY_JWT_TOKEN);

        mReviewsRepository.rateClient(jwtToken, rideId, rating, new IRateClient() {
            @Override
            public void onResponse() {
                Log.d(TAG, "Successfully rated.");
            }

            @Override
            public void onFailure(Throwable t) {
                Log.d(TAG, "Failed to rate: " + t.getMessage());
            }
        });
    }

    public LiveData<Ride> getRide()
    {
        return mOngoingRideLiveData;
    }

    public LiveData<RequestState> getRideState()
    {
        return mRideRequestStateLiveData;
    }

    public LiveData<RequestState> getStartRideState()
    {
        return mStartRideStateLiveData;
    }

    public LiveData<RequestState> getEndRideState()
    {
        return mEndRideStateLiveData;
    }
}

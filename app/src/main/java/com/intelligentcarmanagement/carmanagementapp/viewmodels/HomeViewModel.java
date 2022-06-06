package com.intelligentcarmanagement.carmanagementapp.viewmodels;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.intelligentcarmanagement.carmanagementapp.api.users.responses.IAvailableResponse;
import com.intelligentcarmanagement.carmanagementapp.repositories.users.UsersRepository;
import com.intelligentcarmanagement.carmanagementapp.utils.SessionManager;
import com.intelligentcarmanagement.carmanagementapp.utils.RequestState;

public class HomeViewModel extends AndroidViewModel {
    private static final String TAG = "HomeViewModel";
    private MutableLiveData<Boolean> mAvailabilityLiveData = new MutableLiveData<>();
    private MutableLiveData<RequestState> mAvailabilityStateLiveData = new MutableLiveData<>();

    private SessionManager mSessionManager;
    private UsersRepository mUsersRepository;

    public HomeViewModel(@NonNull Application application) {
        super(application);
        mSessionManager = new SessionManager(application);
        mUsersRepository = new UsersRepository();
    }

    public void makeDriverAvailable(boolean availability)
    {
        mAvailabilityStateLiveData.setValue(RequestState.START);
        String driverId = mSessionManager.getUserData().get(SessionManager.KEY_ID);

        mUsersRepository.makeAvailable(Integer.valueOf(driverId), availability, new IAvailableResponse() {
            @Override
            public void onResponse(boolean response) {
                Log.d(TAG, "onResponse: driver available.");
                mAvailabilityLiveData.setValue(response);
                mAvailabilityStateLiveData.setValue(RequestState.SUCCESS);
            }

            @Override
            public void onFailure(Throwable t) {
                Log.d(TAG, "onFailure: Error: " + t.getMessage());
                mAvailabilityStateLiveData.setValue(RequestState.ERROR);
            }
        });
    }

    public void fetchAvailability()
    {
        mAvailabilityStateLiveData.setValue(RequestState.START);
        String jwtToken = mSessionManager.getUserData().get(SessionManager.KEY_JWT_TOKEN);
        String driverId = mSessionManager.getUserData().get(SessionManager.KEY_ID);

        mUsersRepository.isAvailable(jwtToken, Integer.parseInt(driverId), new IAvailableResponse() {
            @Override
            public void onResponse(boolean response) {
                Log.d(TAG, "onResponse: driver available.");
                mAvailabilityLiveData.setValue(response);
                mAvailabilityStateLiveData.setValue(RequestState.SUCCESS);
            }

            @Override
            public void onFailure(Throwable t) {
                Log.d(TAG, "onFailure: Error: " + t.getMessage());
                mAvailabilityStateLiveData.setValue(RequestState.ERROR);
            }
        });
    }

    public LiveData<RequestState> availabilityState(){
        return mAvailabilityStateLiveData;
    }

    public LiveData<Boolean> isAvailable()
    {
        return mAvailabilityLiveData;
    }
}

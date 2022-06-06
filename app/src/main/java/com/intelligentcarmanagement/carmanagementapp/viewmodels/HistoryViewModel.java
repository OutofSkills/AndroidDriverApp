package com.intelligentcarmanagement.carmanagementapp.viewmodels;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.intelligentcarmanagement.carmanagementapp.api.rides.responses.IGetRidesHistory;
import com.intelligentcarmanagement.carmanagementapp.models.ride.Ride;
import com.intelligentcarmanagement.carmanagementapp.repositories.rides.IRidesRepository;
import com.intelligentcarmanagement.carmanagementapp.repositories.rides.RidesRepository;
import com.intelligentcarmanagement.carmanagementapp.utils.SessionManager;
import com.intelligentcarmanagement.carmanagementapp.utils.RequestState;

import java.util.ArrayList;

public class HistoryViewModel extends AndroidViewModel {
    private static final String TAG = "HistoryViewModel";
    private MutableLiveData<RequestState> mRequestStateLiveData = new MutableLiveData<>();
    private MutableLiveData<ArrayList<Ride>> mRidesLiveData = new MutableLiveData<>();

    private SessionManager mSessionManager;
    private IRidesRepository mRidesRepository;

    public HistoryViewModel(@NonNull Application application) {
        super(application);
        mSessionManager = new SessionManager(application);
        mRidesRepository = new RidesRepository();
    }

    public void getHistory() {
        mRequestStateLiveData.setValue(RequestState.START);

        // Get client's id
        String id = mSessionManager.getUserData().get(SessionManager.KEY_ID);
        String jwtToken = mSessionManager.getUserData().get(SessionManager.KEY_JWT_TOKEN);

        // Fetch the rides
        mRidesRepository.getRides(jwtToken, Integer.valueOf(id), new IGetRidesHistory() {
            @Override
            public void onResponse(ArrayList<Ride> historyRides) {
                mRidesLiveData.postValue(historyRides);
                mRequestStateLiveData.setValue(RequestState.SUCCESS);
            }

            @Override
            public void onFailure(Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage() );
                t.printStackTrace();
                mRequestStateLiveData.setValue(RequestState.ERROR);
            }
        });
    }

    public LiveData<ArrayList<Ride>> getRides(){
        return mRidesLiveData;
    }

    public LiveData<RequestState> getProcessingState(){
        return mRequestStateLiveData;
    }
}

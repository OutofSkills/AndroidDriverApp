package com.intelligentcarmanagement.carmanagementapp.viewmodels;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.intelligentcarmanagement.carmanagementapp.api.rides.responses.IGetRidesHistory;
import com.intelligentcarmanagement.carmanagementapp.models.User;
import com.intelligentcarmanagement.carmanagementapp.models.ride.Ride;
import com.intelligentcarmanagement.carmanagementapp.repositories.users.UsersRepository;
import com.intelligentcarmanagement.carmanagementapp.api.users.responses.IGetUserResponse;
import com.intelligentcarmanagement.carmanagementapp.api.users.responses.IUpdateUserResponse;
import com.intelligentcarmanagement.carmanagementapp.repositories.rides.IRidesRepository;
import com.intelligentcarmanagement.carmanagementapp.repositories.rides.RidesRepository;
import com.intelligentcarmanagement.carmanagementapp.utils.SessionManager;
import com.intelligentcarmanagement.carmanagementapp.utils.RequestState;

import java.util.ArrayList;

public class ProfileViewModel extends AndroidViewModel {
    private static final String TAG = "ProfileViewModel";

    private MutableLiveData<User> mUserMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<Boolean> mUpdatingMutableLiveData = new MutableLiveData<>();

    private MutableLiveData<RequestState> mRatingStateLiveData = new MutableLiveData<>();
//    private MutableLiveData<Double> mRatingMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<Integer> mRidesMutableLiveData = new MutableLiveData<>();

    private UsersRepository mUsersRepository;
    private SessionManager mSessionManager;
    private IRidesRepository mRidesRepository;

    public ProfileViewModel(@NonNull Application application) {
        super(application);
        mUsersRepository = new UsersRepository();
        mRidesRepository = new RidesRepository();
        mSessionManager = new SessionManager(application);
    }

    public void getHistory() {
        mRatingStateLiveData.setValue(RequestState.START);

        // Get client's id
        String id = mSessionManager.getUserData().get(SessionManager.KEY_ID);
        String jwtToken = mSessionManager.getUserData().get(SessionManager.KEY_JWT_TOKEN);

        // Fetch the rides
        mRidesRepository.getRides(jwtToken, Integer.parseInt(id), new IGetRidesHistory() {
            @Override
            public void onResponse(ArrayList<Ride> historyRides) {
                double rating = 0.0;
                int ratedRides = 0;
                for (Ride ride: historyRides) {
                    if(ride.getReview() != null){
                        if(ride.getReview().getRating() == 0) break;

                        ratedRides++;
                        rating = ride.getReview().getRating();
                    }
                }

//                mRatingMutableLiveData.setValue(rating/ratedRides);
                mRidesMutableLiveData.setValue(historyRides.size());
                mRatingStateLiveData.setValue(RequestState.SUCCESS);
            }

            @Override
            public void onFailure(Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage() );
                t.printStackTrace();
                mRatingStateLiveData.setValue(RequestState.ERROR);
            }
        });
    }

    public LiveData<Integer> getRidesNumber(){
        return mRidesMutableLiveData;
    }

//    public LiveData<Double> getRating(){
//        return mRatingMutableLiveData;
//    }

    public void fetchUser(){
        String email = mSessionManager.getUserData().get(mSessionManager.KEY_EMAIL);

        mUsersRepository.getUserByEmail(email, new IGetUserResponse() {
            @Override
            public void onResponse(User userResponse) {
                mUserMutableLiveData.postValue(userResponse);
            }

            @Override
            public void onFailure(Throwable t) {
                Log.d("Profile", "Response user: " + t.getMessage());
            }
        });
    }

    public LiveData<User> getUserMutableLiveData() {
        return mUserMutableLiveData;
    }

    public void updateUserMutableLiveData(User user) {
        mUpdatingMutableLiveData.setValue(true);
        mUsersRepository.updateUser(user.getId(), user, new IUpdateUserResponse() {
            @Override
            public void onResponse(User user) {
                Log.d("ProfileViewModel", "Success update");
                mUserMutableLiveData.setValue(user);
                mSessionManager.addUserAvatar(user.getAvatar());
            }

            @Override
            public void onFailure(Throwable throwable) {
                Log.d("ProfileViewModel", "Fail: " + throwable.getMessage());
            }
        });
        mUpdatingMutableLiveData.setValue(false);
    }

    public LiveData<Boolean> getUpdatingMutableLiveData() {
        return mUpdatingMutableLiveData;
    }
}

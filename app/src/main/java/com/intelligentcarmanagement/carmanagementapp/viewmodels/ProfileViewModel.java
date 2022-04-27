package com.intelligentcarmanagement.carmanagementapp.viewmodels;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.intelligentcarmanagement.carmanagementapp.models.User;
import com.intelligentcarmanagement.carmanagementapp.repositories.UsersRepo;
import com.intelligentcarmanagement.carmanagementapp.api.users.IGetUserResponse;
import com.intelligentcarmanagement.carmanagementapp.api.users.IUpdateUserResponse;
import com.intelligentcarmanagement.carmanagementapp.services.SessionManager;

public class ProfileViewModel extends AndroidViewModel {
    private MutableLiveData<User> mUserMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<Boolean> mUpdatingMutableLiveData = new MutableLiveData<>();

    UsersRepo mUsersRepository;
    SessionManager sessionManager;

    public ProfileViewModel(@NonNull Application application) {
        super(application);
        mUsersRepository = new UsersRepo();
        sessionManager = new SessionManager(application);
    }

    public void fetchUser(){
        String email = sessionManager.getUserData().get(sessionManager.KEY_EMAIL);

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

    public MutableLiveData<User> getUserMutableLiveData() {
        return mUserMutableLiveData;
    }

    public void updateUserMutableLiveData(User user) {
        mUpdatingMutableLiveData.setValue(true);
        mUsersRepository.updateUser(user.getId(), user, new IUpdateUserResponse() {
            @Override
            public void onResponse(User user) {
                Log.d("ProfileViewModel", "Success update");
                mUserMutableLiveData.setValue(user);
                sessionManager.addUserAvatar(user.getAvatar());
            }

            @Override
            public void onFailure(Throwable throwable) {
                Log.d("ProfileViewModel", "Fail: " + throwable.getMessage());
            }
        });
        mUpdatingMutableLiveData.setValue(false);
    }

    public MutableLiveData<Boolean> getUpdatingMutableLiveData() {
        return mUpdatingMutableLiveData;
    }
}

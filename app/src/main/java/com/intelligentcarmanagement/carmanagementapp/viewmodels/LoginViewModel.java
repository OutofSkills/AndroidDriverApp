package com.intelligentcarmanagement.carmanagementapp.viewmodels;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.intelligentcarmanagement.carmanagementapp.models.Login.LoginRequest;
import com.intelligentcarmanagement.carmanagementapp.models.Login.LoginResponse;
import com.intelligentcarmanagement.carmanagementapp.repositories.UsersRepo;
import com.intelligentcarmanagement.carmanagementapp.services.login.ILoginResponse;

public class LoginViewModel {
    MutableLiveData<String> mLoginResultMutableData = new MutableLiveData<>();

    UsersRepo mUsersRepository;

    public LoginViewModel() {
        this.mLoginResultMutableData.postValue("Not logged in");
        mUsersRepository = new UsersRepo();
    }

    public void login(String email, String password)
    {
        mLoginResultMutableData.postValue("Checking");

        mUsersRepository.loginRemote(new LoginRequest(email, password), new ILoginResponse() {
            @Override
            public void onResponse(LoginResponse loginResponse) {
                mLoginResultMutableData.postValue("Login Success");
                Log.d("View model", "Token: " + loginResponse.getToken());
            }

            @Override
            public void onFailure(Throwable t) {
                mLoginResultMutableData.postValue("Login Failure");
            }
        });
    }

    public LiveData<String> getLoginResult()
    {
        return mLoginResultMutableData;
    }
}

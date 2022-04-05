package com.intelligentcarmanagement.carmanagementapp.viewmodels;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.gson.Gson;
import com.intelligentcarmanagement.carmanagementapp.database.DatabaseHelper;
import com.intelligentcarmanagement.carmanagementapp.models.Login.LoginRequest;
import com.intelligentcarmanagement.carmanagementapp.models.Login.LoginResponse;
import com.intelligentcarmanagement.carmanagementapp.repositories.UsersRepo;
import com.intelligentcarmanagement.carmanagementapp.services.login.ILoginResponse;
import com.intelligentcarmanagement.carmanagementapp.utils.JwtParser;
import com.intelligentcarmanagement.carmanagementapp.utils.LoginState;

import java.util.Map;

public class LoginViewModel extends AndroidViewModel {
    MutableLiveData<LoginState> mLoginStateMutableData = new MutableLiveData<>();
    MutableLiveData<String> mLoginErrorMutableData = new MutableLiveData<>();
    MutableLiveData<String> mUserEmailMutableData = new MutableLiveData<>();

    UsersRepo mUsersRepository;
    DatabaseHelper dbHelper;

    public LoginViewModel(Application context) {
        super(context);
        if(mLoginStateMutableData == null)
            mLoginStateMutableData = new MutableLiveData<>();
        if(mLoginStateMutableData == null)
            mLoginErrorMutableData = new MutableLiveData<>();
        mUsersRepository = new UsersRepo();
        dbHelper = new DatabaseHelper(context);
    }

    public void login(String email, String password)
    {
        mLoginStateMutableData.setValue(LoginState.START);
        Log.d("ViewModel", "Login State: " + mLoginStateMutableData.getValue());

        // Make a login request to the API to obtain a token
        // and store it in local storage
        mUsersRepository.loginRemote(new LoginRequest(email, password), new ILoginResponse() {
            @Override
            public void onResponse(LoginResponse loginResponse) {
                try {
                    String token = loginResponse.getToken();
                    if(token == null) {
                        mLoginStateMutableData.setValue(LoginState.ERROR);
                        mLoginErrorMutableData.postValue("Server error. Please try again.");
                    }

                    // Save the token in local storage
                    dbHelper.SaveToken(token);

                    // Get the claims from the token
                    String payload = JwtParser.decoded(token);
                    Map<Object, Object> claims = decodeTokenClaims(payload);
                    mUserEmailMutableData.postValue(claims.get("email").toString());

                    mLoginStateMutableData.setValue(LoginState.SUCCESS);
                } catch (Exception e) {
                    mLoginErrorMutableData.postValue("Server error: " + e.getMessage());
                    mLoginStateMutableData.setValue(LoginState.ERROR);
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Throwable t) {
                mLoginStateMutableData.setValue(LoginState.ERROR);
                mLoginErrorMutableData.postValue("Server error: " + t.getMessage());
            }
        });
    }

    public LiveData<LoginState> getLoginState()
    {
        return mLoginStateMutableData;
    }

    public LiveData<String> getLoginResult(){return mUserEmailMutableData;}

    public LiveData<String> getLoginError()
    {
        return mLoginErrorMutableData;
    }

    private Map<Object, Object> decodeTokenClaims(String payload) {
        try {
            String decodedPayload = JwtParser.decoded(payload);
        }
        catch (Exception e) {
            Log.d("LoginViewModel", "Claims decoding error: " + e.getMessage());
        }

        return new Gson().fromJson(payload, Map.class);
    }
}

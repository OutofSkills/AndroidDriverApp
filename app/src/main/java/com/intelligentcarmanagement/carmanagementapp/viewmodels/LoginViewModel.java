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
import com.intelligentcarmanagement.carmanagementapp.models.User;
import com.intelligentcarmanagement.carmanagementapp.repositories.UsersRepo;
import com.intelligentcarmanagement.carmanagementapp.services.login.ILoginResponse;
import com.intelligentcarmanagement.carmanagementapp.services.users.IGetUserResponse;
import com.intelligentcarmanagement.carmanagementapp.utils.JwtParser;
import com.intelligentcarmanagement.carmanagementapp.utils.LoginState;
import com.intelligentcarmanagement.carmanagementapp.utils.SessionManager;

import java.util.Map;

public class LoginViewModel extends AndroidViewModel {
    MutableLiveData<LoginState> mLoginStateMutableData = new MutableLiveData<>();
    MutableLiveData<String> mLoginErrorMutableData = new MutableLiveData<>();

    SessionManager sessionManager;
    UsersRepo mUsersRepository;
    DatabaseHelper dbHelper;

    public LoginViewModel(Application context) {
        super(context);
        mUsersRepository = new UsersRepo();
        dbHelper = new DatabaseHelper(context);
        sessionManager = new SessionManager(context);
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

                    // Get the claims from the token
                    String payload = JwtParser.decoded(token);
                    Map<Object, Object> claims = decodeTokenClaims(payload);

                    // Create a session
                    sessionManager.createLoginSession(claims.get("id").toString(), claims.get("email").toString(), token);
                    // Fetch the user to retrieve additional data
                    fetchUser(claims.get("email").toString());
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

    public void fetchUser(String email){

        mUsersRepository.getUserByEmail(email, new IGetUserResponse() {
            @Override
            public void onResponse(User userResponse) {
                sessionManager.addUserAvatar(userResponse.getAvatar());
                mLoginStateMutableData.setValue(LoginState.SUCCESS);
            }

            @Override
            public void onFailure(Throwable t) {
                Log.d("Drawer", "Response user: " + t.getMessage());
                mLoginStateMutableData.setValue(LoginState.ERROR);
                mLoginErrorMutableData.postValue("Server error: " + t.getMessage());
            }
        });
    }

    public LiveData<LoginState> getLoginState()
    {
        return mLoginStateMutableData;
    }

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

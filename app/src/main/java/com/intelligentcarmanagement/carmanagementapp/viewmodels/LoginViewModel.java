package com.intelligentcarmanagement.carmanagementapp.viewmodels;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;
import com.intelligentcarmanagement.carmanagementapp.api.notifications.responses.IUpdateToken;
import com.intelligentcarmanagement.carmanagementapp.models.login.LoginRequest;
import com.intelligentcarmanagement.carmanagementapp.models.login.LoginResponse;
import com.intelligentcarmanagement.carmanagementapp.models.User;
import com.intelligentcarmanagement.carmanagementapp.models.errors.ServerErrorResponse;
import com.intelligentcarmanagement.carmanagementapp.models.errors.ServerValidationError;
import com.intelligentcarmanagement.carmanagementapp.repositories.users.UsersRepository;
import com.intelligentcarmanagement.carmanagementapp.api.login.ILoginResponse;
import com.intelligentcarmanagement.carmanagementapp.api.users.responses.IGetUserResponse;
import com.intelligentcarmanagement.carmanagementapp.repositories.notifications.INotificationsRepository;
import com.intelligentcarmanagement.carmanagementapp.repositories.notifications.NotificationsRepository;
import com.intelligentcarmanagement.carmanagementapp.utils.JwtParser;
import com.intelligentcarmanagement.carmanagementapp.utils.RequestState;
import com.intelligentcarmanagement.carmanagementapp.services.SessionManager;

import java.util.HashMap;
import java.util.Map;

public class LoginViewModel extends AndroidViewModel {
    private static final String TAG = "LoginViewModel";
    private MutableLiveData<RequestState> mLoginStateMutableData = new MutableLiveData<>();
    private MutableLiveData<String> mLoginErrorMutableData = new MutableLiveData<>();

    private SessionManager mSessionManager;
    private UsersRepository mUsersRepository;
    private INotificationsRepository mNotificationsRepository;

    public LoginViewModel(Application context) {
        super(context);
        mUsersRepository = new UsersRepository();
        mSessionManager = new SessionManager(context);
    }

    public void login(String email, String password)
    {
        mLoginStateMutableData.setValue(RequestState.START);
        Log.d("ViewModel", "Login State: " + mLoginStateMutableData.getValue());

        // Make a login request to the API to obtain a token
        // and store it in local storage
        mUsersRepository.loginRemote(new LoginRequest(email, password), new ILoginResponse() {
            @Override
            public void onResponse(LoginResponse loginResponse) {
                try {
                    String token = loginResponse.getJwtToken();
                    if(token == null) {
                        mLoginStateMutableData.setValue(RequestState.ERROR);
                        mLoginErrorMutableData.postValue("Server error. Please try again.");
                    }

                    // Get the claims from the token
                    String payload = JwtParser.decoded(token);
                    Map<Object, Object> claims = decodeTokenClaims(payload);

                    // Create a session
                    mSessionManager.createLoginSession(claims.get("id").toString(), claims.get("email").toString(), token);
                    // Update the firebase token
                    checkUpdateFirebaseToken(loginResponse.getFirebaseToken());
                    // Fetch the user to retrieve additional data
                    fetchUser(claims.get("email").toString());
                } catch (Exception e) {
                    mLoginErrorMutableData.postValue("Server error: " + e.getMessage());
                    mLoginStateMutableData.setValue(RequestState.ERROR);
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Throwable t) {
                mLoginStateMutableData.setValue(RequestState.ERROR);
                mLoginErrorMutableData.postValue("Server error: " + t.getMessage());
            }

            @Override
            public void onServerValidationFailure(ServerValidationError errorValidationResponse) {
                mLoginStateMutableData.setValue(RequestState.ERROR);
                mLoginErrorMutableData.postValue("Server error: " + errorValidationResponse.getErrors().values());
            }

            @Override
            public void onServerFailure(ServerErrorResponse serverErrorResponse) {
                mLoginStateMutableData.setValue(RequestState.ERROR);
                mLoginErrorMutableData.postValue("Server error: " + serverErrorResponse.getMessage());
            }
        });
    }

    public void fetchUser(String email){

        mUsersRepository.getUserByEmail(email, new IGetUserResponse() {
            @Override
            public void onResponse(User userResponse) {
                mSessionManager.addUserAvatar(userResponse.getAvatar());
                mSessionManager.changeAvailability(userResponse.isAvailable());
                mLoginStateMutableData.setValue(RequestState.SUCCESS);
            }

            @Override
            public void onFailure(Throwable t) {
                Log.d("LoginViewModel", "Response user: " + t.getMessage());
                mLoginStateMutableData.setValue(RequestState.ERROR);
                mLoginErrorMutableData.postValue("Server error: " + t.getMessage());
            }
        });
    }

    public void checkUpdateFirebaseToken(String serverFirebaseToken)
    {
        // Get the device's notifications token
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "Fetching FCM registration token failed", task.getException());
                            return;
                        }

                        // Get new FCM registration token
                        String localFirebaseToken = task.getResult();

                        if(localFirebaseToken == serverFirebaseToken) {
                            Log.d(TAG, "Firebase token didn't change");
                            return;
                        }

                        HashMap<String, String> userData = mSessionManager.getUserData();

                        try{
                            String userId = userData.get(SessionManager.KEY_ID);
                            String jwtToken = userData.get(SessionManager.KEY_JWT_TOKEN);

                            mNotificationsRepository = new NotificationsRepository();
                            mNotificationsRepository.updateToken(jwtToken, userId, localFirebaseToken, new IUpdateToken() {
                                @Override
                                public void onFailure(Throwable throwable) {
                                    Log.d(TAG, "checkUpdateFirebaseToken exception: Failed to update firebase token of user with id " + userId + " " +throwable.getMessage());
                                }

                                @Override
                                public void onResponse(String newFirebaseToken) {
                                    Log.d(TAG, "checkUpdateFirebaseToken success: Successfully updated firebase token of user with id " + userId);
                                }
                            });
                        }catch (Exception e) {
                            Log.d(TAG, "onNewToken exception: " + e.getMessage());
                        }

                    }
                });
    }

    public LiveData<RequestState> getLoginState()
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

package com.intelligentcarmanagement.carmanagementapp.viewmodels;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.intelligentcarmanagement.carmanagementapp.models.errors.ServerErrorResponse;
import com.intelligentcarmanagement.carmanagementapp.models.errors.ServerValidationError;
import com.intelligentcarmanagement.carmanagementapp.models.account.ChangePasswordDTO;
import com.intelligentcarmanagement.carmanagementapp.repositories.AccountRepo;
import com.intelligentcarmanagement.carmanagementapp.api.account.IPasswordChangeResponse;
import com.intelligentcarmanagement.carmanagementapp.utils.SessionManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ChangePasswordViewModel extends AndroidViewModel {
    MutableLiveData<Boolean> stateMutableLiveData = new MutableLiveData<>();
    MutableLiveData<String[]> mErrorsMutableLiveData = new MutableLiveData<>();

    AccountRepo mAccountRepository;
    SessionManager sessionManager;

    public ChangePasswordViewModel(@NonNull Application application) {
        super(application);
        mAccountRepository = new AccountRepo();
        sessionManager = new SessionManager(application);
    }

    public void changePassword(String currentPassword, String newPassword, String confirmPassword)
    {
        stateMutableLiveData.setValue(true);

        Map<String, String> data = sessionManager.getUserData();
        int id = Integer.valueOf(data.get(sessionManager.KEY_ID));
        String userEmail = data.get(sessionManager.KEY_EMAIL);
        mAccountRepository.changePassword(new ChangePasswordDTO(id, userEmail, currentPassword, newPassword, confirmPassword), new IPasswordChangeResponse() {
            @Override
            public void onResponse(ChangePasswordDTO passwordResponse) {
                Log.d("ProfileSettings", "Change password: Success");
                mErrorsMutableLiveData.postValue(new String[]{});
                stateMutableLiveData.setValue(false);
            }

            @Override
            public void onServerFailure(ServerErrorResponse error) {
                mErrorsMutableLiveData.postValue(new String[]{error.getMessage()});
                stateMutableLiveData.setValue(false);
            }
            
            @Override
            public void onServerValidationFailure(ServerValidationError error) {
                Log.d("ProfileSettings", "Change password: Validation error: ");
                List<String> validationErrors = new ArrayList<>();
                for (String[] array: error.getErrors().values()) {
                    for (String string: array) {
                        validationErrors.add(string);
                    }
                }
                mErrorsMutableLiveData.postValue(validationErrors.toArray(new String[0]));
                stateMutableLiveData.setValue(false);
            }

            @Override
            public void onFailure(Throwable t) {
                Log.d("ProfileSettings", "Change password: Fail, " + t.getMessage());
                mErrorsMutableLiveData.postValue(new String[]{ t.getMessage() });
                stateMutableLiveData.setValue(false);
            }
        });
    }

    public MutableLiveData<Boolean> getStateMutableLiveData() {
        return stateMutableLiveData;
    }

    public MutableLiveData<String[]> getErrorsMutableLiveData() {
        return mErrorsMutableLiveData;
    }
}

package com.intelligentcarmanagement.carmanagementapp.viewmodels;

import android.app.Application;
import android.graphics.Bitmap;
import android.util.Log;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.intelligentcarmanagement.carmanagementapp.repositories.UsersRepo;
import com.intelligentcarmanagement.carmanagementapp.utils.ImageConverter;
import com.intelligentcarmanagement.carmanagementapp.services.SessionManager;

import java.util.HashMap;

public class DrawerViewModel extends AndroidViewModel {
    private MutableLiveData<Bitmap> mAvatarLiveData = new MutableLiveData<>();
    private MutableLiveData<String> mEmailLiveData = new MutableLiveData<>();

    UsersRepo usersRepo;
    SessionManager sessionManager;

    public DrawerViewModel(Application context) {
        super(context);
        usersRepo = new UsersRepo();
        sessionManager = new SessionManager(context);
    }

    public void getUserSession()
    {
        HashMap<String, String> userData = sessionManager.getUserData();

        // Get the avatar
        byte[] imageBytes = ImageConverter.convertBase64ToBytes(userData.get(sessionManager.KEY_AVATAR));
        mAvatarLiveData.postValue(ImageConverter.convertBytesToBitmap(imageBytes));

        // Get the email
        mEmailLiveData.postValue(userData.get(sessionManager.KEY_EMAIL));
    }

    public MutableLiveData<Bitmap> getAvatarLiveData() {
        return mAvatarLiveData;
    }

    public MutableLiveData<String> getEmailLiveData() {
        return mEmailLiveData;
    }

    public void logout()
    {
        try {
            sessionManager.clearSession();
        }catch (Exception e)
        {
            Log.d("LoginViewModel", "Logout error: "+ e.getMessage());
        }
    }
}

package com.intelligentcarmanagement.carmanagementapp.viewmodels;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.intelligentcarmanagement.carmanagementapp.activities.DrawerBaseActivity;
import com.intelligentcarmanagement.carmanagementapp.database.DatabaseHelper;
import com.intelligentcarmanagement.carmanagementapp.models.User;
import com.intelligentcarmanagement.carmanagementapp.repositories.UsersRepo;
import com.intelligentcarmanagement.carmanagementapp.services.users.IGetUserResponse;

public class DrawerViewModel extends AndroidViewModel {
    private MutableLiveData<User> mUserLiveData = new MutableLiveData<>();

    UsersRepo usersRepo;
    DatabaseHelper dbHelper;
    public DrawerViewModel(Application context) {
        super(context);
        usersRepo = new UsersRepo();
        dbHelper = new DatabaseHelper(context);
    }

    // Fetch the user's data by using its email
    public MutableLiveData<User> getUserLiveData() {
        return mUserLiveData;
    }

    public void fetchUser(String email)
    {
        usersRepo.getUserByEmail(email, new IGetUserResponse() {
            @Override
            public void onResponse(User userResponse) {
                mUserLiveData.postValue(userResponse);
            }

            @Override
            public void onFailure(Throwable t) {
                Log.d("Drawer", "Response user: " + t.getMessage());
            }
        });
    }

    public void logout()
    {
        try {
            dbHelper.RemoveToken();
        }catch (Exception e)
        {
            Log.d("LoginViewModel", "Logout error: "+ e.getMessage());
        }
    }
}

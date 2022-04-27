package com.intelligentcarmanagement.carmanagementapp.services;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;

import java.util.HashMap;

import retrofit2.http.PUT;

public class SessionManager {
    private SharedPreferences userSession;
    private SharedPreferences.Editor editor;
    private Context context;

    public static final String KEY_ID = "id";
    public static final String KEY_EMAIL = "email";
    public static final String KEY_AVATAR = "avatar";
    public static final String KEY_AVAILABILITY = "availability";
    public static final String KEY_TOKEN = "token";

    public SessionManager(Context context)
    {
        this.context = context;
        userSession = context.getSharedPreferences("userSession", Context.MODE_PRIVATE);
        editor = userSession.edit();
    }

    public void createLoginSession(String id, String email, String token)
    {
        editor.putString(KEY_ID, id);
        editor.putString(KEY_EMAIL, email);
        editor.putString(KEY_TOKEN, token);

        editor.commit();
    }

    public void addUserAvatar(String avatarBase64)
    {
        editor.putString(KEY_AVATAR, avatarBase64);

        editor.commit();
    }

    public void changeAvailability(boolean availability){
        editor.putBoolean(KEY_AVAILABILITY, availability);

        editor.commit();
    }

    public HashMap<String, String> getUserData()
    {
        HashMap<String, String> userData = new HashMap<>();

        userData.put(KEY_ID, userSession.getString(KEY_ID, null));
        userData.put(KEY_EMAIL, userSession.getString(KEY_EMAIL, null));
        userData.put(KEY_TOKEN, userSession.getString(KEY_TOKEN, null));
        userData.put(KEY_AVATAR, userSession.getString(KEY_AVATAR, null));
        userData.put(KEY_AVAILABILITY, Boolean.toString(userSession.getBoolean(KEY_AVAILABILITY, false)));

        return userData;
    }

    public void clearSession()
    {
        editor.clear();
        editor.commit();
    }

}

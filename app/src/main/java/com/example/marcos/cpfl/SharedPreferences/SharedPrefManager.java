package com.example.marcos.cpfl.SharedPreferences;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.marcos.cpfl.Models.User;

public class SharedPrefManager {

    private static SharedPrefManager mInstance;
    private static Context mCtext;

    private static final String SHARED_PREF_NAME = "myname";
    private static final String KEY_USER_TYPE = "user_type";
    private static final String KEY_USER_CPF = "usercpf";
    private static final String KEY_USER_ID = "userid";
    private static final String KEY_USERNAME = "username";


    private SharedPrefManager(android.content.Context context) {
        mCtext = context;
    }

    public static synchronized SharedPrefManager getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new SharedPrefManager(context);
        }
        return mInstance;
    }

    public void userLogin(User user) {

        SharedPreferences sharedPreferences = mCtext.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putInt(KEY_USER_ID, user.getId());
        editor.putString(KEY_USER_CPF, user.getCpf());
        editor.putInt(KEY_USER_TYPE, user.getUser_type());
        editor.putString(KEY_USERNAME, user.getUsername());

        editor.apply();

    }

    public boolean isLoggedIn() {
        SharedPreferences sharedPreferences = mCtext.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_USER_CPF, null) != null;
    }

    public boolean logout() {
        SharedPreferences sharedPreferences = mCtext.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
        return true;
    }

    public User getUser() {
        SharedPreferences sharedPreferences = mCtext.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return new User(
                sharedPreferences.getInt(KEY_USER_ID, -1),
                sharedPreferences.getString(KEY_USER_CPF, null),
                sharedPreferences.getInt(KEY_USER_TYPE, -1),
                sharedPreferences.getString(KEY_USERNAME, null)
        );
    }
}

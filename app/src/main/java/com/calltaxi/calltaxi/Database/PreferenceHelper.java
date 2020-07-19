package com.calltaxi.calltaxi.Database;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferenceHelper {

    final String INTRO = "INTRO";
    final String USERNAME = "username";
    final String EMAIL = "email";
    SharedPreferences sharedPreferences;
    Context context;

    public PreferenceHelper(Context context){
        sharedPreferences = context.getSharedPreferences("Shared",
                Context.MODE_PRIVATE);
        this.context = context;
    }

    public void putIsLogin(boolean loginorout){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(INTRO, loginorout);
        editor.commit();
    }

    public boolean getIsLogin(){
        return sharedPreferences.getBoolean(INTRO, false);
    }

    public void putUsername(String loginorout){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(USERNAME, loginorout);
        editor.commit();
    }

    public String getUsername(){
        return sharedPreferences.getString(USERNAME, "");
    }

    public void putEmail(String loginorout){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(EMAIL, loginorout);
        editor.commit();
    }

    public String getEmail(){
        return sharedPreferences.getString(EMAIL, "");
    }

}

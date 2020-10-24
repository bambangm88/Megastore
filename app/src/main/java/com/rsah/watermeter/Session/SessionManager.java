package com.rsah.watermeter.Session;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManager {

    // Shared Preferences
    private SharedPreferences pref;

    // Editor for Shared preferences
    private SharedPreferences.Editor editor;

    // Context
    private Context _context;

    // Shared pref mode
    int PRIVATE_MODE = 0;
    // Sharedpref file name
    private static final String PREF_NAME = "closeloop";
    // All Shared Preferences Keys
    private static final String IS_LOGIN = "IsLoggedIn";

    public static final String KEY_USER = "userx_";
    public static final String KEY_AMOUNT = "amountx_";

    public static final String KEY_FONT_SIZE= "fontsizex_";
    public static final String KEY_FONT_STYLE = "fontstylex_";

    // Constructor
    public SessionManager(Context context){
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }


    /**
     * Create login session
     * */
    public void saveUser( String user){ //storeObjectSiswa
        editor.putString(KEY_USER, user);
        editor.putBoolean(IS_LOGIN, true);
        editor.commit();
    }


    public void saveSetting( String fontsize , String fontstyle){ //storeObjectSiswa
        editor.putString(KEY_FONT_SIZE, fontsize);
        editor.putString(KEY_FONT_STYLE, fontstyle);
        editor.commit();
    }


    public String getInstanceUser() {
        return pref.getString(KEY_USER, null);
    }


    public String getFontSize() {
        return pref.getString(KEY_FONT_SIZE, null);
    }

    public String getFontStyle() {
        return pref.getString(KEY_FONT_STYLE, null);
    }

    /**
     * Hapus Data Session
     * */
    public void logoutUser(){
        // Clearing all data from Shared Preferences
        editor.clear();
        editor.commit();

    }

    /**
     * Quick check for login
     * **/
    // Get Login State
    public boolean isLoggedIn(){
        return pref.getBoolean(IS_LOGIN, false);
    }
}
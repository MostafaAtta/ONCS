package com.atta.oncs.model;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.atta.oncs.LoginActivity;
import com.atta.oncs.MainActivity;

public class SessionManager {


    // Shared Preferences
    SharedPreferences pref;

    // Editor for Shared preferences
    SharedPreferences.Editor editor;

    private static SessionManager mInstance;
    // Context
    Context _context;

    // Shared pref mode
    int PRIVATE_MODE = 0;

    // Sharedpref file name
    private static final String PREF_NAME = "ONCS";

    // All Shared Preferences Keys
    private static final String IS_LOGIN = "IsLoggedIn";

    // User name (make variable public to access from outside)
    public static final String KEY_ID = "user_id";

    // User name (make variable public to access from outside)
    public static final String KEY_EMAIL = "email";


    private static final String KEY_USER_REGION_ID = "region";

    private static final String KEY_ORDER_REGION_ID = "location_region";

    private static final String KEY_USER_REGION_NAME = "regionName";

    // User Name (make variable public to access from outside)
    public static final String KEY_PASSWORD = "password";

    // User Name (make variable public to access from outside)
    public static final String KEY_USER_NAME = "userName";

    public static final String KEY_USER_IMAGE = "userImage";

    private static final String KEY_USER_PHONE = "phone";

    private static final String KEY_USER_TOKEN = "token";

    private static final String KEY_IS_LANGUAGE_SELECTED = "isLanguageSelected";

    private static final String KEY_LANGUAGE_SELECTED = "languageSelected";




    // Constructor
    public SessionManager(Context context){
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }


    public static synchronized SessionManager getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new SessionManager(context);
        }
        return mInstance;
    }
    // Get Login State
    public int  getUserId(){
        return pref.getInt(KEY_ID, 0);
    }

    // Get Login State
    public String getUserName(){
        return pref.getString(KEY_USER_NAME, "Guest");
    }

    // Get Login State
    public String getUserPhone(){
        return pref.getString(KEY_USER_PHONE, "");
    }


    // Get Login State
    public String getUserToken(){
        return pref.getString(KEY_USER_TOKEN, "");
    }

    public void setUserToken(String token){

        // Storing national ID in pref
        editor.putString(KEY_USER_TOKEN, token);


        // commit changes
        editor.commit();
    }

    public void setUserRegionName(String regionName){

        // Storing national ID in pref
        editor.putString(KEY_USER_REGION_NAME, regionName);


        // commit changes
        editor.commit();
    }


    public String getUserRegionName(){
        return pref.getString(KEY_USER_REGION_NAME, "");
    }


    public void setOrderregionId(int region){

        // Storing national ID in pref
        editor.putInt(KEY_ORDER_REGION_ID, region);


        // commit changes
        editor.commit();
    }


    public int getOrderRegionId(){
        return pref.getInt(KEY_ORDER_REGION_ID, 0);
    }

    // Get Login State
    public String getEmail(){
        return pref.getString(KEY_EMAIL, "no email");
    }

    // Get Login State
    public boolean checkIfLanguageSelected (){
        return pref.getBoolean(KEY_IS_LANGUAGE_SELECTED, false);
    }

    // Get Login State
    public String getLanguage(){

        return pref.getString(KEY_LANGUAGE_SELECTED, "en");
    }
    // Get Login State
    public String getPassword(){
        return pref.getString(KEY_PASSWORD, "no password");
    }


    // Get Login State
    public String getProfileImage(){
        return pref.getString(KEY_USER_IMAGE, "");
    }
    /**
     * Create login session
     * */
    public void createLoginSession(User user){

        SharedPreferences sharedPreferences = _context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        // Storing login value as TRUE
        editor.putBoolean(IS_LOGIN, true);
        editor.putInt(KEY_ID, user.getId());
        editor.putString(KEY_USER_NAME, user.getUsername());
        editor.putString(KEY_EMAIL, user.getEmail());
        editor.putString(KEY_USER_PHONE, user.getPhone());
        editor.putInt(KEY_USER_REGION_ID, user.getRegionId());
        editor.putString(KEY_USER_IMAGE, user.getProfileImage());
        editor.apply();


        //QueryUtils.removeCartItems( _context, null);
    }


    public void updatePassword (String newpassword){
        // Storing Password in pref
        editor.putString(KEY_PASSWORD, newpassword);

        // commit changes
        editor.commit();
    }

    public void checkLogin(){
        // Check login status
        if(this.isLoggedIn()){

            // user is logged in redirect him to Main Activity
            Intent i = new Intent(_context, MainActivity.class);
            // Closing all the Activities
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            // Add new Flag to start new Activity
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            // Staring Login Activity
            _context.startActivity(i);


        }else {
            // user is not logged in redirect him to Login Activity
            Intent i = new Intent(_context, LoginActivity.class);
            // Closing all the Activities
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            // Add new Flag to start new Activity
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            // Staring Login Activity
            _context.startActivity(i);

        }

    }

    /**
     * Clear session details
     * */
    public void logoutUser(){

        // Clearing all data from Shared Preferences
        editor.clear();
        editor.putBoolean(IS_LOGIN, false);
        editor.putBoolean(KEY_IS_LANGUAGE_SELECTED, false);
        editor.commit();

        // After logout redirect user to Loing Activity
        Intent i = new Intent(_context, LoginActivity.class);
        // Closing all the Activities
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        // Add new Flag to start new Activity
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        // Staring Login Activity
        _context.startActivity(i);
    }

    /**
     * Quick check for login
     * **/
    // Get Login State
    public boolean isLoggedIn(){
        return pref.getBoolean(IS_LOGIN, false);
    }

    public String getUserImage() {
        return pref.getString(KEY_USER_IMAGE, "");
    }

    public void setUserImage(String image) {

        // Storing national ID in pref
        editor.putString(KEY_USER_IMAGE, image);


        // commit changes
        editor.commit();
    }

    public void setLanguage(String language) {

        // Storing national ID in pref
        editor.putBoolean(KEY_IS_LANGUAGE_SELECTED, true);
        editor.putString(KEY_LANGUAGE_SELECTED, language);


        // commit changes
        editor.commit();
    }

    public int getUserRegionId() {
        return pref.getInt(KEY_USER_REGION_ID, 0);
    }
}

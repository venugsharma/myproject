package com.tattleup.app.tattleup.util;

import android.app.Activity;
import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.inputmethod.InputMethodManager;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

public class AppController extends Application {

    public static final String TAG = AppController.class.getSimpleName();
    private SharedPreferences prefs;
    private RequestQueue mRequestQueue;
    private ImageLoader mImageLoader;

    private static AppController mInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
    }

    public static synchronized AppController getInstance() {
        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        return mRequestQueue;
    }

    public ImageLoader getImageLoader() {
        getRequestQueue();
        if (mImageLoader == null) {
            mImageLoader = new ImageLoader(this.mRequestQueue,
                    new LruBitmapCache());
        }
        return this.mImageLoader;
    }

    public <T> void addToRequestQueue(Request<T> req, String tag) {
        // set the default tag if tag is empty
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }

    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }

    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }


    public boolean isFirstTimeLogin(String variable, boolean defaultValue) {
        return prefs.getBoolean(variable, defaultValue);
    }

    public void setFirstTimeLoginCompleted(String variable, boolean data) {
        prefs.edit().putBoolean(variable, data).commit();
    }

    public void setSelectedHospital(String variable, String data) {
        prefs.edit().putString(variable, data).commit();
    }

    public String getSelectedHospital(String variable, String defaultValue) {
        return prefs.getString(variable, defaultValue);
    }

    public void setDATA(String variable, String data) {
        prefs.edit().putString(variable, data).commit();
    }

    public String getDATA(String variable, String defaultValue) {
        return prefs.getString(variable, defaultValue);
    }

    public void setUserLogIn(String variable, boolean data) {
        prefs.edit().putBoolean(variable, data).commit();
    }

    public boolean isUserLogin(String variable, boolean defaultValue) {
        return prefs.getBoolean(variable, defaultValue);
    }

    public void setUserLogInId(String variable, int data) {
        prefs.edit().putInt(variable, data).commit();
    }

    public int getUserLoginId(String variable, int defaultValue) {
        return prefs.getInt(variable, defaultValue);
    }

//    public void logoutFromApp() {
//        prefs.edit().clear().commit();
//        setFirstTimeLoginCompleted(AppUtils.IsFirstTimeLogin, false);
//    }

    public void hideKeyboard(Activity activity) {
        try {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
        } catch (Exception e) {

        }
    }




}
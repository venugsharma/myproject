package com.tattleup.app.tattleup.base;

/**
 * Created by Shiva on 6/21/2016.
 */

import android.app.Activity;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.multidex.MultiDexApplication;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.inputmethod.InputMethodManager;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.GoogleApiClient;
import com.tattleup.app.tattleup.util.LruBitmapCache;


public class TattleUp extends MultiDexApplication {
    //private boolean isCustomerLoaded = false;
    private SharedPreferences prefs;

    public static final String TAG = TattleUp.class.getSimpleName();

    private RequestQueue mRequestQueue;
    private ImageLoader mImageLoader;

    private static TattleUp mInstance;

//    Api Client Component for Google Login
    private GoogleApiClient mGoogleApiClient;
    private GoogleSignInOptions gso;
    public AppCompatActivity activity;


    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        //isCustomerLoaded = isCustomerLoaded(getApplicationContext(), "getCustomerLoaded", false);
        prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
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


    public void hideKeyboard(Activity activity) {
        try {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
        } catch (Exception e) {

        }
    }



    public static synchronized TattleUp getInstance() {
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


//    Enable Google Login
    public GoogleSignInOptions getGoogleSignInOptions() {
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        return gso;
    }

    public GoogleApiClient getGoogleApiClient(BaseActivity activity, GoogleApiClient.OnConnectionFailedListener listener) {
        this.activity = activity;
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this.activity, listener)
                .addApi(Auth.GOOGLE_SIGN_IN_API, getGoogleSignInOptions())
                .build();
        return mGoogleApiClient;
    }
}


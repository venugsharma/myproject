package com.tattleup.app.tattleup.base;

/**
 * Created by Shiva on 6/21/2016.
 */

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Base64;
import android.util.Log;
import android.util.Patterns;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.tattleup.app.tattleup.util.AppUtils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public class BaseActivity extends ActionBarActivity {
    public static final String LOG_TYPE_VERBOSE = "verbose";
    public static final String LOG_TYPE_WARNING = "warning";
    public static final String LOG_TYPE_ERROR = "error";
    protected Context context;
    protected TattleUp tattleUp;
    //private UiLifecycleHelper uiHelper;
    public GoogleApiClient mGoogleApiClient;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        initializeComponents();
        bindEvents();
        tattleUp = (TattleUp) getApplication();
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "tattleup.com",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {
        }
        if (tattleUp.isUserLogin(AppUtils.IS_USER_LOGGED_IN, false)) {
            String loggedInVia = tattleUp.getDATA(AppUtils.LOGIN_USER_VIA, "");
            if (loggedInVia.equals("facebook")) {
//                uiHelper = new UiLifecycleHelper((LoginActivity) context, this);
//                uiHelper.onCreate(savedInstanceState);
            } else if (loggedInVia.equals("google")) {
//                if (mGoogleApiClient.isConnected()) {
//                    Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
//                    mGoogleApiClient.disconnect();
//                    mGoogleApiClient.connect();
//                }
            } else {

            }
        } else {

        }
//        Intent sms = new Intent(this.context, SMSBroadcastReceiver.class);
//        boolean smsRunning = (PendingIntent.getBroadcast(this.context, 0, sms, PendingIntent.FLAG_NO_CREATE) != null);
//        if (smsRunning == false) {
//            PendingIntent pendingIntent = PendingIntent.getBroadcast(this.context, 0, sms, 0);
//            AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
//            alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime(), 15000, pendingIntent);
//        }
//        startService(new Intent(getBaseContext(), MyService.class));
    }

    public void initializeComponents() {
        tattleUp = (TattleUp) getApplication();
//        if(healthAdvisor.isUserLogin(AppUtils.IS_USER_LOGGED_IN, false))
//        {
//            String loggedInVia = healthAdvisor.getDATA(AppUtils.LOGIN_USER_VIA, "");
//            if(loggedInVia.equals("facebook"))
//            {
//
//            }
//            else if(loggedInVia.equals("google"))
//            {
//                mGoogleApiClient = new GoogleApiClient.Builder(this)
//                        .addApi(Plus.API)
//                        .addScope(Plus.SCOPE_PLUS_LOGIN)
//                        .addScope(Plus.SCOPE_PLUS_PROFILE)
//                        .addScope(new Scope("https://www.googleapis.com/auth/userinfo.email"))
//                        .addConnectionCallbacks(this)
//                        .addOnConnectionFailedListener(this).build();
//            }
//            else
//            {
//
//            }
//        }
//        else
//        {
//
//        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (tattleUp.isUserLogin(AppUtils.IS_USER_LOGGED_IN, false)) {
            String loggedInVia = tattleUp.getDATA(AppUtils.LOGIN_USER_VIA, "");
            if (loggedInVia.equals("facebook")) {
//                uiHelper.onResume();
            } else if (loggedInVia.equals("google")) {

            } else {

            }
        } else {

        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (tattleUp.isUserLogin(AppUtils.IS_USER_LOGGED_IN, false)) {
            String loggedInVia = tattleUp.getDATA(AppUtils.LOGIN_USER_VIA, "");
            if (loggedInVia.equals("facebook")) {
//                uiHelper.onPause();
            } else if (loggedInVia.equals("google")) {

            } else {

            }
        } else {

        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
//        MyService myService = new MyService();
//        myService.StartForground();
//        Intent intent = new Intent(getBaseContext(), MyService.class);
//        startService(intent);
//        myService.onTaskRemoved(intent);
//        Notification notification = new NotificationCompat.Builder(this)
//                .setOngoing(false)
//                .setSmallIcon(android.R.color.transparent)
//
//                        //.setSmallIcon(R.drawable.picture)
//                .build();
//        myService.startForegroundCompat(101, notification);
        if (tattleUp.isUserLogin(AppUtils.IS_USER_LOGGED_IN, false)) {
            String loggedInVia = tattleUp.getDATA(AppUtils.LOGIN_USER_VIA, "");
            if (loggedInVia.equals("facebook")) {
//                uiHelper.onDestroy();
            } else if (loggedInVia.equals("google")) {

            } else {

            }
        } else {

        }

    }

    @Override
    public void onSaveInstanceState(Bundle savedState) {
        super.onSaveInstanceState(savedState);
        if (tattleUp.isUserLogin(AppUtils.IS_USER_LOGGED_IN, false)) {
            String loggedInVia = tattleUp.getDATA(AppUtils.LOGIN_USER_VIA, "");
            if (loggedInVia.equals("facebook")) {
//                uiHelper.onSaveInstanceState(savedState);
            } else if (loggedInVia.equals("google")) {

            } else {

            }
        } else {

        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        if (tattleUp.isUserLogin(AppUtils.IS_USER_LOGGED_IN, false)) {
            String loggedInVia = tattleUp.getDATA(AppUtils.LOGIN_USER_VIA, "");

        } else {

        }
    }

    @Override
    protected void onStop() {
        super.onStop();


    }

    public void bindEvents() {
    }

    public void showShortToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    public void log(String message, String type) {
        AppUtils.log(BaseActivity.this, message, type);
    }

    public boolean checkEmail(String email) {
        return email != null && Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public void hideSoftKeyboard(Activity activity) {
        try {
            if (activity.getCurrentFocus() != null) {
                InputMethodManager inputmethodmanager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
                inputmethodmanager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public AlertDialog showMessage(String title, String message, String positiveButtonText,
                                   DialogInterface.OnClickListener positiveCallback) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setCancelable(false);
        builder.setPositiveButton(positiveButtonText, positiveCallback);
        AlertDialog alert = builder.create();
        return alert;
    }


//    @Override
//    public void finish() {
//        super.finish();
//        MyService myService = new MyService();
//        myService.StartForground();
//        Intent intent = new Intent(getBaseContext(), MyService.class);
//        startService(intent);
//
//        myService.onTaskRemoved(intent);
//    }

}


package com.tattleup.app.tattleup;

import android.os.Bundle;

import com.facebook.FacebookSdk;
import com.tattleup.app.tattleup.base.BaseActivity;
import com.tattleup.app.tattleup.fragment.UserLoginFragment;

/**
 * Created by admin on 9/7/2017.
 */

public class LoginActivity extends BaseActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_login);
        UserLoginFragment fragment = new UserLoginFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.content_frame_login, fragment)
                .addToBackStack(null).commit();

    }
}

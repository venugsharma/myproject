package com.tattleup.app.tattleup.fragment;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.tattleup.app.tattleup.MainActivity;
import com.tattleup.app.tattleup.R;
import com.tattleup.app.tattleup.asynctask.BasicPostAsync;
import com.tattleup.app.tattleup.base.BaseActivity;
import com.tattleup.app.tattleup.base.BaseFragment;
import com.tattleup.app.tattleup.base.TattleUp;
import com.tattleup.app.tattleup.util.AppUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * A simple {@link Fragment} subclass.
 */
public class UserLoginFragment extends BaseFragment implements GoogleApiClient.OnConnectionFailedListener, View.OnClickListener {

//    Initialized for Google Login
    private static final String TAG = "GoogleSignInActivity";
    private static final int RC_SIGN_IN = 9001;
    private GoogleSignInOptions gso;
    private GoogleApiClient mGoogleApiClient;
    public static final String PROFILE_USER_ID = "USER_ID";
    public static final String PROFILE_DISPLAY_NAME = "PROFILE_DISPLAY_NAME";
    public static final String PROFILE_USER_EMAIL = "USER_PROFILE_EMAIL";
    public static final String PROFILE_IMAGE_URL = "PROFILE_IMAGE_URL";


    private TattleUp tattleUp;
    LoginButton btnFbLogin;
    CallbackManager callbackManager;
    private Button btnContinueLogin, btnSignUp, btnSingIn;
    TextView btnSkip;
    private View rootView;
    FragmentManager mFragmentManager;
    private Intent intent;
    private SignInButton btnGoogleLogin;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_user_login, container, false);
        super.initializeFragment(rootView);
        setHasOptionsMenu(true);
        FacebookSdk.sdkInitialize(getApplicationContext());


        return rootView;
    }

    @Override
    public View initializeComponents(View rootView) {
        super.initializeComponents(rootView);
        tattleUp = (TattleUp) getActivity().getApplication();

        callbackManager = CallbackManager.Factory.create();
        btnFbLogin = (LoginButton) rootView.findViewById(R.id.btnFbLogin);
//        btnContinueLogin = (Button) findViewById(R.id.btnContLogin);
        btnFbLogin.setOnClickListener(this);

        btnSignUp = (Button) rootView.findViewById(R.id.btnSignUp);
        btnSingIn = (Button) rootView.findViewById(R.id.btnSignIn);

        btnSingIn.setOnClickListener(this);
        btnSignUp.setOnClickListener(this);


        gso = ((TattleUp) getActivity().getApplication()).getGoogleSignInOptions();
        try {
            mGoogleApiClient = ((TattleUp) getActivity().getApplication()).getGoogleApiClient((BaseActivity) getActivity(), this);

        }catch (IllegalStateException e){}
           btnGoogleLogin = (SignInButton)rootView.findViewById(R.id.btnGoogleLogin);
        assert btnGoogleLogin != null;
        btnGoogleLogin.setSize(SignInButton.SIZE_STANDARD);
        btnGoogleLogin.setScopes(gso.getScopeArray());
        btnGoogleLogin.setOnClickListener(this);


        if (getActivity().getIntent().getData() != null) {
            try {
                Uri data = getActivity().getIntent().getData();
                String host = data.getHost();
                String dp = data.getPath();
                if (host.equalsIgnoreCase("www.tattleup.com")) {
                    if (dp.equalsIgnoreCase("/TattleUp/dashBoard")) {
                        if (tattleUp.isUserLogin(AppUtils.IS_USER_LOGGED_IN, true)) {
                            if (tattleUp.getDATA(AppUtils.LOGIN_USER_ID, "") != "0" && Integer.valueOf(tattleUp.getDATA(AppUtils.LOGIN_USER_ID, "")) != 0 && tattleUp.getDATA(AppUtils.LOGIN_USER_ID, "") != null) {
                                Intent intent = new Intent(getActivity(), MainActivity.class);
                                startActivity(intent);
                                 getActivity().finish();
                            }
                        }
                    } else if (dp.equalsIgnoreCase("/ShopAdmin/dashBoard")) {
                        if (tattleUp.isUserLogin(AppUtils.IS_USER_LOGGED_IN, true)) {
                            if (tattleUp.getDATA(AppUtils.LOGIN_USER_ID, "") != "0" && Integer.valueOf(tattleUp.getDATA(AppUtils.LOGIN_USER_ID, "")) != 0 && tattleUp.getDATA(AppUtils.LOGIN_USER_ID, "") != null) {
                                Intent intent = new Intent(getActivity(), MainActivity.class);
                                startActivity(intent);
                                  getActivity().finish();
                            }
                        }
                    } else {
                        try {
                            if (tattleUp.isUserLogin(AppUtils.IS_USER_LOGGED_IN, true)) {
                                if (tattleUp.getDATA(AppUtils.LOGIN_USER_ID, "") != "0" && Integer.valueOf(tattleUp.getDATA(AppUtils.LOGIN_USER_ID, "")) != 0 && tattleUp.getDATA(AppUtils.LOGIN_USER_ID, "") != null)
                                    goToApp();
                            }
                        } catch (Exception eX) {
                            eX.printStackTrace();
                        }
                    }
                } else {
                    try {
                        if (tattleUp.isUserLogin(AppUtils.IS_USER_LOGGED_IN, true)) {
                            if (tattleUp.getDATA(AppUtils.LOGIN_USER_ID, "") != "0" && Integer.valueOf(tattleUp.getDATA(AppUtils.LOGIN_USER_ID, "")) != 0 && tattleUp.getDATA(AppUtils.LOGIN_USER_ID, "") != null)
                                goToApp();
                        }
                    } catch (Exception eX) {
                        eX.printStackTrace();
                    }
                }
            } catch (NullPointerException nEx) {
                nEx.printStackTrace();
            }
        } else {
            try {
                if (tattleUp.isUserLogin(AppUtils.IS_USER_LOGGED_IN, true)) {
                    if (tattleUp.getDATA(AppUtils.LOGIN_USER_ID, "") != "0" && Integer.valueOf(tattleUp.getDATA(AppUtils.LOGIN_USER_ID, "")) != 0 && tattleUp.getDATA(AppUtils.LOGIN_USER_ID, "") != null)
                        goToApp();
                }
            } catch (Exception eX) {
                eX.printStackTrace();
            }
        }
        return rootView;

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }

    }

    private void handleSignInResult(GoogleSignInResult result) {
        Log.d(TAG, "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount userAccount = result.getSignInAccount();
            String googleuserId = userAccount.getId();
            String googleUsername = userAccount.getDisplayName();
            String googleuserEmail = userAccount.getEmail();

            setGoogleLogin(googleuserId, googleUsername, googleuserEmail);
//            String userProfilePhoto = userAccount.getPhotoUrl().toString();
//            Intent googleSignInIntent = new Intent(getActivity(), MainActivity.class);
//            googleSignInIntent.putExtra(PROFILE_USER_ID, googleuserId);
//            googleSignInIntent.putExtra(PROFILE_DISPLAY_NAME, googleUsername);
//            googleSignInIntent.putExtra(PROFILE_USER_EMAIL, googleuserEmail);
////            googleSignInIntent.putExtra(PROFILE_IMAGE_URL, userProfilePhoto);
//            startActivity(googleSignInIntent);
        }
    }
    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
    }



    //    Callback Registration
    private void fbLoginStatus() {

        ArrayList<String> list = new ArrayList<String>();
        list.add("email");
// LoginManager.getInstance().logInWithReadPermissions(this, list);
        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("email", "user_photos", "public_profile"));


        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject json, GraphResponse response) {
                        // Application code
                        if (response.getError() != null) {
                            System.out.println("ERROR");
                        } else {
                            System.out.println("Success");
                            String jsonresult = String.valueOf(json);
                            System.out.println("JSON Result" + jsonresult);

                            String fbUserId = json.optString("id");
                            String fbUserFirstName = json.optString("name");
                            String fbUserEmail = json.optString("email");
                            String fbUserProfilePics = "http://graph.facebook.com/" + fbUserId + "/picture?type=large";
                            setFacebookLogin(fbUserId, fbUserFirstName, fbUserEmail, fbUserProfilePics, "fb");
                        }
                        Log.v("FaceBook Response :", response.toString());
                    }


                });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,email,gender, birthday");
                request.setParameters(parameters);
                request.executeAsync();


            }

            @Override
            public void onCancel() {
                Toast.makeText(getActivity(), "Login Cancelled", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(getActivity(), "Login Error", Toast.LENGTH_SHORT).show();

            }
        });

    }

    private void setGoogleLogin(final String googleuserId, final String googleUsername, String googleuserEmail) {

        BasicPostAsync.OnAsyncResult onAsyncResult = new BasicPostAsync.OnAsyncResult() {
            @Override
            public String OnAsynResult(String result) {
                if (result.equals("error")) {
                    Log.e("error", "Error");
                } else {
                    try {
                        Log.e("result", result);
                        Log.e("Else error", "Error");
                        String re = result;

                        JSONObject jsonObject = new JSONObject(result);
                        Log.e("Status", String.valueOf(jsonObject.has("userId")));
                        if (jsonObject.has("userId")) {
                            JSONArray jsonArray = jsonObject.optJSONArray("userId");
                            try {

                                JSONObject jsonobj = jsonArray.getJSONObject(0);
                                String userId = jsonobj.getString("Id");
                                String userName = jsonobj.getString("UserName");
                                String userImage = jsonobj.getString("ProfileImage");


                                tattleUp.setDATA(AppUtils.LOGIN_USER_ID, userId);
                                tattleUp.setDATA(AppUtils.LOGIN_USER_NAME, userName);
                                tattleUp.setDATA(AppUtils.USER_IMAGE, userImage);

                                tattleUp.setUserLogIn(AppUtils.IS_USER_LOGGED_IN, true);

                                verifyOTPFragment();

                            } catch (NullPointerException nex) {

                            }
                        }
                    } catch (JSONException ex) {
                        // goToApp();
                        Log.e("Else error", "ErrorException");
                        ex.printStackTrace();

                    }
                }
//                    verifyMobileDialog();
                return result;
            }

        };

        String url = AppUtils.SERVICE_BASE_API + "registration?" + "socialId=" + URLEncoder.encode(googleuserId) + "&userName=" + URLEncoder.encode(googleUsername) + "&userEmail=" + URLEncoder.encode(googleuserEmail);
        BasicPostAsync basicPostAsync = new BasicPostAsync(url, onAsyncResult, getActivity(), true);
        basicPostAsync.execute(googleuserId);

    }




    private void setFacebookLogin(final String fbUserId, final String fbUserFirstName, String fbUserEmail, String fbUserProfilePics, String fb) {

        BasicPostAsync.OnAsyncResult onAsyncResult = new BasicPostAsync.OnAsyncResult() {
            @Override
            public String OnAsynResult(String result) {
                if (result.equals("error")) {
                    Log.e("error", "Error");
                } else {
                    try {
                        Log.e("result", result);
                        Log.e("Else error", "Error");
                        String re = result;

                        JSONObject jsonObject = new JSONObject(result);
                        Log.e("Status", String.valueOf(jsonObject.has("userId")));
                        if (jsonObject.has("userId")) {
                            JSONArray jsonArray = jsonObject.optJSONArray("userId");
                            try {

                                JSONObject jsonobj = jsonArray.getJSONObject(0);
                                String userId = jsonobj.getString("Id");
                                String userName = jsonobj.getString("UserName");
                                String userImage = jsonobj.getString("ProfileImage");


                                tattleUp.setDATA(AppUtils.LOGIN_USER_ID, userId);
                                tattleUp.setDATA(AppUtils.LOGIN_USER_NAME, userName);
                                tattleUp.setDATA(AppUtils.USER_IMAGE, userImage);

                                tattleUp.setUserLogIn(AppUtils.IS_USER_LOGGED_IN, true);

                                verifyOTPFragment();

                            } catch (NullPointerException nex) {

                            }
                        }
                    } catch (JSONException ex) {
                        // goToApp();
                        Log.e("Else error", "ErrorException");
                        ex.printStackTrace();

                    }
                }
//                    verifyMobileDialog();
                return result;
            }

        };

        String url = AppUtils.SERVICE_BASE_API + "registration?" + "socialId=" + URLEncoder.encode(fbUserId) + "&userName=" + URLEncoder.encode(fbUserFirstName) + "&userEmail=" + fbUserEmail;
        BasicPostAsync basicPostAsync = new BasicPostAsync(url, onAsyncResult, getActivity(), true);
        basicPostAsync.execute(fbUserId);

    }

    private void verifyOTPFragment() {
        UserOTPFragment userOTPFragment = new UserOTPFragment();
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.content_frame_login, userOTPFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    private FragmentManager getSupportFragmentManager() {
        return mFragmentManager;
    }



    private void goToApp() {
        Intent intent = new Intent(getActivity(), MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
         getActivity().finish();
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btnFbLogin:
                fbLoginStatus();
                break;

            case R.id.btnSignUp:
                signUpPage();
                break;

            case R.id.btnSignIn:
                signInPage();
                break;

            case  R.id.btnGoogleLogin:
                Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
                startActivityForResult(signInIntent, RC_SIGN_IN);
                break;

            default:
                break;
        }

    }

    private void signUpPage() {


        UserSignUpFragment userSignUpFragment = new UserSignUpFragment();
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.content_frame_login, userSignUpFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();

    }

    private void signInPage() {

        UserSignInFragment userSignInFragment = new UserSignInFragment();
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.content_frame_login, userSignInFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();

    }
    @Override
    public void onPause() {
        super.onPause();
        mGoogleApiClient.stopAutoManage(getActivity());
        mGoogleApiClient.disconnect();
    }

}









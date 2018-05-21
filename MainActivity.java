package com.tattleup.app.tattleup;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.login.LoginManager;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.squareup.picasso.Picasso;
import com.tattleup.app.tattleup.asynctask.BasicPostAsync;
import com.tattleup.app.tattleup.base.BaseActivity;
import com.tattleup.app.tattleup.base.TattleUp;
import com.tattleup.app.tattleup.fragment.DashboardFragment;
import com.tattleup.app.tattleup.fragment.SelectInterestFragment;
import com.tattleup.app.tattleup.fragment.UserProfileViewFragment;
import com.tattleup.app.tattleup.helper.GPSTracker;
import com.tattleup.app.tattleup.util.AppUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, ResultCallback<LocationSettingsResult> {

    private LinearLayout labelUserProfile;
    private TattleUp tattleUp;
    private Button footerProfile, footerNotification, footerTrending, footerFavorite, footerCreateTattle;
    private Geocoder geocoder;
    private List<Address> addresses;
    private TextView labelPageName, labelPageTitle;
    private TextView txtUserName, txtUserEmail;
    private CircleImageView imgUser;
    int REQUEST_CHECK_SETTINGS = 100;

    protected GoogleApiClient mGoogleApiClient;
    protected LocationRequest locationRequest;
    // GPSTracker class
    GPSTracker gps;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        tattleUp = (TattleUp) getApplication();


        labelPageName = (TextView) findViewById(R.id.labelPageName);
        labelPageTitle = (TextView) findViewById(R.id.labelPageTitle);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();


        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View headerLayout = navigationView.inflateHeaderView(R.layout.nav_header_main);
        labelUserProfile = (LinearLayout) headerLayout.findViewById(R.id.labelUserProfile);

        txtUserName = (TextView) headerLayout.findViewById(R.id.txtUserNameNav);
        txtUserEmail = (TextView) headerLayout.findViewById(R.id.txtUserEmailNav);
        imgUser = (CircleImageView) headerLayout.findViewById(R.id.imgUserNav);

        getUserDetails();

        footerProfile = (Button) findViewById(R.id.footerPofile);
        footerNotification = (Button) findViewById(R.id.footerNotification);
        footerTrending = (Button) findViewById(R.id.footerTrending);
        footerFavorite = (Button) findViewById(R.id.footerFavorite);
        footerCreateTattle = (Button) findViewById(R.id.footerCreateTattle);

        footerProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                UserProfileViewFragment userProfileViewFragment = null;
                userProfileViewFragment = new UserProfileViewFragment();
                FragmentManager fragmentManager1 = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction1 = fragmentManager1.beginTransaction();
                fragmentTransaction1.replace(R.id.content_frame, userProfileViewFragment);
                fragmentTransaction1.addToBackStack(null);
                fragmentTransaction1.commit();


            }
        });
        footerNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        footerCreateTattle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DashboardFragment dashboardFragment = null;
                dashboardFragment = new DashboardFragment();
                FragmentManager fragmentManager2 = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction2 = fragmentManager2.beginTransaction();
                fragmentTransaction2.replace(R.id.content_frame, dashboardFragment);
                fragmentTransaction2.addToBackStack(null);
                fragmentTransaction2.commit();

            }
        });
        footerTrending.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        footerFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });


        gps = new GPSTracker(MainActivity.this);

        // check if GPS enabled
        if (gps.canGetLocation()) {

            double latitude = gps.getLatitude();
            double longitude = gps.getLongitude();
            getLocation(latitude, longitude);
            // \n is for new line
            Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();
            // getLocation(latitude, longitude);

        } else {
            // can't get location
            // GPS or Network is not enabled
            // Ask user to enable GPS/network in settings
            mGoogleApiClient = new GoogleApiClient.Builder(this).addApi(LocationServices.API).addConnectionCallbacks(this).addOnConnectionFailedListener(this).build();
            mGoogleApiClient.connect();
            locationRequest = LocationRequest.create();
            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            locationRequest.setInterval(30 * 1000);
            locationRequest.setFastestInterval(5 * 1000);
//            gps.showSettingsAlert();

        }


    }

    private void getLocation(double latitude, double longitude) {
        geocoder = new Geocoder(this, Locale.getDefault());

        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            String address = addresses.get(0).getAddressLine(0);// If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
            String sublocality = addresses.get(0).getSubLocality();
            String city = addresses.get(0).getLocality();
            tattleUp.setDATA(AppUtils.USER_SUBLOCALITY, sublocality);
            tattleUp.setDATA(AppUtils.USER_CITY, city);
//        String state = addresses.get(0).getAdminArea();
//        String country = addresses.get(0).getCountryName();
//        String postalCode = addresses.get(0).getPostalCode();
//        String knownName = addresses.get(0).getFeatureName();
            String locality = addresses.get(0).getSubLocality() + ", " + addresses.get(0).getLocality();
            tattleUp.setDATA(AppUtils.USER_GPS_LOCATION, locality);
            if (tattleUp.getDATA(AppUtils.USER_LAST_LOCATION, "") != locality) {
                locationChangeAlert(locality);
            }


//            tattleUp.setDATA(AppUtils.USER_GPS_LOCATION, city);
        } catch (NullPointerException e) {
        } catch (IndexOutOfBoundsException ex) {
        }
    }

    public void locationChangeAlert(final String locality) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);

        // Setting Dialog Title
        alertDialog.setTitle("Locality Changed");

        // Setting Dialog Message
        alertDialog.setMessage("Would you like to update your current location.?");

        // On pressing Settings button
        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                tattleUp.setDATA(AppUtils.USER_LAST_LOCATION, locality);

                Bundle bundle = new Bundle();
                bundle.putString("locality", locality);

                DashboardFragment dashboardFragment = null;
                dashboardFragment = new DashboardFragment();
                dashboardFragment.setArguments(bundle);
                FragmentManager fragmentManager2 = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction2 = fragmentManager2.beginTransaction();
                fragmentTransaction2.replace(R.id.content_frame, dashboardFragment);
                fragmentTransaction2.addToBackStack(null);
                fragmentTransaction2.commit();
            }
        });

        // on pressing cancel button
        alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                String locality = tattleUp.getDATA(AppUtils.USER_LAST_LOCATION, "");

                Bundle bundle = new Bundle();
                bundle.putString("locality", locality);

                DashboardFragment dashboardFragment = null;
                dashboardFragment = new DashboardFragment();
                dashboardFragment.setArguments(bundle);
                FragmentManager fragmentManager2 = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction2 = fragmentManager2.beginTransaction();
                fragmentTransaction2.replace(R.id.content_frame, dashboardFragment);
                fragmentTransaction2.addToBackStack(null);
                fragmentTransaction2.commit();
                dialog.cancel();
            }
        });

        // Showing Alert Message
        alertDialog.show();
    }


    @Override
    public void onResume() {
        super.onResume();
        if (GPSTracker.isFromSetting == true) {
            finish();
            startActivity(getIntent());
            GPSTracker.isFromSetting = false;
        }
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
        if (GPSTracker.isFromSetting == true) {
            finish();
            startActivity(getIntent());
            GPSTracker.isFromSetting = false;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            DashboardFragment dashboardFragment = null;
            dashboardFragment = new DashboardFragment();
            FragmentManager fragmentManager4 = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction4 = fragmentManager4.beginTransaction();
            fragmentTransaction4.replace(R.id.content_frame, dashboardFragment);
            fragmentTransaction4.addToBackStack(null);
            fragmentTransaction4.commit();

        } else if (id == R.id.nav_profile) {
            UserProfileViewFragment userProfileViewFragment = null;
            userProfileViewFragment = new UserProfileViewFragment();
            FragmentManager fragmentManager1 = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction1 = fragmentManager1.beginTransaction();
            fragmentTransaction1.replace(R.id.content_frame, userProfileViewFragment);
            fragmentTransaction1.addToBackStack(null);
            fragmentTransaction1.commit();

        } else if (id == R.id.nav_trending) {
            DashboardFragment dashboardFragment = null;
            dashboardFragment = new DashboardFragment();
            FragmentManager fragmentManager1 = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction1 = fragmentManager1.beginTransaction();
            fragmentTransaction1.replace(R.id.content_frame, dashboardFragment);
            fragmentTransaction1.addToBackStack(null);
            fragmentTransaction1.commit();

        } else if (id == R.id.nav_favorites) {
            SelectInterestFragment selectInterestFragment = null;
            selectInterestFragment = new SelectInterestFragment();
            FragmentManager fragmentManager1 = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction1 = fragmentManager1.beginTransaction();
            fragmentTransaction1.replace(R.id.content_frame, selectInterestFragment);
            fragmentTransaction1.addToBackStack(null);
            fragmentTransaction1.commit();

        } else if (id == R.id.nav_notifications) {

        } else if (id == R.id.nav_settings) {

        } else if (id == R.id.nav_rateUs) {

        } else if (id == R.id.nav_logout) {
            tattleUp.setDATA(AppUtils.LOGIN_USER_ID, "");
            tattleUp.setDATA(AppUtils.LOGIN_USER_EMAIL, "");
            tattleUp.setUserLogIn(AppUtils.IS_USER_LOGGED_IN, false);
            finish();
            LoginManager.getInstance().logOut();
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void setPagename(String pagename) {
        labelPageName.setText(pagename);
    }


    public void setPageTitle(String pageTitle) {
        labelPageTitle.setText(pageTitle);
    }


    private void getUserDetails() {
        if (AppUtils.isNetworkAvailable(this)) {
            String userId = tattleUp.getDATA(AppUtils.LOGIN_USER_ID, "");
            if (userId != null) {
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

                                JSONObject obj = new JSONObject(result);
                                JSONArray res = obj.optJSONArray("userDetails");
                                //   JSONObject json_data=null;
                                if (res != null) {
                                    for (int i = 0; i < res.length(); i++) {
                                        try {
                                            String userName = ((JSONObject) res.get(i)).getString("FbUserName");
                                            String userEmail = (((JSONObject) res.get(i)).getString("EmailAddress"));
                                            String userImage = ("http://api.tattleup.leoinfotech.in/" + ((JSONObject) res.get(i)).getString("ProfileImage"));

                                            tattleUp.setDATA(AppUtils.LOGIN_USER_EMAIL, userEmail);
                                            tattleUp.setDATA(AppUtils.USER_IMAGE_PATH, userImage);
                                            setUserData(userName, userEmail, userImage);
                                        } catch (NullPointerException e) {

                                        }
                                    }
                                }
                            } catch (JSONException ex) {
                                // goToApp();
                                Log.e("Else error", "ErrorException");
                                ex.printStackTrace();
                            }
                        }
                        return result;
                    }

                };
                String url = AppUtils.SERVICE_BASE_API + "getProfile?profileId=" + userId;
                Log.e("URL", url);
                BasicPostAsync basicPostAsync = new BasicPostAsync(url, onAsyncResult, this, true);
                basicPostAsync.execute(userId);
            }
        } else {
            showShortToast("Cannot connect to internet");
        }
    }

    private void setUserData(String userName, String userEmail, String userImage) {
        try {
            txtUserName.setText(userName);
            txtUserEmail.setText(userEmail);
            Picasso.with(this)
                    .load(userImage)
                    .placeholder(R.drawable.user_pic)
                    .resize(400, 400)
                    .into(imgUser);

        } catch (NullPointerException e) {
        }


    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);
        builder.setAlwaysShow(true);
        PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient, builder.build());

        result.setResultCallback(this);


    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onResult(@NonNull LocationSettingsResult locationSettingsResult) {
        final Status status = locationSettingsResult.getStatus();
        switch (status.getStatusCode()) {
            case LocationSettingsStatusCodes.SUCCESS:

                // NO need to show the dialog;

                break;

            case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                //  Location settings are not satisfied. Show the user a dialog

                try {
                    // Show the dialog by calling startResolutionForResult(), and check the result
                    // in onActivityResult().

                    status.startResolutionForResult(MainActivity.this, REQUEST_CHECK_SETTINGS);

                } catch (IntentSender.SendIntentException e) {

                    //failed to show
                }
                break;

            case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                // Location settings are unavailable so not possible to show any dialog now
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CHECK_SETTINGS) {

            if (resultCode == RESULT_OK) {

                gps = new GPSTracker(MainActivity.this);
                if (gps.canGetLocation()) {
                    double latitude = gps.getLatitude();
                    double longitude = gps.getLongitude();


                    getLocation(latitude, longitude);
                    Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();

                }

                Toast.makeText(getApplicationContext(), "GPS enabled", Toast.LENGTH_LONG).show();
            } else {

                Toast.makeText(getApplicationContext(), "GPS is not enabled", Toast.LENGTH_LONG).show();
            }

        }


    }
}

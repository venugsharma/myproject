package com.tattleup.app.tattleup.fragment;


import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.tattleup.app.tattleup.R;
import com.tattleup.app.tattleup.adapter.PlaceArrayAdapter;
import com.tattleup.app.tattleup.asynctask.HttpPostAsyncTask;
import com.tattleup.app.tattleup.base.BaseFragment;
import com.tattleup.app.tattleup.base.TattleUp;
import com.tattleup.app.tattleup.helper.GPSTracker;
import com.tattleup.app.tattleup.util.AppUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import static android.app.Activity.RESULT_OK;
import static com.facebook.FacebookSdk.getApplicationContext;


/**
 * A simple {@link Fragment} subclass.
 */
public class UserAddressFragment extends BaseFragment implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks {
    private static final String LOG_TAG = "MainActivity";
    private View rootView;
    private static final int GOOGLE_API_CLIENT_ID = 1;
    private PlaceArrayAdapter mPlaceArrayAdapter;
    private static final LatLngBounds BOUNDS_MOUNTAIN_VIEW = new LatLngBounds(new LatLng(18.4801574, 73.8266477), new LatLng(18.4801574, 73.8266477));
    private List<Address> addresses;
    private TattleUp tattleUp;

    private ImageView imgDetectLocation;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest locationRequest;
    private int REQUEST_CHECK_SETTINGS = 100;
    private Geocoder geocoder;
    private GPSTracker gps;
    private AutoCompleteTextView txtuserlocality;
    private EditText txtusercity, txtUserState, txtUserPincode, txtUserStreet, txtUserSociety;
    private TextView currentaddress, detectlocation;
    private String country;
    private Button submitButton;
    Context context;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_user_address, container, false);
        super.initializeFragment(rootView);
        setHasOptionsMenu(true);
        return rootView;
    }

    @Override
    public View initializeComponents(View rootView) {
        super.initializeComponents(rootView);
        tattleUp = (TattleUp) getActivity().getApplication();
//        ((MainActivity) getActivity()).setPageTitle("");

        tattleUp = (TattleUp) getActivity().getApplication();

        mGoogleApiClient = new GoogleApiClient.Builder(getActivity()).addApi(Places.GEO_DATA_API).enableAutoManage(getActivity(), GOOGLE_API_CLIENT_ID, this).addConnectionCallbacks(this).build();

        txtuserlocality = (AutoCompleteTextView) rootView.findViewById(R.id.txtUserLocality);
        txtuserlocality.setThreshold(3);

        txtuserlocality.setOnItemClickListener(mAutocompleteClickListener);
        mPlaceArrayAdapter = new PlaceArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, BOUNDS_MOUNTAIN_VIEW, null);
        txtuserlocality.setAdapter(mPlaceArrayAdapter);


        txtusercity = (EditText) rootView.findViewById(R.id.txtUserCity);
        txtUserState = (EditText) rootView.findViewById(R.id.txtUserState);
        txtUserPincode = (EditText) rootView.findViewById(R.id.txtUserPincode);
        txtUserStreet = (EditText) rootView.findViewById(R.id.txtUserStreet);

        txtUserSociety = (EditText) rootView.findViewById(R.id.txtUserSociety);
        imgDetectLocation = (ImageView) rootView.findViewById(R.id.imgDetectLocation);
        imgDetectLocation.setOnClickListener(this);
        submitButton = (Button) rootView.findViewById(R.id.next);


        submitButton.setOnClickListener(this);
        context = getContext();


        return rootView;
    }

/*    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CHECK_SETTINGS) {

            if (resultCode == RESULT_OK) {


                Toast.makeText(getApplicationContext(), "GPS enabled", Toast.LENGTH_LONG).show();
            } else {

                Toast.makeText(getApplicationContext(), "GPS is not enabled", Toast.LENGTH_LONG).show();
            }
        }
    }*/

    private AdapterView.OnItemClickListener mAutocompleteClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            final PlaceArrayAdapter.PlaceAutocomplete item = mPlaceArrayAdapter.getItem(position);
            final String placeId = String.valueOf(item.placeId);
            Log.i(LOG_TAG, "Selected: " + item.description);
            PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi
                    .getPlaceById(mGoogleApiClient, placeId);
            placeResult.setResultCallback(mUpdatePlaceDetailsCallback);
            Log.i(LOG_TAG, "Fetching details for ID: " + item.placeId);
        }
    };

    private ResultCallback<PlaceBuffer> mUpdatePlaceDetailsCallback = new ResultCallback<PlaceBuffer>() {
        @Override
        public void onResult(PlaceBuffer places) {
            if (!places.getStatus().isSuccess()) {
                Log.e(LOG_TAG, "Place query did not complete. Error: " +
                        places.getStatus().toString());
                return;
            }

        }
    };

    @Override
    public void onConnected(Bundle bundle) {
        mPlaceArrayAdapter.setGoogleApiClient(mGoogleApiClient);
        Log.i(LOG_TAG, "Google Places API connected.");

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.e(LOG_TAG, "Google Places API connection failed with error code: " + connectionResult.getErrorCode());
        Toast.makeText(getActivity(),
                "Google Places API connection failed with error code:" +
                        connectionResult.getErrorCode(),
                Toast.LENGTH_LONG).show();
    }

    @Override
    public void onConnectionSuspended(int i) {
        mPlaceArrayAdapter.setGoogleApiClient(null);
        Log.e(LOG_TAG, "Google Places API connection suspended.");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imgDetectLocation:
                detectCurrentLocation();
                break;

            case R.id.next:
                setUserAddressDetails();
                break;

            default:
                break;
        }
    }

    private void detectCurrentLocation() {
        gps = new GPSTracker(getActivity());
        // check if GPS enabled
        if (gps.canGetLocation()) {

            double latitude = gps.getLatitude();
            double longitude = gps.getLongitude();
            getLocation(latitude, longitude);


        } else

        {

            GoogleApiClient googleApiClient = new GoogleApiClient.Builder(context)
                    .addApi(LocationServices.API).build();
            googleApiClient.connect();

            LocationRequest locationRequest = LocationRequest.create();
            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            locationRequest.setInterval(10000);
            locationRequest.setFastestInterval(10000 / 2);

            LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);
            builder.setAlwaysShow(true);
            PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build());
            result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
                @Override
                public void onResult(LocationSettingsResult result) {
                    final Status status = result.getStatus();
                    switch (status.getStatusCode()) {
                        case LocationSettingsStatusCodes.SUCCESS:

                            break;
                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:

                            try {
                                // Show the dialog by calling startResolutionForResult(), and check the result
                                // in onActivityResult().
                                status.startResolutionForResult(getActivity(), REQUEST_CHECK_SETTINGS);
                            } catch (IntentSender.SendIntentException e) {
                            }
                            break;
                        case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                            break;


                    }

                }

            });


        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CHECK_SETTINGS) {

            if (resultCode == RESULT_OK) {

                gps = new GPSTracker(getActivity());
                if(gps.canGetLocation()) {
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

    private void getLocation(double latitude, double longitude) {
        geocoder = new Geocoder(getActivity(), Locale.getDefault());

        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            String address = addresses.get(0).getAddressLine(0);// If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
            String sublocality = addresses.get(0).getSubLocality();
            String city = addresses.get(0).getLocality();
            String pincode = addresses.get(0).getPostalCode();
            String state = addresses.get(0).getAdminArea();
            String society = addresses.get(0).getSubLocality();
            country = addresses.get(0).getCountryName();
//            String street = addresses.get(0).getPremises();
            txtuserlocality.setText(sublocality);
            txtusercity.setText(city);
            txtUserState.setText(state);
            txtUserPincode.setText(pincode);


        } catch (NullPointerException e) {
        }

    }

    public void setUserAddressDetails() {

        String userId = tattleUp.getDATA(AppUtils.LOGIN_USER_ID, "");

        String userlocality = txtuserlocality.getText().toString();
        String userCity = txtusercity.getText().toString();
        String userstate = txtUserState.getText().toString();
        String userPincode = txtUserPincode.getText().toString();
        String userSociety = txtUserSociety.getText().toString();
        String userStreet = txtUserStreet.getText().toString();
        String userAddress = userStreet + ',' + userSociety;
        String userCountry = country;

        tattleUp.setDATA(AppUtils.LOGIN_USER_PINCODE, userPincode);
        tattleUp.setDATA(AppUtils.LOGIN_USER_LOCALITY, userlocality);
        tattleUp.setDATA(AppUtils.LOGIN_USER_CITY, userCity);

        JSONObject jsonObject = new JSONObject();


        try {
            jsonObject.put("address", userAddress);
            jsonObject.put("cityId", 5);
            jsonObject.put("locality", userlocality);
            jsonObject.put("city", userCity);
            jsonObject.put("userId", userId);
            jsonObject.put("countryId", userCountry);
            jsonObject.put("stateId", userstate);
            jsonObject.put("pincode", userPincode);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //  jsonObject.put( "locality",Userlocality );


        if (AppUtils.isNetworkAvailable(getActivity())) {
            if (userId != null) {
                HttpPostAsyncTask.OnAsyncResult onAsyncResult = new HttpPostAsyncTask.OnAsyncResult() {


                    @Override
                    public String OnAsynResult(String result) {

                        if (result.equals("error")) {
                            Log.e("error", "Error");
                        } else {
                            try {
                                Log.e("result", result);
                                Log.e("Else error", "Error");
                                String re = result;
                                JSONArray jsonArray = null;

                                JSONObject obj = new JSONObject(result);

                                String status = obj.getString("status");
                                JSONObject c = obj.getJSONObject("userAddress");

                                String address = c.getString("Address") + "," + c.getString("Locality") + "," + c.getString("City") + "," + c.getString("Pincode");
                                tattleUp.setDATA(AppUtils.LOGIN_USER_ADDRESS, address);

                                if (status == "true") {

                                    UserLoginProfileFragment userLoginProfileFragment = new UserLoginProfileFragment();
                                    FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                                    fragmentTransaction.replace(R.id.content_frame_login, userLoginProfileFragment);
                                    fragmentTransaction.addToBackStack(null);
                                    fragmentTransaction.commit();
                                } else {

                                    Toast.makeText(getActivity(), "Error", Toast.LENGTH_SHORT).show();

                                }
                            } catch (JSONException e) {
                                // goToApp();
                                Log.e("Else error", "ErrorException");
                                e.printStackTrace();
                            }

                        }
                        return result;
                    }


                };
                String url = AppUtils.SERVICE_BASE_API + "setUserAddress";
                Log.e("URL-send", url);
                HttpPostAsyncTask httpPostAsyncTask = new HttpPostAsyncTask(url, onAsyncResult, getActivity(), true, jsonObject);
                Log.e("httpPostAsyncTask", String.valueOf(httpPostAsyncTask));
                httpPostAsyncTask.execute(userId);
            }
        }

    }

    @Override
    public void onPause() {
        super.onPause();
        mGoogleApiClient.stopAutoManage(getActivity());
        mGoogleApiClient.disconnect();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            mGoogleApiClient.stopAutoManage(getActivity());
            mGoogleApiClient.disconnect();
        }
    }
}
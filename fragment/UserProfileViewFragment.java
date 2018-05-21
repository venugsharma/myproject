package com.tattleup.app.tattleup.fragment;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.tattleup.app.tattleup.MainActivity;
import com.tattleup.app.tattleup.R;
import com.tattleup.app.tattleup.asynctask.BasicPostAsync;
import com.tattleup.app.tattleup.base.BaseFragment;
import com.tattleup.app.tattleup.base.TattleUp;
import com.tattleup.app.tattleup.util.AppUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by admin on 10/31/2017.
 */

public class UserProfileViewFragment extends BaseFragment implements View.OnClickListener {

    private com.kosalgeek.android.photoutil.ImageLoader imageLoader;
    private TattleUp tattleUp;
    private String userChoosenTask;
    private String imageName = "";
    private Bitmap bitmap, bitmap_small, bitmap_medium;
    private Uri ImageUrl;
    private String KEY_IMAGE = "image";
    private String KEY_IMAGE_SMALL = "image_small";
    private String KEY_IMAGE_MEDIUM = "image_medium";
    private String KEY_NAME = "name";
    private String selectedPhoto;
    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    private View rootView;
    private int mYear, mMonth, mDay, mHour, mMinute, mSecond;
    private String stringDate = "";
    private CircleImageView imgUser;
    private Button btnEditProfile, btnChkLocation, btnUserTattleList;
    private TextView txtUserName, txtUserMobileNo, txtUserEmail, txtUserOccupation, txtUserDob, txtUserGender;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_view_user_profile, container, false);
        super.initializeFragment(rootView);
        setHasOptionsMenu(true);
        return rootView;
    }

    @Override
    public View initializeComponents(View rootView) {
        super.initializeComponents(rootView);
        tattleUp = (TattleUp) getActivity().getApplication();
        ((MainActivity) getActivity()).setPageTitle("");

        txtUserDob = (TextView) rootView.findViewById(R.id.txtUserDob);
        txtUserDob.setOnClickListener(this);
        imgUser = (CircleImageView) rootView.findViewById(R.id.imgUser);
        btnEditProfile = (Button) rootView.findViewById(R.id.btnEditProfile);
        btnEditProfile.setOnClickListener(this);
        txtUserName = (TextView) rootView.findViewById(R.id.txtUserName);
        txtUserMobileNo = (TextView) rootView.findViewById(R.id.txtUserMobileNo);
        txtUserEmail = (TextView) rootView.findViewById(R.id.txtUserEmail);
        btnChkLocation = (Button) rootView.findViewById(R.id.btnChkLocation);
        btnChkLocation.setOnClickListener(this);
        btnUserTattleList = (Button) rootView.findViewById(R.id.btnUserTattleList);
        btnUserTattleList.setOnClickListener(this);
        txtUserOccupation = (TextView) rootView.findViewById(R.id.txtUserOccupation);
        txtUserDob = (TextView) rootView.findViewById(R.id.txtUserDob);
        txtUserGender = (TextView) rootView.findViewById(R.id.txtUserGender);
        getUserDetails();


        return rootView;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.btnEditProfile:
                UserProfileFragment userProfileFragment = null;
                userProfileFragment = new UserProfileFragment();
                FragmentManager fragmentManager1 = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction1 = fragmentManager1.beginTransaction();
                fragmentTransaction1.replace(R.id.content_frame, userProfileFragment);
                fragmentTransaction1.addToBackStack(null);
                fragmentTransaction1.commit();
                break;

            case R.id.btnChkLocation:
                chkLocationDialog();
                break;

            case R.id.btnUserTattleList:
                UserTattleListFragment userTattleListFragment = null;
                userTattleListFragment = new UserTattleListFragment();
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.content_frame, userTattleListFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
                break;


            default:
                break;
        }
    }



    private void getUserDetails() {
        if (AppUtils.isNetworkAvailable(getActivity())) {
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

                                            String userId = ((JSONObject) res.get(i)).getString("Id");
                                            String userName = ((JSONObject) res.get(i)).getString("FbUserName");
                                            String userEmail = ((JSONObject) res.get(i)).getString("EmailAddress");
                                            String userMobile = ((JSONObject) res.get(i)).getString("ContactNumber");
                                            String userGender = ((JSONObject) res.get(i)).getString("GenderId");
                                            String userOccupation = ((JSONObject) res.get(i)).getString("Occupation");
                                            String userDob = ((JSONObject) res.get(i)).getString("Dob");
                                            String userImage = ("http://api.tattleup.leoinfotech.in/" + ((JSONObject) res.get(i)).getString("ProfileImage"));

                                            setUserData(userId, userName, userEmail, userMobile, userGender, userDob, userOccupation, userImage);
                                        }catch (NullPointerException e) {

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
                BasicPostAsync basicPostAsync = new BasicPostAsync(url, onAsyncResult, getActivity(), true);
                basicPostAsync.execute(userId);
            }
        } else {
            showShortToast("Cannot connect to internet");
        }
    }

    private void setUserData(String userId, String userName, String userEmail, String userMobile, String userGender, String userDob, String userOccupation, String userImage) {
        try {
            txtUserName.setText(userName);
            txtUserMobileNo.setText(userMobile);
            txtUserEmail.setText(userEmail);
            txtUserDob.setText(userDob);
            txtUserOccupation.setText(userOccupation);
            Picasso.with(getActivity())
                    .load(userImage)
                    .placeholder(R.drawable.user_pic)
                    .resize(400,400)
                    .into(imgUser);

            if (String.valueOf(userGender).equals("1")){
                txtUserGender.setText("Male");
            } else if (String.valueOf(userGender).equals("2")){
                txtUserGender.setText("Female");
            } else if (String.valueOf(userGender).equals("3")){
                txtUserGender.setText("Others");
            } else {
                txtUserGender.setText(R.string.not_available);
            }

        } catch (NullPointerException e) {}

    }


    public FragmentManager getSupportFragmentManager() {
        return getFragmentManager();
    }

    public void chkLocationDialog() {
        final AlertDialog.Builder addSmsTemplate = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.dialog_add_address, null);
        addSmsTemplate.setView(dialogView);
        final AlertDialog b = addSmsTemplate.create();
        b.show();

        final EditText locality = (EditText) dialogView.findViewById(R.id.txtLocality);
        final EditText city = (EditText) dialogView.findViewById(R.id.txtCity);
        final TextView yes = (TextView) dialogView.findViewById(R.id.btnDialog_ok);
        final TextView no = (TextView) dialogView.findViewById(R.id.btnDialog_cancel);
        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (locality.equals("") && city.equals("")) {
                    Toast toast = Toast.makeText(getActivity(),"Please enter locality with city.", Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                    return;
                }
                else try {
                    searchLocality(locality.getText().toString(), city.getText().toString());
                    hideSoftKeyboard(getActivity());

                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
                b.dismiss();
            }
        });
        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                b.dismiss();


            }
        });

    }

    public void locationConfirmationDialog(String sublocality, String locality) {
        final AlertDialog.Builder addSmsTemplate = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.dialog_location_welcome_msg, null);
        addSmsTemplate.setView(dialogView);
        final AlertDialog b = addSmsTemplate.create();
        b.show();

        final TextView txtlocality = (TextView) dialogView.findViewById(R.id.txtLocality);
        txtlocality.setText(sublocality + ", " + locality);
        final Button yes = (Button) dialogView.findViewById(R.id.btnResYes);
        final Button no = (Button) dialogView.findViewById(R.id.btnResNo);
        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (txtlocality.equals("")) {
                    Toast toast = Toast.makeText(getActivity(),"Please enter another address.", Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                    chkLocationDialog();
                    return;
                }
                else try {
                    DashboardFragment dashboardFragment = null;
                    dashboardFragment = new DashboardFragment();
                    FragmentManager fragmentManager1 = getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction1 = fragmentManager1.beginTransaction();
                    fragmentTransaction1.replace(R.id.content_frame, dashboardFragment);
                    fragmentTransaction1.addToBackStack(null);
                    fragmentTransaction1.commit();

                    b.dismiss();

                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
                b.dismiss();
            }
        });
        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast toast = Toast.makeText(getActivity(),"Please Search different Location.", Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
                chkLocationDialog();
                b.dismiss();


            }
        });

    }


    private void searchLocality(final String locality, String city) {
        if (AppUtils.isNetworkAvailable(getActivity())) {
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
                                String sublocality = null;
                                String locality = null;

                                JSONObject obj = new JSONObject(result);
                                JSONArray res = obj.optJSONArray("results");
                                if (res != null) {
                                    for (int i = 0; i < res.length(); i++) {
                                        JSONObject objAtIndex =  res.optJSONObject(i);
                                        if (objAtIndex != null) {
                                            JSONArray address_components = objAtIndex.optJSONArray("address_components");
                                            for (int j = 0; j < address_components.length(); j++) {

                                                String types = ((JSONArray)((JSONObject)address_components.get(j)).get("types")).getString(0);
                                                if (types.compareTo("political") == 0) {
                                                    sublocality = ((JSONObject)address_components.get(j)).getString("long_name");
                                                    Log.i("political", sublocality);

                                                }
                                                if (types.compareTo("locality") == 0) {
                                                    locality = ((JSONObject) address_components.get(j)).getString("long_name");
                                                    Log.i("locality", locality);
                                                }
                                            }
                                            locationConfirmationDialog(sublocality, locality);
                                        }
                                        else {
                                        }
                                    }


                                }
                                else {
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
                String url = "\n" + "https://maps.googleapis.com/maps/api/geocode/json?&address=" + URLEncoder.encode(locality) + "," + URLEncoder.encode(city);
                Log.e(locality,url);
                BasicPostAsync basicPostAsync = new BasicPostAsync(url, onAsyncResult, getActivity(), true);
                basicPostAsync.execute(userId, locality);
            }
        } else {
            showShortToast("Cannot connect to internet");
        }
    }
}

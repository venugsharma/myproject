package com.tattleup.app.tattleup.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.InputType;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.kosalgeek.android.photoutil.CameraPhoto;
import com.kosalgeek.android.photoutil.GalleryPhoto;
import com.squareup.picasso.Picasso;
import com.tattleup.app.tattleup.MainActivity;
import com.tattleup.app.tattleup.R;
import com.tattleup.app.tattleup.asynctask.BasicPostAsync;
import com.tattleup.app.tattleup.asynctask.JSONfunctions;
import com.tattleup.app.tattleup.base.BaseFragment;
import com.tattleup.app.tattleup.base.TattleUp;
import com.tattleup.app.tattleup.helper.ApiClientImage;
import com.tattleup.app.tattleup.interfaces.ApiInterfaceImage;
import com.tattleup.app.tattleup.model.ImageClassModel;
import com.tattleup.app.tattleup.util.AppUtils;
import com.tattleup.app.tattleup.util.Utility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by admin on 1/18/2018.
 */

public class UserLoginProfileFragment extends BaseFragment implements View.OnClickListener {
    TattleUp tattleUp;
    private CameraPhoto cameraPhoto = new CameraPhoto(getActivity());
    private com.kosalgeek.android.photoutil.ImageLoader imageLoader;
    private GalleryPhoto galleryPhoto = new GalleryPhoto(getActivity());
    private String userChoosenTask;
    private String imageTitle = "";
    private Bitmap bitmap, bitmap_small, bitmap_medium;
    private Uri ImageUrl;
    private String KEY_IMAGE = "image";
    private String KEY_IMAGE_SMALL = "image_small";
    private String KEY_IMAGE_MEDIUM = "image_medium";
    private String KEY_NAME = "name";
    private String selectedPhoto;
    private String address;
    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    private Spinner blood_Group, relationship_Status;
    private JSONObject jsonobject;
    private JSONArray jsonarray;
    private ArrayList<String> bloodGrouplist;
    private ArrayList<String> relaionshipStatus;
    private Button saveProfile;
    private TextView txt_user_name, txt_userAddress, txt_user_email;
    private String name, email, userId, occupation, Relationship_status, bloodGroup, chkGender, dob;
    private String  locality, city, pincode;
    private View rootview;
    private ImageView edit_userAddress, camera,imgUser,edit_staying,edit_company;
    private TextView  user_occupation,txt_editcompany;
    private String Address;
    private String key,ucity;
    Editable editObj;
    LinearLayout ll;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootview = inflater.inflate(R.layout.fragment_user_login_profile, container, false);
        super.initializeFragment(rootview);
        setHasOptionsMenu(true);

//
//        String mBundle = new String();
//        mBundle = getArguments().getString("key");
////        mBundle.getString("key");
//
//       ucity=mBundle.getString("key");


        getUserDetails();

        return rootview;
    }

    @Override
    public View initializeComponents(View rootView) {
        super.initializeComponents(rootView);
        tattleUp = (TattleUp) getActivity().getApplication();
        saveProfile = (Button) rootview.findViewById(R.id.btnSaveProfileDetail);
        camera = (ImageView) rootview.findViewById(R.id.camera);
        txt_user_name = (TextView) rootview.findViewById(R.id.user_profile_name);
        txt_userAddress = (TextView) rootview.findViewById(R.id.userAddress);
        user_occupation = (TextView) rootview.findViewById(R.id.user_occupation);
        ll=(LinearLayout) rootview.findViewById(R.id.ll);

        txt_editcompany=(TextView)rootview.findViewById(R.id.txt_editcompany);


        txt_user_email = (TextView) rootview.findViewById(R.id.user_profile_short_bio);
        imgUser = (de.hdodenhof.circleimageview.CircleImageView) rootview.findViewById(R.id.user_profile_photo);

        edit_userAddress = (ImageView) rootview.findViewById(R.id.edit_userAddress);
        edit_staying=(ImageView)rootview.findViewById(R.id.staying_edit);

        edit_company=(ImageView)rootview.findViewById(R.id.edit_company);
        relationship_Status = (Spinner) rootview.findViewById(R.id.spinnerstatus);
        blood_Group = (Spinner) rootview.findViewById(R.id.spinnerboodgroup);





        userId = tattleUp.getDATA(AppUtils.LOGIN_USER_ID, "");
        email = tattleUp.getDATA(AppUtils.LOGIN_USER_EMAIL, "");
        txt_user_email.setText(email);
        dob = tattleUp.getDATA(AppUtils.LOGIN_USER_DOB, "");
        chkGender = tattleUp.getDATA(AppUtils.LOGIN_USER_GENDER, "");
        occupation = user_occupation.getText().toString();
        name = tattleUp.getDATA(AppUtils.LOGIN_USER_NAME, "");
        txt_user_name.setText(name);
        address = tattleUp.getDATA(AppUtils.LOGIN_USER_ADDRESS, "");
        pincode = tattleUp.getDATA(AppUtils.LOGIN_USER_PINCODE, "");

        locality = tattleUp.getDATA(AppUtils.LOGIN_USER_LOCALITY, "");
        txt_user_name.setText(name);
        city = tattleUp.getDATA(AppUtils.LOGIN_USER_CITY, "");

        new BloodGroup().execute();
        new RelationshipStatus().execute();
        saveProfile.setOnClickListener(this);

        camera.setOnClickListener(this);
        edit_userAddress.setOnClickListener(this);
        edit_staying.setOnClickListener(this);
        edit_company.setOnClickListener(this);

        return rootview;
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btnSaveProfileDetail:
                setProfileDetails();
                break;


            case R.id.camera:
                selectImage();
                break;

            case R.id.staying_edit:
                user_occupation.setCursorVisible(true);
                user_occupation.setFocusableInTouchMode(true);
                user_occupation.setInputType(InputType.TYPE_CLASS_TEXT);
                user_occupation.requestFocus();
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(user_occupation, InputMethodManager.SHOW_IMPLICIT);
                break;


            case R.id.edit_company:
                txt_editcompany.setCursorVisible(true);
                txt_editcompany.setFocusableInTouchMode(true);
                txt_editcompany.setInputType(InputType.TYPE_CLASS_TEXT);

                txt_editcompany.requestFocus();


                InputMethodManager im = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                im.showSoftInput(txt_editcompany, InputMethodManager.SHOW_IMPLICIT);

                //               ll.setBackgroundColor(Color.parseColor("#01ff90"));


                break;


            case R.id.edit_userAddress:
                UserAddressFragment userAddressFragment = new UserAddressFragment();
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.content_frame_login, userAddressFragment);
                fragmentTransaction.addToBackStack(null);

                Bundle bundle = new Bundle();
                bundle.putString(key,ucity);
                userAddressFragment.setArguments(bundle);
                fragmentTransaction.commit();
                break;


            default:
                break;
        }


    }


    private void goToApp() {
        Intent intent = new Intent(getActivity(), MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        getActivity().finish();
    }


    private void selectImage() {
        final CharSequence[] items = {"Take Photo", "Choose from Library",
                "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result = Utility.checkPermission(getActivity());

                if (items[item].equals("Take Photo")) {
                    userChoosenTask = "Take Photo";
                    if (result)
                        try {
                            cameraIntent();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                } else if (items[item].equals("Choose from Library")) {
                    userChoosenTask = "Choose from Library";
                    if (result)
                        galleryIntent();

                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void galleryIntent() {

        galleryPhoto = new GalleryPhoto(getContext());
        Intent in = galleryPhoto.openGalleryIntent();
        startActivityForResult(in, SELECT_FILE);
    }

    private void cameraIntent() throws IOException {
        cameraPhoto = new CameraPhoto(getContext());
        Intent intent = cameraPhoto.takePhotoIntent();
        startActivityForResult(intent, REQUEST_CAMERA);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE) {
                Uri filePath = data.getData();
                galleryPhoto.setPhotoUri(filePath);
                String photoPath = galleryPhoto.getPath();
                selectedPhoto = photoPath;
                ImageUrl = filePath;
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), filePath);
                    imgUser.setImageBitmap(bitmap.createScaledBitmap(bitmap, 300, 300, false));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else if (requestCode == REQUEST_CAMERA) {
                String photoPath = cameraPhoto.getPhotoPath();
                selectedPhoto = photoPath;
                Uri tempUri = Uri.parse(photoPath);
                ImageUrl = tempUri;
                try {
                    bitmap = imageLoader.init().from(photoPath).requestSize(512, 512).getBitmap();
                    imgUser.setImageBitmap(bitmap.createScaledBitmap(bitmap, 300, 300, false));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private String imageToString() {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {

            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        } catch (NullPointerException e) {
        }

        byte[] imgByte = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(imgByte, Base64.DEFAULT);
    }

    private void uploadImage() {
        String Image = imageToString();

        if (!String.valueOf(Image).equals("")) {
            imageTitle = System.currentTimeMillis() + ".jpeg";
        } else {
            imageTitle = "";
        }

        ApiInterfaceImage apiInterface = ApiClientImage.getApiClient().create(ApiInterfaceImage.class);
        Call<ImageClassModel> call = apiInterface.uploadImage(imageTitle, Image);

        call.enqueue(new Callback<ImageClassModel>() {
            @Override
            public void onResponse(Call<ImageClassModel> call, Response<ImageClassModel> response) {

                ImageClassModel imageClass = response.body();
                Toast.makeText(getContext(), "Server Response: " + imageClass.getResponse(), Toast.LENGTH_SHORT);

            }

            @Override
            public void onFailure(Call<ImageClassModel> call, Throwable t) {

            }
        });

    }


    // Download JSON file AsyncTask
    private class BloodGroup extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {

            bloodGrouplist = new ArrayList<String>();
            jsonobject = JSONfunctions.getJSONfromURL("http://api.tattleup.leoinfotech.in/index.php/api/version1/getBloodGroupList");

            try {
                // Locate the NodeList name
                jsonarray = jsonobject.getJSONArray("All_Relationship_Status_LIst");
                for (int i = 0; i < jsonarray.length(); i++) {
                    jsonobject = jsonarray.getJSONObject(i);
                    bloodGrouplist.add(jsonobject.optString("bloodgroupType"));

                }
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void args) {
            // Locate the spinner in activity_main.xml

            // Spinner adapter
            blood_Group.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, bloodGrouplist));

            // Spinner on item click listener
            blood_Group.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                @Override
                public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long arg3) {
                    // TODO Auto-generated method stub


                    bloodGroup = blood_Group.getItemAtPosition(blood_Group.getSelectedItemPosition()).toString();

                }

                @Override
                public void onNothingSelected(AdapterView<?> arg0) {
                    // TODO Auto-generated method stub
                }
            });
        }
    }


    // Download JSON file AsyncTask
    private class RelationshipStatus extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {

            relaionshipStatus = new ArrayList<String>();
            jsonobject = JSONfunctions.getJSONfromURL("http://api.tattleup.leoinfotech.in/index.php/api/version1/getReltionshipStatus");

            try {
                // Locate the NodeList name
                jsonarray = jsonobject.getJSONArray("All_Relationship_Status_LIst");
                for (int i = 0; i < jsonarray.length(); i++) {
                    jsonobject = jsonarray.getJSONObject(i);
                    relaionshipStatus.add(jsonobject.optString("Status"));

                }
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void args) {

            // Spinner adapter
            relationship_Status.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, relaionshipStatus));

            // Spinner on item click listener
            relationship_Status.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                @Override
                public void onItemSelected(AdapterView<?> arg0,
                                           View arg1, int position, long arg3) {
                    // TODO Auto-generated method stub


                    Relationship_status = relationship_Status.getItemAtPosition(relationship_Status.getSelectedItemPosition()).toString();

                }

                @Override
                public void onNothingSelected(AdapterView<?> arg0) {
                    // TODO Auto-generated method stub
                }
            });
        }
    }

    private void setProfileDetails() {


        String subUrl = "&userId=" + userId + "&profileName=" + URLEncoder.encode(name) + "&contactNo=" +
                "&email=" + URLEncoder.encode(email) + "&dob=" + URLEncoder.encode(dob) + "&gender=" +
                URLEncoder.encode(chkGender) + "&occupation=" + URLEncoder.encode(occupation) + "&favCategoriesId=" +
                "&address1=" + URLEncoder.encode(locality) + "&city1=" + URLEncoder.encode(city) + "&imageName=" +
                URLEncoder.encode(imageTitle) + "relationshipId=" + URLEncoder.encode(Relationship_status) + "bloodGrpId=" +
                URLEncoder.encode(bloodGroup) + "&state1=" + "&pincode1=" + URLEncoder.encode(pincode);

        Log.e(subUrl, "setImageDetails");
        BasicPostAsync.OnAsyncResult onAsyncResult = new BasicPostAsync.OnAsyncResult() {
            @Override
            public String OnAsynResult(String result) {
                if (result.equals("error")) {
                    Log.e("error", "SetProfileError");
                } else {
                    try {
                        JSONObject obj = new JSONObject(result);
                        String status = obj.getString("status");

                        if (status == "true") {
                            uploadImage();

                            goToApp();
                            Toast.makeText(getActivity(), "Profile Saved", Toast.LENGTH_SHORT).show();

                        } else {
                            Toast.makeText(getActivity(), "Connection error", Toast.LENGTH_SHORT).show();
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



        String url = AppUtils.SERVICE_BASE_API + "setProfile?" + subUrl;
        BasicPostAsync basicPostAsync = new BasicPostAsync(url, onAsyncResult, getActivity(), true);
        basicPostAsync.execute(userId);
    }

/*    private void setUserData(String userId, String userEmail, String userMobile, String userGender, String userDob, String userOccupation, String userImage) {
        try {

       //     imgUser.setim

        } catch (NullPointerException e) {}


    }*/

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
                                if (res != null) {
                                    for (int i = 0; i < res.length(); i++) {
                                        try {


                                            Address = ((JSONObject) res.get(i)).getString("Address");

                                            txt_userAddress.setText(Address);

/*
                                            String userId = ((JSONObject) res.get(i)).getString("Id");
                                     //       String userName = ((JSONObject) res.get(i)).getString("FbUserName");
                                            String userEmail = ((JSONObject) res.get(i)).getString("EmailAddress");
                                            String userMobile = ((JSONObject) res.get(i)).getString("ContactNumber");
                                            String userGender = ((JSONObject) res.get(i)).getString("GenderId");
                                            String userOccupation = ((JSONObject) res.get(i)).getString("Occupation");
                                            String userDob = ((JSONObject) res.get(i)).getString("Dob");*/


                                            String userImage = ("http://api.tattleup.leoinfotech.in/" + ((JSONObject) res.get(i)).getString("ProfileImage"));

                                            Picasso.with(getActivity()).load(userImage).resize(300,300).into(imgUser);

                                            //      setUserData(userId, userEmail, userMobile, userGender, userDob, userOccupation, userImage);
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

}


package com.tattleup.app.tattleup.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.kosalgeek.android.photoutil.CameraPhoto;
import com.kosalgeek.android.photoutil.GalleryPhoto;
import com.squareup.picasso.Picasso;
import com.tattleup.app.tattleup.MainActivity;
import com.tattleup.app.tattleup.R;
import com.tattleup.app.tattleup.asynctask.BasicPostAsync;
import com.tattleup.app.tattleup.base.BaseFragment;
import com.tattleup.app.tattleup.base.TattleUp;
import com.tattleup.app.tattleup.helper.ApiClientImage;
import com.tattleup.app.tattleup.helper.DatePickerFragment;
import com.tattleup.app.tattleup.interfaces.ApiInterfaceImage;
import com.tattleup.app.tattleup.model.ImageClassModel;
import com.tattleup.app.tattleup.util.AppUtils;
import com.tattleup.app.tattleup.util.Utility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import id.zelory.compressor.Compressor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by admin on 9/16/2017.
 */

public class UserProfileFragment extends BaseFragment implements View.OnClickListener {


    private com.kosalgeek.android.photoutil.ImageLoader imageLoader;
    private TattleUp tattleUp;
    private CameraPhoto cameraPhoto = new CameraPhoto(getActivity());
    private GalleryPhoto galleryPhoto = new GalleryPhoto(getActivity());
    private String userChoosenTask;
    private String imageTitle = "";
    private Bitmap bitmap, bitmap_small, bitmap_medium;
    private String UPLOAD_URL = "http://mobileadminapi.shopincity.com/uploadImage.php";
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
    private EditText txtUserDob;
    private de.hdodenhof.circleimageview.CircleImageView imgUser;
    private TextView btnSelectPhoto;
    private Button btnSaveProfile;
    private EditText txtUserName, txtUserMobileNo, txtUserEmail, txtUserAddress1, txtUserCity1, txtUserLocality1,
            txtRsdYear, txtUserOccupation;
    private RadioButton radioMale, radioFemale, radioOthers;

    private File compressedImage;
    private String photoPath;
    private File Imagepath;
    private String encodedImage;
    private  String CompressedImg;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_user_profile, container, false);
        super.initializeFragment(rootView);
        setHasOptionsMenu(true);
        return rootView;
    }

    @Override
    public View initializeComponents(View rootView) {
        super.initializeComponents(rootView);
        tattleUp = (TattleUp) getActivity().getApplication();
        ((MainActivity) getActivity()).setPageTitle("");

        txtUserDob = (EditText) rootView.findViewById(R.id.txtUserDob);
        txtUserDob.setOnClickListener(this);
        btnSelectPhoto = (TextView) rootView.findViewById(R.id.btnSelectPhoto);
        btnSelectPhoto.setOnClickListener(this);
        imgUser = (de.hdodenhof.circleimageview.CircleImageView) rootView.findViewById(R.id.imgUser);
        btnSaveProfile = (Button) rootView.findViewById(R.id.btnSaveProfile);
        btnSaveProfile.setOnClickListener(this);
        txtUserName = (EditText) rootView.findViewById(R.id.txtUserName);
        txtUserMobileNo = (EditText) rootView.findViewById(R.id.txtUserMobileNo);
        txtUserEmail = (EditText) rootView.findViewById(R.id.txtUserEmail);
        radioMale = (RadioButton) rootView.findViewById(R.id.radioMale);
        radioFemale = (RadioButton) rootView.findViewById(R.id.radioFemale);
        radioOthers = (RadioButton) rootView.findViewById(R.id.radioOthers);
        txtUserAddress1 = (EditText) rootView.findViewById(R.id.txtUserAddress1);
        txtUserCity1 = (EditText) rootView.findViewById(R.id.txtUserCity1);
        txtUserLocality1 = (EditText) rootView.findViewById(R.id.txtUserLocality1);
        txtRsdYear = (EditText) rootView.findViewById(R.id.txtResdYear1);
        txtUserOccupation = (EditText) rootView.findViewById(R.id.txtUserOccupation);
        getUserDetails();


        return rootView;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.txtUserDob:
                DatePickerFragment datePickerFragment1 = new DatePickerFragment();
                String[] birthDate = txtUserDob.getText().toString().split("-");
                Integer currentyear = 1985;
                Integer month = 0;
                Integer day = 1;
                try {
                    if (birthDate[0] != "0000") {
                        currentyear = Integer.parseInt(birthDate[0]);
                        month = Integer.parseInt(birthDate[1]) - 1;
                        day = Integer.parseInt(birthDate[2]);
                    }
                } catch (NumberFormatException nFEx) {
                }
                datePickerFragment1.setDateSetListener(new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                        Calendar calendar = Calendar.getInstance();
                        calendar.set(Calendar.YEAR, year);
                        calendar.set(Calendar.MONTH, monthOfYear);
                        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        Log.e("date", year + " " + monthOfYear + " " + dayOfMonth);
                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                        txtUserDob.setText(dateFormat.format(calendar.getTime()));
                    }
                });

                Bundle bundle1 = new Bundle();
                bundle1.putInt("year", currentyear);
                bundle1.putInt("month", month);
                bundle1.putInt("day", day);
                datePickerFragment1.setArguments(bundle1);
                datePickerFragment1.setMaxDate();
                datePickerFragment1.show(getChildFragmentManager(), "datepicker");
                break;

            case R.id.btnSelectPhoto:
                selectImage();
                break;

            case R.id.btnSaveProfile:
                customCompressImage();
                setProfileDetails();

                break;


            default:
                break;
        }
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

                Imagepath = new File(galleryPhoto.getPath());

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
                Imagepath = new File(cameraPhoto.getPhotoPath());

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
        compressedImage = new File(Base64.encodeToString(imgByte, Base64.DEFAULT));
         CompressedImg= String.valueOf(compressedImage);
       return Base64.encodeToString(imgByte, Base64.DEFAULT);

     //  return encodedImage;
    }

    private void uploadImage() {
        String Image = imageToString();

        if (!String.valueOf(Image).equals("")) {
            imageTitle = System.currentTimeMillis() + ".jpeg";
        } else {
            imageTitle = "";
        }

        ApiInterfaceImage apiInterface = ApiClientImage.getApiClient().create(ApiInterfaceImage.class);
        Call<ImageClassModel> call = apiInterface.uploadImage(imageTitle, CompressedImg);

        call.enqueue(new Callback<ImageClassModel>() {
            @Override
            public void onResponse(Call<ImageClassModel> call, Response<ImageClassModel> response) {

                ImageClassModel imageClass = response.body();
           //     Toast.makeText(getActivity(), "Server Response: " + imageClass.getResponse(), Toast.LENGTH_SHORT);
              //  setProfileDetails();

            }

            @Override
            public void onFailure(Call<ImageClassModel> call, Throwable t) {

            }
        });

    }


    private void setProfileDetails() {

        String userId = tattleUp.getDATA(AppUtils.LOGIN_USER_ID, "");
        String name = txtUserName.getText().toString().trim();
        String mobile = txtUserMobileNo.getText().toString().trim();
        String email = txtUserEmail.getText().toString().trim();
        String dob = txtUserDob.getText().toString().trim();
        String occupation = txtUserOccupation.getText().toString().trim();
        String address = txtUserAddress1.getText().toString().trim();
        String locality = txtUserLocality1.getText().toString().trim();
        String city = txtUserCity1.getText().toString().trim();
        String resSince = txtRsdYear.getText().toString().trim();


        String chkGender = "";
        if (radioMale.isChecked()) {
            chkGender = "1";
        } else if (radioFemale.isChecked()) {
            chkGender = "2";
        } else if (radioOthers.isChecked()) {
            chkGender = "3";
        }

        String subUrl = "&userId=" + userId + "&profileName=" + URLEncoder.encode(name) + "&contactNo=" + URLEncoder.encode(mobile) +
                "&email=" + URLEncoder.encode(email) + "&dob=" + URLEncoder.encode(dob) + "&gender=" + URLEncoder.encode(chkGender) +
                "&occupation=" + URLEncoder.encode(occupation) + "&favCategoriesId=" + "&address1=" + URLEncoder.encode(locality) +
                "&city1=" + URLEncoder.encode(city) + "&imageName=" + URLEncoder.encode(imageTitle) + "&state1=" + "&pincode1=";

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
                            JSONArray jsonArray = null;
                            JSONObject obj = new JSONObject(result);
                            JSONArray res = obj.optJSONArray("userId");
                            if (res != null) {
                                try {

                                    UserProfileViewFragment userProfileViewFragment = null;
                                    userProfileViewFragment = new UserProfileViewFragment();
                                    FragmentManager fragmentManager1 = getSupportFragmentManager();
                                    FragmentTransaction fragmentTransaction1 = fragmentManager1.beginTransaction();
                                    fragmentTransaction1.replace(R.id.content_frame, userProfileViewFragment);
                                    fragmentTransaction1.addToBackStack(null);
                                    fragmentTransaction1.commit();

                                } catch (NullPointerException nex) {
                                }
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
//                    .placeholder(R.drawable.placeholder)
                    .resize(400, 400)
                    .into(imgUser);


            if (String.valueOf(userGender).equals("1")) {
                radioMale.setChecked(true);
            } else if (String.valueOf(userGender).equals("2")) {
                radioFemale.setChecked(true);
            } else if (String.valueOf(userGender).equals("3")) {
                radioOthers.setChecked(true);
            } else {
//                txtUserGender.setText(R.string.not_available);
            }
        } catch (NullPointerException e) {
        }


    }

    public FragmentManager getSupportFragmentManager() {
        return getFragmentManager();
    }

    public void customCompressImage() {

        if (Imagepath == null) {
        } else {
            try {
                compressedImage = new Compressor(getActivity())
                        .setMaxWidth(1280)
                        .setMaxHeight(720)
                        .setQuality(80)
                        .setCompressFormat(Bitmap.CompressFormat.JPEG)
                        .setDestinationDirectoryPath(Environment.getExternalStoragePublicDirectory(
                                Environment.DIRECTORY_PICTURES).getAbsolutePath())
                        .compressToFile(Imagepath, photoPath);

                uploadImage();

            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

}


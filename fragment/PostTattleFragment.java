package com.tattleup.app.tattleup.fragment;

import android.app.Activity;
import android.app.AlertDialog;
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
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kosalgeek.android.photoutil.CameraPhoto;
import com.kosalgeek.android.photoutil.GalleryPhoto;
import com.tattleup.app.tattleup.MainActivity;
import com.tattleup.app.tattleup.R;
import com.tattleup.app.tattleup.adapter.SubCategoryListButtonsAdapter;
import com.tattleup.app.tattleup.asynctask.BasicPostAsync;
import com.tattleup.app.tattleup.asynctask.HttpPostAsyncTask;
import com.tattleup.app.tattleup.base.BaseFragment;
import com.tattleup.app.tattleup.base.TattleUp;
import com.tattleup.app.tattleup.helper.ApiClientTattleImage;
import com.tattleup.app.tattleup.holder.SubCategoryListPostTattleHolder;
import com.tattleup.app.tattleup.interfaces.ApiInterfaceImage;
import com.tattleup.app.tattleup.model.ImageClassModel;
import com.tattleup.app.tattleup.model.SubCategoryListModel;
import com.tattleup.app.tattleup.util.AppUtils;
import com.tattleup.app.tattleup.util.Utility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import id.zelory.compressor.Compressor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by admin on 12/16/2017.
 */

public class PostTattleFragment extends BaseFragment implements View.OnClickListener, SubCategoryListPostTattleHolder.ItemClickListener{

    private View rootView;
    private Boolean bundelArgs = false;
    private Spinner spinnerCategory, spinnerSubCategory;
    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
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
    private Button btnSaveTattle, btnSelectPhoto, btnCancel;
    private EditText txtTattleTittle, txtTattleDescription;
    private CheckBox chkAnonymous;
    private TattleUp tattleUp;
    private String categoryId="", categoryName="";
    private com.kosalgeek.android.photoutil.ImageLoader imageLoader;
    private ImageView imgTattleImage;
    private CircleImageView imgUser;

    private ArrayList<SubCategoryListModel> listofSubCategory;
    private List<SubCategoryListModel> SubCategoryList = new ArrayList<SubCategoryListModel>();
    private SubCategoryListButtonsAdapter subCategoryListButtonsAdapter;
    private RecyclerView subCategoryList;
    private ArrayList<String> checkedSubCategory;

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
        rootView = inflater.inflate(R.layout.fragment_post_tattle, container, false);
        super.initializeFragment(rootView);
        setHasOptionsMenu(true);
        return rootView;
    }

    @Override
    public View initializeComponents(View rootView) {
        super.initializeComponents(rootView);
        tattleUp = (TattleUp) getActivity().getApplication();
        ((MainActivity) getActivity()).setPageTitle("");

        chkAnonymous = (CheckBox) rootView.findViewById(R.id.chkAnonymous);
        chkAnonymous.setOnClickListener(this);
        btnSelectPhoto = (Button) rootView.findViewById(R.id.btnSelectPhoto);
        btnSelectPhoto.setOnClickListener(this);
        btnCancel = (Button) rootView.findViewById(R.id.btnCancel);
        btnCancel.setOnClickListener(this);


        txtTattleTittle = (EditText) rootView.findViewById(R.id.txtTattleTitle);
        txtTattleDescription = (EditText) rootView.findViewById(R.id.txtTattleDescription);

        btnSaveTattle = (Button) rootView.findViewById(R.id.btnSaveTattle);
        btnSaveTattle.setOnClickListener(this);

        imgTattleImage = (ImageView) rootView.findViewById(R.id.imgTattleImage);
        imgUser = (CircleImageView)rootView.findViewById(R.id.imgUser);
        String userImagePath = tattleUp.getDATA(AppUtils.USER_IMAGE_PATH,"");
//        Picasso.with(getActivity())
//                .load(userImagePath)
//                .placeholder(R.drawable.user_pic)
//                .resize(200,200)
//                .into(imgUser);


        Bundle bundle = getArguments();
        try {
            categoryId = bundle.getString("CategoryId");
            categoryName = bundle.getString("CategoryName");
            ((MainActivity) getActivity()).setPageTitle(categoryName);
        } catch ( NullPointerException e) {}


        subCategoryList = (RecyclerView) rootView.findViewById(R.id.listSubCategoryRecycler);

        subCategoryList.setHasFixedSize(true);
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL);
        subCategoryList.setLayoutManager(staggeredGridLayoutManager);
        subCategoryList.setItemAnimator(new DefaultItemAnimator());


        listofSubCategory = new ArrayList<>();
        subCategoryListButtonsAdapter = new SubCategoryListButtonsAdapter(getContext(), listofSubCategory, this);
        subCategoryList.setAdapter(subCategoryListButtonsAdapter);
        getSubCategories(categoryId);




        return rootView;
    }

    @Override
    public void bindEvents() {
        super.bindEvents();

    }


    @Override
    public void onClick(View v) {

        // TODO Auto-generated method stub
        switch (v.getId()) {

            case R.id.btnSelectPhoto:
                selectImage();
                break;

            case R.id.btnSaveTattle:
              //  uploadImage();
                customCompressImage();
                getCheckedSubCategory();
                try {
                    saveTattleDetails();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;

            case R.id.btnCancel:
                cancelPost();
                break;

            default:
                break;
        }
    }

    private   void getCheckedSubCategory() {
        ArrayList<String> mArrayProductsId = new ArrayList<>();
        for (int i = 0; i < listofSubCategory.size(); i++) {
            View listView = subCategoryList.getChildAt(i);
            try {
                CheckBox checkBox = (CheckBox) listView.findViewById(R.id.chkSubCategory);
                if (checkBox.isChecked()) {
                    try {
                        mArrayProductsId.add(listofSubCategory.get(i).getId());
                    } catch (Exception e) {
                        e.getMessage();
                    }
                }
            } catch (NullPointerException e) {

            }
            //  mArrayProducts.add(subCategoryList.getChildAt(i));
        }
        checkedSubCategory = mArrayProductsId;
        Toast.makeText(getApplicationContext(), "Selected Items: " + mArrayProductsId.toString(), Toast.LENGTH_LONG).show();

    }

    private void cancelPost() {
        String catId = categoryId;
        String catName = categoryName;
        Bundle bundle = new Bundle();
        bundle.putString("CategoryId", catId);
        bundle.putString("CategoryName", catName);


        TattleListFragment fragment = new TattleListFragment();
        fragment.setArguments(bundle);
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.content_frame, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
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
                    bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(),filePath);
//                    bitmap = imageLoader.init().from(photoPath).requestSize(312, 312).getBitmap();
                    imgTattleImage.setImageBitmap(bitmap.createScaledBitmap(bitmap, 300, 300, false));
                    imgTattleImage.setVisibility(View.VISIBLE);
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
                    imgTattleImage.setImageBitmap(bitmap.createScaledBitmap(bitmap, 300, 300, false));
                    imgTattleImage.setVisibility(View.VISIBLE);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private String imageToString(){
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {

            bitmap.compress(Bitmap.CompressFormat.JPEG,100, byteArrayOutputStream);
        } catch (NullPointerException e) {}

        byte[] imgByte = byteArrayOutputStream.toByteArray();

      //  compressedImage = new File(Base64.encodeToString(imgByte, Base64.DEFAULT));
     //   CompressedImg= String.valueOf(compressedImage);
        //return CompressedImg;
        return Base64.encodeToString(imgByte,Base64.DEFAULT);
    }

    private void uploadImage(){
       // String Image = imageToString();
        String Image = encodedImage;


        if (!String.valueOf(Image).equals("")) {
            imageTitle = System.currentTimeMillis() + ".jpeg";
        } else {
            imageTitle = "";
        }

        ApiInterfaceImage apiInterface = ApiClientTattleImage.getApiClient().create(ApiInterfaceImage.class);
        Call<ImageClassModel> call = apiInterface.uploadImage(imageTitle, encodedImage);
        Log.e("Image Name",imageTitle);

        call.enqueue(new Callback<ImageClassModel>() {
            @Override
            public void onResponse(Call<ImageClassModel> call, Response<ImageClassModel> response) {

                ImageClassModel imageClass = response.body();
//                Toast.makeText(getActivity(), "Server Response: "+ imageClass.getResponse(),Toast.LENGTH_SHORT);

            }

            @Override
            public void onFailure(Call<ImageClassModel> call, Throwable t) {

            }
        });

    }

    public void saveTattleDetails() throws JSONException {

        String userId = tattleUp.getDATA( AppUtils.LOGIN_USER_ID,"" );
        String locality = tattleUp.getDATA(AppUtils.USER_SUBLOCALITY, "");
        String city = tattleUp.getDATA(AppUtils.USER_CITY, "");

        String anonymous = "0";
        if(chkAnonymous.isChecked()) {
            anonymous = "1";
        }


        Bundle bundle = getArguments();
//        String subCategories[] = bundle.getStringArrayList("SubCategoryIds");
        ArrayList<String> subCategories = (ArrayList<String>)getArguments().getSerializable("SubCategoryIds");
        String catId = bundle.getString("CategoryId");

        String title = txtTattleTittle.getText().toString().trim();
        String description = txtTattleDescription.getText().toString().trim();

        JSONObject jsonObject = new JSONObject( );

        jsonObject.put( "postdata", new JSONArray(checkedSubCategory));
        jsonObject.put( "title",title );
        jsonObject.put( "description",description );
        jsonObject.put( "categoryId",catId );
        jsonObject.put( "userId",userId );
        jsonObject.put( "anonymousStatus",anonymous );
        jsonObject.put( "locality",locality );
        jsonObject.put( "city",city );
        jsonObject.put( "imageName", imageTitle );

        if (AppUtils.isNetworkAvailable(getActivity())) {
            if ( userId!= null) {
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
                                JSONObject jsonObject = new JSONObject(result);
                                Log.e("Status", String.valueOf(jsonObject.has("message")));
                                if (jsonObject.has("message")) {
                                    showShortToast("Saved Successfully");


                                    TattleListFragment.updateListView();

                                    String catId = categoryId;

                                    Bundle bundle = new Bundle();
                                    bundle.putString("CategoryId", catId);


                                    TattleListFragment fragment = new TattleListFragment();
                                    fragment.setArguments(bundle);
                                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                                    android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                    fragmentTransaction.replace(R.id.content_frame, fragment);
                                    fragmentTransaction.addToBackStack(null);
                                    fragmentTransaction.commit();

                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }


                        }
                        return result;
                    }

                };
                String url = AppUtils.SERVICE_BASE_API + "createPostTattle";
                Log.e("URL-send", url);
                HttpPostAsyncTask httpPostAsyncTask = new HttpPostAsyncTask(url, onAsyncResult, getActivity(), true,jsonObject);
                Log.e( "httpPostAsyncTask", String.valueOf( httpPostAsyncTask ) );
                httpPostAsyncTask.execute(userId);
            }
        } else {
            //showShortToast("Cannot connect to internet");
        }

    }

    public FragmentManager getSupportFragmentManager() {
        return getFragmentManager();
    }

    @Override
    public void onItemClick(View v, int pos, String hint, SubCategoryListModel subCategoryListModel) {

    }

    private void getSubCategories(String categoryId) {
        if (AppUtils.isNetworkAvailable(getActivity())) {
//            String userId = tattleUp.getDATA(AppUtils.LOGIN_USER_ID, "");
//            if (userId != null) {
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
                            Log.e("Status", String.valueOf(jsonObject.has("SubcategoriesList")));
                            if (jsonObject.has("SubcategoriesList")) {
                                //Log.e("finalResult", status);
                                JSONArray jsonArray = jsonObject.getJSONArray("SubcategoriesList");
                                Type typedValue = new TypeToken<ArrayList<SubCategoryListModel>>() {
                                }.getType();
                                listofSubCategory = new Gson().fromJson(jsonArray.toString(), typedValue);
                                for (int i = 0; i < listofSubCategory.size(); i++) {
                                    System.out.println(listofSubCategory.get(i));
                                }
                                // listofTattle.remove(listofTattle.size() - 1);
                                subCategoryListButtonsAdapter.addAllModels(listofSubCategory);
                                subCategoryListButtonsAdapter.notifyDataSetChanged();
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
            String url = AppUtils.SERVICE_BASE_API + "getSubCategories?categoryId=" + categoryId;
            BasicPostAsync basicPostAsync = new BasicPostAsync(url, onAsyncResult, getActivity(), true);
            basicPostAsync.execute();
        }
        else {
            showShortToast("Cannot connect to internet");
        }
    }


    public void customCompressImage() {
       photoPath= String.valueOf(Imagepath);
        if (Imagepath == null) {
        } else {
            try {
                compressedImage = new Compressor(getActivity())
                        .setMaxWidth(1280)
                        .setMaxHeight(720)
                        .setQuality(50)
                        .setCompressFormat(Bitmap.CompressFormat.JPEG)
                        .setDestinationDirectoryPath(Environment.getExternalStoragePublicDirectory(
                                Environment.DIRECTORY_PICTURES).getAbsolutePath())
                        .compressToFile(Imagepath, photoPath);
               convertToBase64(String.valueOf(compressedImage));
                uploadImage();




            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    private String convertToBase64(String compressedImage)

    {

        Bitmap bm = BitmapFactory.decodeFile(compressedImage);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        bm.compress(Bitmap.CompressFormat.JPEG, 70, baos);

        byte[] byteArrayImage = baos.toByteArray();

         encodedImage = Base64.encodeToString(byteArrayImage, Base64.DEFAULT);

        return encodedImage;

    }
}
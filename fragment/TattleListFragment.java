package com.tattleup.app.tattleup.fragment;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kosalgeek.android.photoutil.CameraPhoto;
import com.kosalgeek.android.photoutil.GalleryPhoto;
import com.tattleup.app.tattleup.MainActivity;
import com.tattleup.app.tattleup.R;
import com.tattleup.app.tattleup.adapter.RecyclerViewTattleListAdapter;
import com.tattleup.app.tattleup.adapter.SubCategoryListRecylerViewAdapter;
import com.tattleup.app.tattleup.asynctask.BasicPostAsync;
import com.tattleup.app.tattleup.base.BaseFragment;
import com.tattleup.app.tattleup.base.TattleUp;
import com.tattleup.app.tattleup.holder.SubCategoryListHolder;
import com.tattleup.app.tattleup.holder.TattleListHolder;
import com.tattleup.app.tattleup.model.SubCategoryListModel;
import com.tattleup.app.tattleup.model.TattleListModel;
import com.tattleup.app.tattleup.util.AppUtils;
import com.tattleup.app.tattleup.util.Utility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by admin on 10/6/2017.
 */

public class TattleListFragment extends BaseFragment implements SubCategoryListHolder.ItemClickListener, View.OnClickListener, TattleListHolder.ItemClickListener {


    private static final String ARG_POSITION = "position";
    private View rootView;
    private RecyclerView subCategoryList, recyclerTattleList;
    private ArrayList<TattleListModel> listofTattle;
    private ArrayList<SubCategoryListModel> listofSubCategory;
    private List<TattleListModel> TattleList = new ArrayList<TattleListModel>();
    private List<SubCategoryListModel> SubCategoryList = new ArrayList<SubCategoryListModel>();
    private SubCategoryListRecylerViewAdapter subCategoryListRecylerViewAdapter;
    private RecyclerViewTattleListAdapter recyclerViewTattleListAdapter;
    private ProgressDialog pDialog;
    private int position;
    private TextView postTattle;
    private TextView postPhoto;
    private String categoryId="", categoryName="";
    private TattleUp tattleUp;
    private FloatingActionButton btnCreateTattle;
    private String userChoosenTask;
    private CameraPhoto cameraPhoto = new CameraPhoto(getActivity());
    private GalleryPhoto galleryPhoto = new GalleryPhoto(getActivity());
    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    private CircleImageView imgUser;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_tab_tattle_list, container, false);
        setHasOptionsMenu(true);
        super.initializeFragment(rootView);
        tattleUp = (TattleUp) getActivity().getApplication();

        Bundle bundle = getArguments();
        try {
            categoryId = bundle.getString("CategoryId");
            categoryName = bundle.getString("CategoryName");
            ((MainActivity) getActivity()).setPageTitle(categoryName);
        } catch ( NullPointerException e) {}

        postTattle = (TextView) rootView.findViewById(R.id.postTattle);
        postTattle.setOnClickListener(this);

      //  postPhoto = (TextView) rootView.findViewById(R.id.postPhoto);
//        postPhoto.setOnClickListener(this);

        imgUser = (CircleImageView)rootView.findViewById(R.id.imgUser);
//        String userImagePath = tattleUp.getDATA(AppUtils.USER_IMAGE_PATH,"");
//        Picasso.with(getActivity())
//                .load(userImagePath)
//                .placeholder(R.drawable.user_pic)
//                .resize(200,200)
//                .into(imgUser);

        subCategoryList = (RecyclerView) rootView.findViewById(R.id.listSubCategoryRecycler);

        subCategoryList.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        subCategoryList.setLayoutManager(mLayoutManager);
        subCategoryList.setItemAnimator(new DefaultItemAnimator());
        listofSubCategory = new ArrayList<>();
        subCategoryListRecylerViewAdapter = new SubCategoryListRecylerViewAdapter(getContext(), listofSubCategory, this);
        subCategoryList.setAdapter(subCategoryListRecylerViewAdapter);


        recyclerTattleList = (RecyclerView) rootView.findViewById(R.id.recyclerTattleList);

        recyclerTattleList.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager1 = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerTattleList.setLayoutManager(mLayoutManager1);
        recyclerTattleList.setItemAnimator(new DefaultItemAnimator());
        listofTattle = new ArrayList<>();
        recyclerViewTattleListAdapter = new RecyclerViewTattleListAdapter(getContext(), listofTattle, this);
        recyclerTattleList.setAdapter(recyclerViewTattleListAdapter);

        getTattleList(categoryId, "");
        getSubCategories(categoryId);





        return rootView;

    }

    public static void updateListView() {
    }


    @Override
    public View initializeComponents(View rootView) {
        super.initializeComponents(rootView);
        return rootView;
    }


    @Override
    public void bindEvents() {
        super.bindEvents();
    }


    private void getTattleList(String categoryId , String subCategoryId) {
        String subLocality = tattleUp.getDATA(AppUtils.USER_SUBLOCALITY, "");
        String city = tattleUp.getDATA(AppUtils.USER_CITY, "");
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
                                Log.e("Status", String.valueOf(jsonObject.has("tattleListByCategory")));
                                if (jsonObject.has("tattleListByCategory")) {
                                    //Log.e("finalResult", status);
                                    recyclerViewTattleListAdapter.notifyDataSetChanged();
                                    JSONArray jsonArray = jsonObject.getJSONArray("tattleListByCategory");
                                    Type typedValue = new TypeToken<ArrayList<TattleListModel>>() {
                                    }.getType();
                                    listofTattle = new Gson().fromJson(jsonArray.toString(), typedValue);
                                    for (int i = 0; i < listofTattle.size(); i++) {
                                        System.out.println(listofTattle.get(i));
                                    }
                                   // listofTattle.remove(listofTattle.size() - 1);
                                    recyclerViewTattleListAdapter.addAllModels(listofTattle);
                                    recyclerViewTattleListAdapter.notifyDataSetChanged();
                                    notifyAdapter(listofTattle);
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
                String url = AppUtils.SERVICE_BASE_API + "getTattleListByCategory?" + "CategoryId="  + categoryId + "&SubCategoryId="
                        + subCategoryId + "&Locality=" + URLEncoder.encode(subLocality) + "&City=" + URLEncoder.encode(city);
                BasicPostAsync basicPostAsync = new BasicPostAsync(url, onAsyncResult, getActivity(), true);
                basicPostAsync.execute();
            }
         else {
            showShortToast("Cannot connect to internet");
        }
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
                                subCategoryListRecylerViewAdapter.addAllModels(listofSubCategory);
                                subCategoryListRecylerViewAdapter.notifyDataSetChanged();
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

    private void notifyAdapter(ArrayList<TattleListModel> listofTattle) {
        recyclerViewTattleListAdapter = null;
        recyclerViewTattleListAdapter = new RecyclerViewTattleListAdapter(getActivity(), listofTattle, this);
        recyclerTattleList.setAdapter(recyclerViewTattleListAdapter);
        recyclerViewTattleListAdapter.notifyDataSetChanged();
    }





//    @Override
//    public void onPositionClick(final int position, String type) {
//        if (type == "TattleView") {
//            viewTattle(listofTattle.get(position));
//        }
//        if (type == "UpvoteTattle") {
//            setUserVotes(listofTattle.get(position), "1");
//        }
//        if (type == "DownvoteTattle") {
//            setUserVotes(listofTattle.get(position), "2");
//        }
//        if (type == "ZeroVote") {
//            setUserVotes(listofTattle.get(position), "0");
//        }
//
//
//    }

    private void viewTattle(TattleListModel tattleListModel) {
        String tattleId = tattleListModel.getId();
        String userName = tattleListModel.getFbUserName();
        String title = tattleListModel.getTitle();
        String descripttiopn = tattleListModel.getDescription();
        String imagePath = tattleListModel.getImagePath();
        String userId = tattleUp.getDATA(AppUtils.LOGIN_USER_ID, "");

        Bundle bundle = new Bundle();
        bundle.putString("tattleId", tattleId);
        bundle.putString("userId", userId);
        bundle.putString("title", title);
        bundle.putString("description", descripttiopn);
        bundle.putString("userName", userName);
        bundle.putString("ImagePath", imagePath);

        UserCommentsFragment userCommentsFragment = null;
        userCommentsFragment = new UserCommentsFragment();
        userCommentsFragment.setArguments(bundle);
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.content_frame, userCommentsFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        hidePDialog();
    }

    private void hidePDialog() {
        if (pDialog != null) {
            pDialog.dismiss();
            pDialog = null;
        }
    }

//    @Override
//    public void onItemClick(View v, int pos, String hint, TattleListModel tattleListModel) {
//
//
//    }

    @Override
    public void onItemClick(View v, int pos, String hint, SubCategoryListModel subCategoryListModel) {
        if (hint.equals("SubCategoryList")) {
            getTattleList(categoryId, subCategoryListModel.getId());
        }

    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {

            case R.id.postTattle:
                String catId = categoryId;
                String catName = categoryName;
                Bundle bundle = new Bundle();

                bundle.putString("CategoryId", catId);
                bundle.putString("CategoryName", catName);
                PostTattleFragment postTattleFragment = null;
                postTattleFragment = new PostTattleFragment();
                postTattleFragment.setArguments(bundle);
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.content_frame, postTattleFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();

         /*  case R.id.postPhoto:
//                selectImage();
                break;*/


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
//        Intent intent = new Intent();
//        intent.setType("image/*");
//        intent.setAction(Intent.ACTION_GET_CONTENT);//
        //  startActivityForResult(Intent.createChooser(intent, "Select File"), SELECT_FILE);
        galleryPhoto = new GalleryPhoto(getContext());
        Intent in = galleryPhoto.openGalleryIntent();
        startActivityForResult(in, SELECT_FILE);
    }

    private void cameraIntent() throws IOException {
        // Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraPhoto = new CameraPhoto(getContext());
        Intent intent = cameraPhoto.takePhotoIntent();
        startActivityForResult(intent, REQUEST_CAMERA);
    }

    private void setUserVotes(TattleListModel tattleListModel, String status) {
        String tattleId = tattleListModel.getId();
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

                                if (obj.has("message"))
                                {
                                    String string = obj.getString("message");
                                    showShortToast(string);
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
                String url = AppUtils.SERVICE_BASE_API + "setUserVotes?userId=" + userId + "&tattleId=" + tattleId +"&status="+status;
                Log.e("URL", url);
                BasicPostAsync basicPostAsync = new BasicPostAsync(url, onAsyncResult, getActivity(), true);
                basicPostAsync.execute(userId);
            }
        } else {
            showShortToast("Cannot connect to internet");
        }
    }

    @Override
    public void onItemClick(View v, int pos, String hint, TattleListModel tattleListModel) {

        if (hint == "TattleView") {
            viewTattle(listofTattle.get(position));
        }
        if (hint == "UpvoteTattle") {
            setUserVotes(listofTattle.get(position), "1");
        }
        if (hint == "DownvoteTattle") {
            setUserVotes(listofTattle.get(position), "2");
        }
        if (hint == "ZeroVote") {
            setUserVotes(listofTattle.get(position), "0");
        }
    }
}

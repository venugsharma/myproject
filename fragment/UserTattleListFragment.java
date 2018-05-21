package com.tattleup.app.tattleup.fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tattleup.app.tattleup.MainActivity;
import com.tattleup.app.tattleup.R;
import com.tattleup.app.tattleup.adapter.UserTattleListAdapter;
import com.tattleup.app.tattleup.asynctask.BasicPostAsync;
import com.tattleup.app.tattleup.base.BaseFragment;
import com.tattleup.app.tattleup.base.TattleUp;
import com.tattleup.app.tattleup.holder.SubCategoryListHolder;
import com.tattleup.app.tattleup.model.SubCategoryListModel;
import com.tattleup.app.tattleup.model.UserTattleListModel;
import com.tattleup.app.tattleup.util.AppUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by leo on 22/11/17.
 */

public class UserTattleListFragment extends BaseFragment implements UserTattleListAdapter.OnPositionClick, SubCategoryListHolder.ItemClickListener {


    private static final String ARG_POSITION = "position";
    private View rootView;
    private ArrayList<UserTattleListModel> listofTattle;
    private List<UserTattleListModel> TattleList = new ArrayList<UserTattleListModel>();
    private UserTattleListAdapter adapter;
    private ProgressDialog pDialog;
    private int position;
    private String categoryId="", categoryName="";
    private TattleUp tattleUp;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_tab_tattle_list, container, false);
        setHasOptionsMenu(true);
        super.initializeFragment(rootView);
        tattleUp = (TattleUp) getActivity().getApplication();
        ((MainActivity) getActivity()).setPageTitle("My Tattles");



//        listofTattle = new ArrayList<>();
//        adapter = new UserTattleListAdapter(getActivity(), TattleList, this);
//        tattleList.setAdapter(adapter);

        getTattleList();

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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        position = getArguments().getInt(ARG_POSITION);
        Bundle bundle = getArguments();
        try {
            categoryId = bundle.getString("CategoryId");
            categoryName = bundle.getString("CategoryName");
        } catch ( NullPointerException e) {}
      //  getTattleList();
    }

    @Override
    public void bindEvents() {
        super.bindEvents();
    }


    private void getTattleList() {
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
                                JSONObject jsonObject = new JSONObject(result);
                                Log.e("Status", String.valueOf(jsonObject.has("userTattleList")));
                                if (jsonObject.has("userTattleList")) {
                                    //Log.e("finalResult", status);
                                    JSONArray jsonArray = jsonObject.getJSONArray("userTattleList");
                                    listofTattle.clear();
                                    try {
                                        Type typedValue = new TypeToken<ArrayList<UserTattleListModel>>() {
                                        }.getType();
                                        listofTattle = new Gson().fromJson(jsonArray.toString(), typedValue);
                                        for (int i = 0; i < listofTattle.size(); i++) {
                                            System.out.println(listofTattle.get(i));
                                        }
                                        adapter.addAllModels(listofTattle);
                                        adapter.notifyDataSetChanged();
                                    } catch (NullPointerException e) {}
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
                String url = AppUtils.SERVICE_BASE_API + "getUserTattleList?" + "userId=" + userId;
                BasicPostAsync basicPostAsync = new BasicPostAsync(url, onAsyncResult, getActivity(), true);
                basicPostAsync.execute();
            }
        }

        else {
            showShortToast("Cannot connect to internet");
        }
    }

    @Override
    public UserTattleListFragment onPositionClick(final int position, String type) {
        if (type == "TattleView") {
            viewTattle(listofTattle.get(position));
        }
        return null;
    }

    private void viewTattle(UserTattleListModel tattleuserlistModel) {
        String tattleId = tattleuserlistModel.getId();
        String title = tattleuserlistModel.getTitle();
        String descripttiopn = tattleuserlistModel.getDescription();
        String shopId = tattleUp.getDATA(AppUtils.LOGIN_USER_ID, "");

        Bundle bundle = new Bundle();
        bundle.putString("tattleId", tattleId);
        bundle.putString("shopId", shopId);
        bundle.putString("title", title);
        bundle.putString("description", descripttiopn);

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

    @Override
    public void onItemClick(View v, int pos, String hint, SubCategoryListModel subCategoryListModel) {
        if (hint.equals("SubCategoryList")){
//            custDetailsDialog(listOfCustomers.get(pos));
        }

    }
}

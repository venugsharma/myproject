package com.tattleup.app.tattleup.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tattleup.app.tattleup.MainActivity;
import com.tattleup.app.tattleup.R;
import com.tattleup.app.tattleup.adapter.SubCategoryRecyclerAdapter;
import com.tattleup.app.tattleup.asynctask.BasicPostAsync;
import com.tattleup.app.tattleup.base.BaseFragment;
import com.tattleup.app.tattleup.base.TattleUp;
import com.tattleup.app.tattleup.helper.RecyclerItemClickListener;
import com.tattleup.app.tattleup.model.SubCategoryListModel;
import com.tattleup.app.tattleup.util.AppUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2/10/2018.
 */

public class MoreSubCategoriesGridViewFragment extends BaseFragment {

    private View rootView;
    private TattleUp tattleUp;
    private String subCatId = "";
    private Menu menu;
    private List<SubCategoryListModel> subCategoryList1 = new ArrayList<SubCategoryListModel>();

    private SubCategoryRecyclerAdapter subCategoryRecyclerAdapter1;
    private RecyclerView recyclerView1;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_more_subcategories, container, false);
        super.initializeFragment(rootView);
        setHasOptionsMenu(true);
        return rootView;
    }

    @Override
    public View initializeComponents(View rootView) {
        super.initializeComponents(rootView);
        ((MainActivity) getActivity()).setPageTitle("");

        Bundle bundle = getArguments();
        try {
            subCatId = bundle.getString("SubCategoryId");
            //          ((MainActivity) getActivity()).setPagenamebyUtils(AppUtils.USER_GPS_LOCATION, "");
        } catch (NullPointerException ex) {
        }

        tattleUp = (TattleUp) getActivity().getApplication();

        /////////////////////////category recycler view Groups////////////////////////////
        recyclerView1 = (RecyclerView) rootView.findViewById(R.id.recyclerViewCategory1);
        subCategoryRecyclerAdapter1 = new SubCategoryRecyclerAdapter(getContext(), subCategoryList1);

        recyclerView1.setHasFixedSize(true);
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(3, LinearLayoutManager.VERTICAL);
        recyclerView1.setLayoutManager(staggeredGridLayoutManager);
        recyclerView1.setItemAnimator(new DefaultItemAnimator());
        recyclerView1.setAdapter(subCategoryRecyclerAdapter1);
        getSubCategoriesList1();
        recyclerView1.addOnItemTouchListener(
                new RecyclerItemClickListener(getActivity(), recyclerView1, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        String catId = subCategoryList1.get(position).getId();
                        String catName = subCategoryList1.get(position).getName();

                        Bundle bundle1 = new Bundle();
                        bundle1.putString("CategoryId", catId);
                        bundle1.putString("CategoryName", catName);

                        TattleListFragment fragment = new TattleListFragment();
                        fragment.setArguments(bundle1);
                        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                        android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.content_frame, fragment);
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();
                    }

                    @Override
                    public void onLongItemClick(View view, int position) {
                        // do whatever
                    }
                })
        );
        return rootView;
    }

    private void getSubCategoriesList1() {
        if (AppUtils.isNetworkAvailable(getActivity())) {
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
                                JSONArray jsonArray = jsonObject.getJSONArray("SubcategoriesList");
                                Type typedValue = new TypeToken<ArrayList<SubCategoryListModel>>() {
                                }.getType();
                                subCategoryList1 = new Gson().fromJson(jsonArray.toString(), typedValue);
                                for (int i = 0; i < subCategoryList1.size(); i++) {
                                    System.out.println(subCategoryList1.get(i));
                                }
                                subCategoryRecyclerAdapter1.addAllModels(subCategoryList1);
                                subCategoryRecyclerAdapter1.notifyDataSetChanged();
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
            String url = AppUtils.SERVICE_BASE_API + "getSubCategories?categoryId=" + subCatId;
            BasicPostAsync basicPostAsync = new BasicPostAsync(url, onAsyncResult, getActivity(), true);
            basicPostAsync.execute();
        }
        else {
            showShortToast("Cannot connect to internet");
        }
    }
}

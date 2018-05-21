package com.tattleup.app.tattleup.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
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
import com.tattleup.app.tattleup.adapter.SelectInterestRecyclerAdapter;
import com.tattleup.app.tattleup.asynctask.BasicPostAsync;
import com.tattleup.app.tattleup.base.BaseFragment;
import com.tattleup.app.tattleup.base.TattleUp;
import com.tattleup.app.tattleup.model.SubCategoryListModel;
import com.tattleup.app.tattleup.util.AppUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2/8/2018.
 */

public class SelectInterestFragment extends BaseFragment {

    private View rootView;
    private TattleUp tattleUp;
    private String locality="";
    private Menu menu;
    private List<SubCategoryListModel> subCategoryList1 = new ArrayList<SubCategoryListModel>();
    private List<SubCategoryListModel> subCategoryList2 = new ArrayList<SubCategoryListModel>();
    private List<SubCategoryListModel> subCategoryList3 = new ArrayList<SubCategoryListModel>();
    private SelectInterestRecyclerAdapter selectInterestRecyclerAdapter1, selectInterestRecyclerAdapter2, selectInterestRecyclerAdapter3;
    private RecyclerView recyclerView1, recyclerView2, recyclerView3;
    private static final String TAG = MainActivity.class.getSimpleName();
    private String URLp, URLproducts;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_select_interest, container, false);
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
            locality = bundle.getString("locality");
            ((MainActivity) getActivity()).setPagename(locality);
            //          ((MainActivity) getActivity()).setPagenamebyUtils(AppUtils.USER_GPS_LOCATION, "");
        } catch (NullPointerException ex) {
        }

        tattleUp = (TattleUp) getActivity().getApplication();

        /////////////////////////category recycler view Groups////////////////////////////
        recyclerView1 = (RecyclerView) rootView.findViewById(R.id.recyclerViewCategory1);
        selectInterestRecyclerAdapter1 = new SelectInterestRecyclerAdapter(getContext(), subCategoryList1);
        recyclerView1.setHasFixedSize(true);
        StaggeredGridLayoutManager staggeredGridLayoutManager1 = new StaggeredGridLayoutManager(3, LinearLayoutManager.VERTICAL);
        recyclerView1.setLayoutManager(staggeredGridLayoutManager1);
        recyclerView1.setItemAnimator(new DefaultItemAnimator());
        recyclerView1.setAdapter(selectInterestRecyclerAdapter1);
        getSubCategoriesList1();

//
//        FlexboxLayoutManager layoutManager = new FlexboxLayoutManager(context);
//        layoutManager.setFlexDirection(FlexDirection.COLUMN);
//        layoutManager.setJustifyContent(JustifyContent.FLEX_END);
//        recyclerView.setLayoutManager(layoutManager)

        recyclerView2 = (RecyclerView) rootView.findViewById(R.id.recyclerViewCategory2);
        selectInterestRecyclerAdapter2 = new SelectInterestRecyclerAdapter(getContext(), subCategoryList2);
        recyclerView2.setHasFixedSize(true);
        StaggeredGridLayoutManager staggeredGridLayoutManager2 = new StaggeredGridLayoutManager(3, LinearLayoutManager.VERTICAL);
        recyclerView2.setLayoutManager(staggeredGridLayoutManager2);;
        recyclerView2.setItemAnimator(new DefaultItemAnimator());
        recyclerView2.setAdapter(selectInterestRecyclerAdapter2);
        getSubCategoriesList2();

        recyclerView3 = (RecyclerView) rootView.findViewById(R.id.recyclerViewCategory3);
        selectInterestRecyclerAdapter3 = new SelectInterestRecyclerAdapter(getContext(), subCategoryList3);
        recyclerView3.setHasFixedSize(true);
        StaggeredGridLayoutManager staggeredGridLayoutManager3 = new StaggeredGridLayoutManager(3, LinearLayoutManager.VERTICAL);
        recyclerView3.setLayoutManager(staggeredGridLayoutManager3);
        recyclerView3.setItemAnimator(new DefaultItemAnimator());
        recyclerView3.setAdapter(selectInterestRecyclerAdapter3);
        getSubCategoriesList3();
//        recyclerView1.addOnItemTouchListener(
//                new RecyclerItemClickListener(this, recyclerView1, new RecyclerItemClickListener.OnItemClickListener() {
//                    @Override
//                    public void onItemClick(View view, int position) {
//                        String pId = ProductListList1.get(position).getProductId();
//                        String pDescription = ProductListList1.get(position).getShortDescription();
//                        String productName = ProductListList1.get(position).getProductname();
//                        String productSellPrice = ProductListList1.get(position).getAmount();
//                        String pDiscount = ProductListList1.get(position).getDiscount();
//                        String pImage = ProductListList1.get(position).getThumbnailUrl();
//
//                        ProductDetailsFragment productDetailsFragment1 = null;
//                        productDetailsFragment1 = new ProductDetailsFragment();
//                        Bundle args = new Bundle();
//                        args.putString("ProductId", pId);
//                        args.putString("ProductSDescription", pDescription);
//                        args.putString("ProductName", productName);
//                        args.putString("Productamount", productSellPrice);
//                        args.putString("ProductDiscount", pDiscount);
//                        args.putString("ProductImage", pImage);
//
//                        productDetailsFragment1.setArguments(args);
//
//                        FragmentManager fragmentManager10 = getSupportFragmentManager();
//                        FragmentTransaction fragmentTransaction10 = fragmentManager10.beginTransaction();
//                        fragmentTransaction10.replace(R.id.content_frame, productDetailsFragment1);
//                        fragmentTransaction10.addToBackStack(null);
//                        fragmentTransaction10.commit();
//
//                    }
//
//                    @Override
//                    public void onLongItemClick(View view, int position) {
//                        // do whatever
//                    }
//                })
//        );


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
                                selectInterestRecyclerAdapter1.addAllModels(subCategoryList1);
                                selectInterestRecyclerAdapter1.notifyDataSetChanged();
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
            String url = AppUtils.SERVICE_BASE_API + "getSubCategories?categoryId=1";
            BasicPostAsync basicPostAsync = new BasicPostAsync(url, onAsyncResult, getActivity(), true);
            basicPostAsync.execute();
        }
        else {
            showShortToast("Cannot connect to internet");
        }
    }

    private void getSubCategoriesList2() {
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
                                subCategoryList2 = new Gson().fromJson(jsonArray.toString(), typedValue);
                                for (int i = 0; i < subCategoryList2.size(); i++) {
                                    System.out.println(subCategoryList2.get(i));
                                }
                                selectInterestRecyclerAdapter2.addAllModels(subCategoryList2);
                                selectInterestRecyclerAdapter2.notifyDataSetChanged();
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
            String url = AppUtils.SERVICE_BASE_API + "getSubCategories?categoryId=2";
            BasicPostAsync basicPostAsync = new BasicPostAsync(url, onAsyncResult, getActivity(), true);
            basicPostAsync.execute();
        }
        else {
            showShortToast("Cannot connect to internet");
        }
    }

    private void getSubCategoriesList3() {
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
                                subCategoryList3 = new Gson().fromJson(jsonArray.toString(), typedValue);
                                for (int i = 0; i < subCategoryList3.size(); i++) {
                                    System.out.println(subCategoryList3.get(i));
                                }
                                selectInterestRecyclerAdapter3.addAllModels(subCategoryList3);
                                selectInterestRecyclerAdapter3.notifyDataSetChanged();
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
            String url = AppUtils.SERVICE_BASE_API + "getSubCategories?categoryId=3";
            BasicPostAsync basicPostAsync = new BasicPostAsync(url, onAsyncResult, getActivity(), true);
            basicPostAsync.execute();
        }
        else {
            showShortToast("Cannot connect to internet");
        }
    }
}

package com.tattleup.app.tattleup.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

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
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2/7/2018.
 */

public class DashboardFragment extends BaseFragment implements View.OnClickListener{

    private View rootView;
    private TattleUp tattleUp;
    private String locality="";
    private Menu menu;
    private List<SubCategoryListModel> subCategoryList1 = new ArrayList<SubCategoryListModel>();
    private List<SubCategoryListModel> subCategoryList2 = new ArrayList<SubCategoryListModel>();
    private List<SubCategoryListModel> subCategoryList3 = new ArrayList<SubCategoryListModel>();
    private SubCategoryRecyclerAdapter subCategoryRecyclerAdapter1, subCategoryRecyclerAdapter2, subCategoryRecyclerAdapter3;
    private RecyclerView recyclerView1, recyclerView2, recyclerView3;
    private static final String TAG = MainActivity.class.getSimpleName();
    private String URLp, URLproducts;
    private TextView moreCategory1, moreCategory2, moreCategory3;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_dashboard, container, false);
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

        moreCategory1 = (TextView)rootView.findViewById(R.id.moreCategory1);
        moreCategory1.setOnClickListener(this);
        moreCategory2 = (TextView)rootView.findViewById(R.id.moreCategory2);
        moreCategory2.setOnClickListener(this);
        moreCategory3 = (TextView)rootView.findViewById(R.id.moreCategory3);
        moreCategory3.setOnClickListener(this);

        /////////////////////////category recycler view Groups////////////////////////////
        recyclerView1 = (RecyclerView) rootView.findViewById(R.id.recyclerViewCategory1);
        subCategoryRecyclerAdapter1 = new SubCategoryRecyclerAdapter(getContext(), subCategoryList1);
        RecyclerView.LayoutManager mLayoutManager1 = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView1.setLayoutManager(mLayoutManager1);
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

        /////////////////////////category recycler view Governance////////////////////////////
        recyclerView2 = (RecyclerView) rootView.findViewById(R.id.recyclerViewCategory2);
        subCategoryRecyclerAdapter2 = new SubCategoryRecyclerAdapter(getContext(), subCategoryList2);
        RecyclerView.LayoutManager mLayoutManager2 = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView2.setLayoutManager(mLayoutManager2);
        recyclerView2.setItemAnimator(new DefaultItemAnimator());
        recyclerView2.setAdapter(subCategoryRecyclerAdapter2);
        getSubCategoriesList2();

        recyclerView2.addOnItemTouchListener(
                new RecyclerItemClickListener(getActivity(), recyclerView2, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        String catId = subCategoryList2.get(position).getId();
                        String catName = subCategoryList2.get(position).getName();

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

        /////////////////////////category recycler view Services////////////////////////////
        recyclerView3 = (RecyclerView) rootView.findViewById(R.id.recyclerViewCategory3);
        subCategoryRecyclerAdapter3 = new SubCategoryRecyclerAdapter(getContext(), subCategoryList3);
        RecyclerView.LayoutManager mLayoutManager3 = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView3.setLayoutManager(mLayoutManager3);
        recyclerView3.setItemAnimator(new DefaultItemAnimator());
        recyclerView3.setAdapter(subCategoryRecyclerAdapter3);
        getSubCategoriesList3();
        recyclerView3.addOnItemTouchListener(
                new RecyclerItemClickListener(getActivity(), recyclerView3, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        String catId = subCategoryList3.get(position).getId();
                        String catName = subCategoryList3.get(position).getName();

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

    @SuppressLint("NewApi")
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {
        // Inflate the menu; this adds items to the action bar if it is present.+
        menuInflater.inflate(R.menu.menu_search, menu);
        Log.e("oncreate optinmenu", "menu getting");
//        menu.findItem(R.id.actionSearch).setVisible(true);

        //  menu.findItem(R.id.actionLoggedUsername).setTitle("Logged in \n" + AppUtils.firstLetterCapitalized(healthAdvisor.getDATA(AppUtils.LOGIN_USER_NAME, "")));
        MenuItem item = menu.findItem(R.id.action_search).setVisible(true);
        SearchView searchView = (SearchView) item.getActionView();
        searchView.setQueryHint("search locality with city.");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchLocality(query);


                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                //FILTER AS YOU TYPE
                //  adapter.getFilter().filter(query);
                //  adapter.notifyDataSetChanged();
                return false;
            }
        });

//        menu.findItem(R.id.actionList).setVisible(true);
//        menu.findItem(R.id.actionMAP).setVisible(false);
        this.menu = menu;
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
                                subCategoryRecyclerAdapter2.addAllModels(subCategoryList2);
                                subCategoryRecyclerAdapter2.notifyDataSetChanged();
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
                                subCategoryRecyclerAdapter3.addAllModels(subCategoryList3);
                                subCategoryRecyclerAdapter3.notifyDataSetChanged();
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

    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.moreCategory1:
                getAllSubCategories("1");
                break;
            case R.id.moreCategory2:
                getAllSubCategories("2");
                break;
            case R.id.moreCategory3:
                getAllSubCategories("3");
                break;
            default:
                break;

        }

    }

    private void getAllSubCategories(String subCategoryId) {
        Bundle bundle1 = new Bundle();
        bundle1.putString("SubCategoryId", subCategoryId);

        MoreSubCategoriesGridViewFragment moreSubCategoriesGridViewFragment = new MoreSubCategoriesGridViewFragment();
        moreSubCategoriesGridViewFragment.setArguments(bundle1);
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.content_frame, moreSubCategoriesGridViewFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    private void searchLocality(final String locality) {
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
                                                    tattleUp.setDATA(AppUtils.USER_SUBLOCALITY, sublocality);
                                                }
                                                if (types.compareTo("locality") == 0) {
                                                    locality = ((JSONObject)address_components.get(j)).getString("long_name");
                                                    Log.i("locality", locality);
                                                    tattleUp.setDATA(AppUtils.USER_CITY, locality);
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
                String url = "\n" + "https://maps.googleapis.com/maps/api/geocode/json?&address=" + URLEncoder.encode(locality);
                Log.e(locality,url);
                BasicPostAsync basicPostAsync = new BasicPostAsync(url, onAsyncResult, getActivity(), true);
                basicPostAsync.execute(userId, locality);
            }
        } else {
            showShortToast("Cannot connect to internet");
        }
    }

    public void locationConfirmationDialog(final String sublocality, final String locality) {
        final AlertDialog.Builder addSmsTemplate = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.dialog_search_custom_location, null);
        addSmsTemplate.setView(dialogView);
        final AlertDialog b = addSmsTemplate.create();
        b.show();

        final TextView txtlocality = (TextView) dialogView.findViewById(R.id.txtLocality);
        String location = sublocality + ", " + locality;
        txtlocality.setText(location);
        tattleUp.setDATA(AppUtils.USER_LAST_LOCATION, location);
        final Button yes = (Button) dialogView.findViewById(R.id.btnResYes);
        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (txtlocality.equals("")) {
                    Toast toast = Toast.makeText(getActivity(),"Please enter another address.", Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                    // chkLocationDialog();
                    return;
                }
                else try {
                    b.dismiss();
                    ((MainActivity) getActivity()).setPagename(sublocality + ", " + locality);
                    DashboardFragment dashboardFragment = null;
                    dashboardFragment = new DashboardFragment();
                    FragmentManager fragmentManager1 = getActivity().getSupportFragmentManager();
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

    }
}

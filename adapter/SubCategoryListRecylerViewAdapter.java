package com.tattleup.app.tattleup.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tattleup.app.tattleup.R;
import com.tattleup.app.tattleup.holder.SubCategoryListHolder;
import com.tattleup.app.tattleup.model.SubCategoryListModel;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Shiva on 12/31/2016.
 */

public class SubCategoryListRecylerViewAdapter extends RecyclerView.Adapter<SubCategoryListHolder> {

    Context c;
    public List<SubCategoryListModel> taskList = new ArrayList<SubCategoryListModel>();
    public List<SubCategoryListModel> filterList = new ArrayList<SubCategoryListModel>();
  //  CustomCustomerFilter filter;
  SubCategoryListHolder.ItemClickListener itemClickListener;


    public SubCategoryListRecylerViewAdapter(Context ctx, List<SubCategoryListModel> players, SubCategoryListHolder.ItemClickListener itemClickListener) {
        this.c = ctx;
        this.taskList = players;
        this.filterList = players;
        this.itemClickListener = itemClickListener;
    }

    public SubCategoryListModel getItem(int position) {
        return taskList.get(position);
    }

    public void addAllModels(ArrayList<SubCategoryListModel> models) {
        taskList.addAll(models);
    }

    @Override
    public SubCategoryListHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //CONVERT XML TO VIEW ONBJ
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_subcategory_recycler, null);

        //HOLDER
        SubCategoryListHolder holder = new SubCategoryListHolder(v);

        return holder;
    }

    @Override
    public void onBindViewHolder(final SubCategoryListHolder holder, final int position) {
        //BIND DATA
        // getting movie data for the row
        final SubCategoryListModel subCategoryListModel = taskList.get(position);
        holder.txtSubCategory.setText(subCategoryListModel.getName().toString());



        holder.cardViewSubCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                itemClickListener.onItemClick(v, position, "SubCategoryList", subCategoryListModel);
            }
        });
    }



    //GET TOTAL NUM OF PLAYERS
    @Override
    public int getItemCount() {
        return taskList.size();
    }


}

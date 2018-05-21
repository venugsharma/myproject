package com.tattleup.app.tattleup.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import com.tattleup.app.tattleup.R;
import com.tattleup.app.tattleup.holder.SubCategoryListPostTattleHolder;
import com.tattleup.app.tattleup.model.SubCategoryListModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 1/2/2018.
 */

public class SubCategoryListButtonsAdapter extends RecyclerView.Adapter<SubCategoryListPostTattleHolder> {

    Context c;
    public List<SubCategoryListModel> subCategoryList = new ArrayList<SubCategoryListModel>();
    public List<SubCategoryListModel> filterList = new ArrayList<SubCategoryListModel>();
    //  CustomCustomerFilter filter;
    SubCategoryListPostTattleHolder.ItemClickListener itemClickListener;
    private SparseBooleanArray sparseBooleanArray;


    public SubCategoryListButtonsAdapter(Context ctx, List<SubCategoryListModel> subCategoryList, SubCategoryListPostTattleHolder.ItemClickListener itemClickListener) {
        this.c = ctx;
        this.subCategoryList = subCategoryList;
        this.filterList = subCategoryList;
        this.itemClickListener = itemClickListener;
        sparseBooleanArray = new SparseBooleanArray();
    }

    public ArrayList<SubCategoryListModel> getCheckedItems() {
        ArrayList<SubCategoryListModel> mTempArry = new ArrayList<SubCategoryListModel>();
        for(int i=0;i<subCategoryList.size();i++) {
            if(sparseBooleanArray.get(i)) {
                mTempArry.add(subCategoryList.get(i));
            }
        }
        return mTempArry;
    }

    public SubCategoryListModel getItem(int position) {
        return subCategoryList.get(position);
    }

    public void addAllModels(ArrayList<SubCategoryListModel> models) {
        subCategoryList.addAll(models);
    }

    @Override
    public SubCategoryListPostTattleHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //CONVERT XML TO VIEW ONBJ
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_post_tattle_subcategories_recycler, null);

        //HOLDER
        SubCategoryListPostTattleHolder holder = new SubCategoryListPostTattleHolder(v);

        return holder;
    }

    @Override
    public void onBindViewHolder(final SubCategoryListPostTattleHolder holder, final int position) {
        //BIND DATA
        // getting movie data for the row
        final SubCategoryListModel subCategoryListModel = subCategoryList.get(position);
        try {
            holder.txtSubCategory.setText(subCategoryListModel.getName().toString());
        }catch (NullPointerException e){
        }

        holder.chkSubCategory.setTag(position);


        holder.chkSubCategory.setChecked(sparseBooleanArray.get(position));


        holder.chkSubCategory.setOnCheckedChangeListener(mCheckedChangeListener);

//        holder.btnSubCategory.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                holder.btnSubCategory.setEnabled(false);
//                itemClickListener.onItemClick(v, position, "SubCategoryList", subCategoryListModel);
//            }
//        });
    }

    CompoundButton.OnCheckedChangeListener mCheckedChangeListener = new CompoundButton.OnCheckedChangeListener() {




        @Override


        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {


            // TODO Auto-generated method stub


            sparseBooleanArray.put((Integer) buttonView.getTag(), isChecked);


        }


    };



    //GET TOTAL NUM OF PLAYERS
    @Override
    public int getItemCount() {
        return subCategoryList.size();
    }


}

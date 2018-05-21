package com.tattleup.app.tattleup.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tattleup.app.tattleup.R;
import com.tattleup.app.tattleup.model.SubCategoryListModel;

import java.util.List;

/**
 * Created by admin on 2/8/2018.
 */

public class SelectInterestRecyclerAdapter extends RecyclerView.Adapter<SelectInterestRecyclerAdapter.MyViewHolder>{


    private Context mContext;
    private List<SubCategoryListModel> listOfSubCategories;
    private int count;

    public void addAllModels(List<SubCategoryListModel> subCategoryList1) { listOfSubCategories.addAll(subCategoryList1);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView txtSubCategory;

        public MyViewHolder(View view) {
            super(view);
            txtSubCategory = (TextView) view.findViewById(R.id.txtSubCategory);

        }
    }


    public SelectInterestRecyclerAdapter(Context mContext, List<SubCategoryListModel> listOfSubCategories) {
        this.mContext = mContext;
        this.listOfSubCategories = listOfSubCategories;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_select_interest_recycler, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final SubCategoryListModel subCategoryListModel = listOfSubCategories.get(position);

        // title
        holder.txtSubCategory.setText(subCategoryListModel.getName());

        holder.txtSubCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (count == 1) {
                    view.setBackgroundResource(R.drawable.circular_colorbackground_box);
                    count++;
                } else if(count == 2){
                    view.setBackgroundResource(R.drawable.circular_background_box);
                    count++;
                }



            }

        });

    }

    @Override
    public int getItemCount() {
        return listOfSubCategories.size();
    }

}

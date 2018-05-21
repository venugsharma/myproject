package com.tattleup.app.tattleup.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.tattleup.app.tattleup.R;
import com.tattleup.app.tattleup.model.SubCategoryListModel;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by admin on 2/7/2018.
 */

public class SubCategoryRecyclerAdapter extends RecyclerView.Adapter<SubCategoryRecyclerAdapter.MyViewHolder>{

    private Context mContext;
    private List<SubCategoryListModel> listOfSubCategories;

    public void addAllModels(List<SubCategoryListModel> subCategoryList1) { listOfSubCategories.addAll(subCategoryList1);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView txtSubCategory;
        public ProgressBar progressBar;
        public CircleImageView thumbNail;

        public MyViewHolder(View view) {
            super(view);
            thumbNail = (CircleImageView) view
                    .findViewById(R.id.thumbnail);
            progressBar=(ProgressBar)view.findViewById(R.id.progressBar);
            txtSubCategory = (TextView) view.findViewById(R.id.txtSubCategory);

        }
    }


    public SubCategoryRecyclerAdapter(Context mContext, List<SubCategoryListModel> listOfSubCategories) {
        this.mContext = mContext;
        this.listOfSubCategories = listOfSubCategories;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_subcategory_recycler, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        SubCategoryListModel m = listOfSubCategories.get(position);

        // title
        holder.txtSubCategory.setText(m.getName());
    }

    @Override
    public int getItemCount() {
        return listOfSubCategories.size();
    }

}

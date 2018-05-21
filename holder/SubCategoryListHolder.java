package com.tattleup.app.tattleup.holder;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.tattleup.app.tattleup.R;
import com.tattleup.app.tattleup.model.SubCategoryListModel;

/**
 * Created by admin on 12/2/2017.
 */

public class SubCategoryListHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    //OUR VIEWS
    public CardView cardViewSubCategory;
    public TextView txtSubCategory;
    public SubCategoryListModel subCategoryListModel;


    SubCategoryListHolder.ItemClickListener itemClickListener;
    private char[] id;


    public SubCategoryListHolder(View itemView) {
        super(itemView);
        cardViewSubCategory = (CardView) itemView.findViewById(R.id.cardviewRecyclerSubCategory);
        txtSubCategory = (TextView) itemView.findViewById(R.id.txtSubCategory);
        cardViewSubCategory.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        try {
            this.itemClickListener.onItemClick(v, getLayoutPosition(), "hint", subCategoryListModel);
        } catch (NullPointerException nEx) {
            nEx.printStackTrace();
        }


    }

    public void setItemClickListener(SubCategoryListHolder.ItemClickListener ic) {
        this.itemClickListener = ic;
    }

    public char[] getId() { return id; }

    public interface ItemClickListener {

        void onItemClick(View v, int pos, String hint, SubCategoryListModel subCategoryListModel);


    }
}

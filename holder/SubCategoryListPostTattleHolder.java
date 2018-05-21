package com.tattleup.app.tattleup.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CheckedTextView;

import com.tattleup.app.tattleup.R;
import com.tattleup.app.tattleup.model.SubCategoryListModel;

/**
 * Created by admin on 1/2/2018.
 */

public class SubCategoryListPostTattleHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    //OUR VIEWS
//    public CardView cardViewSubCategory;
    public CheckedTextView txtSubCategory;
    public CheckBox chkSubCategory;
    public SubCategoryListModel subCategoryListModel;


    SubCategoryListPostTattleHolder.ItemClickListener itemClickListener;
    private char[] id;


    public SubCategoryListPostTattleHolder(View itemView) {
        super(itemView);
//        cardViewSubCategory = (CardView) itemView.findViewById(R.id.cardviewRecyclerSubCategory);
        txtSubCategory = (CheckedTextView) itemView.findViewById(R.id.txtSubCategory);
        chkSubCategory = (CheckBox) itemView.findViewById(R.id.chkSubCategory);

        txtSubCategory.setOnClickListener(this);
//        cardViewSubCategory.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        try {
            this.itemClickListener.onItemClick(v, getLayoutPosition(), "hint", subCategoryListModel);
        } catch (NullPointerException nEx) {
            nEx.printStackTrace();
        }


    }

    public void setItemClickListener(SubCategoryListPostTattleHolder.ItemClickListener ic) {
        this.itemClickListener = ic;
    }

    public char[] getId() { return id; }

    public interface ItemClickListener {

        void onItemClick(View v, int pos, String hint, SubCategoryListModel subCategoryListModel);


    }
}

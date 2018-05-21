package com.tattleup.app.tattleup.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tattleup.app.tattleup.R;
import com.tattleup.app.tattleup.model.TattleListModel;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Anurag Harod on 2/9/2018.
 */

public class TattleListHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    //OUR VIEWS
    public LinearLayout layoutTattle;
    public Button btnComments, btnSharePost;
    public TextView txtUserName, txtTitle, txtDescription, txtTattleDate, countUpvote, countDownvote, countComments;
    public TextView btnUpVote, btnDownVote;
    public ImageView imgTattle;
    public CircleImageView imgUser;
    public TattleListModel tattleListModel;


    TattleListHolder.ItemClickListener itemClickListener;
    private char[] id;


    public TattleListHolder(View itemView) {
        super(itemView);
        layoutTattle = (LinearLayout) itemView.findViewById(R.id.layoutTattle);
        txtUserName = (TextView) itemView.findViewById(R.id.txtUserName);
        txtTitle = (TextView) itemView.findViewById(R.id.txtTattleTitle);
        txtDescription = (TextView) itemView.findViewById(R.id.txtTattleDescription);
        txtTattleDate = (TextView) itemView.findViewById(R.id.txtTattleDate);

        btnUpVote = (TextView) itemView.findViewById(R.id.btnUpVote);
        btnDownVote = (TextView) itemView.findViewById(R.id.btnDownVote);
        btnComments = (Button) itemView.findViewById(R.id.btnComments);
        countUpvote = (TextView) itemView.findViewById(R.id.countUpvote);
        countDownvote = (TextView) itemView.findViewById(R.id.countDownvote);
        countComments = (TextView) itemView.findViewById(R.id.countComments);
        btnSharePost = (Button) itemView.findViewById(R.id.btnSharePost);

        imgTattle = (ImageView)itemView.findViewById(R.id.imgTattle);
        imgUser = (CircleImageView) itemView.findViewById(R.id.imgUser);
    }

    @Override
    public void onClick(View v) {
        try {
            this.itemClickListener.onItemClick(v, getLayoutPosition(), "hint", tattleListModel);
        } catch (NullPointerException nEx) {
            nEx.printStackTrace();
        }


    }

    public void setItemClickListener(TattleListHolder.ItemClickListener ic) {
        this.itemClickListener = ic;
    }

    public char[] getId() { return id; }

    public interface ItemClickListener {

        void onItemClick(View v, int pos, String hint, TattleListModel tattleListModel);


    }
}


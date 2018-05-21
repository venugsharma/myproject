package com.tattleup.app.tattleup.adapter;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.toolbox.ImageLoader;
import com.squareup.picasso.Picasso;
import com.tattleup.app.tattleup.R;
import com.tattleup.app.tattleup.base.TattleUp;
import com.tattleup.app.tattleup.holder.TattleListHolder;
import com.tattleup.app.tattleup.model.TattleListModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Anurag Harod on 2/9/2018.
 */

public class RecyclerViewTattleListAdapter  extends RecyclerView.Adapter<TattleListHolder> {

    Context c;
    public List<TattleListModel> tattleList = new ArrayList<TattleListModel>();
    public List<TattleListModel> filterList = new ArrayList<TattleListModel>();
    //  CustomCustomerFilter filter;
    TattleListHolder.ItemClickListener itemClickListener;
    ImageLoader imageLoader = TattleUp.getInstance().getImageLoader();
    private Activity activity;
    private int count;
    private int one = 1;
    private Context context;


    public RecyclerViewTattleListAdapter(Context ctx, List<TattleListModel> players, TattleListHolder.ItemClickListener itemClickListener) {
        this.context = ctx;
        this.tattleList = players;
        this.filterList = players;
        this.itemClickListener = itemClickListener;
    }

    public TattleListModel getItem(int position) {
        return tattleList.get(position);
    }

    public void addAllModels(ArrayList<TattleListModel> models) {
        tattleList.addAll(models);
    }

    @Override
    public TattleListHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //CONVERT XML TO VIEW ONBJ
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_tattles, null);

        //HOLDER
        TattleListHolder tattleListHolder = new TattleListHolder(v);

        return tattleListHolder;
    }

    @Override
    public void onBindViewHolder(final TattleListHolder holder, final int position) {
        //BIND DATA
        // getting movie data for the row
        final TattleListModel tattleListModel = tattleList.get(position);
        if (imageLoader == null)
            imageLoader = TattleUp.getInstance().getImageLoader();
        final TattleListModel tattleListModel1 = tattleList.get(position);

        if (imageLoader == null)
            imageLoader = TattleUp.getInstance().getImageLoader();
        final TattleListModel tattleListModel2 = tattleList.get(position);
        //if (productListModel.getImageName() != null) {
        String imageUrl = "";
        if (tattleListModel2.getImagePath() == null || tattleListModel2.getImagePath() == "null" || tattleListModel2.getImagePath() == "") {
            imageUrl = "http://mobileadminapi.shopincity.com/Images/NoImageAvailable.png";
            holder.imgTattle.setVisibility(View.GONE);
//            holder.imgTattle.setImageUrl(imageUrl, imageLoader);
        } else {
            holder.imgTattle.setVisibility(View.VISIBLE);
            imageUrl = "http://api.tattleup.leoinfotech.in/" + tattleListModel2.getImagePath();
//            holder.imgProductImage.setImageUrl(imageUrl, imageLoader);
            Picasso.with(context)
                    .load(imageUrl)
//                    .placeholder(R.drawable.placeholder)
                    .resize(500,500)
                    .into(holder.imgTattle);
        }

        if (tattleListModel2.getProfileImage() == null || tattleListModel2.getProfileImage() == "null" || tattleListModel2.getProfileImage() == "") {
            Picasso.with(context)
                    .load(imageUrl)
                    .placeholder(R.drawable.user_pic)
                    .resize(100,100)
                    .into(holder.imgUser);
//            holder.imgTattle.setImageUrl(imageUrl, imageLoader);
        } else {
            imageUrl = "http://api.tattleup.leoinfotech.in/" + tattleListModel2.getProfileImage();
//            holder.imgProductImage.setImageUrl(imageUrl, imageLoader);
            Picasso.with(context)
                    .load(imageUrl)
//                    .placeholder(R.drawable.placeholder)
                    .resize(100,100)
                    .into(holder.imgUser);
        }




        try {
            holder.txtUserName.setText(tattleListModel.getFbUserName());
        } catch (NullPointerException ex) {
        }
        try {
            holder.txtTitle.setText(tattleListModel.getTitle());
        } catch (NullPointerException ex) {
        }
        try {
            holder.txtDescription.setText(tattleListModel.getDescription());
        } catch (NullPointerException ex) {
        }
        try {
            holder.txtTattleDate.setText(tattleListModel.getCreatedDate());
        } catch (NullPointerException ex) {
        }

        try {
            holder.countUpvote.setText(tattleListModel.getUpvote());
        } catch (NullPointerException ex) {
        }
        try {
            holder.countDownvote.setText(tattleListModel.getDownvote());
        } catch (NullPointerException ex) {
        }
        try {
            holder.countComments.setText(tattleListModel.getComments());
        } catch (NullPointerException ex) {
        }
        holder.btnComments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                listener.onPositionClick(position, "TattleView");
                itemClickListener.onItemClick(v, position, "TattleView", tattleListModel);
            }
        });

        holder.btnUpVote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (count == 1) {
                    v.setBackgroundResource(R.drawable.upvote_ic);
                    holder.btnDownVote.setBackgroundResource(R.drawable.dislike);
                    holder.countDownvote.setText(tattleListModel.getDownvote());

                    count++;

                    int like= Integer.parseInt(tattleListModel.getUpvote());
                    String total= String.valueOf(like+one);
                    holder.countUpvote.setText(total);
//                    listener.onPositionClick(position, "UpvoteTattle");
                    itemClickListener.onItemClick(v, position, "UpvoteTattle", tattleListModel);
                }     else if(count==2){
                    v.setBackgroundResource(R.drawable.like);
                    holder.btnDownVote.setBackgroundResource(R.drawable.dislike);

                    //  int like= Integer.parseInt(tattleListModel.getUpvote());
                    // holder.countUpvote.setText(like);
                    holder.countUpvote.setText(tattleListModel.getUpvote());

                    count --;
//                    listener.onPositionClick(position, "ZeroVote");
                    itemClickListener.onItemClick(v, position, "ZeroVote", tattleListModel);
                }


                else {
                    v.setBackgroundResource(R.drawable.like);
                    holder.btnDownVote.setBackgroundResource(R.drawable.dislike);
                    count = 1;
//                    listener.onPositionClick(position, "ZeroVote");
                    itemClickListener.onItemClick(v, position, "ZeroVote", tattleListModel);
                }



            }

        });

        holder.btnDownVote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (count == 2) {
                    v.setBackgroundResource(R.drawable.downvote_ic);
                    holder.btnUpVote.setBackgroundResource(R.drawable.like);
                    count++;
                    holder.countUpvote.setText(tattleListModel.getUpvote());

                    int dislike= Integer.parseInt(tattleListModel.getDownvote());
                    String ttl= String.valueOf(dislike+one);
                    holder.countDownvote.setText(ttl);
                    // holder.countDownvote.setText(tattleListModel.getDownvote() + String.valueOf(1));
//                    listener.onPositionClick(position, "DownvoteTattle");
                    itemClickListener.onItemClick(v, position, "DownvoteTattle", tattleListModel);
                }  else if(count==3){
                    v.setBackgroundResource(R.drawable.dislike);
                    holder.btnDownVote.setBackgroundResource(R.drawable.like);
                    holder.countDownvote.setText(tattleListModel.getDownvote());

                    //   holder.countUpvote.setText(tattleListModel.getUpvote());

                    count --;
//                    listener.onPositionClick(position, "ZeroVote");
                    itemClickListener.onItemClick(v, position, "ZeroVote", tattleListModel);
                }


                else {
                    v.setBackgroundResource(R.drawable.dislike);
                    holder.btnUpVote.setBackgroundResource(R.drawable.like);
                    count = 2;
//                    listener.onPositionClick(position, "ZeroVote");
                    itemClickListener.onItemClick(v, position, "ZeroVote", tattleListModel);
                }



            }

        });

        holder.btnSharePost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String postId = tattleListModel.getId();
                String postTitle = tattleListModel.getTitle();
                String postDetails = tattleListModel.getDescription();
                Bundle bundle = new Bundle();
                bundle.putString("ProductId", postId);
                String message = "*Localitie* shared a post \n" +"*" + postTitle +"* \n" + postDetails;
                Intent share = new Intent(Intent.ACTION_SEND);
                share.setType("text/plain");
                share.putExtra(Intent.EXTRA_TEXT, message);
                context.startActivity(Intent.createChooser(share, "Share Via"));
            }
        });
//        holder.cardViewSubCategory.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                itemClickListener.onItemClick(v, position, "SubCategoryList", tattleListModel);
//            }
//        });
    }



    //GET TOTAL NUM OF PLAYERS
    @Override
    public int getItemCount() {
        return tattleList.size();
    }


}



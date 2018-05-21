package com.tattleup.app.tattleup.adapter;

import android.app.Activity;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.tattleup.app.tattleup.R;
import com.tattleup.app.tattleup.base.TattleUp;
import com.tattleup.app.tattleup.fragment.UserCommentsFragment;
import com.tattleup.app.tattleup.fragment.UserTattleListFragment;
import com.tattleup.app.tattleup.model.UserCommentsModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 11/30/2017.
 */

public class UserCommentsAdapter extends BaseAdapter implements ListAdapter {

    public List<UserCommentsModel> listofComments;
    public List<UserCommentsModel> commentList = new ArrayList<UserCommentsModel>();
    private Activity activity;
    private LayoutInflater layoutInflater;
    ImageLoader imageLoader = TattleUp.getInstance().getImageLoader();
    private UserCommentsAdapter.OnPositionClick listener;
    private int count;


    public interface OnPositionClick {
        UserTattleListFragment onPositionClick(int position, String type);
    }

    public UserCommentsAdapter(FragmentActivity activity, List<UserCommentsModel> comments, UserCommentsFragment userCommentsFragment) {
        this.activity = activity;
        this.listofComments = comments;
        this.commentList = comments;
        this.listener = listener;
        this.layoutInflater = layoutInflater.from(activity);

    }

    @Override
    public int getCount() {
        return listofComments.size(); }

    public void addAllModels(ArrayList<UserCommentsModel> models) {
        listofComments.addAll(models);
    }

    @Override
    public UserCommentsModel getItem(int position) {
        return listofComments.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.list_item_comments, null);
            UserCommentsAdapter.ViewHolder viewHolder = new UserCommentsAdapter.ViewHolder();
            viewHolder.layoutTattle = (LinearLayout) convertView.findViewById(R.id.layoutTattle);
            viewHolder.txtUserName = (TextView) convertView.findViewById(R.id.txtUserName);
            viewHolder.txtUserComment = (TextView) convertView.findViewById(R.id.txtUserComments);

            convertView.setTag(viewHolder);

        }
        UserCommentsModel item = listofComments.get(position);


        initializeViews(getItem(position), (UserCommentsAdapter.ViewHolder) convertView.getTag(), position);
        return convertView;
    }

    private void initializeViews(UserCommentsModel userCommentsModel, final UserCommentsAdapter.ViewHolder holder, final int position) {

        if (imageLoader == null)
            imageLoader = TattleUp.getInstance().getImageLoader();
        final UserCommentsModel userCommentsModel1 = commentList.get(position);
        String imageUrl = "";

        try {
            holder.txtUserName.setText(userCommentsModel.getUserId());
        } catch (NullPointerException ex) {
        }
        try {
            holder.txtUserComment.setText(userCommentsModel.getComment());
        } catch (NullPointerException ex) {
        }

    }


    private Activity getView() {
        return null;
    }


    @Override
    public boolean isEmpty() {
        return false;
    }

    public class ViewHolder
    {

        public LinearLayout layoutTattle;
        public TextView txtUserName, txtUserComment;

        }
}
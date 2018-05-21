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
import com.tattleup.app.tattleup.fragment.UserTattleListFragment;
import com.tattleup.app.tattleup.model.UserTattleListModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by leo on 21/11/17.
 */

public class UserTattleListAdapter extends BaseAdapter implements ListAdapter {

    public List<UserTattleListModel> listofTattle;
    public List<UserTattleListModel> mytattleList = new ArrayList<UserTattleListModel>();
    private Activity activity;
    private LayoutInflater layoutInflater;
    ImageLoader imageLoader = TattleUp.getInstance().getImageLoader();
    private OnPositionClick listener;
    private int count;


    public interface OnPositionClick {
        UserTattleListFragment onPositionClick(int position, String type);
    }

    public UserTattleListAdapter(FragmentActivity activity, List<UserTattleListModel> tattles, OnPositionClick listener) {
        this.activity = activity;
        this.listofTattle = tattles;
        this.mytattleList = tattles;
        this.listener = listener;
        this.layoutInflater = layoutInflater.from(activity);

    }

    @Override
    public int getCount() {
        return listofTattle.size(); }

    public void addAllModels(ArrayList<UserTattleListModel> models) {
        listofTattle.addAll(models);
    }

    @Override
    public UserTattleListModel getItem(int position) {
        return listofTattle.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.list_item_tattles, null);
            ViewHolder viewHolder = new ViewHolder();
            viewHolder.layoutTattle = (LinearLayout) convertView.findViewById(R.id.layoutTattle);
            viewHolder.txtUserName = (TextView) convertView.findViewById(R.id.txtUserName);
            viewHolder.txtTitle = (TextView) convertView.findViewById(R.id.txtTattleTitle);
            viewHolder.txtDescription = (TextView) convertView.findViewById(R.id.txtTattleDescription);
            viewHolder.txtTattleDate = (TextView) convertView.findViewById(R.id.txtTattleDate);

            viewHolder.btnUpVote = (TextView) convertView.findViewById(R.id.btnUpVote);
            viewHolder.btnDownVote = (TextView) convertView.findViewById(R.id.btnDownVote);
            convertView.setTag(viewHolder);

        }
        UserTattleListModel item = listofTattle.get(position);


        initializeViews(getItem(position), (ViewHolder) convertView.getTag(), position);
        return convertView;
    }

    private void initializeViews(UserTattleListModel userTattleListModel, final UserTattleListAdapter.ViewHolder holder, final int position) {

        if (imageLoader == null)
            imageLoader = TattleUp.getInstance().getImageLoader();
        final UserTattleListModel userTattleListModel1 = mytattleList.get(position);
        String imageUrl = "";

        try {
            holder.txtUserName.setText(userTattleListModel.getUserId());
        } catch (NullPointerException ex) {
        }
        try {
            holder.txtTitle.setText(userTattleListModel.getTitle());
        } catch (NullPointerException ex) {
        }
        try {
            holder.txtDescription.setText(userTattleListModel.getDescription());
        } catch (NullPointerException ex) {
        }


        try {
            holder.txtTattleDate.setText(userTattleListModel.getCreatedDate());
        } catch (NullPointerException ex) {
        }
        holder.layoutTattle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onPositionClick(position, "TattleView");
            }
        });

        holder.btnUpVote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (count == 1) {
                    view.setBackgroundResource(R.drawable.upvote_ic);
                    count++;
                } else {
                    view.setBackgroundResource(R.drawable.like);
                    count = 1;
                }



            }

        });
        holder.btnDownVote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (count == 2) {
                    view.setBackgroundResource(R.drawable.downvote_ic);
                    count++;
                } else {
                    view.setBackgroundResource(R.drawable.dislike);
                    count = 2;
                }


            }

        });


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
        public TextView txtUserName, txtTitle, txtDescription, txtTattleDate, txtlike;
        public TextView btnDownVote, btnUpVote;
    }
}


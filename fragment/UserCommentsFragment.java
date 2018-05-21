package com.tattleup.app.tattleup.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;
import com.tattleup.app.tattleup.MainActivity;
import com.tattleup.app.tattleup.R;
import com.tattleup.app.tattleup.adapter.UserCommentsAdapter;
import com.tattleup.app.tattleup.asynctask.BasicPostAsync;
import com.tattleup.app.tattleup.base.BaseFragment;
import com.tattleup.app.tattleup.base.TattleUp;
import com.tattleup.app.tattleup.model.UserCommentsModel;
import com.tattleup.app.tattleup.util.AppUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 9/16/2017.
 */

public class UserCommentsFragment extends BaseFragment{


    private TattleUp tattleUp;
    private View rootView;
    private TextView txtTitle, txtDescription, txtUserName;
    private TextView btnUpVote, btnDownVote;
    private Button btnComments;
    private String tattleId="", title="", description="", userName="", imagePath="";
    private int count;
    private Button btnSaveComment;
    private EditText editComment;
    private ListView listUserComments;
    private UserCommentsAdapter userCommentsAdapter;
    private ArrayList<UserCommentsModel> listofComments;
    private ImageView imgTattle;
    private List<UserCommentsModel> CommentsList = new ArrayList<UserCommentsModel>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_comments, container, false);
        super.initializeFragment(rootView);
        setHasOptionsMenu(true);
        return rootView;
    }

    @Override
    public View initializeComponents(View rootView) {
        super.initializeComponents(rootView);

        tattleUp = (TattleUp) getActivity().getApplication();
        ((MainActivity) getActivity()).setPageTitle("Comments");

        txtUserName = (TextView) rootView.findViewById(R.id.txtUserName);
        txtTitle = (TextView) rootView.findViewById(R.id.txtTattleTitle);
        txtDescription = (TextView) rootView.findViewById(R.id.txtTattleDescription);
        btnUpVote = (TextView) rootView.findViewById(R.id.btnUpVote);
        btnDownVote = (TextView) rootView.findViewById(R.id.btnDownVote);
        btnComments = (Button) rootView.findViewById(R.id.btnComments);
        btnSaveComment = (Button) rootView.findViewById(R.id.btnSaveComment);
        editComment = (EditText) rootView.findViewById(R.id.editComment);
        listUserComments = (ListView) rootView.findViewById(R.id.commentList);
        imgTattle = (ImageView) rootView.findViewById(R.id.imgTattle);


        listofComments = new ArrayList<>();
        userCommentsAdapter = new UserCommentsAdapter(getActivity(), CommentsList, this);
        listUserComments.setAdapter(userCommentsAdapter);

        Bundle bundle = getArguments();
        try {
            tattleId = bundle.getString("tattleId");
            title = bundle.getString("title");
            description = bundle.getString("description");
            userName = bundle.getString("userName");
            imagePath = bundle.getString("ImagePath");

        } catch (NullPointerException ex) {
        }

        txtTitle.setText(title);
        txtDescription.setText(description);
        txtUserName.setText(userName);

        if (imagePath != "" || imagePath != null || imagePath != "null"){
            imgTattle.setVisibility(View.VISIBLE);
            String imageUrl = "http://api.tattleup.leoinfotech.in/" + imagePath;
            Picasso.with(this.getActivity())
                    .load(imageUrl)
                    .resize(400,400)
                    .into(imgTattle);
        } else {
            imgTattle.setVisibility(View.GONE);
        }


        btnUpVote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (count == 1) {
                    view.setBackgroundResource(R.drawable.upvote_ic);
                    btnDownVote.setBackgroundResource(R.drawable.dislike);
                    count++;
                    setUserVotes(tattleId, "1");
                } else {
                    view.setBackgroundResource(R.drawable.like);
                    btnDownVote.setBackgroundResource(R.drawable.dislike);
                    count = 1;
                    setUserVotes(tattleId, "0");
                }

                String status = "2";
                //  getsetUserVotes(tattleId, status,topicId,comment);

            }

        });
        btnDownVote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (count == 2) {
                    view.setBackgroundResource(R.drawable.downvote_ic);
                    btnUpVote.setBackgroundResource(R.drawable.like);
                    count++;
                    setUserVotes(tattleId, "2");
                } else {
                    view.setBackgroundResource(R.drawable.dislike);
                    btnUpVote.setBackgroundResource(R.drawable.like);
                    count = 2;
                    setUserVotes(tattleId, "0");
                }

                String status = "2";
                //   getsetUserVotes(tattleId, status,topicId,comment);

            }


        });

        editComment.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable arg0) {
                enableSubmitIfReady();
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });

        btnSaveComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //sendChatMessage();
                setUserComments(tattleId);
                editComment.setText("");

            }
        });

        getCommentsList(tattleId);

        return rootView;

    }

    public void enableSubmitIfReady() {

        boolean isReady = editComment.getText().toString().length() > 3;
        btnSaveComment.setVisibility(View.VISIBLE);
    }

    private void setUserComments(final String tattleId) {

        String comment = editComment.getText().toString().trim();

        if(comment != "" || comment != null) {

            if (AppUtils.isNetworkAvailable(getActivity())) {
                String userId = tattleUp.getDATA(AppUtils.LOGIN_USER_ID, "");
                if (userId != null) {
                    BasicPostAsync.OnAsyncResult onAsyncResult = new BasicPostAsync.OnAsyncResult() {
                        @Override
                        public String OnAsynResult(String result) {
                            if (result.equals("error")) {
                                Log.e("error", "Error");
                            } else {
                                try {
                                    Log.e("result", result);
                                    Log.e("Else error", "Error");
                                    String re = result;

                                    JSONObject obj = new JSONObject(result);

                                    if (obj.has("message")) {
                                        String string = obj.getString("message");
                                        showShortToast(string);
                                    }
                                    getCommentsList(tattleId);


                                } catch (JSONException ex) {
                                    // goToApp();
                                    Log.e("Else error", "ErrorException");
                                    ex.printStackTrace();
                                }
                            }
                            return result;
                        }

                    };
                    String url = AppUtils.SERVICE_BASE_API + "setUserComments?userId=" + userId + "&tattleId=" + tattleId + "&comment=" + URLEncoder.encode(comment);
                    Log.e("URL", url);
                    BasicPostAsync basicPostAsync = new BasicPostAsync(url, onAsyncResult, getActivity(), true);
                    basicPostAsync.execute(userId);
                }
            } else {
                showShortToast("Cannot connect to internet");
            }
        } else {
            showShortToast("write a comment...");
        }
    }

    private void setUserVotes(String tattleId, String status) {
        if (AppUtils.isNetworkAvailable(getActivity())) {
            String userId = tattleUp.getDATA(AppUtils.LOGIN_USER_ID, "");
            if (userId != null) {
                BasicPostAsync.OnAsyncResult onAsyncResult = new BasicPostAsync.OnAsyncResult() {
                    @Override
                    public String OnAsynResult(String result) {
                        if (result.equals("error")) {
                            Log.e("error", "Error");
                        } else {
                            try {
                                Log.e("result", result);
                                Log.e("Else error", "Error");
                                String re = result;

                                JSONObject obj = new JSONObject(result);

                                if (obj.has("message"))
                                {
                                    String string = obj.getString("message");
                                    showShortToast(string);
                                }


                            } catch (JSONException ex) {
                                // goToApp();
                                Log.e("Else error", "ErrorException");
                                ex.printStackTrace();
                            }
                        }
                        return result;
                    }

                };
                String url = AppUtils.SERVICE_BASE_API + "setUserVotes?userId=" + userId + "&tattleId=" + tattleId +"&status="+status;
                Log.e("URL", url);
                BasicPostAsync basicPostAsync = new BasicPostAsync(url, onAsyncResult, getActivity(), true);
                basicPostAsync.execute(userId);
            }
        } else {
            showShortToast("Cannot connect to internet");
        }
    }

    private void getCommentsList(String tattleId) {
        if (AppUtils.isNetworkAvailable(getActivity())) {
            String userId = tattleUp.getDATA(AppUtils.LOGIN_USER_ID, "");
            if (userId != null) {
                BasicPostAsync.OnAsyncResult onAsyncResult = new BasicPostAsync.OnAsyncResult() {
                    @Override
                    public String OnAsynResult(String result) {
                        if (result.equals("error")) {
                            Log.e("error", "Error");
                        } else {
                            try {
                                Log.e("result", result);
                                Log.e("Else error", "Error");
                                String re = result;
                                JSONObject jsonObject = new JSONObject(result);
                                Log.e("Status", String.valueOf(jsonObject.has("userComment")));
                                if (jsonObject.has("userComment")) {
                                    //Log.e("finalResult", status);
                                    userCommentsAdapter.notifyDataSetChanged();
                                    JSONArray jsonArray = jsonObject.getJSONArray("userComment");
                                    Type typedValue = new TypeToken<ArrayList<UserCommentsModel>>() {
                                    }.getType();
                                    listofComments = new Gson().fromJson(jsonArray.toString(), typedValue);
                                    for (int i = 0; i < listofComments.size(); i++) {
                                        System.out.println(listofComments.get(i));
                                    }
                                    // listofTattle.remove(listofTattle.size() - 1);
                                    userCommentsAdapter.addAllModels(listofComments);
                                    userCommentsAdapter.notifyDataSetChanged();
                                    notifyAdapter(listofComments);


                                }
                            } catch (JSONException ex) {
                                // goToApp();
                                Log.e("Else error", "ErrorException");
                                ex.printStackTrace();
                            }
                        }
                        return result;
                    }

                };
                String url = AppUtils.SERVICE_BASE_API + "getUserComments?&tattleId=" + tattleId;
                Log.e("URL", url);
                BasicPostAsync basicPostAsync = new BasicPostAsync(url, onAsyncResult, getActivity(), true);
                basicPostAsync.execute(userId);
            }
        } else {
            showShortToast("Cannot connect to internet");
        }
    }

    private void notifyAdapter(ArrayList<UserCommentsModel> listofComments) {
        userCommentsAdapter = null;
        userCommentsAdapter = new UserCommentsAdapter(getActivity(), listofComments, this);
        listUserComments.setAdapter(userCommentsAdapter);
        userCommentsAdapter.notifyDataSetChanged();
    }



//    @Override
//    public UserCommentsFragment onPositionClick(int position, String type) {
//        return null;
//    }
}

package com.tattleup.app.tattleup.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 11/30/2017.
 */

public class UserCommentsModel implements Parcelable {
    private String Id;
    private String TattleId;
    private String Comment;
    private String Status;
    private String UserId;

    protected UserCommentsModel(Parcel in) {
        Id = in.readString();
        TattleId = in.readString();
        Comment = in.readString();
        Status = in.readString();
        UserId = in.readString();

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(Id);
        dest.writeString(TattleId);
        dest.writeString(Comment);
        dest.writeString(Status);
        dest.writeString(UserId);
    }

    public static final Parcelable.Creator<UserCommentsModel> CREATOR = new Parcelable.Creator<UserCommentsModel>() {
        @Override
        public UserCommentsModel createFromParcel(Parcel in) {
            return new UserCommentsModel(in);
        }

        @Override
        public UserCommentsModel[] newArray(int size) {
            return new UserCommentsModel[size];
        }
    };

    public String getId() { return Id; }

    public void setId(String id) { this.Id = id; }

    public String getTattleId() { return TattleId; }

    public void setTattleId(String tattleId) { this.TattleId = tattleId; }

    public String getComment() { return Comment; }

    public void setComment(String comment) { this.Comment = comment; }

    public String getStatus(String status) { return Status; }

    public void setStatus(String status) { this.Status = status; }

    public String getUserId() { return UserId; }

    public void setUserId(String userId) { this.UserId = userId; }

    public static void extractFromJson(String jsonString, ArrayList<UserCommentsModel> tattles) {
        Gson gson = new Gson();
        Type typedValue = new TypeToken<ArrayList<UserCommentsModel>>() {
        }.getType();
        ArrayList<UserCommentsModel> listofComments = gson.fromJson(jsonString, typedValue);
        tattles.addAll(listofComments);
    }

    public static List<UserCommentsModel> createContactsList(int i, int page) {
        return null;
    }


    public static UserCommentsModel get(int position) {
        return null;
    }

}

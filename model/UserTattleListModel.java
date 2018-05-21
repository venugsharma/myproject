package com.tattleup.app.tattleup.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by leo on 21/11/17.
 */

public class UserTattleListModel implements Parcelable {
    private String Id;
    private String CategoryId;
    private String SubCategoryId;
    private String Locality;
    private String Title;
    private String Description;
    private String Status;
    private String UserId;
    private String AnonymousStatus;
    private String CreatedDate;



    protected UserTattleListModel(Parcel in) {
        Id = in.readString();
        CategoryId = in.readString();
        SubCategoryId = in.readString();
        Locality = in.readString();
        Title = in.readString();
        Description = in.readString();
        Status = in.readString();
        UserId = in.readString();
        AnonymousStatus = in.readString();
        CreatedDate = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(Id);
        dest.writeString(CategoryId);
        dest.writeString(SubCategoryId);
        dest.writeString(Locality);
        dest.writeString(Title);
        dest.writeString(Description);
        dest.writeString(Status);
        dest.writeString(UserId);
        dest.writeString(AnonymousStatus);
        dest.writeString(CreatedDate);
    }

    public static final Parcelable.Creator<UserTattleListModel> CREATOR = new Parcelable.Creator<UserTattleListModel>() {
        @Override
        public UserTattleListModel createFromParcel(Parcel in) {
            return new UserTattleListModel(in);
        }

        @Override
        public UserTattleListModel[] newArray(int size) {
            return new UserTattleListModel[size];
        }
    };

    public String getId() { return Id; }

    public void setId(String id) { this.Id = id; }

    public String getCategoryId() { return CategoryId; }

    public void setCategoryId(String categoryId) { this.CategoryId = categoryId; }

    public String getSubCategoryId() { return SubCategoryId; }

    public void setSubCategoryId(String subCategoryId) { this.SubCategoryId = subCategoryId; }

    public String getLocality() { return Locality; }

    public void setLocality(String locality) { this.Locality = locality; }

    public String getTitle() { return Title; }

    public void setTitle(String title) { this.Title = title; }

    public String getDescription() { return Description; }

    public void setDescription(String description) { this.Description = description; }

    public String getStatus(String status) { return Status; }

    public void setStatus(String status) { this.Status = status; }

    public String getAnonymousStatus() { return AnonymousStatus; }

    public void setAnonymousStatus(String anonymousStatus) { this.AnonymousStatus = anonymousStatus; }

    public String getUserId() { return UserId; }

    public void setUserId(String userId) { this.UserId = userId; }

    public String getCreatedDate() { return CreatedDate; }

    public void setCreatedDate(String createdDate) { this.CreatedDate = createdDate; }

    public static void extractFromJson(String jsonString, ArrayList<UserTattleListModel> tattles) {
        Gson gson = new Gson();
        Type typedValue = new TypeToken<ArrayList<UserTattleListModel>>() {
        }.getType();
        ArrayList<UserTattleListModel> listOfCustomers = gson.fromJson(jsonString, typedValue);
        tattles.addAll(listOfCustomers);
    }

    public static List<UserTattleListModel> createContactsList(int i, int page) {
        return null;
    }


    public static UserTattleListModel get(int position) {
        return null;
    }

}

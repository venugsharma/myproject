package com.tattleup.app.tattleup.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 10/6/2017.
 */

public class TattleListModel implements Parcelable {

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
    private String Upvote;
    private String Downvote;
    private String Comments;
    private String FbUserName;
    private String ProfileImage;
    private String ImagePath;


    protected TattleListModel(Parcel in) {
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
        Upvote = in.readString();
        Downvote = in.readString();
        Comments = in.readString();
        FbUserName = in.readString();
        ProfileImage = in.readString();
        ImagePath = in.readString();

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
        dest.writeString(Upvote);
        dest.writeString(Downvote);
        dest.writeString(Comments);
        dest.writeString(FbUserName);
        dest.writeString(ImagePath);
        dest.writeString(ProfileImage);
    }

    public static final Parcelable.Creator<TattleListModel> CREATOR = new Parcelable.Creator<TattleListModel>() {
        @Override
        public TattleListModel createFromParcel(Parcel in) {
            return new TattleListModel(in);
        }

        @Override
        public TattleListModel[] newArray(int size) {
            return new TattleListModel[size];
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

    public String getStatus() { return Status; }

    public void setStatus(String status) { this.Status = status; }

    public String getAnonymousStatus() { return AnonymousStatus; }

    public void setAnonymousStatus(String anonymousStatus) { this.AnonymousStatus = anonymousStatus; }


    public String getUserId() { return UserId; }

    public void setUserId(String userId) { this.UserId = userId; }

    public String getCreatedDate() { return CreatedDate; }

    public void setCreatedDate(String createdDate) { this.CreatedDate = createdDate; }

    public String getUpvote() { return  Upvote; }

    public void  setUpvote(String upvote) { this.Upvote = upvote; }


    public String getDownvote() { return  Downvote; }

    public void  setDownvote(String downvote) { this.Downvote = downvote; }


    public String getComments() { return  Comments; }

    public void  setComments(String comments) { this.Comments = comments; }

    public String getFbUserName() { return  FbUserName; }

    public void setFbUserName(String fbUserName) {this.FbUserName = fbUserName; }


    public String getImagePath() { return  ImagePath; }

    public void setImagePath(String imagePath) {this.ImagePath = imagePath; }


    public String getProfileImage() { return  ProfileImage; }

    public void setProfileImage(String profileImage) {this.ProfileImage = profileImage; }

    public static void extractFromJson(String jsonString, ArrayList<TattleListModel> tattles) {
        Gson gson = new Gson();
        Type typedValue = new TypeToken<ArrayList<TattleListModel>>() {
        }.getType();
        ArrayList<TattleListModel> listOfCustomers = gson.fromJson(jsonString, typedValue);
        tattles.addAll(listOfCustomers);
    }

    public static List<TattleListModel> createContactsList(int i, int page) {
        return null;
    }


    public static TattleListModel get(int position) {
        return null;
    }
}

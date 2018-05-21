package com.tattleup.app.tattleup.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Shiva on 7/9/2016.
 */
public class SubCategoryListModel implements Parcelable {
    private String Id;
    private String Name;

    public SubCategoryListModel(){

    }

    protected SubCategoryListModel(Parcel in) {
        Id = in.readString();
        Name = in.readString();
            }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(Name);
        dest.writeString(Id);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<SubCategoryListModel> CREATOR = new Creator<SubCategoryListModel>() {
        @Override
        public SubCategoryListModel createFromParcel(Parcel in) {
            return new SubCategoryListModel(in);
        }

        @Override
        public SubCategoryListModel[] newArray(int size) {
            return new SubCategoryListModel[size];
        }
    };

    public String getId() { return Id; }

    public void setId(String id) { Id = id;}

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }


}

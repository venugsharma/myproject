package com.tattleup.app.tattleup.model;
import com.google.gson.annotations.SerializedName;

/**
 * Created by admin on 12/18/2017.
 */

public class ImageClassModel {

    @SerializedName("imageName")
    private String Title;

    @SerializedName("encodedString")
    private String Image;

    @SerializedName("response")
    private String Response;

    public String getResponse() { return Response; }
}

package com.tattleup.app.tattleup.interfaces;

import com.tattleup.app.tattleup.model.ImageClassModel;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by admin on 12/18/2017.
 */

public interface ApiInterfaceImage {


    @FormUrlEncoded
    @POST("Version1.php")
    Call<ImageClassModel> uploadImage(@Field("imageName") String title, @Field("encodedString") String image);
}

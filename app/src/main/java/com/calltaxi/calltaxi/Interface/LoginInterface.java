package com.calltaxi.calltaxi.Interface;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface LoginInterface {

    String LOGINURL = "http://omgaram.com/android/";
    @FormUrlEncoded
    @POST("user_login.php")
    Call<String> getUserLogin(
            @Field("email") String email,
            @Field("password") String password
    );
}

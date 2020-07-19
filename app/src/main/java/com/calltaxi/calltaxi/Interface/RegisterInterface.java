package com.calltaxi.calltaxi.Interface;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface RegisterInterface {

    String REGIURL = "http://omgaram.com/android/";
    @FormUrlEncoded
    @POST("user_register.php")
    Call<String> getUserRegi(
            @Field("username") String username,
            @Field("email") String email,
            @Field("mobile") String mobile,
            @Field("password") String password

    );
}

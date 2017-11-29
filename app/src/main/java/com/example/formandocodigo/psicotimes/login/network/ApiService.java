package com.example.formandocodigo.psicotimes.login.network;

import com.example.formandocodigo.psicotimes.login.entity.RegisterResponse;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by FormandoCodigo on 01/12/2017.
 */

public interface ApiService {

    @POST("register")
    @FormUrlEncoded
    Call<RegisterResponse> register(@Field("name") String name, @Field("email") String email, @Field("password") String password);
}

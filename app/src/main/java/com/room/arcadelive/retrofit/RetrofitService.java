package com.room.arcadelive.retrofit;


import com.room.arcadelive.retrofit.responseStructures.LoginStructure;

import java.util.HashMap;

import io.reactivex.Single;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface RetrofitService {

    @POST("o/token/")
    Single<LoginStructure> login(@Body HashMap<String, Object> params);

}

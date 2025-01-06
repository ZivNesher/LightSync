package com.example.myapplication.api;

import com.example.myapplication.models.UserBoundary;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ApiService {
    @GET("/aii/users/login/{systemID}/{userEmail}")
    Call<UserBoundary> loginUser(
            @Path("systemID") String systemId,
            @Path("userEmail") String userEmail
    );
}

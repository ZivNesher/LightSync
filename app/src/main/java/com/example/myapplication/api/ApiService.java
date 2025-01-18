package com.example.myapplication.api;

import com.example.myapplication.models.NewUserBoundary;
import com.example.myapplication.models.ObjectBoundary;
import com.example.myapplication.models.UserBoundary;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {
    @GET("/aii/users/login/{systemID}/{userEmail}")
    Call<UserBoundary> loginUser(
            @Path("systemID") String systemId,
            @Path("userEmail") String userEmail
    );
    @POST("/aii/users")
    Call<Void> createUser(@Body NewUserBoundary newUserBoundary);

    @PUT("/aii/users/{systemID}/{userEmail}")
    Call<Void> updateUser(
            @Path("systemID") String systemId,
            @Path("userEmail") String userEmail,
            @Body UserBoundary userBoundary
    );

    @POST("/aii/objects")
    Call<ObjectBoundary> createRoom(@Body ObjectBoundary room);

    @GET("/aii/objects/{roomId}/lightbulbs")
    Call<List<ObjectBoundary>> getLightbulbs(@Path("roomId") String roomId);

    @POST("/aii/objects/{roomId}/lightbulbs")
    Call<ObjectBoundary> createLightbulb(@Path("roomId") String roomId, @Body ObjectBoundary lightbulb);

    @GET("/aii/objects")
    Call<List<ObjectBoundary>> getRooms(
            @Query("userSystemID") String systemID,
            @Query("userEmail") String email,
            @Query("page") int page,
            @Query("size") int size
    );


}

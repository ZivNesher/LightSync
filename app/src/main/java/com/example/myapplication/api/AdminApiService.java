package com.example.myapplication.api;

import com.example.myapplication.models.CommandBoundary;
import com.example.myapplication.models.UserBoundary;

import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface AdminApiService {

    @DELETE("/aii/admin/users")
    Call<Void> deleteAllUsers(
            @Query("userSystemID") String systemID,
            @Query("userEmail") String email
    );

    @DELETE("/aii/admin/objects")
    Call<Void> deleteAllObjects(
            @Query("userSystemID") String systemID,
            @Query("userEmail") String email
    );

    @DELETE("/aii/admin/commands")
    Call<Void> deleteAllCommands(
            @Query("userSystemID") String systemID,
            @Query("userEmail") String email
    );

    @GET("/aii/admin/users")
    Call<UserBoundary[]> exportAllUsers(
            @Query("userSystemID") String systemID,
            @Query("userEmail") String email,
            @Query("page") int page,
            @Query("size") int size
    );

    @GET("/aii/admin/commands")
    Call<CommandBoundary[]> exportAllCommands(
            @Query("userSystemID") String systemID,
            @Query("userEmail") String email,
            @Query("page") int page,
            @Query("size") int size
    );
}

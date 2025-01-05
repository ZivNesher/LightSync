package com.example.myapplication.api;

import com.example.myapplication.models.CommandBoundary;
import com.example.myapplication.models.UserBoundary;

import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.GET;

public interface AdminApiService {

    @DELETE("/aii/admin/users")
    Call<Void> deleteAllUsers();

    @DELETE("/aii/admin/objects")
    Call<Void> deleteAllObjects();

    @DELETE("/aii/admin/commands")
    Call<Void> deleteAllCommands();

    @GET("/aii/admin/users")
    Call<UserBoundary[]> exportAllUsers();

    @GET("/aii/admin/commands")
    Call<CommandBoundary[]> exportAllCommands();
}

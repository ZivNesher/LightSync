package com.example.myapplication.api;

import com.example.myapplication.models.NewUserBoundary;
import com.example.myapplication.models.UserBoundary;

import okhttp3.Request;
import okio.Timeout;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MockApiService implements ApiService {

    @Override
    public Call<UserBoundary> loginUser(String systemId, String userEmail) {
        return new Call<UserBoundary>() {
            @Override
            public void enqueue(Callback<UserBoundary> callback) {
                // Define mock credentials
                String mockEmail = "test";
                String mockPassword = "123"; // Mock password is assumed to match for this example

                // Check email (username) matches predefined mock credentials
                if (userEmail.equals(mockEmail)) {
                    // Create a mock UserBoundary object
                    UserBoundary.UserId userId = new UserBoundary.UserId();
                    userId.setSystemId(systemId);
                    userId.setEmail(userEmail);

                    UserBoundary mockUser = new UserBoundary();
                    mockUser.setUserId(userId);
                    mockUser.setRole("END_USER");
                    mockUser.setUsername("Mock User");
                    mockUser.setAvatar("https://example.com/avatar.png"); // Mock avatar URL

                    // Simulate a successful response
                    Response<UserBoundary> response = Response.success(mockUser);
                    callback.onResponse(this, response);
                } else {
                    // Simulate an invalid login response
                    Response<UserBoundary> errorResponse = Response.error(401, okhttp3.ResponseBody.create(
                            null, "Invalid email or password"));
                    callback.onResponse(this, errorResponse);
                }
            }

            @Override
            public Response<UserBoundary> execute() { return null; }

            @Override
            public boolean isExecuted() { return false; }

            @Override
            public void cancel() {}

            @Override
            public boolean isCanceled() { return false; }

            @Override
            public Call<UserBoundary> clone() { return null; }

            @Override
            public Request request() { return null; }

            @Override
            public Timeout timeout() {
                // Return a default Timeout object for mocking
                return new Timeout();
            }
        };
    }

    @Override
    public Call<Void> createUser(NewUserBoundary newUserBoundary) {
        return new Call<Void>() {
            @Override
            public void enqueue(Callback<Void> callback) {
                // Simulate a successful user creation
                callback.onResponse(this, Response.success(null));
            }

            @Override
            public Response<Void> execute() { return null; }

            @Override
            public boolean isExecuted() { return false; }

            @Override
            public void cancel() {}

            @Override
            public boolean isCanceled() { return false; }

            @Override
            public Call<Void> clone() { return null; }

            @Override
            public Request request() { return null; }

            @Override
            public Timeout timeout() {
                // Return a default Timeout object for mocking
                return new Timeout();
            }
        };
    }

    @Override
    public Call<Void> updateUser(String systemId, String userEmail, UserBoundary userBoundary) {
        return new Call<Void>() {
            @Override
            public void enqueue(Callback<Void> callback) {
                // Simulate a successful update
                callback.onResponse(this, Response.success(null));
            }

            @Override
            public Response<Void> execute() { return null; }

            @Override
            public boolean isExecuted() { return false; }

            @Override
            public void cancel() {}

            @Override
            public boolean isCanceled() { return false; }

            @Override
            public Call<Void> clone() { return null; }

            @Override
            public Request request() { return null; }

            @Override
            public Timeout timeout() {
                // Return a default Timeout object for mocking
                return new Timeout();
            }
        };
    }
}

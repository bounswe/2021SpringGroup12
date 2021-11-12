package com.group12.beabee.network.mocking;

import com.group12.beabee.models.requests.LoginRequest;
import com.group12.beabee.models.requests.SignUpRequest;
import com.group12.beabee.models.responses.LoginResponse;
import com.group12.beabee.models.responses.SignUpResponse;
import com.group12.beabee.models.responses.UserDTO;
import com.group12.beabee.network.ServiceAPI;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MockService implements ServiceAPI {

    @Override
    public Call<SignUpResponse> signUpRequest(SignUpRequest user) {
        return new MockCall<SignUpResponse>() {
            @Override
            public void enqueue(Callback<SignUpResponse> callback) {
                SignUpResponse signUpResponse = new SignUpResponse();
                signUpResponse.message="You have successfully signed up to the app!!";
                signUpResponse.messageType="SUCCESS";
                callback.onResponse(this, Response.success(signUpResponse));
            }
        };
    }

    @Override
    public Call<LoginResponse> loginRequest(LoginRequest loginRequest) {
        return new MockCall<LoginResponse>() {
            @Override
            public void enqueue(Callback<LoginResponse> callback) {
                LoginResponse loginResponse = new LoginResponse();
                loginResponse.message="You have successfully logged in!!";
                loginResponse.messageType="SUCCESS";
                loginResponse.userDTO = new UserDTO();
                loginResponse.userDTO.email = loginRequest.email;
                loginResponse.userDTO.username = loginRequest.username;
                loginResponse.jwt = "asdasfsadfas";

                callback.onResponse(this, Response.success(loginResponse));
            }
        };
    }

}

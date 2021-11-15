package com.group12.beabee.network.mocking;

import com.group12.beabee.models.GoalShort;
import com.group12.beabee.models.requests.LoginRequest;
import com.group12.beabee.models.requests.SignUpRequest;
import com.group12.beabee.models.responses.BasicResponse;
import com.group12.beabee.models.responses.LoginResponse;
import com.group12.beabee.models.responses.SignUpResponse;
import com.group12.beabee.models.responses.UserDTO;
import com.group12.beabee.network.ServiceAPI;

import java.util.ArrayList;
import java.util.List;

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

    @Override
    public Call<List<GoalShort>> getGoalsOfUser(int userId) {
        List<GoalShort> tempList = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            GoalShort temp = new GoalShort();
            temp.id = i;
            temp.title = "title"+i;
            temp.description = "description"+i;
            temp.goalType = "GOAL";
            temp.createdAt = "2021-11-15T18:01:25.047Z";
            temp.deadLine = "2021-11-15T18:01:25.047Z";
            tempList.add(temp);
        }
        return new MockCall<List<GoalShort>>() {
            @Override
            public void enqueue(Callback<List<GoalShort>> callback) {
                callback.onResponse(this, Response.success(tempList));
            }
        };
    }

    @Override
    public Call<BasicResponse> createGoalOfUser(int userId, GoalShort goal) {
        return returnBasicResponse();
    }

    private Call<BasicResponse> returnBasicResponse(){
        return new MockCall<BasicResponse>() {
            @Override
            public void enqueue(Callback<BasicResponse> callback) {
                BasicResponse temp = new BasicResponse();
                temp.message = "Succesful";
                temp.messageType = "SUCCESS";
                callback.onResponse(this, Response.success(temp));
            }
        };
    }
}

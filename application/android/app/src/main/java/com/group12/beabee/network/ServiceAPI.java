package com.group12.beabee.network;

import com.group12.beabee.models.GoalShort;
import com.group12.beabee.models.requests.LoginRequest;
import com.group12.beabee.models.requests.SignUpRequest;
import com.group12.beabee.models.responses.BasicResponse;
import com.group12.beabee.models.responses.LoginResponse;
import com.group12.beabee.models.responses.SignUpResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ServiceAPI {

    @POST("/signup")
    Call<SignUpResponse> signUpRequest(@Body() SignUpRequest signUpRequest);

    @POST("/login")
    Call<LoginResponse> loginRequest(@Body() LoginRequest loginRequest);

    @GET("/goals/of_user/{user_id}")
    Call<List<GoalShort>> getGoalsOfUser(@Path("user_id") int userId);

    @POST("/goals/{user_id}")
    Call<BasicResponse> createGoalOfUser(@Path("user_id") int userId, @Body GoalShort userDTO);
}

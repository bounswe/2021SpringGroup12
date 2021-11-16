package com.group12.beabee.network;

import com.group12.beabee.models.GoalDTO;
import com.group12.beabee.models.requests.LoginRequest;
import com.group12.beabee.models.requests.SignUpRequest;
import com.group12.beabee.models.responses.BasicResponse;
import com.group12.beabee.models.responses.Entity;
import com.group12.beabee.models.responses.LoginResponse;
import com.group12.beabee.models.responses.QuestionDTO;
import com.group12.beabee.models.responses.ReflectionDTO;
import com.group12.beabee.models.responses.RoutineDTO;
import com.group12.beabee.models.responses.SignUpResponse;
import com.group12.beabee.models.responses.SubgoalDTO;
import com.group12.beabee.models.responses.TaskDTO;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ServiceAPI {

    @POST("/signup")
    Call<SignUpResponse> signUpRequest(@Body() SignUpRequest signUpRequest);

    @POST("/login")
    Call<LoginResponse> loginRequest(@Body() LoginRequest loginRequest);

    @GET("/goals/of_user/{user_id}")
    Call<List<GoalDTO>> getGoalsOfUser(@Path("user_id") int userId);

    @GET("/goals/{goal_id}")
    Call<GoalDTO> getGoalDetail(@Path("goal_id") int goalId);

    @POST("/goals/{user_id}")
    Call<BasicResponse> createGoalOfUser(@Path("user_id") int userId, @Body GoalDTO userDTO);

    @PUT("/goals")
    Call<BasicResponse> updateGoalOfUser(@Body GoalDTO userDTO);

    @GET("/entities/{entity_id}/sublinks")
    Call<List<Entity>> getSublinksForEntity(@Path("entity_id") int entityId);

    @GET("/entities/goal/{goal_id}")
    Call<List<Entity>> getSublinksForGoal(@Path("goal_id") int goalId);

    @GET("/entities/user/{user_id}")
    Call<List<Entity>> getEntitiesOfUser(@Path("user_id") int userId);

    @POST("/entities/{id}/link/{child_id}")
    Call<BasicResponse> linkEntities(@Path("id") int id, @Path("child_id") int childId);


    @GET("/entities/entiti/{id}")
    Call<SubgoalDTO> getSubgoal(@Path("id") int id);

    @PUT("/entities/subgoal/{id}")
    Call<BasicResponse> updateSubgoal(@Path("id") int id, @Body SubgoalDTO subgoalDTO);

    @DELETE("/entities/subgoal/{id}")
    Call<BasicResponse> deleteSubgoal(@Path("id") int id);

    @POST("/entities/subgoal")
    Call<BasicResponse> createSubgoal(@Body SubgoalDTO subgoalDTO);


    @GET("/entities/entiti/{id}")
    Call<TaskDTO> getTask(@Path("id") int id);

    @PUT("/entities/task/{id}")
    Call<BasicResponse> updateTask(@Path("id") int id, @Body TaskDTO taskDTO);

    @DELETE("/entities/task/{id}")
    Call<BasicResponse> deleteTask(@Path("id") int id);

    @POST("/entities/task")
    Call<BasicResponse> createTask(@Body TaskDTO taskDTO);


    @GET("/entities/entiti/{id}")
    Call<ReflectionDTO> getReflection(@Path("id") int id);

    @PUT("/entities/reflection/{id}")
    Call<BasicResponse> updateReflection(@Path("id") int id, @Body ReflectionDTO reflectionDTO);

    @DELETE("/entities/reflection/{id}")
    Call<BasicResponse> deleteReflection(@Path("id") int id);

    @POST("/entities/reflection")
    Call<BasicResponse> createReflection(@Body ReflectionDTO reflectionDTO);


    @GET("/entities/entiti/{id}")
    Call<RoutineDTO> getRoutine(@Path("id") int id);

    @PUT("/entities/routine/{id}")
    Call<BasicResponse> updateRoutine(@Path("id") int id, @Body RoutineDTO routineDTO);

    @DELETE("/entities/routine/{id}")
    Call<BasicResponse> deleteRoutine(@Path("id") int id);

    @POST("/entities/routine")
    Call<BasicResponse> createRoutine(@Body RoutineDTO routineDTO);


    @GET("/entities/entiti/{id}")
    Call<QuestionDTO> getQuestion(@Path("id") int id);

    @PUT("/entities/question/{id}")
    Call<BasicResponse> updateQuestion(@Path("id") int id, @Body QuestionDTO questionDTO);

    @DELETE("/entities/question/{id}")
    Call<BasicResponse> deleteQuestion(@Path("id") int id);

    @POST("/entities/question")
    Call<BasicResponse> createQuestion(@Body QuestionDTO questionDTO);
}

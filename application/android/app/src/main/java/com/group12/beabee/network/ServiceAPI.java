package com.group12.beabee.network;

import com.group12.beabee.models.requests.ExtendDeadline;
import com.group12.beabee.models.requests.Goal;
import com.group12.beabee.models.GroupGoalDetail;
import com.group12.beabee.models.requests.LoginRequest;
import com.group12.beabee.models.requests.Question;
import com.group12.beabee.models.requests.Reflection;
import com.group12.beabee.models.requests.Routine;
import com.group12.beabee.models.requests.SignUpRequest;
import com.group12.beabee.models.requests.Subgoal;
import com.group12.beabee.models.requests.Task;
import com.group12.beabee.models.responses.BasicResponse;
import com.group12.beabee.models.responses.EntityShort;
import com.group12.beabee.models.responses.GoalDetail;
import com.group12.beabee.models.responses.GoalShort;
import com.group12.beabee.models.responses.GroupGoalShort;
import com.group12.beabee.models.responses.QuestionDetail;
import com.group12.beabee.models.responses.ReflectionDetail;
import com.group12.beabee.models.responses.RoutineDetail;
import com.group12.beabee.models.responses.SubgoalDetail;
import com.group12.beabee.models.responses.LoginResponse;
import com.group12.beabee.models.responses.TaskDetail;
import com.group12.beabee.models.responses.SignUpResponse;
import com.group12.beabee.models.responses.UserDTO;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ServiceAPI {

    @POST("signup")
    Call<SignUpResponse> signUpRequest(@Body() SignUpRequest signUpRequest);

    @POST("login")
    Call<LoginResponse> loginRequest(@Body() LoginRequest loginRequest);

    @GET("goals/of_user/{user_id}")
    Call<List<GoalShort>> getGoalsOfUser(@Path("user_id") int userId);

    @GET("goals/{goal_id}")
    Call<GoalDetail> getGoalDetail(@Path("goal_id") int goalId);

    @POST("goals/{user_id}")
    Call<BasicResponse> createGoalOfUser(@Path("user_id") int userId, @Body Goal goal);

    @PUT("goals")
    Call<BasicResponse> updateGoalOfUser(@Body GoalDetail goalDetail);

    @GET("entities/user/{user_id}")
    Call<List<EntityShort>> getEntitiesOfUser(@Path("user_id") int userId);

    @GET("entities/goal/{goal_id}")
    Call<List<EntityShort>> getEntitiesOfGoal(@Path("goal_id") int goalId);

    @POST("entities/{id}/link/{child_id}")
    Call<BasicResponse> linkEntities(@Path("id") int id, @Path("child_id") int childId);

    /////

    @GET("subgoals/{id}")
    Call<SubgoalDetail> getSubgoal(@Path("id") int id);

    @PUT("subgoals")
    Call<BasicResponse> updateSubgoal(@Body SubgoalDetail subgoalDetail);

    @DELETE("subgoals/{id}")
    Call<BasicResponse> deleteSubgoal(@Path("id") int id);

    @POST("subgoals")
    Call<BasicResponse> createSubgoalUnderSubgoal(@Body Subgoal subgoal);

    @POST("goals/subgoal")
    Call<BasicResponse> createSubgoalUnderGoal(@Body Subgoal subgoal);


    @GET("entities/task/{id}")
    Call<TaskDetail> getTask(@Path("id") int id);

    @PUT("entities/task/")
    Call<BasicResponse> updateTask(@Body TaskDetail taskDetail);

    @DELETE("entities/task/{id}")
    Call<BasicResponse> deleteTask(@Path("id") int id);

    @POST("entities/task")
    Call<BasicResponse> createTask(@Body Task task);


    @GET("entities/reflection/{id}")
    Call<ReflectionDetail> getReflection(@Path("id") int id);

    @PUT("entities/reflection/")
    Call<BasicResponse> updateReflection(@Body ReflectionDetail reflectionDetail);

    @DELETE("entities/reflection/{id}")
    Call<BasicResponse> deleteReflection(@Path("id") int id);

    @POST("entities/reflection")
    Call<BasicResponse> createReflection(@Body Reflection reflection);


    @GET("entities/routine/{id}")
    Call<RoutineDetail> getRoutine(@Path("id") int id);

    @PUT("entities/routine/")
    Call<BasicResponse> updateRoutine(@Body RoutineDetail routineDetail);

    @DELETE("entities/routine/{id}")
    Call<BasicResponse> deleteRoutine(@Path("id") int id);

    @POST("entities/routine")
    Call<BasicResponse> createRoutine(@Body Routine routine);


    @GET("entities/question/{id}")
    Call<QuestionDetail> getQuestion(@Path("id") int id);

    @PUT("entities/question/")
    Call<BasicResponse> updateQuestion(@Body QuestionDetail questionDetail);

    @DELETE("entities/question/{id}")
    Call<BasicResponse> deleteQuestion(@Path("id") int id);

    @POST("entities/question")
    Call<BasicResponse> createQuestion(@Body Question question);





    //GROUP GOAL LINKS:
    @PUT("groupgoals")
    Call<BasicResponse> updateGG(@Body GroupGoalDetail userDTO);

    @GET("groupgoals/{groupgoal_id}")
    Call<GroupGoalDetail> getGGDetail(@Path("groupgoal_id") int goalId);

    @DELETE("groupgoals/{groupgoal_id}")
    Call<BasicResponse> deleteGG(@Path("groupgoal_id") int goalId);

    @PUT("groupgoals/{groupgoal_id}/members")
    Call<BasicResponse> addMember(@Body GroupGoalDetail goalDTO, UserDTO userDTO);

    @POST("groupgoals/{groupgoal_id}/token")
    Call<BasicResponse> createToken(@Path("groupgoal_id") int goalId);

    @POST("groupgoals/{user_id}")
    Call<BasicResponse> createGG(@Path("user_id") int user_id, @Body Goal ggDTO);

    @DELETE("groupgoals/{user_id}/{groupgoal_id}")
    Call<BasicResponse> leaveGG(@Path("user_id") int userId, @Path("groupgoal_id") int goalId);

    @POST("groupgoals/{user_id}/join")
    Call<BasicResponse> joinGG(@Path("user_id") int user_id,@Query("token") String token);

    @GET("groupgoals/created_by/{user_id}")
    Call<List<GroupGoalShort>> getAllGGCreatedByUser(@Path("user_id") int user_id, @Body GroupGoalDetail ggDTO);

    @GET("groupgoals/member_of/{user_id}")
    Call<List<GroupGoalShort>> getAllGGofUser(@Path("user_id") int user_id);

    @POST("groupgoals/subgoal")
    Call<BasicResponse> createSubgoalInGG(@Body Subgoal subgoal);

    //extends

    @PUT("goals/extend/{goal_id}")
    Call<BasicResponse> extendGoal(@Path("goal_id") int goal_id, @Body ExtendDeadline newDeadline);

    @PUT("entities/extend/{entiti_id}")
    Call<BasicResponse> extendEntity(@Path("entiti_id") int entity_id, @Body ExtendDeadline newDeadline);

    @PUT("subgoals/extend/{subgoal_id}")
    Call<BasicResponse> extendSubgoal(@Path("subgoal_id") int subgoal_id, @Body ExtendDeadline newDeadline);

}
//7YPxFmM3yTaAzaSi3Q61B
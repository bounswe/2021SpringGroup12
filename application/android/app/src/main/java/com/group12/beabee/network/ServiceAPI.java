package com.group12.beabee.network;

import com.group12.beabee.models.requests.ExtendDeadline;
import com.group12.beabee.models.requests.Goal;
import com.group12.beabee.models.GroupGoalDetail;
import com.group12.beabee.models.requests.Link;
import com.group12.beabee.models.requests.LoginRequest;
import com.group12.beabee.models.requests.Question;
import com.group12.beabee.models.requests.Reflection;
import com.group12.beabee.models.requests.Routine;
import com.group12.beabee.models.requests.SignUpRequest;
import com.group12.beabee.models.requests.Subgoal;
import com.group12.beabee.models.requests.Task;
import com.group12.beabee.models.responses.ActivityStream;
import com.group12.beabee.models.responses.Analytics;
import com.group12.beabee.models.responses.BasicResponse;
import com.group12.beabee.models.responses.EntityShort;
import com.group12.beabee.models.responses.GoalDetail;
import com.group12.beabee.models.responses.GoalShort;
import com.group12.beabee.models.responses.GroupGoalShort;
import com.group12.beabee.models.responses.PrototypeGoalDetail;
import com.group12.beabee.models.responses.QuestionDetail;
import com.group12.beabee.models.responses.ReflectionDetail;
import com.group12.beabee.models.responses.RoutineDetail;
import com.group12.beabee.models.responses.SubgoalDetail;
import com.group12.beabee.models.responses.LoginResponse;
import com.group12.beabee.models.responses.TaskDetail;
import com.group12.beabee.models.responses.SignUpResponse;
import com.group12.beabee.models.responses.UserDTO;
import com.group12.beabee.models.responses.UserSearchData;

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

    @PUT("goals/complete/{goal_id}")
    Call<BasicResponse> completeGoal(@Path("goal_id") int subgoalId);

    @PUT("goals")
    Call<BasicResponse> updateGoalOfUser(@Body GoalDetail goalDetail);

    @PUT("goals/{goal_id}/tag")
    Call<BasicResponse> addTagToGoal(@Path("goal_id") int goalId, @Body List<String> tags);

    @PUT("goals/{goal_id}/removetag/{tag}")
    Call<BasicResponse> removeTagFromGoal(@Path("goal_id") int goalId, @Path("tag") String tag);

    @PUT("groupgoals/{goal_id}/tag")
    Call<BasicResponse> addTagToGroupGoal(@Path("goal_id") int goalId, @Body List<String> tags);

    @PUT("groupgoals/{goal_id}/removetag/{tag}")
    Call<BasicResponse> removeTagFromGroupGoal(@Path("goal_id") int goalId, @Path("tag") String tag);

    @GET("entities/user/{user_id}")
    Call<List<EntityShort>> getEntitiesOfUser(@Path("user_id") int userId);

    @GET("entities/goal/{goal_id}")
    Call<List<EntityShort>> getEntitiesOfGoal(@Path("goal_id") int goalId);
    @GET("entities/groupgoal/{goal_id}")
    Call<List<EntityShort>> getEntitiesOfGroupGoal(@Path("goal_id") int groupGoalId);

    @POST("entities/{id}/link")
    Call<BasicResponse> linkEntities(@Path("id") int id, @Body Link link);

    /////

    @GET("subgoals/{id}")
    Call<SubgoalDetail> getSubgoal(@Path("id") int id);

    @PUT("subgoals")
    Call<BasicResponse> updateSubgoal(@Body SubgoalDetail subgoalDetail);

    @PUT("subgoals/complete/{subgoal_id}/{rating}")
    Call<BasicResponse> completeSubgoal(@Path("subgoal_id") int subgoalId, @Path("rating") int rating);

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

    @PUT("entities/completetask/{task_id}/{rating}")
    Call<BasicResponse> completeTask(@Path("task_id") int taskId, @Path("rating") int rating);


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

    @PUT("entities/completeroutine/{routine_id}/{rating}")
    Call<BasicResponse> completeRoutine(@Path("routine_id") int routineId, @Path("rating") int rating);

    @PUT("entities/rate/{routine_id}/{rating}")
    Call<BasicResponse> rateRoutine(@Path("routine_id") int routineId, @Path("rating") int rating);


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


    @PUT("goals/extend/{goal_id}")
    Call<BasicResponse> extendGoal(@Path("goal_id") int goal_id, @Body ExtendDeadline newDeadline);

    @PUT("entities/extend/{entiti_id}")
    Call<BasicResponse> extendEntity(@Path("entiti_id") int entity_id, @Body ExtendDeadline newDeadline);

    @PUT("subgoals/extend/{subgoal_id}")
    Call<BasicResponse> extendSubgoal(@Path("subgoal_id") int subgoal_id, @Body ExtendDeadline newDeadline);

    //ANALYTICS
    @GET("users/analytics/{user_id}")
    Call<Analytics>getUserAnalytics(@Path("user_id") int user_id);


    //PROTOTYPE-MARKET PLACE

    @GET("/v2/prototypes/")
    Call<List<PrototypeGoalDetail>> getMarketPlaceData();

    @GET("/v2/prototypes/{id}")
    Call<PrototypeGoalDetail>getProGoal(@Path("id") int public_goal_id);

    @POST("/v2/prototypes/publish/{id}")
    Call<BasicResponse> publishGoal(@Path("id") int public_goal_id);

    @POST("/v2/prototypes/republish/{id}")
    Call<BasicResponse> republishGoal(@Path("id") int public_goal_id);

    @POST("/v2/prototypes/unpublish/{id}")
    Call<BasicResponse> unpublishGoal(@Path("id") int public_goal_id);

    @POST("/v2/goals/copy_prototype/{user_id}/{prototype_id}")
    Call<BasicResponse> copyGoal(@Path("user_id") int user_id,@Path("prototype_id") int prototype_id);

    //SEARCH IN MARKET PLACE
    @GET("/v2/prototypes/search/")
    Call<List<GoalDetail>>getProGoalSearch(@Query("query") String query);

    @GET("/v2/prototypes/search/{tag}")
    Call<List<GoalDetail>>getProGoalTagSearch(@Path("tag") String tag);


    //FEED
    @GET("/v2/activitystreams/{userId}")
    Call<List<ActivityStream>>getActivityStream(@Path("userId") int userId);



    //SEARCH USER

    @GET("/v2/users/search/{query}")
    Call<List<UserSearchData>>getUserSearch(@Path("query") String query);

    @GET("/v2/users/get/{id}")
    Call<UserSearchData>getUser(@Path("id") int id);

    @POST("/v2/users/{userId}/follow/{targetId}")
    Call<BasicResponse>followUser(@Path("userId") int userId, @Path("targetId") int targetId);

    @POST("/v2/users/{userId}/unfollow/{targetId}")
    Call<BasicResponse>unfollowUser(@Path("userId") int userId, @Path("targetId") int targetId);

    @GET("/v2/users/{userId}/followings")
    Call<List<UserSearchData>>getFollowings(@Path("userId") int userId);

    @GET("/v2/users/{userId}/followers")
    Call<List<UserSearchData>>getFollowers(@Path("userId") int userId);




}
//7YPxFmM3yTaAzaSi3Q61B


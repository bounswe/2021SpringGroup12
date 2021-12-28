package com.group12.beabee.network.mocking;


import com.group12.beabee.models.GroupGoalDetail;
import com.group12.beabee.models.requests.ExtendDeadline;
import com.group12.beabee.models.requests.Goal;
import com.group12.beabee.models.requests.LoginRequest;
import com.group12.beabee.models.requests.Question;
import com.group12.beabee.models.requests.Reflection;
import com.group12.beabee.models.requests.Routine;
import com.group12.beabee.models.requests.SignUpRequest;
import com.group12.beabee.models.requests.Subgoal;
import com.group12.beabee.models.requests.Task;
import com.group12.beabee.models.responses.Analytics;
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
    public Call<GoalDetail> getGoalDetail(int goalId) {
        GoalDetail temp = new GoalDetail();
        temp.id = 1;
        temp.title = "title"+1;
        temp.description = "description"+1;
        temp.goalType = "GOAL";
        temp.createdAt = "2021-11-15T18:01:25.047Z";
        temp.deadline = "2021-11-15T18:01:25.047Z";

        return new MockCall<GoalDetail>() {
            @Override
            public void enqueue(Callback<GoalDetail> callback) {
                callback.onResponse(this, Response.success(temp));
            }
        };
    }

    @Override
    public Call<BasicResponse> createGoalOfUser(int userId, Goal goal) {
        return returnBasicResponse();
    }

    @Override
    public Call<BasicResponse> completeGoal(int subgoalId) {
        return returnBasicResponse();
    }

    @Override
    public Call<BasicResponse> updateGoalOfUser(GoalDetail goalDetail) {
        return returnBasicResponse();
    }

    @Override
    public Call<List<EntityShort>> getEntitiesOfUser(int userId) {
        List<EntityShort> tempList = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            EntityShort temp = new EntityShort();
            temp.id = i;
            temp.title = "title"+i;
            temp.description = "description"+i;
            temp.entityType = "TASK";
            temp.deadline = "2021-12-11T16:50:33.578Z";
            tempList.add(temp);
        }
        return new MockCall<List<EntityShort>>() {
            @Override
            public void enqueue(Callback<List<EntityShort>> callback) {
                callback.onResponse(this, Response.success(tempList));
            }
        };
    }

    @Override
    public Call<List<EntityShort>> getEntitiesOfGoal(int goalId) {
        List<EntityShort> tempList = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            EntityShort temp = new EntityShort();
            temp.id = i;
            temp.title = "title"+i;
            temp.description = "description"+i;
            temp.entityType = "TASK";
            temp.deadline = "2021-12-11T16:50:33.578Z";
            tempList.add(temp);
        }
        return new MockCall<List<EntityShort>>() {
            @Override
            public void enqueue(Callback<List<EntityShort>> callback) {
                callback.onResponse(this, Response.success(tempList));
            }
        };
    }

    @Override
    public Call<BasicResponse> linkEntities(int id, int childId) {
        return returnBasicResponse();
    }

    //TODO implement these

    @Override
    public Call<SubgoalDetail> getSubgoal(int id) {
        return null;
    }

    @Override
    public Call<BasicResponse> updateSubgoal(SubgoalDetail subgoalDetail) {
        return returnBasicResponse();
    }

    @Override
    public Call<BasicResponse> completeSubgoal(int subgoalId, int rating) {
        return returnBasicResponse();
    }

    @Override
    public Call<BasicResponse> deleteSubgoal(int id) {
        return returnBasicResponse();
    }

    @Override
    public Call<BasicResponse> createSubgoalUnderSubgoal(Subgoal subgoal) {
        return returnBasicResponse();
    }

    @Override
    public Call<BasicResponse> createSubgoalUnderGoal(Subgoal subgoal) {
        return returnBasicResponse();
    }

    @Override
    public Call<TaskDetail> getTask(int id) {
        return null;
    }

    @Override
    public Call<BasicResponse> updateTask(TaskDetail taskDetail) {
        return returnBasicResponse();
    }

    @Override
    public Call<BasicResponse> deleteTask(int id) {
        return returnBasicResponse();
    }

    @Override
    public Call<BasicResponse> createTask(Task task) {
        return returnBasicResponse();
    }

    @Override
    public Call<BasicResponse> completeTask(int taskId, int rating) {
        return returnBasicResponse();
    }

    @Override
    public Call<ReflectionDetail> getReflection(int id) {
        return null;
    }

    @Override
    public Call<BasicResponse> updateReflection(ReflectionDetail reflectionDetail) {
        return returnBasicResponse();
    }

    @Override
    public Call<BasicResponse> deleteReflection(int id) {
        return returnBasicResponse();
    }

    @Override
    public Call<BasicResponse> createReflection(Reflection reflection) {
        return returnBasicResponse();
    }

    @Override
    public Call<RoutineDetail> getRoutine(int id) {
        return null;
    }

    @Override
    public Call<BasicResponse> updateRoutine(RoutineDetail routineDetail) {
        return returnBasicResponse();
    }

    @Override
    public Call<BasicResponse> deleteRoutine(int id) {
        return returnBasicResponse();
    }

    @Override
    public Call<BasicResponse> createRoutine(Routine routine) {
        return returnBasicResponse();
    }

    @Override
    public Call<BasicResponse> completeRoutine(int routineId, int rating) {
        return returnBasicResponse();
    }

    @Override
    public Call<BasicResponse> rateRoutine(int routineId, int rating) {
        return returnBasicResponse();
    }

    @Override
    public Call<QuestionDetail> getQuestion(int id) {
        return null;
    }

    @Override
    public Call<BasicResponse> updateQuestion(QuestionDetail questionDetail) {
        return returnBasicResponse();
    }

    @Override
    public Call<BasicResponse> deleteQuestion(int id) {
        return returnBasicResponse();
    }

    @Override
    public Call<BasicResponse> createQuestion(Question question) {
        return returnBasicResponse();
    }

    @Override
    public Call<BasicResponse> extendEntity(int entity_id, ExtendDeadline newDeadline) {
        return returnBasicResponse();
    }



    @Override
    public Call<BasicResponse> deleteGG(int goalId) {
        return null;
    }

    @Override
    public Call<BasicResponse> addMember(GroupGoalDetail goalDTO, UserDTO userDTO) {
        return null;
    }

    @Override
    public Call<BasicResponse> createToken(int goalId) {
        return null;
    }

    @Override
    public Call<BasicResponse> createGG(int user_id, Goal ggDTO) {
        return returnBasicResponse();
    }

    @Override
    public Call<BasicResponse> leaveGG(int goalId, int userId) {
        return null;
    }

    @Override
    public Call<BasicResponse> joinGG(int user_id,String token) {
        return null;
    }

    @Override
    public Call<BasicResponse> updateGG(GroupGoalDetail userDTO) {
        return null;
    }

    @Override
    public Call<GroupGoalDetail> getGGDetail(int goalId) {
        return null;
    }

    @Override
    public Call<List<GroupGoalShort>> getAllGGCreatedByUser(int user_id, GroupGoalDetail ggDTO) {
        return null;
    }

    @Override
    public Call<List<GroupGoalShort>> getAllGGofUser(int user_id) {
        return null;
    }

    @Override
    public Call<BasicResponse> createSubgoalInGG(Subgoal subgoal) {
        return null;
    }

    @Override
    public Call<BasicResponse> extendGoal(int goal_id, ExtendDeadline newDeadline) {
        return returnBasicResponse();
    }

    @Override
    public Call<BasicResponse> extendSubgoal(int subgoal_id, ExtendDeadline newDeadline) {
        return returnBasicResponse();
    }
      
    @Override
    public Call<Analytics> getUserAnalytics(int user_id) {
        return null;
    }

    @Override
    public Call<List<GoalDetail>> getMarketPlaceData() {
        return null;
    }

    @Override
    public Call<GoalDetail> getProGoal(int public_goal_id) {
        return null;
    }

    @Override
    public Call<List<EntityShort>> getProEntity(int public_entity_id) {
        return null;
    }

    @Override
    public Call<SubgoalDetail> getProSubgoal(int public_subgoal_id) {
        return null;
    }

    @Override
    public Call<BasicResponse> publishGoal(int public_goal_id) {
        return null;
    }

    @Override
    public Call<BasicResponse> copyGoal(int user_id, int prototype_id) {
        return null;
    }

    @Override
    public Call<List<GoalDetail>> getProGoalSearch(String query) {
        return null;
    }

    @Override
    public Call<List<GoalDetail>> getProGoalTagSearch(String tag) {
        return null;
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

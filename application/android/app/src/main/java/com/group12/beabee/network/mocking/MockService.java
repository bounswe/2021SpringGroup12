package com.group12.beabee.network.mocking;

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
    public Call<List<GoalDTO>> getGoalsOfUser(int userId) {
        List<GoalDTO> tempList = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            GoalDTO temp = new GoalDTO();
            temp.id = i;
            temp.title = "title"+i;
            temp.description = "description"+i;
            temp.goalType = "GOAL";
            temp.createdAt = "2021-11-15T18:01:25.047Z";
            temp.deadLine = "2021-11-15T18:01:25.047Z";
            tempList.add(temp);
        }
        return new MockCall<List<GoalDTO>>() {
            @Override
            public void enqueue(Callback<List<GoalDTO>> callback) {
                callback.onResponse(this, Response.success(tempList));
            }
        };
    }

    @Override
    public Call<GoalDTO> getGoalDetail(int goalId) {
        GoalDTO temp = new GoalDTO();
        temp.id = 1;
        temp.title = "title"+1;
        temp.description = "description"+1;
        temp.goalType = "GOAL";
        temp.createdAt = "2021-11-15T18:01:25.047Z";
        temp.deadLine = "2021-11-15T18:01:25.047Z";

        return new MockCall<GoalDTO>() {
            @Override
            public void enqueue(Callback<GoalDTO> callback) {
                callback.onResponse(this, Response.success(temp));
            }
        };
    }

    @Override
    public Call<BasicResponse> createGoalOfUser(int userId, GoalDTO goal) {
        return returnBasicResponse();
    }

    @Override
    public Call<BasicResponse> updateGoalOfUser(GoalDTO userDTO) {
        return returnBasicResponse();
    }

    @Override
    public Call<List<Entity>> getSublinksForEntity(int entityId) {
        List<Entity> tempList = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            Entity temp = new Entity();
            temp.id = i;
            temp.title = "title"+i;
            temp.description = "description"+i;
            tempList.add(temp);
        }
        return new MockCall<List<Entity>>() {
            @Override
            public void enqueue(Callback<List<Entity>> callback) {
                callback.onResponse(this, Response.success(tempList));
            }
        };
    }

    @Override
    public Call<List<Entity>> getSublinksForGoal(int goalId) {
        List<Entity> tempList = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            Entity temp = new Entity();
            temp.id = i;
            temp.title = "title"+i;
            temp.description = "description"+i;
            tempList.add(temp);
        }
        return new MockCall<List<Entity>>() {
            @Override
            public void enqueue(Callback<List<Entity>> callback) {
                callback.onResponse(this, Response.success(tempList));
            }
        };
    }

    @Override
    public Call<List<Entity>> getEntitiesOfUser(int userId) {
        List<Entity> tempList = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            Entity temp = new Entity();
            temp.id = i;
            temp.title = "title"+i;
            temp.description = "description"+i;
            tempList.add(temp);
        }
        return new MockCall<List<Entity>>() {
            @Override
            public void enqueue(Callback<List<Entity>> callback) {
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
    public Call<SubgoalDTO> getSubgoal(int id) {
        return null;
    }

    @Override
    public Call<BasicResponse> updateSubgoal(int id, SubgoalDTO subgoalDTO) {
        return null;
    }

    @Override
    public Call<BasicResponse> deleteSubgoal(int id) {
        return null;
    }

    @Override
    public Call<BasicResponse> createSubgoal(SubgoalDTO subgoalDTO) {
        return null;
    }

    @Override
    public Call<TaskDTO> getTask(int id) {
        return null;
    }

    @Override
    public Call<BasicResponse> updateTask(int id, TaskDTO taskDTO) {
        return null;
    }

    @Override
    public Call<BasicResponse> deleteTask(int id) {
        return null;
    }

    @Override
    public Call<BasicResponse> createTask(TaskDTO taskDTO) {
        return null;
    }

    @Override
    public Call<ReflectionDTO> getReflection(int id) {
        return null;
    }

    @Override
    public Call<BasicResponse> updateReflection(int id, ReflectionDTO reflectionDTO) {
        return null;
    }

    @Override
    public Call<BasicResponse> deleteReflection(int id) {
        return null;
    }

    @Override
    public Call<BasicResponse> createReflection(ReflectionDTO reflectionDTO) {
        return null;
    }

    @Override
    public Call<RoutineDTO> getRoutine(int id) {
        return null;
    }

    @Override
    public Call<BasicResponse> updateRoutine(int id, RoutineDTO routineDTO) {
        return null;
    }

    @Override
    public Call<BasicResponse> deleteRoutine(int id) {
        return null;
    }

    @Override
    public Call<BasicResponse> createRoutine(RoutineDTO routineDTO) {
        return null;
    }

    @Override
    public Call<QuestionDTO> getQuestion(int id) {
        return null;
    }

    @Override
    public Call<BasicResponse> updateQuestion(int id, QuestionDTO questionDTO) {
        return null;
    }

    @Override
    public Call<BasicResponse> deleteQuestion(int id) {
        return null;
    }

    @Override
    public Call<BasicResponse> createQuestion(QuestionDTO questionDTO) {
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

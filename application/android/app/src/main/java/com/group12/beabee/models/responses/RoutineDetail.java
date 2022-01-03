package com.group12.beabee.models.responses;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class RoutineDetail implements Serializable {
    public int id;
    @SerializedName("goal_id")
    public int goalId;
    @SerializedName("subgoal_id")
    public int subgoalId;
    @SerializedName("groupgoal_id")
    public int groupGoalId;
    public String title;
    public String description;
    public String entitiType;
    public boolean isDone;
    public double rating;
    public String createdAt;
    public List <String> deadline;
    public int period;
    @SerializedName("sublinked_entities")
    public List<EntityShort> entities;
}

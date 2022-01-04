package com.group12.beabee.models.responses;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class TaskDetail implements Serializable {
    public int id;
    @SerializedName("goal_id")
    public int goalId;
    @SerializedName("subgoal_id")
    public int subgoalId;
    @SerializedName("groupgoal_id")
    public int groupGoalId;
    public String title;
    public String description;
    public double rating;
    public String entitiType;
    public boolean isDone;
    public String createdAt;
    public String deadline;
    @SerializedName("sublinked_entities")
    public List<EntityShort> entities;
}

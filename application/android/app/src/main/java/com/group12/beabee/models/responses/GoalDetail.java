package com.group12.beabee.models.responses;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class GoalDetail implements Serializable {
    public int id;
    public String title;
    @SerializedName("user_id")
    public int userId;
    public String description;
    /** Can be "GROUPGOAL" or "GOAL"*/
    public String goalType;
    public boolean isDone;
    public String createdAt;
    public String deadline;
    @SerializedName("linkedEntities")
    public List<EntityShort> entities;
    public List<SubgoalShort> subgoals;
    public List<String> tags;
    public String username;
    public boolean isPublished;
    public int download_count;
}

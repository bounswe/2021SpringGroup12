package com.group12.beabee.models.responses;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class SubgoalDetail implements Serializable {
    public int id;
    @SerializedName("main_goal_id")
    public int mainGoalId;
    @SerializedName("parent_subgoal_id")
    public int parentSubgoalId;
    @SerializedName("main_groupgoal_id")
    public int mainGroupGoalId;
    public int rating;
    public String title;
    public String description;
    public boolean isDone;
    public String createdAt;
    public String deadline;
    public List<EntityShort> entities;
    public List<SubgoalShort> sublinks;
}

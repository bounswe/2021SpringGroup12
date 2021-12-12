package com.group12.beabee.models.requests;

import com.google.gson.annotations.SerializedName;

public class Subgoal {
    public String title;
    public String description;
    public String deadLine;
    @SerializedName("main_goal_id")
    public int mainGoalId;
    @SerializedName("main_groupgoal_id")
    public int mainGroupGoalId;
    @SerializedName("parent_subgoal_id")
    public int parentSubgoalId;
}

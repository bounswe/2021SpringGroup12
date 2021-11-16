package com.group12.beabee.models.responses;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ReflectionDTO implements Serializable {
    public String title;
    public String description;
    @SerializedName("entitiType")
    /** Can be SUBGOAL, TASK, ROUTINE, QUESTION, REFLECTION*/
    public String entityType;
    public int id;
    public boolean isDone;
    @SerializedName("mainGoal_id")
    public int mainGoalId;
    public String createdAt;
    public String deadline = null;
    public String rating = null;
    public String period = null;
    public String created = null;
}

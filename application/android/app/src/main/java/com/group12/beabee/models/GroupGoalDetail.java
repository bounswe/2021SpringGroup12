package com.group12.beabee.models;

import com.google.gson.annotations.SerializedName;
import com.group12.beabee.models.responses.EntityShort;
import com.group12.beabee.models.responses.SubgoalShort;

import java.io.Serializable;
import java.util.List;

public class GroupGoalDetail implements Serializable {
    public int id;
    public String title;
    public String description;
    /** Can be "GROUPGOAL" or "GOAL"*/
    public String goalType;
    public List<User> members;
    public String token;
    public int user_id;
    public boolean isDone;
    public String createdAt;
    public String deadline;
    @SerializedName("linkedEntities")
    public List<EntityShort> entities;
    public List<SubgoalShort> subgoals;
}
package com.group12.beabee.models;

import com.group12.beabee.models.responses.Entity;

import java.io.Serializable;
import java.util.List;

public class GroupGoalDTO implements Serializable {
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
    public String deadLine;
    public List<Entity> entities;
    public List<Entity> subgoals;
}
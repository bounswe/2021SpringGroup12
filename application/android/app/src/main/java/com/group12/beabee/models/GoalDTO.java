package com.group12.beabee.models;

import java.io.Serializable;

public class GoalDTO implements Serializable {
    public int id;
    public String title;
    public String description;
    /** Can be "GROUPGOAL" or "GOAL"*/
    public String goalType;
    public boolean isDone;
    public String createdAt;
    public String deadLine;
}
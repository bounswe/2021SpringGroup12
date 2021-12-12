package com.group12.beabee.models.responses;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class EntityShort implements Serializable {
    public int id;
    public String title;
    public String description;
    @SerializedName("entitiType")
    /** Can be SUBGOAL, TASK, ROUTINE, QUESTION, REFLECTION*/
    public String entityType;
    public String deadline;
}
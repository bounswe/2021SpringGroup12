package com.group12.beabee.models.responses;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class ProGoalDetail implements Serializable {

    public int id;
    public String title;
    public String description;
    public List<EntityShort> entities;
    public List<SubgoalShort> subgoals;
}

package com.group12.beabee.models.responses;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class Analytics implements Serializable {
    public int activeGoalCount;
    public int averageCompletionTimeOfGoalsInMiliseconds;
    public int averageExtensionCount;
    public int averageRating;
    public GoalShort bestGoal;
    public GoalShort longestGoal;
    public GoalShort shortestGoal;
    public GoalShort worstGoal;
    public int completedGoalCount;
}

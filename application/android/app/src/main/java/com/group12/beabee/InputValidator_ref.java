package com.group12.beabee;

import com.group12.beabee.models.responses.Analytics;
import com.group12.beabee.models.responses.GoalDetail;
import com.group12.beabee.models.responses.GoalShort;

import java.util.List;

public class InputValidator_ref {


    public static boolean IsNonEmptyGoalDetailList(List<GoalDetail> input){
        if (input==null)
            return false;
        return input.size() > 0;
    }
    public static boolean IsNonEmptyAnalytics(Analytics input){
        if (input==null)
            return false;
        return true;
    }
    public static boolean IsNonEmptyGoalShortList(List<GoalShort> input){//can be used in analytics
        if (input==null)
            return false;
        return input.size() > 0;
    }
    public static boolean IsNonEmptyGoalShort(GoalShort input){//can be used in analytics
        if (input==null)
            return false;
        return true;
    }
    public static boolean IsNonEmptyGoalDetail(GoalDetail input){//can be used in analytics
        if (input==null)
            return false;
        return true;
    }
    public static boolean IsInteger(String x){
        try
        {
            Integer.parseInt(x);
            return true;
        } catch (NumberFormatException ex)
        {
            return false;
        }
    }
}

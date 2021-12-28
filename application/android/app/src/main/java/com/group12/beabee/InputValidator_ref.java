package com.group12.beabee;

import com.group12.beabee.models.responses.GoalDetail;
import com.group12.beabee.models.responses.GoalShort;

import java.util.List;

public class InputValidator_ref {


    public static boolean IsNonEmptyGoalDetail(List<GoalDetail> input){
        if (input==null)
            return false;
        return input.size() > 0;
    }
    public static boolean IsNonEmptyGoalShort(List<GoalShort> input){
        if (input==null)
            return false;
        return input.size() > 0;
    }

    public static boolean IsTextMinimumLength(String input, int minimumLength){
        if (input==null)
            return false;

        return input.length() >= minimumLength;
    }

    public static boolean IsTextEmailFormat(String input){
        if (input==null)
            return false;

        boolean at = input.contains("@");
        if (!at)
            return false;

        int idxAt = input.indexOf('@');

        if (idxAt<1 || idxAt >= input.length()-1)
            return false;

        String first = input.substring(0,idxAt);
        String domain = input.substring(idxAt+1);

        if (!domain.contains("."))
            return false;

        if (domain.charAt(domain.length()-1) == '.')
            return false;

        if (domain.charAt(0) == '.')
            return false;

        return true;
    }
}

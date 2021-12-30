package com.group12.beabee;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.group12.beabee.models.responses.Analytics;
import com.group12.beabee.models.responses.GoalDetail;
import com.group12.beabee.models.responses.GoalShort;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class InputValidatorUnitTest_ref {

    @Test
    public void inputEmptyGoalDetailTest(){
        List<GoalDetail> empty_goals =new ArrayList<GoalDetail>();
        List<GoalDetail> non_empty_goals = new ArrayList<GoalDetail>();
        GoalDetail a= new GoalDetail();
        a.id=1;
        a.title="goal1";
        a.userId=3;
        a.description="goal1 description";
        non_empty_goals.add(a);
        GoalDetail b = new GoalDetail();
        b.id=2;
        b.title="goal2";
        b.userId=3;
        b.description="goal2 description";
        b.goalType="GOAL";
        non_empty_goals.add(b);
        GoalDetail c=null;
        GoalDetail d=null;
        List<GoalDetail> non_empty_null_goals = new ArrayList<GoalDetail>();
        non_empty_null_goals.add(c);
        non_empty_null_goals.add(d);
        assertFalse(InputValidator_ref.IsNonEmptyGoalDetailList(empty_goals));
        assertTrue(InputValidator_ref.IsNonEmptyGoalDetailList(non_empty_goals));
        assertTrue(InputValidator_ref.IsNonEmptyGoalDetailList(non_empty_null_goals));
    }
    public void inputEmptyAnalyticsTest(){
        Analytics a= new Analytics();
        Analytics b= new Analytics();
        b.bestGoal=new GoalShort();
        assertFalse(InputValidator_ref.IsNonEmptyAnalytics(a));
        assertTrue(InputValidator_ref.IsNonEmptyAnalytics(b));
    }
    public void inputNonEmptyGoalShortTest(){
        GoalShort a=null;
        GoalShort b=new GoalShort();
        b.title="goal Short test";
        b.description="Goal short description test";
        assertFalse(InputValidator_ref.IsNonEmptyGoalShort(a));
        assertTrue(InputValidator_ref.IsNonEmptyGoalShort(b));
    }
    @Test
    public void inputEmptyGoalShortTest(){
        List<GoalShort> empty_goals =new ArrayList<GoalShort>();
        List<GoalShort> non_empty_goals = new ArrayList<GoalShort>();
        GoalShort a= new GoalShort();
        a.id=1;
        a.title="goal1";
        a.description="goal1 description";
        non_empty_goals.add(a);
        GoalShort b = new GoalShort();
        b.id=2;
        b.title="goal2";
        b.description="goal2 description";
        non_empty_goals.add(b);
        GoalShort c=null;
        GoalShort d=null;
        List<GoalShort> non_empty_null_goals = new ArrayList<GoalShort>();
        non_empty_null_goals.add(c);
        non_empty_null_goals.add(d);
        assertFalse(InputValidator_ref.IsNonEmptyGoalShortList(empty_goals));
        assertTrue(InputValidator_ref.IsNonEmptyGoalShortList(non_empty_goals));
        assertTrue(InputValidator_ref.IsNonEmptyGoalShortList(non_empty_null_goals));
    }
    @Test
    public void inputEmptyTextTest(){
        assertFalse(InputValidator_ref.IsInteger(null));
        assertTrue(InputValidator_ref.IsInteger("34"));
        assertTrue(InputValidator_ref.IsInteger("0"));
        assertFalse(InputValidator_ref.IsInteger("576fhfh"));
    }
}
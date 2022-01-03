package com.group12.beabee;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.gson.annotations.SerializedName;
import com.group12.beabee.models.responses.EntityShort;
import com.group12.beabee.models.responses.GoalDetail;
import com.group12.beabee.models.responses.GoalShort;
import com.group12.beabee.models.responses.SubgoalShort;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

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
        assertFalse(InputValidator_ref.IsNonEmptyGoalDetail(empty_goals));
        assertTrue(InputValidator_ref.IsNonEmptyGoalDetail(non_empty_goals));
        assertTrue(InputValidator_ref.IsNonEmptyGoalDetail(non_empty_null_goals));
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
        assertFalse(InputValidator_ref.IsNonEmptyGoalShort(empty_goals));

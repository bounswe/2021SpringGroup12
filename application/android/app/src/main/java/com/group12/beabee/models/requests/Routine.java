package com.group12.beabee.models.requests;

import com.group12.beabee.models.LinkingType;
import com.group12.beabee.models.ParentType;

public class Routine {
    public String title;
    public String description;
    public String deadline;
    public int period;
    public LinkingType initialLinkType;
    public int initialParentId;
    public ParentType goalType;
    public int goalId;
}

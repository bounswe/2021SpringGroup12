package com.group12.beabee.models.requests;

import com.google.gson.annotations.SerializedName;
import com.group12.beabee.models.ParentType;

public class Reflection {
    @SerializedName("parent_id")
    public int parentId;
    public String title;
    public String description;
    public ParentType parentType;
}

package com.group12.beabee.models.requests;

import com.google.gson.annotations.SerializedName;
import com.group12.beabee.models.LinkingType;

public class Link {
    @SerializedName("childId")
    public int parentId;
    @SerializedName("childType")
    public LinkingType parentType;
}

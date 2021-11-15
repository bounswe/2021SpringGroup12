package com.group12.beabee.models.responses;

import com.google.gson.annotations.SerializedName;

public class UserDTO {
    public String email;
    public String username;
    @SerializedName("user_id")
    public int userId;
}

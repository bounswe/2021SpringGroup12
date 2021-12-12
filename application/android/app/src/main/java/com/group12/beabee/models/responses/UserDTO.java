package com.group12.beabee.models.responses;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class UserDTO implements Serializable {
    public String email;
    public String username;
    @SerializedName("user_id")
    public int userId;
}

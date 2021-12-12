package com.group12.beabee.models.responses;

import com.google.gson.annotations.SerializedName;

public class LoginResponse {
    public String message;
    /** Can be "SUCCESS", "ERROR" or "INFO" */
    public String messageType;
    public String jwt;
    @SerializedName("userCredentialsGetDTO")
    public UserDTO userDTO;
}


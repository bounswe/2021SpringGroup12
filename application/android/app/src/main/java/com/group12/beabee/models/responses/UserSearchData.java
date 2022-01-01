package com.group12.beabee.models.responses;

import com.group12.beabee.models.User;

import java.io.Serializable;

public class UserSearchData implements Serializable {
    public int id;
    public int followerCount;
    public int followingCount;
    public User userCredentials;
}

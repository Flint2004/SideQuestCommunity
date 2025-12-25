package com.sidequest.identity.interfaces.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class UserPublicDTO {
    private Long id;
    private String nickname;
    private String avatar;
    private String signature;
    private String role;
    private Integer followerCount;
    private Integer followingCount;
    private Integer totalLikedCount;
    private Integer postCount;
    
    @JsonProperty("isFollowing")
    private boolean isFollowing;
}


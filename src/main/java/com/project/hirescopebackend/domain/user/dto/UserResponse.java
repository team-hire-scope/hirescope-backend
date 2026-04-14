package com.project.hirescopebackend.domain.user.dto;

import com.project.hirescopebackend.domain.user.entity.User;
import com.project.hirescopebackend.domain.user.entity.UserRole;
import lombok.Getter;

@Getter
public class UserResponse {

    private final Long userId;
    private final String email;
    private final String name;
    private final UserRole role;

    private UserResponse(Long userId, String email, String name, UserRole role) {
        this.userId = userId;
        this.email = email;
        this.name = name;
        this.role = role;
    }

    public static UserResponse from(User user) {
        return new UserResponse(user.getId(), user.getEmail(), user.getName(), user.getRole());
    }
}

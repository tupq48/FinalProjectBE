package com.app.final_project.user.utils;

import com.app.final_project.enums.RoleType;
import com.app.final_project.user.User;
import com.app.final_project.user.dto.UserRequest;

public class UserUtils {
    public static User convertUserRequestToUser(UserRequest request) {
        return User.builder()
                .username(request.getUsername())
                .email(request.getGmail())
                .role(RoleType.USER)
                .phoneNumber(request.getPhoneNumber())
                .build();
    }
}

package com.app.final_project.auth;

import com.app.final_project.enums.RoleType;
import com.app.final_project.event.Event;
import com.app.final_project.event.dto.EventRequest;
import com.app.final_project.user.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;


public interface AuthService {
    public AuthResponse register(RegisterRequest request);

    public AuthResponse authenticate(AuthRequest response);
}

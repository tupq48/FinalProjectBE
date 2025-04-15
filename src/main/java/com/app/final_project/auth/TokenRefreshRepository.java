package com.app.final_project.auth;

import com.app.final_project.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TokenRefreshRepository extends JpaRepository<TokenRefresh, Long> {
    TokenRefresh findByToken(String token);

    TokenRefresh findByUser(User user);
}
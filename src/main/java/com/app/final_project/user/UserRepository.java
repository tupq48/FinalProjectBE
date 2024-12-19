package com.app.final_project.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer>, UserRepositoryCustom {

    Optional<User> findByUsername(String userName);
    boolean existsUserByUsername(@Param("username") String username);

    User findByEmail(String email);
}

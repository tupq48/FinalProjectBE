package com.app.final_project.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserImageRepository extends JpaRepository<UserImage, Integer> {

    List<UserImage> findAllByUserId(Integer userId);
}

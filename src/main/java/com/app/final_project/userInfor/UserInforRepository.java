package com.app.final_project.userInfor;

import com.app.final_project.user.User;
import com.app.final_project.user.UserRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserInforRepository extends JpaRepository<UserInfor, Integer> {
    @Query("SELECT ui FROM UserInfor ui WHERE ui.user.user_id = :userId")
    Optional<UserInfor> findByUser_User_Id(int userId);

}

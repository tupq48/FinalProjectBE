package com.app.final_project.registration;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RegistrationRepository extends JpaRepository<Registration, Integer> {

    Optional<Registration> findByEventIdAndUserId(Integer eventId, Integer userId);
    void deleteByEventIdAndUserId(Integer eventId, Integer userId);

}

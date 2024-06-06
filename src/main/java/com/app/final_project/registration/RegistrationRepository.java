package com.app.final_project.registration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RegistrationRepository extends JpaRepository<Registration, Integer>,RegistrationRepositoryCustom {

    Optional<Registration> findByEventIdAndUserId(Integer eventId, Integer userId);

    Optional<Registration> findByEventIdAndUserIdAndStatus(Integer eventId, Integer userId, RegistrationStatus status);

    void deleteByEventIdAndUserId(Integer eventId, Integer userId);

}

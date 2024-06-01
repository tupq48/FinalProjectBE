package com.app.final_project.event;

import com.app.final_project.enums.Gender;
import com.app.final_project.event.dto.EventDto;
import com.app.final_project.event.dto.EventRegistrationResponse;
import com.app.final_project.registration.RegistrationStatus;
import com.app.final_project.user.dto.UserDto;
import com.app.final_project.util.ConnectionProvider;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import org.hibernate.Session;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
@Repository
public class EventRepositoryImpl implements EventRepositoryCustom{
    @Autowired
    EntityManager entityManager;
    final String GET_EVENT_ATTENDED ="select ev.event_id, ev.event_name, ev.start_time, ev.end_time, ev.location,\n" +
            " ev.point, rg.status, rg.user_id, rg.image_url\n" +
            " from registrations rg \n" +
            " join events ev on ev.event_id= rg.event_id\n" +
            " where rg.user_id = :userId and rg.status!=\"cancelled\" and ev.is_deleted=0";
    private EventRegistrationResponse convertQueryToEventRegistrationResponse(Object[] row) {
        Integer eventId = (int) row[0];
        String eventName = (String) row[1];
        LocalDateTime startTime = (LocalDateTime) row[2];
        LocalDateTime endTime = (LocalDateTime) row[3];
        String location = (String) row[4];
        Integer point = (Integer) row[5];
        Integer userId = (int) row[6];
        RegistrationStatus status = RegistrationStatus.valueOf((String) row[7]);
        String imageUrl = (String) row[8];
        return EventRegistrationResponse.builder()
                .eventName(eventName)
                .startTime(startTime)
                .endTime(endTime)
                .location(location)
                .point(point)
                .eventId(eventId)
                .status(status)
                .userId(userId)
                .imageUrl(imageUrl)
                .build();
    }
    @Override
    public List<EventRegistrationResponse> getListEventAttended(Integer userId) {
        Session session = ConnectionProvider.openSession();
        Query query = session.createNativeQuery(GET_EVENT_ATTENDED)
                .setParameter("userId",userId)
                .addScalar("event_id", StandardBasicTypes.INTEGER)
                .addScalar("event_name", StandardBasicTypes.STRING)
                .addScalar("start_time", StandardBasicTypes.LOCAL_DATE_TIME)
                .addScalar("end_time", StandardBasicTypes.LOCAL_DATE_TIME)
                .addScalar("location", StandardBasicTypes.STRING)
                .addScalar("point", StandardBasicTypes.INTEGER)
                .addScalar("user_id", StandardBasicTypes.INTEGER)
                .addScalar("status", StandardBasicTypes.STRING)
                .addScalar("image_url", StandardBasicTypes.STRING);

        List<Object[]> rows = query.getResultList();

        var result = rows.stream()
                .map(this::convertQueryToEventRegistrationResponse)
                .toList();

        session.close();
        return result;
    }
}

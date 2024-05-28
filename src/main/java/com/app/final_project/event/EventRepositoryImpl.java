package com.app.final_project.event;

import com.app.final_project.enums.Gender;
import com.app.final_project.event.dto.EventDto;
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
    final String GET_EVENT_ATTENDED ="select ev.event_name, ev.start_time, ev.end_time, ev.location,\n" +
            " ev.point, rg.status, rg.user_id\n" +
            " from registrations rg \n" +
            " join events ev on ev.event_id= rg.event_id\n" +
            " where rg.user_id = :userId and rg.status=\"registered\"";
    private EventDto convertQueryToEventDto(Session session, Object[] row) {
        String name = (String) row[0];
        LocalDateTime startTime = (LocalDateTime) row[1];
        LocalDateTime endTime = (LocalDateTime) row[2];
        String location = (String) row[3];
        Integer point = (Integer) row[4];
        return EventDto.builder()
                .eventName(name)
                .startTime(startTime)
                .endTime(endTime)
                .location(location)
                .point(point)
                .build();
    }
    @Override
    public List<EventDto> getListEventAttended(Integer userId) {
        Session session = ConnectionProvider.openSession();
        Query query = session.createNativeQuery(GET_EVENT_ATTENDED)
                .setParameter("userId",userId)
                .addScalar("event_name", StandardBasicTypes.STRING)
                .addScalar("start_time", StandardBasicTypes.LOCAL_DATE_TIME)
                .addScalar("end_time", StandardBasicTypes.LOCAL_DATE_TIME)
                .addScalar("location", StandardBasicTypes.STRING)
                .addScalar("point", StandardBasicTypes.INTEGER);

        List<Object[]> rows = query.getResultList();

        List<EventDto> result = rows.stream()
                .map(row -> convertQueryToEventDto(session, row))
                .toList();

        session.close();
        return result;
    }
}

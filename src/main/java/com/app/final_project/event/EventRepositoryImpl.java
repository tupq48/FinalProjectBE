package com.app.final_project.event;

import com.app.final_project.enums.Gender;
import com.app.final_project.event.dto.EventDto;
import com.app.final_project.event.dto.EventRegistrationResponse;
import com.app.final_project.registration.RegistrationStatus;
import com.app.final_project.registration.dto.AttendanceImage;
import com.app.final_project.user.dto.UserDto;
import com.app.final_project.util.ConnectionProvider;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import org.hibernate.Session;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Comparator;
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
    final String GET_IMAGES_USER ="select rg.image_url as image_upload , ui.url_image as image_train\n" +
            "from registrations rg\n" +
            "join user_image ui\n" +
            "on ui.user_id =rg.user_id\n" +
            "where rg.user_id= :userId and rg.event_id= :eventId\n" +
            "limit 1;";
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
    private AttendanceImage convertQueryToAttendanceImage(Object[] row) {
        String uploadImageUrl = (String) row[0];
        String trainImageUrl = (String) row[1];

        return AttendanceImage.builder().
                uploadImageUrl(uploadImageUrl)
                .trainImageUrl(trainImageUrl)
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
                .sorted(Comparator.comparing(EventRegistrationResponse::getStartTime).reversed())
                .toList();

        session.close();
        return result;
    }

    @Override
    public List<AttendanceImage> getListImagesUser(Integer userId, Integer eventId) {
        Session session = ConnectionProvider.openSession();
        Query query = session.createNativeQuery(GET_IMAGES_USER)
                .setParameter("userId",userId)
                .setParameter("eventId", eventId)
                .addScalar("image_upload", StandardBasicTypes.STRING)
                .addScalar("image_train", StandardBasicTypes.STRING);


        List<Object[]> rows = query.getResultList();
        var result = rows.stream()
                .map(this::convertQueryToAttendanceImage)
                .toList();

        session.close();
        return result;
    }

    @Override
    public List<Object[]> getEventByStatus(Integer pageSize, Integer page, Integer filterBy) {
        String SQL_TOTAL_FILTER = null;
        String CONDITIONS = null;
        int offset = (page - 1) * pageSize;
        switch (filterBy) {
            case 0: {
                SQL_TOTAL_FILTER = "(SELECT COUNT(*) FROM events WHERE is_deleted != 1) AS total\n";
                CONDITIONS = "WHERE e.is_deleted != 1\n";
                break;
            }
            case 1: {
                SQL_TOTAL_FILTER = "(SELECT COUNT(*) FROM events WHERE is_deleted != 1 and start_time <= curdate() and end_time >= curdate()) AS total\n";
                CONDITIONS = "WHERE e.is_deleted != 1 and e.start_time <= curdate() and e.end_time >= curdate()\n";
                break;
            }
            case 2: {
                SQL_TOTAL_FILTER = "(SELECT COUNT(*) FROM events WHERE is_deleted != 1 and start_time > curdate()) AS total\n";
                CONDITIONS = "WHERE e.is_deleted != 1 and e.start_time > curdate()\n";
                break;
            }
            case 3: {
                SQL_TOTAL_FILTER = "(SELECT COUNT(*) FROM events WHERE is_deleted != 1 and end_time < curdate()) AS total\n";
                CONDITIONS = "WHERE e.is_deleted != 1 and e.end_time < curdate()\n";
                break;
            }
            default:
                break;
        }
        String GET_EVENT_BY_STATUS = "SELECT \n" +
                "    e.*,\n" +
                "    ei.list_url,\n" +
                SQL_TOTAL_FILTER +
                "FROM \n" +
                "    events e\n" +
                "LEFT JOIN (\n" +
                "    SELECT \n" +
                "        event_id AS id,\n" +
                "        GROUP_CONCAT(image_url ORDER BY image_url ASC SEPARATOR ', ') AS list_url\n" +
                "    FROM \n" +
                "        event_images\n" +
                "    GROUP BY \n" +
                "        event_id\n" +
                ") AS ei ON ei.id = e.event_id \n" +
                CONDITIONS +
                "ORDER BY \n" +
                "    e.start_time DESC\n" +
                "LIMIT :limit OFFSET :offset";
        Session session = ConnectionProvider.openSession();
        Query query = session.createNativeQuery(GET_EVENT_BY_STATUS)
                .setParameter("limit", pageSize)
                .setParameter("offset", offset)
                .addScalar("event_id", StandardBasicTypes.INTEGER)
                .addScalar("event_name", StandardBasicTypes.STRING)
                .addScalar("start_time", StandardBasicTypes.LOCAL_DATE_TIME)
                .addScalar("end_time", StandardBasicTypes.LOCAL_DATE_TIME)
                .addScalar("location", StandardBasicTypes.STRING)
                .addScalar("point", StandardBasicTypes.INTEGER)
                .addScalar("description", StandardBasicTypes.STRING)
                .addScalar("max_attenders", StandardBasicTypes.INTEGER)
                .addScalar("is_deleted", StandardBasicTypes.BOOLEAN)
                .addScalar("list_url", StandardBasicTypes.STRING)
                .addScalar("total", StandardBasicTypes.INTEGER);

        List<Object[]> rows = query.getResultList();
        return  rows;
    }
}

package com.app.final_project.user;

import com.app.final_project.enums.Gender;
import com.app.final_project.registration.RegistrationStatus;
import com.app.final_project.user.dto.TopUsersByEventPoints;
import com.app.final_project.user.dto.UserDto;
import com.app.final_project.util.ConnectionProvider;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import org.hibernate.Session;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserRepositoryImpl implements UserRepositoryCustom {
    @Autowired
    EntityManager entityManager;
    final String GET_ALL_USERS ="SELECT us.user_id, uif.fullname, us.email, us.phone_number, us.is_enabled, uif.url_avatar, uif.gender\n" +
            "FROM users us\n" +
            "LEFT JOIN user_info uif ON us.user_id = uif.user_id\n" +
            "where us.role ='USER'";
    final String GET_TOP_USERS_BY_EVENT_POINTS ="SELECT u.user_id, ui.fullname, ui.gender, u.email, count(e.event_id) amount_of_event, SUM(e.point) AS total_points, ui.url_avatar \n" +
            "FROM users u\n" +
            "JOIN user_info ui ON u.user_id = ui.user_id\n" +
            "JOIN registrations r ON u.user_id = r.user_id\n" +
            "JOIN events e ON r.event_id = e.event_id\n" +
            "WHERE r.status = 'attended' AND r.registration_time >= :startTime\n" +
            "    AND r.registration_time <= :endTime\n" +
            "GROUP BY u.user_id, u.username\n" +
            "ORDER BY total_points DESC\n" +
            "LIMIT 10";

    private UserDto convertQueryToUserDto(Session session, Object[] row) {
        Integer id = (Integer) row[0];
        String name = (String) row[1];
        String email = (String) row[2];
        String phoneNumber = (String) row[3];
        boolean isEnabled = (Boolean) row[4];
        String imageUrl = (String) row[5];
        String gender=(row[6]!= null)?(String)row[6]:null;
        Gender genderType = null;
        if(gender != null) genderType= (gender.equals("MALE"))? Gender.MALE:Gender.FEMALE;
        return UserDto.builder()
                .id(id)
                .name(name)
                .gmail(email)
                .phoneNumber(phoneNumber)
                .isEnabled(isEnabled)
                .urlImage(imageUrl)
                .gender(genderType)
                .build();
    }
    private TopUsersByEventPoints convertQueryToTopUsersByEventPointsDto(Object[] row) {
        Integer id = (Integer) row[0];
        String name = (String) row[1];
        String gender=(row[2]!= null)? (String)row[2]:null;
        String email = (String) row[3];
        Integer amountOfEvent = (Integer) row[4];
        Integer point = (Integer) row[5];
        String imageUrl = (String) row[6];
        Gender genderType = null;
        if(gender != null) genderType= (gender.equals("MALE"))? Gender.MALE:Gender.FEMALE;
        return TopUsersByEventPoints.builder()
                .id(id)
                .name(name)
                .gmail(email)
                .amountOfEvent(amountOfEvent)
                .point(point)
                .urlImage(imageUrl)
                .gender(genderType)
                .build();
    }
    @Override
    public List<UserDto> getAllUsersCustom() {
        Session session = ConnectionProvider.openSession();
        Query query = session.createNativeQuery(GET_ALL_USERS)
                .addScalar("user_id", StandardBasicTypes.INTEGER)
                .addScalar("fullname", StandardBasicTypes.STRING)
                .addScalar("email", StandardBasicTypes.STRING)
                .addScalar("phone_number", StandardBasicTypes.STRING)
                .addScalar("is_enabled", StandardBasicTypes.BOOLEAN)
                .addScalar("url_avatar", StandardBasicTypes.STRING)
                .addScalar("gender", StandardBasicTypes.STRING);

        List<Object[]> rows = query.getResultList();

        List<UserDto> result = rows.stream()
                .map(row -> convertQueryToUserDto(session, row))
                .toList();

        session.close();
        return result;
    }
    @Override
    public List<UserDto> getLisOfEventRegistrants(Integer id, Integer filterBy) {
        String GET_LIST_OF_EVENT_REGISTRANTS="select us.user_id, uif.fullname, us.email, " +
                "us.phone_number, us.is_enabled, uif.url_avatar, uif.gender \n" +
                "from registrations rg   \n" +
                "left join users us on rg.user_id = us.user_id \n" +
                "LEFT JOIN user_info uif ON us.user_id = uif.user_id \n" +
                "where rg.event_id= :eventId and rg.status = 'registered' and rg.image_url is not null";
        System.out.println("filterBy: " + filterBy);

        switch (filterBy){
            case 1: {
                GET_LIST_OF_EVENT_REGISTRANTS+=" AND isaipredicted =1";
                break;
            }
            case 2:{
                GET_LIST_OF_EVENT_REGISTRANTS+=" AND isaipredicted=0";
                break;
            }
            case 3: {
                 GET_LIST_OF_EVENT_REGISTRANTS="select us.user_id, uif.fullname, us.email, " +
                        "us.phone_number, us.is_enabled, uif.url_avatar, uif.gender \n" +
                        "from registrations rg   \n" +
                        "left join users us on rg.user_id = us.user_id \n" +
                        "LEFT JOIN user_info uif ON us.user_id = uif.user_id \n" +
                        "where rg.event_id= :eventId and rg.status = 'attended';";
            }
            default: break;
        }
        System.out.println("SQL: " + GET_LIST_OF_EVENT_REGISTRANTS);
        Session session = ConnectionProvider.openSession();
        Query query = session.createNativeQuery(GET_LIST_OF_EVENT_REGISTRANTS)
                    .setParameter("eventId",id)
                .addScalar("user_id", StandardBasicTypes.INTEGER)
                .addScalar("fullname", StandardBasicTypes.STRING)
                .addScalar("email", StandardBasicTypes.STRING)
                .addScalar("phone_number", StandardBasicTypes.STRING)
                .addScalar("is_enabled", StandardBasicTypes.BOOLEAN)
                .addScalar("url_avatar", StandardBasicTypes.STRING)
                .addScalar("gender", StandardBasicTypes.STRING);

        List<Object[]> rows = query.getResultList();

        List<UserDto> result = rows.stream()
                .map(row -> convertQueryToUserDto(session, row))
                .toList();

        session.close();
        return result;
    }

    @Override
    public List<TopUsersByEventPoints> getTopUsersByEventPoints(String startDate, String endDate) {
        Session session = ConnectionProvider.openSession();
        Query query = session.createNativeQuery(GET_TOP_USERS_BY_EVENT_POINTS)
                .setParameter("startTime", startDate)
                .setParameter("endTime", endDate)
                .addScalar("user_id", StandardBasicTypes.INTEGER)
                .addScalar("fullname", StandardBasicTypes.STRING)
                .addScalar("gender", StandardBasicTypes.STRING)
                .addScalar("email", StandardBasicTypes.STRING)
                .addScalar("amount_of_event", StandardBasicTypes.INTEGER)
                .addScalar("total_points", StandardBasicTypes.INTEGER)
                .addScalar("url_avatar", StandardBasicTypes.STRING);

        List<Object[]> rows = query.getResultList();

        List<TopUsersByEventPoints> result = rows.stream()
                .map(row -> convertQueryToTopUsersByEventPointsDto(row))
                .toList();

        session.close();
        return result;    }

}


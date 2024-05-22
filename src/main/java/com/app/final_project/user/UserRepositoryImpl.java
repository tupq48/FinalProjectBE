package com.app.final_project.user;

import com.app.final_project.enums.Gender;
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
            "LEFT JOIN user_image ui ON us.user_id = ui.user_id\n" +
            "LEFT JOIN user_info uif ON us.user_id = uif.user_id\n" +
            "where us.role ='USER';";
    private UserDto convertQueryToProductDto(Session session, Object[] row) {
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
                .map(row -> convertQueryToProductDto(session, row))
                .toList();

        session.close();
        return result;
    }

}


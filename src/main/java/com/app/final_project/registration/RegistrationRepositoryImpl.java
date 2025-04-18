package com.app.final_project.registration;

import com.app.final_project.util.ConnectionProvider;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;

@Repository
public class RegistrationRepositoryImpl implements RegistrationRepositoryCustom{
    @Autowired
    EntityManager entityManager;
    final String UPDATE_STATUS_REGISTRATION ="UPDATE registrations " +
            "SET status =  ? "+
            "WHERE event_id = ? and user_id = ?";
    final String UPDATE_STATUS_REGISTRATION_PREDICTED = "update registrations \n" +
            "set status = ? " +
            "where event_id = ? and status = ? and image_url is not null and isaipredicted =1";
    @Override
    public boolean updateStatusRegistrants(Integer eventId, Integer userId, Integer updateBy) {
        Session session = ConnectionProvider.openSession();
        Transaction transaction = null;
        boolean isUpdated = false;
        String status =null;
        switch (updateBy){
            case 0: {
                status = RegistrationStatus.attended.toString();
                break;
            }
            case 1:{
                status = RegistrationStatus.registered_but_not_attended.toString();
                break;
            }
            default: break;
        }
        System.out.println("status: "  +status);
        try {
            transaction = session.beginTransaction();

            Query query = session.createNativeQuery(UPDATE_STATUS_REGISTRATION)
                    .setParameter(1, status) // Chuyển sang chuỗi
                    .setParameter(2, eventId)
                    .setParameter(3, userId);

            int rowsAffected = query.executeUpdate();

            if (rowsAffected > 0) {
                isUpdated = true;
            }

            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        } finally {
            session.close();
        }

        return isUpdated;
    }
    @Override
    public boolean updateStatusRegistrantsPredicted(Integer eventId) {
        Session session = ConnectionProvider.openSession();
        Transaction transaction = null;
        boolean isUpdated = false;
        try {
            transaction = session.beginTransaction();
            Query query = session.createNativeQuery(UPDATE_STATUS_REGISTRATION_PREDICTED)
                    .setParameter(1, RegistrationStatus.attended.toString())
                    .setParameter(2, eventId)
                    .setParameter(3, RegistrationStatus.registered.toString());

            int rowsAffected = query.executeUpdate();
            System.out.println("row: " + rowsAffected);

            if (rowsAffected > 0) {
                isUpdated = true;
            }

            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        } finally {
            session.close();
        }

        return isUpdated;
    }


}

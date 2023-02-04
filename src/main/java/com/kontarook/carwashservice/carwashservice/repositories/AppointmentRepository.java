package com.kontarook.carwashservice.carwashservice.repositories;

import com.kontarook.carwashservice.carwashservice.entities.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Integer> {


    @Query("SELECT a FROM Appointment a WHERE (a.startTime BETWEEN :start_time AND :end_time) " +
            "OR (a.endTime BETWEEN :start_time AND :end_time) " +
            "OR (:start_time BETWEEN a.startTime AND a.endTime) " +
            "OR (:end_time BETWEEN a.startTime AND a.endTime)")
    List<Appointment> checkAppointmentTimeIsFree(
            @Param("start_time") LocalDateTime start_time, @Param("end_time") LocalDateTime end_time);


    @Query(value = "WITH available_appointments AS " +
            "(SELECT * " +
            "FROM appointments a " +
            "WHERE NOT EXISTS " +
            "(SELECT 1 " +
            "FROM appointments b " +
            "WHERE a.end_time >= b.start_time " +
            "AND b.start_time < a.end_time + :duration * INTERVAL '1 MINUTE' )), " +
            "last_appointment AS " +
            "(SELECT end_time " +
            "FROM appointments " +
            "WHERE end_time >= CURRENT_TIMESTAMP " +
            "ORDER BY end_time DESC " +
            "LIMIT 1) " +
            "SELECT COALESCE( " +
            "(SELECT end_time " +
            "FROM available_appointments " +
            "ORDER BY end_time " +
            "LIMIT 1), " +
            "(SELECT end_time " +
            "FROM last_appointment), " +
            "CURRENT_TIMESTAMP) AS result ", nativeQuery = true)
    Timestamp findNearestAvailableAppointment(@Param("duration") Long duration);

    @Query("SELECT a FROM Appointment a WHERE a.user.id = :user_id AND a.startTime > CURRENT_TIMESTAMP")
    List<Appointment> findAllWaitingAssistanceByUserId(@Param("user_id") Integer id);

    List<Appointment> findAllByUserId(Integer id);
}



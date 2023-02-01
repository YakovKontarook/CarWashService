package com.kontarook.carwashservice.carwashservice.repositories;

import com.kontarook.carwashservice.carwashservice.entities.Assistance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface AssistanceRepository extends JpaRepository<Assistance, Integer> {
    List<Assistance> findAllByIdIn(Collection<Integer> ids);

    @Query("SELECT SUM(s.duration) FROM Assistance s WHERE s IN :assistances")
    Long findTotalDurationOfAssistances(@Param("assistances") List<Assistance> assistances);

    @Query("SELECT SUM(s.price) FROM Assistance s WHERE s IN :assistances")
    Double findTotalPriceOfAssistances(@Param("assistances") List<Assistance> assistances);
}

package com.votingsystem.constituencyservice.repository;

import com.votingsystem.constituencyservice.model.Pooling;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface PoolingRepository extends JpaRepository<Pooling, Long> {

  @Query("SELECT p FROM Pooling p WHERE :currentDate BETWEEN p.startDate AND p.endDate")
  Optional<Pooling> findActivePooling(Date currentDate);

  Optional<Pooling> findPoolingByStartDateAfter(Date currentDate);

  @Query("SELECT p FROM Pooling p WHERE :endDate > p.startDate OR :startDate < p.endDate")
  Optional<Pooling> findByStartDateAfterOrEndDateBefore(Date endDate, Date startDate);

  List<Pooling> findAllByOrderByEndDateDesc();

  @Query("SELECT p FROM Pooling p WHERE p.endDate <= CURRENT_TIMESTAMP ORDER BY p.endDate DESC")
  List<Pooling> findLastPollingWithEndDateInPast();

}

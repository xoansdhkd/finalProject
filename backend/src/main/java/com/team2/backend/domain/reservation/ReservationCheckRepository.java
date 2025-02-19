package com.team2.backend.domain.reservation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;

@Repository
public interface ReservationCheckRepository extends JpaRepository<ReservationCheck, Long> {

    List<ReservationCheck> findAllByReservNo(Long ReservNo);
    List<ReservationCheck> findByResourceNoAndCheckDate(Long resourceNo, String checkDate);
    List<ReservationCheck> findAllByResourceNoAndCheckDate(Long resourceNo,String checkDate);
    ReservationCheck findByReservNoAndCheckDate(Long reservNo, String checkDate);
    List<ReservationCheck> findAllByResourceNoAndCheckDateBetween(Long resourceNo, String startTime, String endTime);
    List<ReservationCheck> findAllByResourceNoAndReservNoAndCheckDateBetween(Long resourceNo, Long reservNo , String startTime, String endTime);

    @Transactional
    @Modifying
    @Query("DELETE FROM ReservationCheck WHERE checkNo = :checkNo")
    void deleteAllByCheckNo(@Param("checkNo")Long checkNo);
}

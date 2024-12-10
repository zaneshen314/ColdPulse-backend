package com.oocl.ita.web.repository;

import com.oocl.ita.web.domain.po.ConcertScheduleClass;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConcertScheduleClassRepository extends JpaRepository<ConcertScheduleClass, Integer> {


    ConcertScheduleClass findByConcertScheduleIdAndConcertClassId(Integer concertScheduleId, Integer concertClassId);
}
package com.oocl.ita.web.repository;

import com.oocl.ita.web.domain.po.CharityEventParticipation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CharityEventParticipationRepository extends JpaRepository<CharityEventParticipation, Integer> {

    public List<CharityEventParticipation> findAllByUserId(Integer userId);

}

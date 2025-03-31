package com.svc.ems.repo;

import com.svc.ems.entity.MemberEventEntity;
import com.svc.ems.entity.MemberEventPkEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MemberEventRepository extends JpaRepository<MemberEventEntity, MemberEventPkEntity> {

    @Query("SELECT m.memberId FROM MemberEventEntity m WHERE m.eventId = :eventId")
    List<String> findMemberIdsByEventId(String eventId);
}
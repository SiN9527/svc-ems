package com.svc.ems.repo;

import com.svc.ems.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AdminEventRepository extends JpaRepository<AdminEventEntity, AdminEventPkEntity> {


    Optional<AdminEventEntity> findByAdminEmailAndEventId(String adminEmail, String eventId);
}
package com.svc.ems.repo;

import com.svc.ems.entity.EmailTemplateEntity;
import com.svc.ems.entity.EmailTemplatePkEntity;
import com.svc.ems.entity.EventEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EventRepository extends JpaRepository<EventEntity, String> {



}

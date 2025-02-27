package com.svc.ems.repo;

import com.svc.ems.entity.AdminRoleEntity;
import com.svc.ems.entity.EmailTemplateEntity;
import com.svc.ems.entity.EmailTemplatePkEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmailTemplateRepository extends JpaRepository<EmailTemplateEntity, EmailTemplatePkEntity> {


    Optional<EmailTemplateEntity> findByEventIdAndTemplateType(String eventId, String templateType);

    void deleteByEventIdAndEmailId(String eventId, Integer emailId);

    List<EmailTemplateEntity> findByEventId(String eventId);
}

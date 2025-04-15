package com.svc.ems.repo;

import com.svc.ems.entity.MemberRoleEventEntity;
import com.svc.ems.entity.MemberRoleEventPkEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRoleEventRepository extends JpaRepository<MemberRoleEventEntity, MemberRoleEventPkEntity> {
}
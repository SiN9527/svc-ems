package com.svc.ems.repo;

import com.svc.ems.entity.AdminMainEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberEventRepository extends JpaRepository<AdminMainEntity, Long> {


}
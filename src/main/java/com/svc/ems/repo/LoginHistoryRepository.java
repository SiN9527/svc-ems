package com.svc.ems.repo;

import com.svc.ems.entity.LoginHistoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LoginHistoryRepository extends JpaRepository<LoginHistoryEntity, Long> {

    
}

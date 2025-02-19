package com.svc.ems.repo;

import com.svc.ems.entity.AuthTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthTokenRepository extends JpaRepository<AuthTokenEntity, Long> {


}

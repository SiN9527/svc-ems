package com.svc.ems.repo;

import com.svc.ems.entity.AdminMainEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface AdminMainRepository extends JpaRepository<AdminMainEntity, Long> {

    Optional<AdminMainEntity> findByUserName(String username);

    Optional<AdminMainEntity> findByEmail(String email);

    Optional<AdminMainEntity> findByUserNameOrEmail(String username, String email);

    Boolean existsByUserName(String username);

    Boolean existsByEmail(String email);

    Boolean existsByEmailAndPassword(String email, String password);
}
package com.svc.ems.repo;

import com.svc.ems.entity.UserMainEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface UserMainRepository extends JpaRepository<UserMainEntity, Long> {

    Optional<UserMainEntity> findByUserName(String username);

    Optional<UserMainEntity> findByEmail(String email);

    Optional<UserMainEntity> findByUserNameOrEmail(String username, String email);

    Boolean existsByUserName(String username);

    Boolean existsByEmail(String email);

    Boolean existsByEmailAndPassword(String email, String password);
}
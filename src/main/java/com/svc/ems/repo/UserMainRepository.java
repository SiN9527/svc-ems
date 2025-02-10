package com.svc.ems.repo;

import com.svc.ems.entity.UserMain;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface UserMainRepository extends JpaRepository<UserMain, Long> {

    Optional<UserMain> findByUserName(String username);

    Optional<UserMain> findByEmail(String email);

    Optional<UserMain> findByUserNameOrEmail(String username, String email);

    Boolean existsByUserName(String username);

    Boolean existsByEmail(String email);
}
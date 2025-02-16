package com.svc.ems.repo;

import com.svc.ems.entity.MemberMain;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberMainRepository extends JpaRepository<MemberMain, String> {

    Optional<MemberMain> findByEmail(String email); // 根據 email 查找會員

    Boolean existsByEmail(String email);



    boolean existsByEmailAndPassword(String email, String password);
}

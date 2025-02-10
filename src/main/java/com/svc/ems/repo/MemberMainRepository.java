package com.svc.ems.repo;

import com.svc.ems.entity.MemberMain;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberMainRepository extends JpaRepository<MemberMain, String> {

    Optional<MemberMain> findByEmail(String email); // 根據 email 查找會員
}

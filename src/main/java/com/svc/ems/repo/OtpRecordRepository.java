package com.svc.ems.repo;

import com.svc.ems.entity.OtpRecordEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OtpRecordRepository extends JpaRepository<OtpRecordEntity, Long> {
}
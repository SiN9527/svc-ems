package com.svc.ems.repo;

import com.svc.ems.entity.PromotionCodeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PromotionCodeRepository extends JpaRepository<PromotionCodeEntity, String> {
}
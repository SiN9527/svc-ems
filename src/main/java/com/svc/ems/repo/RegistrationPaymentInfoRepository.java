 package com.svc.ems.repo;

import com.svc.ems.entity.RegistrationDetailEntity;
import com.svc.ems.entity.RegistrationPaymentInfoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

 public interface RegistrationPaymentInfoRepository extends JpaRepository<RegistrationPaymentInfoEntity, Long> {
     RegistrationPaymentInfoEntity findByRegistrationId(String groupLeaderId);

     RegistrationPaymentInfoEntity findByRegistrationIdAndIsGroup(String groupLeaderId, boolean b);


 }
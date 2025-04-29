package com.svc.ems.repo;

import com.svc.ems.entity.RegistrationDetailEntity;
import jakarta.validation.constraints.Size;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RegistrationDetailRepository extends JpaRepository<RegistrationDetailEntity, Long> {

}
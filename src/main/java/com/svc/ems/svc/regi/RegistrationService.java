package com.svc.ems.svc.regi;

import com.svc.ems.dto.base.ApiResponseTemplate;
import com.svc.ems.dto.registration.RegistrationDetailRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.transaction.annotation.Transactional;

public interface RegistrationService {
    @Transactional
    ResponseEntity<ApiResponseTemplate<String>> registerStep1(RegistrationDetailRequest req, UserDetails userDetails, HttpServletResponse response);
}

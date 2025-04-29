package com.svc.ems.svc.regi;

import com.svc.ems.dto.base.ApiResponseTemplate;
import com.svc.ems.dto.registration.*;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.transaction.annotation.Transactional;

public interface RegistrationService {


    ResponseEntity<ApiResponseTemplate<RegistrationQueryResponse>> registerStep1Query(UserDetails userDetails, HttpServletResponse response);


    @Transactional
    ResponseEntity<ApiResponseTemplate<SoloRegistrationEventResponse>> registerStep1(SoloRegistrationEventRequest req, UserDetails userDetails, HttpServletResponse response);

    @Transactional
    ResponseEntity<ApiResponseTemplate<GroupRegistrationEventResponse>> registerGroupStep1(GroupRegistrationEventRequest req, UserDetails userDetails, HttpServletResponse response);
}

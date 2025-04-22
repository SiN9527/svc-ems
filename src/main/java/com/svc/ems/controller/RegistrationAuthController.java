package com.svc.ems.controller;

import com.svc.ems.dto.auth.MemberProfileResponse;
import com.svc.ems.dto.auth.MemberPwdUpdateRequest;
import com.svc.ems.dto.auth.MemberResetPwdRequest;
import com.svc.ems.dto.auth.MemberUpdateRequest;
import com.svc.ems.dto.base.ApiResponseTemplate;
import com.svc.ems.dto.registration.GroupRegistrationEventRequest;
import com.svc.ems.dto.registration.RegistrationQueryResponse;
import com.svc.ems.dto.registration.SoloRegistrationEventRequest;
import com.svc.ems.svc.auth.MemberAuthService;
import com.svc.ems.svc.regi.RegistrationService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth/event")
public class RegistrationAuthController {


    private final RegistrationService registrationService;


    public RegistrationAuthController(RegistrationService registrationService) {
        this.registrationService = registrationService;

    }

    //登入者單人報名
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/soloRegi")
    @Operation(summary = "登入者單人報名")
    public ResponseEntity<ApiResponseTemplate<String>> soloRegistration(
            @RequestBody SoloRegistrationEventRequest req, @AuthenticationPrincipal UserDetails userDetails, HttpServletResponse response) {
        return registrationService.registerStep1(req,userDetails,response);
    }

    //{ withCredentials: true }


    @PreAuthorize("isAuthenticated()")
    @GetMapping("/regiInfo")
    @Operation(summary = "登入者取得報名資料")
    public ResponseEntity<ApiResponseTemplate<RegistrationQueryResponse>> memberGetProfile(@AuthenticationPrincipal UserDetails userDetails, HttpServletResponse response) {
        return registrationService.registerStep1Query(userDetails,response);
    }

//登入者團體報名
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/multiRegi")
    @Operation(summary = "登入者團體報名")
    ResponseEntity<ApiResponseTemplate<String>> registerGroupStep1(  @RequestBody GroupRegistrationEventRequest req,@AuthenticationPrincipal UserDetails userDetails, HttpServletResponse response){
        return registrationService.registerGroupStep1(req,userDetails,response);
    };




}

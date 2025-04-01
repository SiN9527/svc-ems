package com.svc.ems.controller;

import com.svc.ems.dto.auth.LoginRequest;
import com.svc.ems.dto.auth.MemberPwdUpdateRequest;
import com.svc.ems.dto.auth.MemberRegisterRequest;
import com.svc.ems.dto.base.ApiResponseTemplate;
import com.svc.ems.dto.mail.EmailTemplateDTO;
import com.svc.ems.svc.auth.JwtAuthLoginService;
import com.svc.ems.svc.auth.MemberAuthService;
import com.svc.ems.svc.mail.EmailTemplateService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/mail/member")
@RequiredArgsConstructor
public class MailTemplateController {


    private final EmailTemplateService emailTemplateService;

    /**
     * 查詢某活動的所有信件模板
     */
    @PostMapping("/list")
    public ResponseEntity<ApiResponseTemplate<?>> listTemplates(@RequestBody EmailTemplateDTO req) {
        return emailTemplateService.getTemplatesByEvent(req);
    }

    /**
     * 新增或更新信件模板
     */
    @PostMapping("/save")
    public ResponseEntity<ApiResponseTemplate<?>> saveTemplate(@Valid @RequestBody EmailTemplateDTO req) {
        return emailTemplateService.saveOrUpdate(req);
    }

    /**
     * 根據主鍵刪除模板
     */
    @PostMapping("/delete")
    public ResponseEntity<ApiResponseTemplate<?>> deleteTemplate(@RequestBody EmailTemplateDTO req) {
        return emailTemplateService.deleteTemplate(req);
    }




}

package com.svc.ems.controller.base;

import com.svc.ems.dto.auth.SwaggerUserLoginRequest;
import com.svc.ems.dto.base.ApiResponseTemplate;
import com.svc.ems.svc.auth.MemberAuthService;
import com.svc.ems.svc.base.BaseService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class BaseController {


    private final BaseService baseService;
    private final MemberAuthService memberAuthService;


    public BaseController(BaseService baseService, MemberAuthService memberAuthService) {
        this.baseService = baseService;
        this.memberAuthService = memberAuthService;
    }

    @PostMapping("/getToken")
    @Operation(summary = "直接取得 Bear Token")
    public String login(@RequestBody SwaggerUserLoginRequest req) {

        // 返回 Bearer Token
        return baseService.getToken(req);
    }

    @PostMapping("/clearToken")
    @Operation(summary = "強制清除 Http only cookie")
    public ResponseEntity<ApiResponseTemplate<?>> clearToken(HttpServletResponse response) {
        return memberAuthService.memberLogout(response);
    }


}

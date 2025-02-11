package com.svc.ems.controller.base;

import com.svc.ems.dto.auth.SwaggerUserLoginRequest;
import com.svc.ems.dto.auth.UserRegisterRequest;
import com.svc.ems.svc.base.BaseService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class BaseController {


    private final BaseService baseService;


    public BaseController(BaseService baseService) {
        this.baseService = baseService;
    }

    @PostMapping("/getToken")
    public String login(@RequestBody SwaggerUserLoginRequest req) {

        // 返回 Bearer Token
        return baseService.getToken(req);
    }


}

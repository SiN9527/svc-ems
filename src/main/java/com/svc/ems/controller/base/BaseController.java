package com.svc.ems.controller.base;

import com.svc.ems.dto.auth.SwaggerUserLoginRequest;
import com.svc.ems.dto.base.ApiResponseTemplate;
import com.svc.ems.dto.common.CommonCodeList;
import com.svc.ems.dto.common.CommonCodeReqDTO;
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

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/base")
public class BaseController {


    private final BaseService baseService;
    private final MemberAuthService memberAuthService;


    public BaseController(BaseService baseService, MemberAuthService memberAuthService) {
        this.baseService = baseService;
        this.memberAuthService = memberAuthService;
    }

    //CommonCode 暫放
    @PostMapping("/getCommonCode")
    @Operation(summary = "CommonCode-根據codeType回傳資料，舉例['IDREC_LOCATION','INDUSTRY_CODE']")
    public ResponseEntity<ApiResponseTemplate<Map<String, List<CommonCodeList>>>> searchByCodeType(@RequestBody CommonCodeReqDTO reqDTO) {
        Map<String, List<CommonCodeList>> result = baseService.searchByCodeTypes(reqDTO.getCodeType());

        if (result != null && !result.isEmpty()) {
            return ResponseEntity.ok(ApiResponseTemplate.success(result));
        }

        return ResponseEntity.notFound().build();
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

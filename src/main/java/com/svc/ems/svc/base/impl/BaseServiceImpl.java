package com.svc.ems.svc.base.impl;

import com.svc.ems.config.jwt.JwtAdminDetailsService;
import com.svc.ems.config.jwt.JwtMemberDetailsService;
import com.svc.ems.config.jwt.JwtUtil;
import com.svc.ems.dto.auth.SwaggerUserLoginRequest;
import com.svc.ems.dto.common.CommonCodeList;
import com.svc.ems.entity.CommonCodeEntity;
import com.svc.ems.repo.AdminMainRepository;
import com.svc.ems.repo.CommonCodeRepository;
import com.svc.ems.svc.base.BaseService;
import com.svc.ems.utils.MapperUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class BaseServiceImpl implements BaseService {

    private final JwtUtil jwtUtil;
    private final JwtAdminDetailsService userDetailsService;
    private final JwtMemberDetailsService memberDetailsService;
    private final PasswordEncoder passwordEncoder;
    private final AdminMainRepository adminMainRepository;
    private final CommonCodeRepository commonCodeRepository;

    public BaseServiceImpl(JwtUtil jwtUtil,
                           JwtAdminDetailsService userDetailsService,
                           JwtMemberDetailsService memberDetailsService,
                           PasswordEncoder passwordEncoder,
                           AdminMainRepository adminMainRepository, CommonCodeRepository commonCodeRepository) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
        this.memberDetailsService = memberDetailsService;
        this.passwordEncoder = passwordEncoder;
        this.adminMainRepository = adminMainRepository;
        this.commonCodeRepository = commonCodeRepository;
    }


    @Override
    public String getToken(SwaggerUserLoginRequest req) {

        String email = req.getEmail();
        String password = req.getPassword();

        // 確定身份類型（USER 或 MEMBER）
        boolean isUser = userDetailsService.userExists(email);
        boolean isMember = memberDetailsService.memberExists(email);

        if (!isUser && !isMember) {
            return "Invalid email or password.";
        }

        UserDetails userDetails;
        String type;

        if (isUser) {
            userDetails = userDetailsService.loadUserByUsername(email);
            type = "USER"; // 後台使用者
        } else {
            userDetails = memberDetailsService.loadUserByUsername(email);
            type = "MEMBER"; // 會員
        }

        // 驗證密碼
        if (!passwordEncoder.matches(password, userDetails.getPassword())) {
            return "Invalid email or password.";
        }

        // 生成 JWT
        List<String> roles = new ArrayList<>();

        return jwtUtil.generateToken(email, type, roles);

    }


    @Override
    public Map<String, List<CommonCodeList>> searchByCodeTypes(List<String> codeTypes) {
        Map<String, List<CommonCodeList>> result = new HashMap<>();

        for (String codeType : codeTypes) {
            List<CommonCodeEntity> byCodeTypes = commonCodeRepository.findByCodeType(codeType);
            List<CommonCodeList> commonCodeLists = MapperUtils.mapList(byCodeTypes, CommonCodeList.class);
            result.put(codeType, commonCodeLists);
        }

        return result;
    }
}

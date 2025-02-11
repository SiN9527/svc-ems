package com.svc.ems.svc.base;

import com.svc.ems.dto.auth.SwaggerUserLoginRequest;
import org.springframework.http.ResponseEntity;

public interface BaseService {

    public String getToken(SwaggerUserLoginRequest req);


}

package com.svc.ems.svc.base;

import com.svc.ems.dto.auth.SwaggerUserLoginRequest;
import com.svc.ems.dto.common.CommonCodeList;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

public interface BaseService {

    public String getToken(SwaggerUserLoginRequest req);

    List<CommonCodeList> searchByCodeTypes(List<String> codeTypes);


}

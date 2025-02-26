package com.svc.ems.svc.base;


import com.svc.ems.dto.common.CommonCodeList;
import com.svc.ems.dto.common.CommonCodeResDTO;

import java.util.List;
import java.util.Map;

/**
 * @Author 郭庭安
 * @Create 2024/7/27 10:41 PM
 * @Version 1.0
 */
public interface CommonCodeService {

    Map<String, List<CommonCodeList>> searchByCodeTypes(List<String> codeTypes);

}

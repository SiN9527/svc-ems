package com.svc.ems.svc.base.impl;


import com.alibaba.fastjson.JSON;
import com.svc.ems.dto.common.CommonCodeList;
import com.svc.ems.entity.CommonCodeEntity;
import com.svc.ems.repo.CommonCodeRepository;
import com.svc.ems.svc.base.CommonCodeService;
import com.svc.ems.utils.MapperUtils;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @Author 郭庭安
 * @Create 2024/7/27 10:41 PM
 * @Version 1.0
 */
@Slf4j
@Service
public class CommonCodeServiceImpl implements CommonCodeService {

    @Resource
    private CommonCodeRepository commonCodeRepository;

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

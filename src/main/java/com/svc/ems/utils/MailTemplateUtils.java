package com.svc.ems.utils;


import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Component
public class MailTemplateUtils {
    /**
     * 將模板中的占位符（如 {name}）替換成對應參數值
     * @param templateContent 原始模板
     * @param params 替換用的變數 map
     * @return 替換後的內容
     */
    public static String fillTemplate(String templateContent, Map<String, String> params) {
        for (Map.Entry<String, String> entry : params.entrySet()) {
            templateContent = templateContent.replace("{" + entry.getKey() + "}", entry.getValue());
        }
        return templateContent;
    }



}

package com.svc.ems.dto.common;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

/**
 * ClassName: CommonCodeReqDTO
 * Package: com.timmy.bankProject.dto.composite.commoncode
 * Description:
 *
 * @Author 郭庭安
 * @Create 2024/7/27 10:34 PM
 * @Version 1.0
 */
@Data
public class CommonCodeReqDTO {

    @JsonProperty("CodeType")
    private List<String> codeType;
}

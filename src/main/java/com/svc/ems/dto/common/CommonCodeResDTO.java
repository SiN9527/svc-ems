package com.svc.ems.dto.common;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * ClassName: CommonCodeResDTO
 * Package: com.timmy.bankProject.dto.composite.commoncode
 * Description:
 *
 * @Author 郭庭安
 * @Create 2024/7/27 10:33 PM
 * @Version 1.0
 */
@Data
public class CommonCodeResDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @JsonProperty("CommonCodeList")
    private List<CommonCodeList> commonCodeList;
}

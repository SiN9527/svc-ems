package com.svc.ems.dto.common;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * ClassName: CommonCodeList
 * Package: com.timmy.bankProject.dto.composite.commoncode
 * Description:
 *
 * @Author 郭庭安
 * @Create 2024/7/27 10:38 PM
 * @Version 1.0
 */
@Data
public class CommonCodeList implements Serializable {
    private static final long serialVersionUID = 1L;

    @JsonProperty("CodeType")
    private String codeType;

    @JsonProperty("CodeOption")
    private String codeOption;

    @JsonProperty("Seq")
    private Integer indCodeseq;

    @JsonProperty("CodeName")
    private String codeName;

    @JsonProperty("CodeTag")
    private String codeTag;

    @JsonProperty("LangKey")
    private String langKey;
}

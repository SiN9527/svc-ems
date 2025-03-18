package com.svc.ems.enums;

import lombok.Getter;

/**
 *  統一錯誤代碼管理
 * - 格式：E/A/S-分類-數字
 * - VALIDATION (E01-xxx) => 參數錯誤、欄位驗證失敗
 * - AUTH (A01-xxx) => 未授權、權限不足
 * - SYSTEM (S01-xxx) => 系統內部錯誤
 */
@Getter
public enum EmailTypesEnum {


    REGISTER_SUCCESS("01","registration success"),      // 註冊成功
    PASSWORD_RESET("02","password reset success"),        // 忘記密碼
    PROFILE_UPDATE("04","profile update success") ,        // 個人資訊/密碼變更
    EMAIL_VERIFICATION("05","email verify success"),    // 驗證信
    PAYMENT_SUCCESS("06","pay success"),       // 繳費成功
    SUBMISSION_SUCCESS ("07","submission success") ;  // 報名成功 (報名表)


    private final String code;
    private final String desc;

    EmailTypesEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}

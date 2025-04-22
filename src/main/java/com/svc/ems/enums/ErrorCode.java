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
public enum ErrorCode {


    SUCCESS("SUCCESS", "Request success.", 200),

    //  **通用錯誤 (C01)**
    DATA_NOT_FOUND("C01", "Data not found.", 400),
    SOMETHING_GOING_WRONG("C02", "Something going wrong.", 400),
    INVALID_REQUEST("C03", "Invalid request.", 400),
    CUSTOM_ERROR("C04", "Custom error.", 400),

    //  **驗證類錯誤 (E01)**

    ADMIN_NOT_FOUND("E01", "Admin not found.", 400),
    MEMBER_NOT_FOUND("E02", "Member not found.", 400),
    VALIDATION_FAILED("E03", "Request validation failed.", 400),
    MISSING_PARAMETER("E04", "Required parameter is missing.", 400),
    INVALID_EMAIL_FORMAT("E05", "Email format is invalid.", 400),
    PASSWORD_TOO_WEAK("E06", "Password does not meet security requirements.", 400),
    DUPLICATE_ENTRY("E07", "The record already exists.", 409),
    INVALID_CREDENTIALS("E08", "Invalid email or password.", 400),
    INVALID_VERIFICATION_CODE("E09", "Invalid verification code.", 400),
    INVALID_VERIFICATION_URL("E10", "Invalid verification URL.", 400),
    ACCOUNT_ALREADY_REGISTERED("E11", "Account is already registered.", 400),
    EMAIL_ALREADY_REGISTERED("E12", "Email is already registered.", 400),
    EMAIL_TEMPLATE_NOT_FOUND("E13", "Email template not found.", 400),
    INVALID_EMAIL_OR_PASSWORD("E14", "Invalid email or password.", 400),
    ACCOUNT_IS_DISABLED("E15", "Account is disabled.", 400),
    REGISTRATION_ALREADY_EXISTS("E16", "registration already exist.", 400),




    //  **身份驗證錯誤 (A01)**
    UNAUTHORIZED("A01", "Unauthorized access.", 401),
    TOKEN_EXPIRED("A02", "Token has expired, please log in again.", 401),
    TOKEN_INVALID("A03", "Invalid authentication token.", 401),
    INVALID_FRESH_TOKEN("A04", "Invalid refresh token.", 401),

    FORBIDDEN("A05", "You do not have permission to access this resource.", 403),

    //  **系統錯誤 (S01)**
    INTERNAL_SERVER_ERROR("S01", "Internal server error.", 500),
    DATABASE_ERROR("S02", "Database operation failed.", 500),
    SERVICE_UNAVAILABLE("S03", "Service is temporarily unavailable.", 503);

    private final String code;
    private final String message;
    private final int httpStatus;

    ErrorCode(String code, String message, int httpStatus) {
        this.code = code;
        this.message = message;
        this.httpStatus = httpStatus;
    }
}

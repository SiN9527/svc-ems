package com.svc.ems.config;


import com.svc.ems.dto.base.ApiResponseTemplate;
import com.svc.ems.exception.AdminDuplicatedException;
import com.svc.ems.exception.AdminNotFoundException;
import com.svc.ems.exception.ServiceException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


/**
 * **全域異常處理（Global Exception Handler）**
 * - 負責捕捉應用程式中的異常，並統一返回標準 API 格式的錯誤回應
 * - 確保所有異常都能被正確處理，避免未處理異常導致 500 內部伺服器錯誤
 */
@Slf4j // **啟用日誌紀錄**
@RestControllerAdvice // **攔截所有 Controller 的異常**
public class GlobalExceptionHandler {

    private final HttpServletRequest request; // **HttpServletRequest 用於獲取請求資訊**

    /**
     * **透過建構子注入 `HttpServletRequest`**
     * - `request.getRequestURI()` 需要 `HttpServletRequest` 來獲取請求的 URI
     */
    public GlobalExceptionHandler(HttpServletRequest request) {
        this.request = request;
    }

    /**
     * **處理 `ServiceException` 及其子類**
     * - `ServiceException` 是業務邏輯異常，這裡會根據不同的異常類別返回對應的錯誤訊息
     *
     * @param e 捕捉到的 `ServiceException`
     * @return `ApiResponse<Void>` 統一格式的錯誤回應
     */
    @ExceptionHandler(ServiceException.class)
    public ApiResponseTemplate<Void> handleServiceException(ServiceException e) {
        log.error("ServiceException: {}", e.getMessage(), e); // **記錄異常日誌**

        if (e instanceof AdminDuplicatedException) {
            // **管理員重複錯誤**
            return buildErrorResponse(HttpStatus.BAD_REQUEST, "ADMIN_DUPLICATED", "管理員已存在");
        } else if (e instanceof AdminNotFoundException) {
            // **找不到管理員錯誤**
            return buildErrorResponse(HttpStatus.NOT_FOUND, "ADMIN_NOT_FOUND", "找不到該管理員");
        }
        // **未定義的 ServiceException 預設為伺服器錯誤**
        return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "UNKNOWN_SERVICE_ERROR", "系統發生錯誤，請稍後再試");
    }

    /**
     * **捕捉所有未處理的 `Exception`**
     * - 確保所有未預期的異常都有統一的處理方式，避免直接拋出 500 內部伺服器錯誤
     *
     * @param e 捕捉到的 `Exception`
     * @return `ApiResponse<Void>` 統一格式的錯誤回應
     */
    @ExceptionHandler(Exception.class)
    public ApiResponseTemplate<Void> handleGlobalException(Exception e) {
        log.error("Unexpected Exception: {}", e.getMessage(), e); // **記錄異常日誌**
        return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "INTERNAL_ERROR", "系統發生未知錯誤");
    }

    /**
     * **建構 API 錯誤回應**
     * - 所有異常統一返回此格式，方便前端處理
     *
     * @param status HTTP 狀態碼（`HttpStatus`）
     * @param error 錯誤類型識別碼（字串）
     * @param messageDetail 錯誤訊息（給前端顯示）
     * @return `ApiResponse<Void>` 統一格式的錯誤回應
     */
    private ApiResponseTemplate<Void> buildErrorResponse(HttpStatus status, String error, String messageDetail) {
        return ApiResponseTemplate.<Void>builder()
                .httpStatusCode(status.value())  // **HTTP 狀態碼**
                .error(error)                    // **錯誤識別碼**
                .messageDetail(messageDetail)    // **錯誤詳細資訊**
                .path(request.getRequestURI())   // **請求的 API 路徑**
                .traceId(UUID.randomUUID().toString()) // **產生唯一識別碼，方便除錯**
                .build();
    }





    /**
     * **處理 `MethodArgumentNotValidException`**
     * - 當 Controller 方法的參數驗證不通過時，會拋出此異常
     * - 這裡將驗證錯誤的欄位與錯誤訊息封裝成 Map，並返回給前端
     *
     * @param ex 捕捉到的 `MethodArgumentNotValidException`
     * @return 驗證錯誤的欄位與錯誤訊息的 Map
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public  ApiResponseTemplate<Void> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();

        // 取得所有驗證錯誤的欄位與對應的錯誤訊息
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.put(error.getField(), error.getDefaultMessage());
        }

        return buildErrorResponse(HttpStatus.BAD_REQUEST, "REQUEST_ERROR", "請求參數錯誤");
    }
}
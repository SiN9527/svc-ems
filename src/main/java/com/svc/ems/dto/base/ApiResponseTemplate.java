package com.svc.ems.dto.base;

/**

 * Description:
 *
 * @Author 郭庭安
 * @Create 2025/1/23 下午8:56
 * @Version 1.0
 */

import java.io.Serializable;
import java.time.Instant;

/**
 * Json格式的數據進行響應
 */


import lombok.Data;

/**
 * 通用 API 回應模板
 *
 * @param <E> 泛型數據類型
 */
@Data
public class ApiResponseTemplate<E> implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * HTTP 狀態碼，例如 200、400、500
     */
    private Integer httpStatusCode;

    /**
     * 錯誤類型（可選，例如 "USER_NOT_FOUND", "TOKEN_INVALID"）
     */
    private String error;

    /**
     * 錯誤或成功的詳細訊息
     */
    private String messageDetail;

    /**
     * 具體的回應數據
     */
    private E payload;

    /**
     * API 請求的時間戳
     */
    private Long timestamp;

    /**
     * 請求的 URI（方便 Debug）
     */
    private String path;

    /**
     * 唯一請求識別碼（可搭配 MDC 用來追蹤請求）
     */
    private String traceId;

    /**
     * 除錯訊息（僅在開發環境回傳）
     */
    private String debugMessage;

    /**
     * 私有建構子，避免直接 new
     */
    private ApiResponseTemplate() {
        this.timestamp = Instant.now().toEpochMilli(); // 設定請求的時間戳
    }

    //  **成功回應**
    public static <E> ApiResponseTemplate<E> success(E payload) {
        ApiResponseTemplate<E> response = new ApiResponseTemplate<>();
        response.httpStatusCode = 200;
        response.messageDetail = "Success";
        response.payload = payload;
        return response;
    }

    public static <E> ApiResponseTemplate<E> success(String message, E payload) {
        ApiResponseTemplate<E> response = new ApiResponseTemplate<>();
        response.httpStatusCode = 200;
        response.messageDetail = message;
        response.payload = payload;
        return response;
    }

    //  **失敗回應**
    public static <E> ApiResponseTemplate<E> fail(int httpStatusCode, String error, String messageDetail) {
        ApiResponseTemplate<E> response = new ApiResponseTemplate<>();
        response.httpStatusCode = httpStatusCode;
        response.error = error;
        response.messageDetail = messageDetail;
        return response;
    }

    public static <E> ApiResponseTemplate<E> fail(int httpStatusCode, String messageDetail) {
        return fail(httpStatusCode, null, messageDetail);
    }

    //  **支援 Builder 模式**
    public static <E> Builder<E> builder() {
        return new Builder<>();
    }

    public static class Builder<E> {
        private final ApiResponseTemplate<E> response;

        public Builder() {
            this.response = new ApiResponseTemplate<>();
        }

        public Builder<E> httpStatusCode(int httpStatusCode) {
            response.httpStatusCode = httpStatusCode;
            return this;
        }

        public Builder<E> error(String error) {
            response.error = error;
            return this;
        }

        public Builder<E> messageDetail(String messageDetail) {
            response.messageDetail = messageDetail;
            return this;
        }

        public Builder<E> payload(E payload) {
            response.payload = payload;
            return this;
        }

        public Builder<E> path(String path) {
            response.path = path;
            return this;
        }

        public Builder<E> traceId(String traceId) {
            response.traceId = traceId;
            return this;
        }

        public Builder<E> debugMessage(String debugMessage) {
            response.debugMessage = debugMessage;
            return this;
        }

        public ApiResponseTemplate<E> build() {
            return response;
        }
    }
}


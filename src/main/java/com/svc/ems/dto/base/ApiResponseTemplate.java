package com.svc.ems.dto.base;

/**

 * Description:
 *
 * @Author 郭庭安
 * @Create 2025/1/23 下午8:56
 * @Version 1.0
 */

import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;

/**
 * Json格式的數據進行響應
 */


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * 通用 API 回應模板
 *
 * @param <E> 泛型數據類型
 */
@Data
public class ApiResponseTemplate<E> implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 是否成功
     */
    @JsonProperty("Success")
    private boolean success;

    /**
     * HTTP 狀態碼，例如 200、400、500
     */
    @JsonProperty("HttpStatusCode")
    private Integer httpStatusCode;

    /**
     * 錯誤或成功的訊息
     */
    @JsonProperty("Message")
    private String message;

    /**
     * 具體的回應數據
     */
    @JsonProperty("Data")
    private E data;

    /**
     * API 請求的時間戳
     */
    @JsonProperty("Timestamp")
    private Long timestamp;

    /**
     * 請求的 URI（方便 Debug）
     */
    @JsonProperty("Path")
    private String path;

    /**
     * 私有建構子，避免直接 new
     */
    private ApiResponseTemplate() {
        this.timestamp = Instant.now().toEpochMilli(); // 設定請求的時間戳
    }

    // **成功回應**
    public static <E> ApiResponseTemplate<E> success(E data) {
        ApiResponseTemplate<E> response = new ApiResponseTemplate<>();
        response.success = true;
        response.httpStatusCode = 200;
        response.message = "Success";
        response.data = data;
        return response;
    }

    public static <E> ApiResponseTemplate<E> success(String message, E data) {
        ApiResponseTemplate<E> response = new ApiResponseTemplate<>();
        response.success = true;
        response.httpStatusCode = 200;
        response.message = message;
        response.data = data;
        return response;
    }

    // **失敗回應**
    public static <E> ApiResponseTemplate<E> fail(int httpStatusCode, String message) {
        ApiResponseTemplate<E> response = new ApiResponseTemplate<>();
        response.success = false;
        response.httpStatusCode = httpStatusCode;
        response.message = message;
        return response;
    }

    // **支援 Builder 模式**
    public static <E> Builder<E> builder() {
        return new Builder<>();
    }

    public static class Builder<E> {
        private final ApiResponseTemplate<E> response;

        public Builder() {
            this.response = new ApiResponseTemplate<>();
        }

        public Builder<E> success(boolean success) {
            response.success = success;
            return this;
        }

        public Builder<E> httpStatusCode(int httpStatusCode) {
            response.httpStatusCode = httpStatusCode;
            return this;
        }

        public Builder<E> message(String message) {
            response.message = message;
            return this;
        }

        public Builder<E> data(E data) {
            response.data = data;
            return this;
        }

        public Builder<E> path(String path) {
            response.path = path;
            return this;
        }

        public ApiResponseTemplate<E> build() {
            return response;
        }
    }
}

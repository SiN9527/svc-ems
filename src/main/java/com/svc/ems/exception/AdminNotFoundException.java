package com.svc.ems.exception;

/**
 * ClassName: com.sweetolive.exhibition_backend.exception.AdminNotFoundException
 * Package: com.sweetolive.exhibition_backend.exception
 * Description:
 *
 * @Author 郭庭安
 * @Create 2025/1/23 下午9:06
 * @Version 1.0
 */
public class AdminNotFoundException extends ServiceException {
    public AdminNotFoundException() {
        super();
    }

    public AdminNotFoundException(String message) {
        super(message);
    }

    public AdminNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public AdminNotFoundException(Throwable cause) {
        super(cause);
    }

    protected AdminNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}

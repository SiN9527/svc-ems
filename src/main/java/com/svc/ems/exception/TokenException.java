package com.svc.ems.exception;

/**
 * ClassName: com.sweetolive.exhibition_backend.exception.AdminDuplicatedException
 * Package: com.sweetolive.exhibition_backend.exception
 * Description:
 *
 * @Author 郭庭安
 * @Create 2025/1/23 下午9:09
 * @Version 1.0
 */
public class TokenException extends ServiceException {
    public TokenException() {
        super();
    }

    public TokenException(String message) {
        super(message);
    }

    public TokenException(String message, Throwable cause) {
        super(message, cause);
    }

    public TokenException(Throwable cause) {
        super(cause);
    }

    protected TokenException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
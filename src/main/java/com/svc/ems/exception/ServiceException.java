package com.svc.ems.exception;

/**
 * ClassName: com.sweetolive.exhibition_backend.exception.ServiceException
 * Package: com.sweetolive.exhibition_backend.exception
 * Description:
 *
 * @Author 郭庭安
 * @Create 2025/1/23 下午9:04
 * @Version 1.0
 */
public class ServiceException extends RuntimeException{
    public ServiceException() {
        super();
    }

    public ServiceException(String message) {
        super(message);
    }

    public ServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    public ServiceException(Throwable cause) {
        super(cause);
    }

    protected ServiceException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}

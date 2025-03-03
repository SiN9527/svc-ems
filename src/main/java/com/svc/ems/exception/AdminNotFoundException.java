package com.svc.ems.exception;

import com.svc.ems.enums.ErrorCode;

/**
 * 管理員找不到異常
 */
public class AdminNotFoundException extends ServiceException {

    public AdminNotFoundException() {
        super("Admin not found", ErrorCode.ADMIN_NOT_FOUND);
    }

    public AdminNotFoundException(String message) {
        super(message, ErrorCode.ADMIN_NOT_FOUND);
    }

    public AdminNotFoundException(String message, Throwable cause) {
        super(message, cause, ErrorCode.ADMIN_NOT_FOUND);
    }

    public AdminNotFoundException(Throwable cause) {
        super(cause, ErrorCode.ADMIN_NOT_FOUND);
    }
}

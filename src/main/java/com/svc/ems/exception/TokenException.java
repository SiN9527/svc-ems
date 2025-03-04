package com.svc.ems.exception;

import com.svc.ems.enums.ErrorCode;


public class TokenException extends ServiceException {

    public TokenException() {
        super("Token is invalid", ErrorCode.TOKEN_INVALID);
    }

    public TokenException(String message) {
        super(message, ErrorCode.TOKEN_INVALID);
    }

    public TokenException(String message, Throwable cause) {
        super(message, cause, ErrorCode.TOKEN_INVALID);
    }

    public TokenException(Throwable cause) {
        super(cause, ErrorCode.TOKEN_INVALID);
    }
}
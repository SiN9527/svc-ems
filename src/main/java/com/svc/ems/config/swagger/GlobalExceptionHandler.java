package com.svc.ems.config.swagger;


import com.svc.ems.exception.AdminDuplicatedException;
import com.svc.ems.exception.AdminNotFoundException;
import com.svc.ems.exception.ServiceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import com.svc.ems.utils.JsonResult;


@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(ServiceException.class)
    public JsonResult<Void> handlerException(Throwable e) {
        JsonResult<Void> result = new JsonResult<>(e);
        if (e instanceof AdminDuplicatedException) {
            result.setState(4000);
        } else if (e instanceof AdminNotFoundException) {
            result.setState(4001);
        }
        return result;
    }
}


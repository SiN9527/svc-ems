package com.svc.ems.config;

import com.svc.ems.config.jwt.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

import java.util.Arrays;
import java.util.UUID;

@Aspect
@Component
@Slf4j
public class LoggingAspect {

    private final HttpServletRequest request;
    private final JwtUtil jwtUtil;

    public LoggingAspect(HttpServletRequest request, JwtUtil jwtUtil) {
        this.request = request;
        this.jwtUtil = jwtUtil;
    }

    /**
     * 定義 Pointcut，攔截 Controller 層的所有方法
     */
    @Pointcut("execution(* com.svc.ems.controller..*(..))")
    public void controllerMethods() {}
    /**
     * 在 Controller 方法執行前記錄請求參數
     */
    @Before("controllerMethods()")
    public void logRequestDetails(JoinPoint joinPoint) {
        String traceId = UUID.randomUUID().toString(); // 生成請求唯一識別碼
        String token = request.getHeader("Authorization"); // 從 Header 取出 Token

        String userType = "UNKNOWN";
        String username = "ANONYMOUS";

        if (token != null && token.startsWith("Bearer ")) {
            String jwt = token.substring(7);
            userType = jwtUtil.extractUserType(jwt);
            username = jwtUtil.extractUsername(jwt);
        }

        log.info("🔍 [API請求] traceId={} | UserType={} | User={} | Method={} | Args={}",
                traceId, userType, username, joinPoint.getSignature(), Arrays.toString(joinPoint.getArgs()));
    }

    /**
     * 在 Controller 方法發生異常時記錄
     */
    @AfterThrowing(pointcut = "controllerMethods()", throwing = "exception")
    public void logError(JoinPoint joinPoint, Throwable exception) {
        log.error("[API異常] Method={} | Message={ } | Exception={}",
                joinPoint.getSignature(), exception.getMessage(), exception);
    }
}

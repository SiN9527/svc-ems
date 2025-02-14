package com.svc.ems.controller;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class TestController {
    private static final Logger logger = LoggerFactory.getLogger(TestController.class);

    @GetMapping("/test")
    public String test() {
        log.debug("這是 debug 日誌");
        log.info("這是 info 日誌");
        log.warn("這是 warn 日誌");
        log.error("這是 error 日誌");
        return "測試完成";
    }
}
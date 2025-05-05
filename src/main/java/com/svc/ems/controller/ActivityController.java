package com.svc.ems.controller;

import com.svc.ems.dto.base.ApiResponseTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/activity")
public class ActivityController {

    public ResponseEntity<ApiResponseTemplate<?>> getActivityList() {

        return null;
    }
}

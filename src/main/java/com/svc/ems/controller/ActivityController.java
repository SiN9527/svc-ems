package com.svc.ems.controller;

import com.svc.ems.dto.activity.activityQueryResponse;
import com.svc.ems.dto.base.ApiResponseTemplate;
import com.svc.ems.svc.activity.ActivityService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/activity")
@RequiredArgsConstructor
public class ActivityController {

    private final ActivityService activityService;

    @PostMapping("/eventQuery")
    @Operation(summary = "活動清單")
    public ResponseEntity<ApiResponseTemplate<List<activityQueryResponse>>> getActivityList() {
        log.info("eventQuery===> {}", "12344");
        return activityService.activityQuery();
    }
}

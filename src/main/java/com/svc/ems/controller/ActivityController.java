package com.svc.ems.controller;

import com.svc.ems.dto.activity.ActivityQueryResponse;
import com.svc.ems.dto.base.ApiResponseTemplate;
import com.svc.ems.svc.activity.ActivityService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Activity 活動相關功能
 */
@RestController
@RequestMapping("/api/activity")
@RequiredArgsConstructor
public class ActivityController {

    private final ActivityService activityService;

    @GetMapping("/eventQuery")
    @Operation(summary = "活動清單")
    public ResponseEntity<ApiResponseTemplate<List<ActivityQueryResponse>>> getActivityList() {
        return activityService.activityQuery();
    }
}

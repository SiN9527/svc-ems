package com.svc.ems.svc.activity;

import com.svc.ems.dto.activity.activityQueryResponse;
import com.svc.ems.dto.base.ApiResponseTemplate;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface ActivityService {

    public ResponseEntity<ApiResponseTemplate<List<activityQueryResponse>>> activityQuery();
}

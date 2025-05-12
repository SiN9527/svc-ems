package com.svc.ems.svc.activity;

import com.svc.ems.dto.activity.ActivityQueryResponse;
import com.svc.ems.dto.activity.ActivityRegDataRequest;
import com.svc.ems.dto.activity.ActivityRegDataResponse;
import com.svc.ems.dto.base.ApiResponseTemplate;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface ActivityService {

    public ResponseEntity<ApiResponseTemplate<List<ActivityQueryResponse>>> activityQuery();

    public ResponseEntity<ApiResponseTemplate<List<ActivityRegDataResponse>>> activityReqData(ActivityRegDataRequest req);
}

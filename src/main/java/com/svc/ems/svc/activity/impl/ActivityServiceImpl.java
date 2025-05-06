package com.svc.ems.svc.activity.impl;

import com.svc.ems.dto.activity.ActivityQueryResponse;
import com.svc.ems.dto.base.ApiResponseTemplate;
import com.svc.ems.entity.EventEntity;
import com.svc.ems.enums.ErrorCode;
import com.svc.ems.repo.EventRepository;
import com.svc.ems.svc.activity.ActivityService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ActivityServiceImpl implements ActivityService {

    private final EventRepository eventRepository;
    
    @Override
    public ResponseEntity<ApiResponseTemplate<List<ActivityQueryResponse>>> activityQuery() {
        List<EventEntity> eventList = eventRepository.findAll();
        if (eventList.isEmpty()) {
            return  ResponseEntity.badRequest().body(ApiResponseTemplate.fail(400, ErrorCode.DATA_NOT_FOUND));
        }

        List<ActivityQueryResponse> responsesList = new ArrayList<>();
        for (EventEntity event : eventList) {
            ActivityQueryResponse response = new ActivityQueryResponse();
            response.setEventId(event.getEventId());
            response.setEventName(event.getEventName());
            responsesList.add(response);
        }

        return ResponseEntity.ok(ApiResponseTemplate.success("Success", responsesList));
    }
}

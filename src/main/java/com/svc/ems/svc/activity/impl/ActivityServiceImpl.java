package com.svc.ems.svc.activity.impl;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.svc.ems.dto.activity.ActivityQueryResponse;
import com.svc.ems.dto.activity.ActivityRegDataRequest;
import com.svc.ems.dto.activity.ActivityRegDataResponse;
import com.svc.ems.dto.base.ApiResponseTemplate;
import com.svc.ems.entity.*;
import com.svc.ems.enums.ErrorCode;
import com.svc.ems.repo.EventRepository;
import com.svc.ems.repo.RegistrationMainRepository;
import com.svc.ems.svc.activity.ActivityService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

@Slf4j
@Service
@RequiredArgsConstructor
public class ActivityServiceImpl implements ActivityService {

    private final EventRepository eventRepository;
    private final RegistrationMainRepository registrationMainRepository;
    private final DateTimeFormatter yyyyMMddHHmmss = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");

    @Override
    public ResponseEntity<ApiResponseTemplate<List<ActivityQueryResponse>>> activityQuery() {
        List<EventEntity> eventList = eventRepository.findAll();
        if (eventList.isEmpty()) {
            return ResponseEntity.badRequest().body(ApiResponseTemplate.fail(400, ErrorCode.DATA_NOT_FOUND));
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

    @Override
    public ResponseEntity<ApiResponseTemplate<List<ActivityRegDataResponse>>> activityReqData(ActivityRegDataRequest req) {
        String eventId = req.getEventId();
        List<RegistrationMainEntity> regMainList = registrationMainRepository.findAll(RegistrationMainSpecification.filter(req));

        List<ActivityRegDataResponse> responsesList = new ArrayList<>();
        if (!regMainList.isEmpty()) {
            for (RegistrationMainEntity regMain : regMainList) {
                ActivityRegDataResponse response = new ActivityRegDataResponse();
                response.setRegistrationId(regMain.getRegistrationId());
                response.setRegistrationStatus(regMain.getRegistrationStatus());
                response.setPaymentStatus(regMain.getPaymentStatus());
                response.setFee(calculateFee(regMain.getRegistrationExtra(), regMain.getRegistrationPaymentInfo()));

                Timestamp regDate = regMain.getCreatedAt();
                if (regDate != null) {
                    response.setReqDate(regDate.toLocalDateTime().format(yyyyMMddHHmmss));
                }

                RegistrationDetailEntity registrationDetail = regMain.getRegistrationDetail();
                if (registrationDetail != null) {
                    response.setEmail(registrationDetail.getEmail());
                    response.setFullName(stringIsNotBlank(registrationDetail.getFirstName()) + " " + stringIsNotBlank(registrationDetail.getLastName()));
                    response.setCnName(stringIsNotBlank(registrationDetail.getFullNameCn()));
                }
                responsesList.add(response);
            }
        }
        return ResponseEntity.ok(ApiResponseTemplate.success("Success", responsesList));
    }

    private String stringIsNotBlank(String str) {
        return str == null || str.isEmpty() ? "" : str;
    }

    private String calculateFee(RegistrationExtraEntity extraEntity, RegistrationPaymentInfoEntity paymentInfoEntity) {
        AtomicReference<BigDecimal> totalFee = new AtomicReference<>(BigDecimal.ZERO);

        if (extraEntity != null && extraEntity.getFee() != null) {
            totalFee.set(new BigDecimal(extraEntity.getFee()));
        }

        if (paymentInfoEntity != null && paymentInfoEntity.getTotalAmount() != null) {
            totalFee.set(totalFee.get().add(new BigDecimal(paymentInfoEntity.getTotalAmount())));
        }
        return totalFee.get().toPlainString();
    }
}

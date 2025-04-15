package com.svc.ems.svc.regi.impl;

import com.svc.ems.config.jwt.JwtUtil;
import com.svc.ems.dto.base.ApiResponseTemplate;
import com.svc.ems.dto.registration.RegistrationDetailRequest;
import com.svc.ems.entity.MemberMainEntity;
import com.svc.ems.entity.RegistrationDetailEntity;
import com.svc.ems.entity.RegistrationMainEntity;
import com.svc.ems.enums.ErrorCode;
import com.svc.ems.repo.MemberMainRepository;
import com.svc.ems.repo.RegistrationDetailRepository;
import com.svc.ems.repo.RegistrationMainRepository;
import com.svc.ems.svc.regi.RegistrationService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class RegistrationServiceImpl implements RegistrationService {

    private final RegistrationMainRepository registrationMainRepository;
    private final RegistrationDetailRepository registrationDetailRepository;
    private final MemberMainRepository memberMainRepository;

    private final  JwtUtil jwtUtil;

    /**
     * Step 1 報名：填寫個人資料（個人報名或團體主報名人）
     */
    @Override
    @Transactional
    public ResponseEntity<ApiResponseTemplate<String>> registerStep1(RegistrationDetailRequest req, UserDetails userDetails, HttpServletResponse response) {


        Optional<MemberMainEntity> member = jwtUtil.validateAndGetEntity(userDetails, memberMainRepository);
        if (member.isEmpty()) {
            return ResponseEntity.badRequest().body(ApiResponseTemplate.fail(400, ErrorCode.MEMBER_NOT_FOUND));
        }
        // 1. 檢查該會員是否已有此活動報名紀錄
        RegistrationMainEntity mainEntity = registrationMainRepository.findByEventIdAndMemberId(req.getEventId(), member.get().getMemberId())
                .orElseGet(() -> createNewRegistrationMain(req , member.get()));

        // 2. 儲存報名詳細資料
        RegistrationDetailEntity detailEntity = RegistrationDetailEntity.builder()
                .registrationId(mainEntity.getRegistrationId())
                .title(req.getTitle())
                .firstName(req.getFirstName())
                .middleName(req.getMiddleName())
                .lastName(req.getLastName())
                .fullNameEn(req.getFirstName()+" "+req.getMiddleName()+" "+req.getLastName())
                .gender(req.getGender())
                .dateOfBirth(req.getDateOfBirth())
                .nationality(req.getNationality())
                .passportNumber(req.getPassportNumber())
                .department(req.getDepartment())
                .affiliation(req.getAffiliation())
                .cityOfAffiliation(req.getCityOfAffiliation())
                .countryOfAffiliation(req.getCountryOfAffiliation())
                .telNumber(req.getTelNumber())
                .mobileNumber(req.getMobileNumber())
                .email(req.getEmail())
                .dietaryRequest(req.getDietaryRequest())
                .registrationRole("PRESENT")
                .uploadUrl("")
                .isAccompanyingPerson(false)
                .createdAt(new Timestamp(System.currentTimeMillis()))
                .build();

        registrationDetailRepository.save(detailEntity);

        return ResponseEntity.ok(ApiResponseTemplate.success("Registration Step 1 completed."
              ));
    }

    /**
     * 若無主表則新增一筆
     */
    private RegistrationMainEntity createNewRegistrationMain(RegistrationDetailRequest req, MemberMainEntity member) {
        RegistrationMainEntity mainEntity = RegistrationMainEntity.builder()
                .registrationId(UUID.randomUUID().toString())
                .eventId(req.getEventId())
                .memberId(member.getMemberId())
                .registrationType("PRESENT")
                .isDomestic(false)
                .feeAmount(0) // Step2再計算
                .paymentStatus("UNPAID")
                .registrationStatus("PENDING")
                .isGroupMain(false)
                .createdAt(new Timestamp(System.currentTimeMillis()))
                .updatedAt(new Timestamp(System.currentTimeMillis()))
                .build();
        return registrationMainRepository.save(mainEntity);
    }
}

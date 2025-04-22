package com.svc.ems.svc.regi.impl;

import com.svc.ems.config.jwt.JwtUtil;
import com.svc.ems.dto.base.ApiResponseTemplate;
import com.svc.ems.dto.registration.*;
import com.svc.ems.entity.*;
import com.svc.ems.enums.ErrorCode;
import com.svc.ems.repo.*;
import com.svc.ems.svc.regi.RegistrationService;
import com.svc.ems.utils.MapperUtils;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class RegistrationServiceImpl implements RegistrationService {

    private final RegistrationMainRepository registrationMainRepository;
    private final RegistrationDetailRepository registrationDetailRepository;
    private final RegistrationExtraRepository registrationExtraRepository;
    private final AccompanyingPersonEntityRepository accompanyingPersonEntityRepository;
    private final MemberMainRepository memberMainRepository;

    private final JwtUtil jwtUtil;
    private final MapperUtils mapper;

    @Override
    public ResponseEntity<ApiResponseTemplate<RegistrationQueryResponse>> registerStep1Query(UserDetails userDetails, HttpServletResponse response) {
        Optional<MemberMainEntity> member = jwtUtil.validateAndGetEntity(userDetails, memberMainRepository);
        if (member.isEmpty()) {
            return ResponseEntity.badRequest().body(ApiResponseTemplate.fail(400, ErrorCode.MEMBER_NOT_FOUND));
        }
        MemberMainDto memberDto = MapperUtils.map(member, MemberMainDto.class);
        RegistrationQueryResponse resp = RegistrationQueryResponse.builder().memberMainDto(memberDto).build();
        return ResponseEntity.ok(ApiResponseTemplate.success(resp
        ));
     
    }

    /**
     * Step 1 報名：填寫個人資料（個人報名或團體主報名人）
     */
    @Override
    @Transactional
    public ResponseEntity<ApiResponseTemplate<String>> registerStep1(SoloRegistrationEventRequest req, UserDetails userDetails, HttpServletResponse response) {


        Optional<MemberMainEntity> member = jwtUtil.validateAndGetEntity(userDetails, memberMainRepository);
        if (member.isEmpty()) {
            return ResponseEntity.badRequest().body(ApiResponseTemplate.fail(400, ErrorCode.MEMBER_NOT_FOUND));
        }


        RegistrationMainDto registrationMainDto = req.getRegistrationMainDto();
        RegistrationDetailDto registrationDetailDto = req.getRegistrationDetailDto();
        RegistrationExtraDto registrationExtraDto = req.getRegistrationExtraDto();
        List<AccompanyingPersonDto> accompanyingPersonDtoList = req.getAccompanyingPersonDtoList();

        String uuid = UUID.randomUUID().toString();
        // 1. 檢查該會員是否已有此活動報名紀錄
        RegistrationMainEntity mainEntity = registrationMainRepository.findByEventIdAndMemberId(registrationMainDto.getEventId(), member.get().getMemberId())
                .orElseGet(() -> createNewRegistrationMain(req, member.get(),uuid));

        // 2. 儲存報名詳細資料
        RegistrationDetailEntity detailEntity = RegistrationDetailEntity.builder()
                .registrationId(uuid)
                .title(registrationDetailDto.getTitle())
                .firstName(registrationDetailDto.getFirstName())
                .lastName(registrationDetailDto.getLastName())
                .fullNameCn(mainEntity.getIsDomestic() ?registrationDetailDto.getFullNameCn():"")
                .gender(registrationDetailDto.getGender())
                .dateOfBirth(registrationDetailDto.getDateOfBirth())
                .nationality(registrationDetailDto.getNationality())
                .passportNumber(registrationDetailDto.getPassportNumber())
                .department(registrationDetailDto.getDepartment())
                .affiliation(registrationDetailDto.getAffiliation())
                .cityOfAffiliation(registrationDetailDto.getCityOfAffiliation())
                .countryOfAffiliation(registrationDetailDto.getCountryOfAffiliation())
                .telNumber(registrationDetailDto.getTelNumber())
                .mobileNumber(registrationDetailDto.getMobileNumber())
                .email(registrationDetailDto.getEmail())
                .dietaryRequest(registrationDetailDto.getDietaryRequest())
                .registrationRole("PRESENT")
                .uploadUrl("")
                .isAccompanyingPerson(false)
                .createdAt(new Timestamp(System.currentTimeMillis()))
                .build();
        registrationDetailRepository.save(detailEntity);



        RegistrationExtraEntity registrationExtraEntity = MapperUtils.map(registrationExtraDto, RegistrationExtraEntity.class);
        registrationExtraEntity.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        registrationExtraRepository.save(registrationExtraEntity);



        registrationExtraEntity.setRegistrationId(uuid);
        accompanyingPersonDtoList.forEach(x->
                {x.setMemberFollowedId(uuid);
                x.setCreateAt(new Timestamp(System.currentTimeMillis()));});

        List<AccompanyingPersonEntity> accompanyingPersonEntities = MapperUtils.mapList(accompanyingPersonDtoList, AccompanyingPersonEntity.class);
        accompanyingPersonEntityRepository.saveAll(accompanyingPersonEntities);
        return ResponseEntity.ok(ApiResponseTemplate.success("Registration Step 1 completed."
        ));
    }

    /**
     * 若無主表則新增一筆
     */
    private RegistrationMainEntity createNewRegistrationMain(SoloRegistrationEventRequest req, MemberMainEntity member,String uuid) {
        RegistrationMainDto registrationMainDto = req.getRegistrationMainDto();
        RegistrationMainEntity mainEntity = RegistrationMainEntity.builder()
                .registrationId(uuid)
                .eventId(req.getRegistrationMainDto().getEventId())
                .memberId(member.getMemberId())
                .registrationType(registrationMainDto.getRegistrationType())
                .isDomestic(registrationMainDto.getIsDomestic())
                .feeAmount(registrationMainDto.getFeeAmount()) // Step2再計算
                .paymentStatus("UNPAID")
                .registrationStatus("PENDING")
                .isGroupMain(false)
                .anyAccompanyingPerson(registrationMainDto.getAnyAccompanyingPerson())
                .createdAt(new Timestamp(System.currentTimeMillis()))
                .updatedAt(new Timestamp(System.currentTimeMillis()))
                .build();
         registrationMainRepository.save(mainEntity);
        return mainEntity;
    }
}

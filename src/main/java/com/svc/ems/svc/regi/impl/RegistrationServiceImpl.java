package com.svc.ems.svc.regi.impl;

import com.svc.ems.config.jwt.JwtUtil;
import com.svc.ems.dto.base.ApiResponseTemplate;
import com.svc.ems.dto.registration.*;
import com.svc.ems.entity.*;
import com.svc.ems.enums.ErrorCode;
import com.svc.ems.repo.*;
import com.svc.ems.svc.regi.RegistrationService;
import com.svc.ems.utils.IdGeneratorUtils;
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
import java.util.concurrent.atomic.AtomicInteger;

@RequiredArgsConstructor
@Service
public class RegistrationServiceImpl implements RegistrationService {

    private final RegistrationMainRepository registrationMainRepository;
    private final RegistrationDetailRepository registrationDetailRepository;
    private final RegistrationExtraRepository registrationExtraRepository;
    private final AccompanyingPersonEntityRepository accompanyingPersonEntityRepository;
    private final MemberMainRepository memberMainRepository;

    private final IdGeneratorUtils idGeneratorUtils;
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

        String registrationId = idGeneratorUtils.generateRegNo(registrationDetailDto.getCountryOfAffiliation(),registrationMainDto.getRegistrationType());
        // 1. 檢查該會員是否已有此活動報名紀錄
        RegistrationMainEntity mainEntity = registrationMainRepository.findByEventIdAndMemberId(registrationMainDto.getEventId(), member.get().getMemberId())
                .orElseGet(() -> createNewRegistrationMain(req, member.get(), registrationId));

        // 2. 儲存報名詳細資料
       saveRegistrationDetail(registrationDetailDto,registrationId);
       saveRegistrationExtra(registrationExtraDto,registrationId);
       saveRegistrationAccompany(accompanyingPersonDtoList,registrationId);





        return ResponseEntity.ok(ApiResponseTemplate.success("Registration Step 1 completed."
        ));
    }

    private void saveRegistrationAccompany(List<AccompanyingPersonDto> accompanyingPersonDtoList, String registrationId) {
        if (!accompanyingPersonDtoList.isEmpty()) {
            AtomicInteger seq = new AtomicInteger(0);
            accompanyingPersonDtoList.forEach(x ->
            {
                x.setMemberFollowedId(registrationId);
                x.setSeq(seq.getAndIncrement());
                x.setCreateAt(new Timestamp(System.currentTimeMillis()));
            });
            List<AccompanyingPersonEntity> accompanyingPersonEntities = MapperUtils.mapList(accompanyingPersonDtoList, AccompanyingPersonEntity.class);
            accompanyingPersonEntityRepository.saveAll(accompanyingPersonEntities);
        }
    }

    /**
     * 團體報名 Step1
     */
    @Transactional
    public ResponseEntity<ApiResponseTemplate<String>> registerGroupStep1(GroupRegistrationEventRequest req, UserDetails userDetails, HttpServletResponse response) {

        Optional<MemberMainEntity> member = jwtUtil.validateAndGetEntity(userDetails, memberMainRepository);
        if (member.isEmpty()) {
            return ResponseEntity.badRequest().body(ApiResponseTemplate.fail(400, ErrorCode.MEMBER_NOT_FOUND));
        }
       String eventId = req.getRegistrationMainDtoList().get(0).getEventId();
        // 產生團體代碼，例如 GP1-2025-UUID縮寫
        Integer groupId = idGeneratorUtils.generateGroupIndex(eventId);
        String groupCode = "GP" + groupId;
        int seq = 1;

        AtomicInteger counter = new AtomicInteger(1);

        for (RegistrationMainDto dto : req.getRegistrationMainDtoList()) {
            String regId = idGeneratorUtils.generateFullRegistrationNumber(groupCode, seq);
            RegistrationDetailDto registrationDetailDto = dto.getRegistrationDetailDto();
            RegistrationExtraDto registrationExtraDto = dto.getRegistrationExtraDto();
            List<AccompanyingPersonDto> accompanyingPersonDtoList = dto.getAccompanyingPersonDtoList();

            saveGroupRegistrationMain(dto,member.get(),regId);
            saveRegistrationDetail(registrationDetailDto,regId);
            saveRegistrationExtra(registrationExtraDto,regId);
            saveRegistrationAccompany(accompanyingPersonDtoList,regId);
            // 1. 建立 Main
//            RegistrationMainEntity main = RegistrationMainEntity.builder()
//                    .registrationId(regId)
//                    .eventId(eventId)
//                    .memberId(member.get().getMemberId()) // 此處統一由主報名人送出
//                    .groupCode(groupCode)
//                    .registrationType(dto.getRegistrationType())
//                    .isDomestic(dto.getIsDomestic())
//                    .feeAmount(dto.getFeeAmount())
//                    .paymentStatus("UNPAID")
//                    .registrationStatus("PENDING")
//                    .isGroupMain(counter.get() == 1)
//                    .anyAccompanyingPerson(dto.getAnyAccompanyingPerson())
//                    .createdAt(new Timestamp(System.currentTimeMillis()))
//                    .updatedAt(new Timestamp(System.currentTimeMillis()))
//                    .build();
//
//            registrationMainRepository.save(main);
//            List<AccompanyingPersonDto> accompanyingPersonDtoList = dto.getAccompanyingPersonDtoList();
//
//            int finalSeq = seq;
//            accompanyingPersonDtoList.forEach(x->{
//                AccompanyingPersonEntity entity = MapperUtils.map(x, AccompanyingPersonEntity.class);
//                entity.setSeq(finalSeq);
//                entity.setMemberFollowedId(regId);
//                entity.setCreateAt(new Timestamp(System.currentTimeMillis()));
//                accompanyingPersonEntityRepository.save(entity);
//            });
            seq++;



            counter.incrementAndGet();
        }

        return ResponseEntity.ok(ApiResponseTemplate.success("Group registration submitted successfully. Group Code: " + groupCode));
    }


    private void saveGroupRegistrationMain(RegistrationMainDto dto, MemberMainEntity member, String regId){

        RegistrationMainEntity mainEntity = RegistrationMainEntity.builder()
                .registrationId(regId)
                .eventId(dto.getEventId())
                .memberId(member.getMemberId())
                .registrationType(dto.getRegistrationType())
                .isDomestic(dto.getIsDomestic())
                .feeAmount(dto.getFeeAmount()) // Step2再計算
                .paymentStatus("UNPAID")
                .registrationStatus("PENDING")
                .isGroupMain(dto.getIsGroupMain())
                .anyAccompanyingPerson(dto.getAnyAccompanyingPerson())
                .createdAt(new Timestamp(System.currentTimeMillis()))
                .updatedAt(new Timestamp(System.currentTimeMillis()))
                .build();
        registrationMainRepository.save(mainEntity);

    }

    private void saveRegistrationDetail(RegistrationDetailDto registrationDetailDto,String registrationId){

        RegistrationDetailEntity detailEntity = RegistrationDetailEntity.builder()
                .registrationId(registrationId)
                .title(registrationDetailDto.getTitle())
                .firstName(registrationDetailDto.getFirstName())
                .lastName(registrationDetailDto.getLastName())
                .fullNameCn("Taiwan".equals(registrationDetailDto.getCountryOfAffiliation()) ? registrationDetailDto.getFullNameCn() : "")
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
    }

    private void saveRegistrationExtra(RegistrationExtraDto dto,String registrationId){

        RegistrationExtraEntity registrationExtraEntity = MapperUtils.map(dto, RegistrationExtraEntity.class);
        registrationExtraEntity.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        registrationExtraEntity.setRegistrationId(registrationId);
        registrationExtraRepository.save(registrationExtraEntity);
    }


    /**
     * 若無主表則新增一筆
     */
    private RegistrationMainEntity createNewRegistrationMain(SoloRegistrationEventRequest req, MemberMainEntity member, String regId) {
        RegistrationMainDto registrationMainDto = req.getRegistrationMainDto();
        RegistrationMainEntity mainEntity = RegistrationMainEntity.builder()
                .registrationId(regId)
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

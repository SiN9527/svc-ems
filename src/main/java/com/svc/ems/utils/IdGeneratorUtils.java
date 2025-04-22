package com.svc.ems.utils;

import com.svc.ems.entity.RegistrationGroupMainEntity;
import com.svc.ems.entity.RegistrationMainEntity;
import com.svc.ems.repo.RegistrationGroupMainRepository;
import com.svc.ems.repo.RegistrationMainRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


    @Component
    @RequiredArgsConstructor
    public class IdGeneratorUtils {

        private final RegistrationGroupMainRepository registrationGroupMainRepository;
        private final RegistrationMainRepository registrationMainRepository;




        public String generateRegNo(String country, String regType) {
            // Step 1: 組合前綴（不含流水碼）
            String countryCode = "Taiwan".equals(country) ? "D" : "O";

            String typeCode = switch (regType.toLowerCase()) {
                case "doctor", "professor" -> "P";
                case "resident", "graduate", "researcher", "nurse", "educator", "individual researcher" -> "R";
                case "student" -> "S";
                case "member" -> "M";
                default -> throw new IllegalArgumentException("無效的註冊類型：" + regType);
            };

            String dateStr = new SimpleDateFormat("yyyyMMdd").format(new Date());
            String prefix = countryCode + typeCode + dateStr;

            // Step 2: 查詢當前 prefix 的最大 reg_no
            String maxRegNo = registrationMainRepository.findMaxRegistrationIdByPrefix(prefix);

            int nextSerial = 1;
            if (maxRegNo != null && maxRegNo.length() >= prefix.length() + 3) {
                String lastSerialStr = maxRegNo.substring(prefix.length());
                try {
                    nextSerial = Integer.parseInt(lastSerialStr) + 1;
                } catch (NumberFormatException e) {
                    // 忽略錯誤，保持 nextSerial = 1
                }
            }

            // Step 3: 補上三碼流水號
            String serial = String.format("%03d", nextSerial);
            return prefix + serial;
        }
















        /**
         * 根據活動產生新的團體代碼 GP1, GP2...
         */
        public Integer generateGroupIndex(String eventId) {
            List<RegistrationGroupMainEntity> existingGroups = registrationGroupMainRepository.findByEventIdOrderByGroupIdAsc(eventId);
            return existingGroups.size() + 1;
        }

        /**
         * 依據團體代碼取得目前流水編號（001、002...）
         */
        public int getNextGroupSeq(String eventId,String groupId) {
            List<RegistrationMainEntity> groupMembers = registrationGroupMainRepository.findByEventIdAndGroupId(eventId,groupId);
            return groupMembers.size() + 1;
        }

        /**
         * 組合成顯示編號：GP1-001
         */
        public String generateFullRegistrationNumber(String groupCode, int groupSeq) {
            return groupCode + "-" + String.format("%03d", groupSeq);
        }
    }
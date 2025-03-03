package com.svc.ems.svc.mail.impl;

import com.svc.ems.entity.EmailTemplateEntity;
import com.svc.ems.enums.ErrorCode;
import com.svc.ems.exception.ServiceException;
import com.svc.ems.repo.EmailTemplateRepository;
import com.svc.ems.svc.mail.EmailService;
import com.svc.ems.svc.mail.EmailTemplateService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
public class EmailTemplateServiceImpl implements EmailTemplateService {

    private final EmailTemplateRepository emailTemplateRepository; // JWT 工具類


    public EmailTemplateServiceImpl(EmailTemplateRepository emailTemplateRepository) {
        this.emailTemplateRepository = emailTemplateRepository;
    }

    /**
     * 取得特定研討會的郵件模板
     */
    public List<EmailTemplateEntity> getTemplatesByEventId(String eventId) {
        return emailTemplateRepository.findByEventId(eventId);
    }

    /**
     * 取得特定類型的郵件模板
     */
    public EmailTemplateEntity getTemplate(String eventId, String templateType) {
        Optional<EmailTemplateEntity> template = emailTemplateRepository.findByEventIdAndTemplateType(eventId, templateType);
        if (template.isEmpty()) {
            throw new ServiceException(ErrorCode.EMAIL_TEMPLATE_NOT_FOUND);
        }
        return template.get();
    }

    /**
     * 創建 / 更新郵件模板
     */
    public EmailTemplateEntity saveTemplate(EmailTemplateEntity template) {
        return emailTemplateRepository.save(template);
    }

    /**
     * 刪除郵件模板
     */
    public void deleteTemplate(String eventId, Integer emailId) {
        emailTemplateRepository.deleteByEventIdAndEmailId(eventId,emailId);
    }

    /**
     * **使用占位符填充郵件內容**
     * e.g. `Dear {name}, your registration is confirmed.` 會替換 `{name}`
     */
    public String fillTemplate(String templateContent, Map<String, String> params) {
        for (Map.Entry<String, String> entry : params.entrySet()) {
            templateContent = templateContent.replace("{" + entry.getKey() + "}", entry.getValue());
        }
        return templateContent;
    }
}

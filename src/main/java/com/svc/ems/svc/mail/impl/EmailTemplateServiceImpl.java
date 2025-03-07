package com.svc.ems.svc.mail.impl;

import com.svc.ems.dto.base.ApiResponseTemplate;
import com.svc.ems.dto.mail.EmailTemplateDTO;
import com.svc.ems.entity.EmailTemplateEntity;
import com.svc.ems.entity.EventEntity;
import com.svc.ems.enums.ErrorCode;
import com.svc.ems.exception.ServiceException;
import com.svc.ems.repo.EmailTemplateRepository;
import com.svc.ems.repo.EventRepository;
import com.svc.ems.svc.mail.EmailTemplateService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
public class EmailTemplateServiceImpl implements EmailTemplateService {

    private final EmailTemplateRepository emailTemplateRepository; // JWT 工具類

    private final EventRepository eventRepository;

    /**
     * **創建/更新郵件模板**
     */
    public ApiResponseTemplate<String> createOrUpdateTemplate(EmailTemplateDTO dto) {
        Optional<EventEntity> event = eventRepository.findById(dto.getEventId());
        if (event.isEmpty()) {
            return ApiResponseTemplate.fail(404, "Event not found");
        }

        // 找到該活動的對應模板，若無則新增
        EmailTemplateEntity template = emailTemplateRepository
                .findByEventIdAndTemplateType(dto.getEventId(), dto.getTemplateType())
                .orElse(new EmailTemplateEntity());

        template.setEventId(dto.getEventId());
        template.setTemplateType(dto.getTemplateType());
        template.setSubject(dto.getSubject());
        template.setContent(dto.getContent());
        template.setUpdateBy(dto.getUpdateBy());
        template.setUpdatedAt(new Timestamp(System.currentTimeMillis()));

        emailTemplateRepository.save(template);
        return ApiResponseTemplate.success("Email template saved successfully");
    }

    /**
     * **取得該活動的所有郵件模板**
     */
    public ApiResponseTemplate<List<EmailTemplateEntity>> getTemplatesByEvent(String eventId) {
        return ApiResponseTemplate.success(emailTemplateRepository.findByEventId(eventId));
    }

    /**
     * **刪除郵件模板**
     */
    public ApiResponseTemplate<String> deleteTemplate(Integer emailId,String eventId) {
        emailTemplateRepository.deleteByEmailIdAndEventId(emailId,eventId);
        return ApiResponseTemplate.success("Email template deleted successfully");
    }



    public EmailTemplateServiceImpl(EmailTemplateRepository emailTemplateRepository, EventRepository eventRepository) {
        this.emailTemplateRepository = emailTemplateRepository;
        this.eventRepository = eventRepository;
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

package com.svc.ems.svc.mail;

import com.svc.ems.dto.base.ApiResponseTemplate;
import com.svc.ems.dto.mail.EmailTemplateDTO;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;

public interface EmailTemplateService {


    ResponseEntity<ApiResponseTemplate<?>> getTemplatesByEvent(EmailTemplateDTO req);

    ResponseEntity<ApiResponseTemplate<?>> saveOrUpdate(@Valid EmailTemplateDTO req);

    ResponseEntity<ApiResponseTemplate<?>> deleteTemplate(EmailTemplateDTO req);
}

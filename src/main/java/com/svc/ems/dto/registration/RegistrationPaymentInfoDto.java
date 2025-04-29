package com.svc.ems.dto.registration;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.svc.ems.entity.RegistrationPaymentInfoEntity;
import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * DTO for {@link RegistrationPaymentInfoEntity}
 */
@Data
public class RegistrationPaymentInfoDto implements Serializable {


    @JsonProperty("RegistrationId")
    private String registrationId;

    @JsonProperty("PaymentMethod")
    private String paymentMethod;

    @JsonProperty("PaymentStatus")
    private String paymentStatus;

    @JsonProperty("TotalAmount")
    private Integer totalAmount;
    @JsonProperty("ReceiptRequired")
    private Boolean receiptRequired;

    @JsonProperty("ReceiptTitle")
    private String receiptTitle;

    @JsonProperty("VatNumber")
    private String vatNumber;

    @JsonProperty("ReportYear")
    private String reportYear;

    @JsonProperty("MailingAddress")
    private String mailingAddress;

    @JsonProperty("IsGroup")
    private boolean isGroup;
    @JsonProperty("CreatedAt")
    private Timestamp createdAt;
    @JsonProperty("UpdatedAt")
    private Timestamp updatedAt;
}
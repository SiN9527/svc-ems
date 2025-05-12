package com.svc.ems.entity;

import com.svc.ems.dto.activity.ActivityRegDataRequest;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class RegistrationMainSpecification {
    public static Specification<RegistrationMainEntity> filter(ActivityRegDataRequest req) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<Predicate>();
            predicates.add(cb.equal(root.get("eventId"), req.getEventId()));

            String regType = req.getRegType();
            if (regType.equals("GP")) {
                predicates.add(cb.like(root.get("registrationId"), regType + "%"));
            } else {
                predicates.add(cb.notLike(root.get("registrationId"), "GP%"));
            }

            if (req.getEmail() != null && !req.getEmail().isEmpty()) {
                predicates.add(cb.like(root.join("registrationDetail").get("email"), "%" + req.getEmail() + "%"));
            }

            if (req.getFirstName() != null && !req.getFirstName().isEmpty()) {
                predicates.add(cb.like(root.join("registrationDetail").get("firstName"), "%" + req.getFirstName() + "%"));
            }

            if (req.getLastName() != null && !req.getLastName().isEmpty()) {
                predicates.add(cb.like(root.join("registrationDetail").get("lastName"), "%" + req.getLastName() + "%"));
            }

            if (req.getCountryOfAffiliation() != null && !req.getCountryOfAffiliation().isEmpty()) {
                predicates.add(cb.like(root.join("registrationDetail").get("countryOfAffiliation"), "%" + req.getCountryOfAffiliation() + "%"));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}

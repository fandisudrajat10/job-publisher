package com.job.publisher.validator;

import com.job.publisher.domain.JobStatus;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ValidStatusValidator implements ConstraintValidator<ValidStatus, String> {
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return false;
        }
        for (JobStatus status : JobStatus.values()) {
            if (status.name().equals(value)) {
                return true;
            }
        }
        return false;
    }
}

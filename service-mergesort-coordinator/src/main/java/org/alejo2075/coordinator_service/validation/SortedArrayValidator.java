package org.alejo2075.coordinator_service.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class SortedArrayValidator implements ConstraintValidator<Sorted, int[]> {
    @Override
    public void initialize(Sorted constraintAnnotation) {
    }

    @Override
    public boolean isValid(int[] value, ConstraintValidatorContext context) {
        if (value == null || value.length < 2) {
            return true; // Not the job of this validator to check for null or size
        }
        for (int i = 0; i < value.length - 1; i++) {
            if (value[i] > value[i + 1]) {
                return false;
            }
        }
        return true;
    }
}


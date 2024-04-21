package org.alejo2075.coordinator_service.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = SortedArrayValidator.class)
@Target({ ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface Sorted {
    String message() default "The array must be sorted";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}


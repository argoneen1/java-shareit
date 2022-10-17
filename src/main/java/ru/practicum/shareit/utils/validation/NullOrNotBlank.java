package ru.practicum.shareit.utils.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;

@Constraint(validatedBy = NullOrNotBlankValidator.class)
@Retention(RetentionPolicy.RUNTIME)
@Target({ METHOD, FIELD, PARAMETER})
public @interface NullOrNotBlank {
    String message() default "must be a null or not blank string.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}

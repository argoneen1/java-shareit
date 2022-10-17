package ru.practicum.shareit.utils.validation;

import org.springframework.util.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;


public class NullOrNotBlankValidator implements ConstraintValidator<NullOrNotBlank, String> {
    @Override
    public boolean isValid(String str, ConstraintValidatorContext constraintValidatorContext) {
        return str == null || StringUtils.hasText(str);
    }
}

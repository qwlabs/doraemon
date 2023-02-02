package com.qwlabs.validator.constraintvalidators.oneof;

import com.qwlabs.validator.constraints.OneOf;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public class OneOfValidator implements ConstraintValidator<OneOf, String> {
    private Set<String> allowValues;
    private boolean caseInsensitive;
    private boolean ignoreWhitespace;
    private boolean allowEmpty;

    @Override
    public void initialize(OneOf annotation) {
        this.caseInsensitive = annotation.ignoreCase();
        this.ignoreWhitespace = annotation.ignoreWhitespace();
        this.allowEmpty = annotation.allowEmpty();
        this.allowValues = Arrays.stream(annotation.value())
                .map(v -> this.safeValue(v, caseInsensitive, ignoreWhitespace))
                .collect(Collectors.toSet());
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }
        String v = this.safeValue(value, this.caseInsensitive, this.ignoreWhitespace);
        return (this.allowEmpty && v.isEmpty()) || this.allowValues.contains(v);
    }

    private String safeValue(String value,
                             boolean caseInsensitive,
                             boolean ignoreWhitespace) {
        String v = value;
        if (ignoreWhitespace) {
            v = v.trim();
        }
        if (caseInsensitive) {
            v = v.toLowerCase();
        }
        return v;
    }
}

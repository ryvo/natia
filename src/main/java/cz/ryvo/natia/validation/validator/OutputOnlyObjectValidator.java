package cz.ryvo.natia.validation.validator;

import cz.ryvo.natia.validation.constraint.OutputOnly;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class OutputOnlyObjectValidator implements ConstraintValidator<OutputOnly, Object> {
    @Override
    public void initialize(OutputOnly constraintAnnotation) {
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        return value == null;
    }
}

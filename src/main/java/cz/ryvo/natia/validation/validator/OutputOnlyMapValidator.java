package cz.ryvo.natia.validation.validator;

import cz.ryvo.natia.validation.constraint.OutputOnly;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Map;

public class OutputOnlyMapValidator implements ConstraintValidator<OutputOnly, Map<?, ?>> {
    @Override
    public void initialize(OutputOnly constraintAnnotation) {
    }

    @Override
    public boolean isValid(Map<?, ?> value, ConstraintValidatorContext context) {
        return value == null || value.isEmpty();
    }
}

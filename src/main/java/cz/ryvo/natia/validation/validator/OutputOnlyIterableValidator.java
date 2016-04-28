package cz.ryvo.natia.validation.validator;

import cz.ryvo.natia.validation.constraint.OutputOnly;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class OutputOnlyIterableValidator implements ConstraintValidator<OutputOnly, Iterable<?>> {
    @Override
    public void initialize(OutputOnly constraintAnnotation) {
    }

    @Override
    public boolean isValid(Iterable<?> value, ConstraintValidatorContext context) {
        return value == null || !value.iterator().hasNext();
    }
}

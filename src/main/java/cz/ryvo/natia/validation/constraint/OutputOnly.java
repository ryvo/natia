package cz.ryvo.natia.validation.constraint;

import cz.ryvo.natia.validation.validator.OutputOnlyIterableValidator;
import cz.ryvo.natia.validation.validator.OutputOnlyMapValidator;
import cz.ryvo.natia.validation.validator.OutputOnlyObjectValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({FIELD, TYPE})
@Retention(RUNTIME)
@Constraint(validatedBy = {
        OutputOnlyObjectValidator.class,
        OutputOnlyIterableValidator.class,
        OutputOnlyMapValidator.class
})
@Documented
public @interface OutputOnly {

    String message() default "{cz.ryvo.natia.validation.constraint.OutputOnly.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}

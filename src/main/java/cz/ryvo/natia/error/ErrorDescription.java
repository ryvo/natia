package cz.ryvo.natia.error;

import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Retention(RUNTIME)
@Target(TYPE)
public @interface ErrorDescription {

    String value() default "1000";

    String message() default "Unknown error";

    int statusCode() default Integer.MIN_VALUE;

    Class<? extends Annotation>[] annotation() default {ErrorDescription.NoValidationAnnotation.class};

    public @interface NoValidationAnnotation {
    }
}

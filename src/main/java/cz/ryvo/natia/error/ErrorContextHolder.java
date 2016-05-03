package cz.ryvo.natia.error;

import org.slf4j.Logger;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.core.type.filter.AssignableTypeFilter;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static java.lang.String.format;
import static org.slf4j.LoggerFactory.getLogger;

public class ErrorContextHolder {

    private static final Logger log = getLogger(ErrorContextHolder.class);

    private final static Map<Class<? extends ErrorCode>, ErrorDescription> CODES = new HashMap<>();

    private static final Map<Class<? extends Annotation>, ErrorMetadata> DEFAULT_CODES = new HashMap<>();

    static {
        ClassPathScanningCandidateComponentProvider scanner = new ClassPathScanningCandidateComponentProvider(false);
        scanner.addIncludeFilter(new AnnotationTypeFilter(ErrorDescription.class));
        scanner.addIncludeFilter(new AssignableTypeFilter(ErrorCode.class));
        Set<BeanDefinition> candidateComponents = scanner.findCandidateComponents("cz.ryvo.natia");
        processErrorClasses(candidateComponents);
    }

    private static void processErrorClasses(Set<BeanDefinition> candidateComponents) {
        Set<String> errorCodes = new HashSet<>(candidateComponents.size());
        candidateComponents.forEach(beanDefinition -> {
            try {
                Class<? extends ErrorCode> beanClass = (Class<? extends ErrorCode>) Class.forName(beanDefinition.getBeanClassName());
                ErrorDescription annotation = beanClass.getAnnotation(ErrorDescription.class);
                if (annotation != null) {
                    String errorCode = annotation.value();
                    if (errorCodes.contains(errorCode)) {
                        throw new RuntimeException(String.format("Duplicate error code '%s'", errorCode));
                    }
                    CODES.put(beanClass, annotation);
                    errorCodes.add(errorCode);

                    Class<? extends Annotation>[] defaultAnnotations = annotation.annotation();
                    for (Class<? extends Annotation> defaultAnnotation : defaultAnnotations) {
                        if (!ErrorDescription.NoValidationAnnotation.class.equals(defaultAnnotation)) {
                            if (DEFAULT_CODES.containsKey(defaultAnnotation)) {
                                throw new RuntimeException(format("Duplicate default annotation '%s'", defaultAnnotation.getName()));
                            }
                            DEFAULT_CODES.put(defaultAnnotation, new ErrorMetadata(annotation, beanClass));
                        }
                    }
                }
            } catch (ClassNotFoundException e) {
                log.warn("Class " + beanDefinition.getBeanClassName() + "not found.", e);
            }
        });
    }

    public static String getCode(Class<? extends ErrorCode> clazz) {
        ErrorDescription errorDescription = CODES.get(clazz);
        return errorDescription != null ? errorDescription.value() : ErrorCode.UNKNOWN_ERROR_CODE;
    }

    public static String getRawMessage(Class<? extends ErrorCode> clazz) {
        ErrorDescription errorDescription = CODES.get(clazz);
        return errorDescription != null ? errorDescription.message() : ErrorCode.UNKNOWN_ERROR_MESSAGE;
    }
}

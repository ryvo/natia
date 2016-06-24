package cz.ryvo.natia.error;

import lombok.experimental.UtilityClass;
import org.apache.tomcat.jni.*;

@UtilityClass
public final class Errors {

    @ErrorDescription(value = "1000", message = "Internal server error")
    public  static final class INTERNAL_ERROR implements ErrorCode {}

    @ErrorDescription(value = "1001", message = "Resource '{resource}' with ID {id} not found")
    public static final class RESOURCE_NOT_FOUND implements ErrorCode {}

    // INPUT DATA
    @ErrorDescription(value = "2000", message = "Invalid file")
    public static final class INVALID_FILE implements ErrorCode {}

    @ErrorDescription(value = "2001", message = "Invalid rank {rank} for resource {resource}")
    public static final class INVALID_ITEM_RANK implements ErrorCode {}

    @ErrorDescription(value = "2002", message = "Rule with name '{name}' exists already")
    public static final class DUPLICATE_RULE_NAME implements ErrorCode {}

    // OPERATION
    @ErrorDescription(value = "4000", message = "Search parameters are inaccurate and would provide too many results")
    public static final class SEARCH_PARAMS_TOO_INACCURATE implements ErrorCode {}
}

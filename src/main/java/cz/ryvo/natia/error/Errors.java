package cz.ryvo.natia.error;

import lombok.experimental.UtilityClass;

@UtilityClass
public final class Errors {

    @ErrorDescription(value = "1000", message = "Resource '{resource}' with ID {id} not found")
    public static final class RESOURCE_NOT_FOUND implements ErrorCode {}

    // INPUT DATA
    @ErrorDescription(value = "2000", message = "Invalid file")
    public static final class INVALID_FILE implements ErrorCode {}

}

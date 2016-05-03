package cz.ryvo.natia.error;

public class ErrorMetadata {
    private final ErrorDescription description;

    private final Class<? extends ErrorCode> clazz;

    public ErrorMetadata(ErrorDescription description, Class<? extends ErrorCode> clazz) {
        this.description = description;
        this.clazz = clazz;
    }

    public ErrorDescription getDescription() {
        return description;
    }

    public Class<? extends ErrorCode> getClazz() {
        return clazz;
    }
}

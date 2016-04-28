package cz.ryvo.natia.converter;

import javax.annotation.Nullable;

public interface DomainConverter<DOMAIN, API> {

    default DOMAIN toDomain(@Nullable API api) {
        return null;
    }
}

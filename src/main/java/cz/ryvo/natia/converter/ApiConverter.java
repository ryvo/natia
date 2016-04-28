package cz.ryvo.natia.converter;

import javax.annotation.Nullable;

public interface ApiConverter<API, DOMAIN> {

    @Nullable
    default API toApi(@Nullable DOMAIN domain) {
        return null;
    }
}

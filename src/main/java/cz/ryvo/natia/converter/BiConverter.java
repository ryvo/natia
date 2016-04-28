package cz.ryvo.natia.converter;

public interface BiConverter<API, DOMAIN> extends DomainConverter<DOMAIN, API>, ApiConverter<API, DOMAIN> {
}

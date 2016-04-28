package cz.ryvo.natia.converter;

import cz.ryvo.natia.api.Catalogue;
import cz.ryvo.natia.domain.CatalogueVO;

import java.util.List;

import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;
import static org.springframework.util.CollectionUtils.isEmpty;

@Converter
public class CatalogueConverter implements BiConverter<Catalogue, CatalogueVO> {

    public Catalogue toApi(CatalogueVO domain) {
        Catalogue api = new Catalogue();
        api.setId(domain.getId());
        api.setName(domain.getName());
        api.setValidFrom(domain.getValidFrom());
        api.setValidTo(domain.getValidTo());
        return api;
    }

    public CatalogueVO toDomain(Catalogue api) {
        CatalogueVO domain = new CatalogueVO();
        domain.setName(domain.getName());
        domain.setValidFrom(domain.getValidFrom());
        domain.setValidTo(domain.getValidTo());
        return domain;

    }

    public List<Catalogue> toApi(List<CatalogueVO> domains) {
        if (isEmpty(domains)) {
            return emptyList();
        }

        return domains.stream().map(this::toApi).collect(toList());
    }
}

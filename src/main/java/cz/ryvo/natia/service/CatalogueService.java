package cz.ryvo.natia.service;

import cz.ryvo.natia.domain.CatalogueVO;

import java.util.List;

public interface CatalogueService {

    List<CatalogueVO> listCatalogues();

    CatalogueVO getCatalogue(Long id);
}

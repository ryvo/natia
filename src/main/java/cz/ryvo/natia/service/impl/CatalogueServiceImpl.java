package cz.ryvo.natia.service.impl;

import cz.ryvo.natia.domain.CatalogueVO;
import cz.ryvo.natia.repository.CatalogueRepository;
import cz.ryvo.natia.service.CatalogueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.springframework.data.domain.Sort.Direction.DESC;

@Service
public class CatalogueServiceImpl implements CatalogueService {

    @Autowired
    private CatalogueRepository catalogueRepository;

    @Override
    public List<CatalogueVO> listCatalogues() {
        return catalogueRepository.findAll(new Sort(DESC, "creationTime"));
    }
}

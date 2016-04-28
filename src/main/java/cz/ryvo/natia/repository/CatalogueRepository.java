package cz.ryvo.natia.repository;

import cz.ryvo.natia.domain.CatalogueVO;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CatalogueRepository extends JpaRepository<CatalogueVO, Long> {
}

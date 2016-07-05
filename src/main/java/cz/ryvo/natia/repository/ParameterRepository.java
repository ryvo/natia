package cz.ryvo.natia.repository;

import cz.ryvo.natia.domain.ParameterVO;
import cz.ryvo.natia.domain.ParameterVO.ParameterEnum;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ParameterRepository extends JpaRepository<ParameterVO, ParameterEnum> {
}

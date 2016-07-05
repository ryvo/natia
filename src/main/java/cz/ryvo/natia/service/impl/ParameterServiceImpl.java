package cz.ryvo.natia.service.impl;

import cz.ryvo.natia.domain.ParameterVO;
import cz.ryvo.natia.domain.ParameterVO.ParameterEnum;
import cz.ryvo.natia.repository.ParameterRepository;
import cz.ryvo.natia.service.ParameterService;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Nullable;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class ParameterServiceImpl implements ParameterService {

    @Autowired
    private ParameterRepository parameterRepository;

    @Override
    public void setParameter(@NonNull ParameterEnum id, @Nullable String value) {
        ParameterVO param = parameterRepository.findOne(id);
        if (param != null) {
            if (value == null) {
                parameterRepository.delete(param);
                return;
            }
            param.setValue(value);

        } else {
            param = new ParameterVO(id, value);
        }
        parameterRepository.save(param);
    }

    @Override
    public ParameterVO getParameter(@NonNull ParameterEnum id) {
        return parameterRepository.findOne(id);
    }

    @Override
    public Map<ParameterEnum, String> getParameters(Set<ParameterEnum> ids) {
        List<ParameterVO> params = parameterRepository.findAll(ids);
        return params.stream().collect(Collectors.toMap(ParameterVO::getId, ParameterVO::getValue));
    }
}

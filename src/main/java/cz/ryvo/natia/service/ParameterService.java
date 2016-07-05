package cz.ryvo.natia.service;

import cz.ryvo.natia.domain.ParameterVO;
import cz.ryvo.natia.domain.ParameterVO.ParameterEnum;
import lombok.NonNull;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface ParameterService {

    void setParameter(@Nonnull ParameterEnum id, @Nullable String value);

    ParameterVO getParameter(@NonNull ParameterEnum id);

    Map<ParameterEnum, String> getParameters(Set<ParameterEnum> ids);
}

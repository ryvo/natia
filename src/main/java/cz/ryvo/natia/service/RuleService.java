package cz.ryvo.natia.service;

import cz.ryvo.natia.domain.RuleVO;
import lombok.NonNull;

import javax.annotation.Nonnull;
import java.util.List;

public interface RuleService {

    List<RuleVO> getRules();

    RuleVO getRule(@Nonnull Long id);

    Long createRule(@Nonnull RuleVO rule);

    void updateRule(@Nonnull Long id, @Nonnull RuleVO rule);

    void deleteRule(@Nonnull Long id);
}

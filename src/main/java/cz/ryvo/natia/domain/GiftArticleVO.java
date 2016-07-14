package cz.ryvo.natia.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@EqualsAndHashCode(exclude = {
        "description",
        "pieces",
        "inCatalogue",
        "rules"
})
public class GiftArticleVO {

    private String code;

    private String description;

    private Integer pieces;

    private Boolean inCatalogue;

    private List<GiftArticleRuleVO> rules = new ArrayList<>();
}

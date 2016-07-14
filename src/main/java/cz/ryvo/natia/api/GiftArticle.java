package cz.ryvo.natia.api;

import cz.ryvo.natia.validation.constraint.OutputOnly;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class GiftArticle {

    @OutputOnly
    private String code;

    @OutputOnly
    private String description;

    @OutputOnly
    private Integer pieces;

    @OutputOnly
    private Boolean inCatalogue;

    @OutputOnly
    private List<GiftArticleRule> rules;
}

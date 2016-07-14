package cz.ryvo.natia.api;

import cz.ryvo.natia.validation.constraint.OutputOnly;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class GiftArticleRule {

    @OutputOnly
    private String name;

    @OutputOnly
    private Integer pieces;
}

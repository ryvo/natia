package cz.ryvo.natia.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude = "pieces")
public class GiftArticleRuleVO {

    private String name;

    private Integer pieces;
}

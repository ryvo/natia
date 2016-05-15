package cz.ryvo.natia.api;

import cz.ryvo.natia.validation.constraint.OutputOnly;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@NoArgsConstructor
public class Rule {

    @OutputOnly
    private Long id;

    @NotNull
    @Length(min = 1, max = 128)
    private String name;

    private Integer rank;

    @Valid
    private List<RuleArticle> outputArticles;

    @Valid
    private List<RuleArticle> inputArticles;
}

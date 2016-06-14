package cz.ryvo.natia.api;

import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import cz.ryvo.natia.validation.constraint.OutputOnly;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@NoArgsConstructor
public class Rule extends ApiObject {

    public static final Long serialVersionUID = 1L;

    @JsonPropertyDescription("Spec: Rule unique identifier")
    @OutputOnly
    private Long id;

    @JsonPropertyDescription(
            "Spec: Rule name, " +
            "Rule: @Length(min = 1, max = 128)"
    )
    @NotNull
    @Length(min = 1, max = 128)
    private String name;

    @JsonPropertyDescription(
            "Spec: Rank for rule ordering, " +
            "Rule: @Min(0)"
    )
    @Min(0)
    private Integer rank;

    @JsonPropertyDescription(
            "Spec: Articles required to generate output articles"
    )
    @Valid
    private List<RuleArticle> inputArticles;

    @JsonPropertyDescription(
            "Spec: Articles generated from input articles"
    )
    @Valid
    private List<RuleArticle> outputArticles;
}

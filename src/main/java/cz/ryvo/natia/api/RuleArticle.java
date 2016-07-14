package cz.ryvo.natia.api;

import cz.ryvo.natia.validation.constraint.OutputOnly;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@NoArgsConstructor
public class RuleArticle implements Serializable {

    public static final Long serialVersionUID = 1L;

    @OutputOnly
    private Long id;

    @NotNull
    @Length(min = 1, max = 50)
    private String code;

    private String description;

    @NotNull
    @Min(1)
    private Integer pieces;

    @OutputOnly
    private Boolean inCatalogue;
}

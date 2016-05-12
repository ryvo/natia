package cz.ryvo.natia.api;

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

    @NotNull
    @Length(min = 1, max = 50)
    private String code;

    private String description;

    @NotNull
    @Min(1)
    private Integer amount;
}

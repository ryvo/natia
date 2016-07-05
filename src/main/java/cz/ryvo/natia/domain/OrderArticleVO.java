package cz.ryvo.natia.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderArticleVO implements Serializable {

    public static final Long serialVersionUID = 1L;

    private String code;

    private String description;

    private Integer amount;
}

package cz.ryvo.natia.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Data
@NoArgsConstructor
@MappedSuperclass
public abstract class RuleArticleVO implements Serializable {

    public static final Long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "rule_id", referencedColumnName = "id")
    private RuleVO rule;

    @Column(name = "code", length = 50, nullable = false)
    private String code;

    @Column(name = "description", length = 128, nullable = false)
    private String description;

    @Column(name = "amount", nullable = false)
    private Integer amount;

    @Column(name = "in_catalogue", nullable = false)
    private Boolean inCatalogue;
}

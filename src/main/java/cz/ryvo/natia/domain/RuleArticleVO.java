package cz.ryvo.natia.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;

import static javax.persistence.CascadeType.ALL;
import static javax.persistence.InheritanceType.SINGLE_TABLE;

@Data
@IdClass(RuleArticlePK.class)
@MappedSuperclass
public abstract class RuleArticleVO implements Serializable {

    public static final Long serialVersionUID = 1L;

    @Id
    @ManyToOne(optional = false)
    @JoinColumn(name = "rule_id", referencedColumnName = "id")
    private RuleVO rule;

    @Id
    private String code;

    @Column(name = "description", length = 128, nullable = false)
    private String description;

    @Column(name = "amount", nullable = false)
    private Integer amount;
}

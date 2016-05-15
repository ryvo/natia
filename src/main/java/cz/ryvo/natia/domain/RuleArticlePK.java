package cz.ryvo.natia.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;

@Data
@NoArgsConstructor
public class RuleArticlePK implements Serializable {

    public static final Long serialVersionUID = 1L;

    @ManyToOne(optional = false)
    @JoinColumn(name = "rule_id", referencedColumnName = "id")
    private RuleVO rule;

    @Column(name = "code", length = 50, nullable = false)
    private String code;
}

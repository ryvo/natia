package cz.ryvo.natia.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.IdClass;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "rule_output_article")
public class RuleOutputArticleVO extends RuleArticleVO {
}

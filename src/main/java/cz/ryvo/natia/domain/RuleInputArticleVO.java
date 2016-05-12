package cz.ryvo.natia.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.Table;

import static javax.persistence.InheritanceType.SINGLE_TABLE;

@Data
@NoArgsConstructor
@Entity
@Table(name = "rule_input_article")
public class RuleInputArticleVO extends RuleArticleVO {
}

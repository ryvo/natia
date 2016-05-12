package cz.ryvo.natia.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.List;

import static javax.persistence.CascadeType.ALL;

@Data
@NoArgsConstructor
@Entity
@Table(name = "rule")
public class RuleVO {

    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "name", length = 128, nullable = false, unique = true)
    private String name;

    @OneToMany(mappedBy = "rule", cascade = ALL, orphanRemoval = true)
    private List<RuleOutputArticleVO> outputArticles;

    @OneToMany(mappedBy = "rule", cascade = ALL, orphanRemoval = true)
    private List<RuleInputArticleVO> inputArticles;
}

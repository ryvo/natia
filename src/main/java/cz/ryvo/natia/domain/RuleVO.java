package cz.ryvo.natia.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OrderColumn;
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

    @Column(name = "rank", nullable = false)
    private Integer rank;

    @OneToMany(mappedBy = "rule", cascade = ALL, orphanRemoval = true)
    @OrderColumn(name = "rank")
    private List<RuleInputArticleVO> inputArticles;

    @OneToMany(mappedBy = "rule", cascade = ALL, orphanRemoval = true)
    @OrderColumn(name = "rank")
    private List<RuleOutputArticleVO> outputArticles;

    public RuleVO(Long id, String name, Integer rank) {
        this.id = id;
        this.name = name;
        this.rank = rank;
    }
}

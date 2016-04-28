package cz.ryvo.natia.domain;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.io.Serializable;

@Data
@NoArgsConstructor
@Entity
@Table(name = "article")
@IdClass(ArticlePK.class)
@ToString(exclude = "catalogue")
public class ArticleVO implements Serializable {

    public static final Long serialVersionUID = 1L;

    @Id
    private CatalogueVO catalogue;

    @Id
    private String code;

    @Column(name = "name", length = 128, nullable = false)
    private String name;
}

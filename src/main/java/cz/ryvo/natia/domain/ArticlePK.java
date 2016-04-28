package cz.ryvo.natia.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import java.io.Serializable;

@Data
@NoArgsConstructor
public class ArticlePK implements Serializable {

    public static final Long serialVersionUID = 1L;

    @ManyToOne(optional = false)
    @JoinColumns(value = {
            @JoinColumn(name = "catalogue_id", referencedColumnName = "id")
    },
            foreignKey = @ForeignKey(name = "fk__article__catalogue")
    )
    private CatalogueVO catalogue;

    @Column(name = "code", length = 50, nullable = false)
    private String code;
}

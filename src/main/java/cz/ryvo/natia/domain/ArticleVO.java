package cz.ryvo.natia.domain;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import java.io.Serializable;

import static javax.persistence.GenerationType.IDENTITY;

@Data
@NoArgsConstructor
@Entity
@Table(name = "article")
public class ArticleVO implements Serializable {

    public static final Long serialVersionUID = 1L;

    @Id
    @Column(name = "code", length = 50, nullable = false)
    private String code;

    @Column(name = "description", length = 128, nullable = false)
    private String description;
}

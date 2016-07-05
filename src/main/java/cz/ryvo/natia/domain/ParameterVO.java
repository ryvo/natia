package cz.ryvo.natia.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "parameter")
public class ParameterVO implements Serializable {

    public static final Long serialVersionUID = 1L;

    @Id
    @Enumerated(EnumType.STRING)
    @Column(name = "id", length = 128, nullable = false)
    private ParameterEnum id;

    @Column(name = "value", length = 128, nullable = false)
    private String value;

    public enum ParameterEnum {
        TIME_OF_CATALOGUE_IMPORT,
        FILENAME_OF_IMPORTED_CATALOGUE,
        NUMBER_OF_IMPORTED_CATALOGUE_ITEMS
    }
}

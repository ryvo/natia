package cz.ryvo.natia.api;

import cz.ryvo.natia.validation.constraint.OutputOnly;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CatalogueInfo implements Serializable {

    public static final Long serialVersionUID = 1L;

    @OutputOnly
    private LocalDateTime dateAndTimeOfImport;

    @OutputOnly
    private LocalDateTime currentDateAndTime;

    @OutputOnly
    private String fileName;

    @OutputOnly
    private Long numberOfImportedItems;
}

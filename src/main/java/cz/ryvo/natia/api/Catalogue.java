package cz.ryvo.natia.api;

import cz.ryvo.natia.validation.constraint.OutputOnly;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.List;

@Data
public class Catalogue {

    @OutputOnly
    private Long id;

    @NotNull
    @Size(min = 1, max = 128)
    private String name;

    private LocalDate validFrom;

    private LocalDate validTo;

    @OutputOnly
    private List<Article> articles;
}

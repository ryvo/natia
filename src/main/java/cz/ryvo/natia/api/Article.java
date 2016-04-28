package cz.ryvo.natia.api;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class Article {

    @NotNull
    @Size(min = 1, max = 20)
    private String code;

    @NotNull
    @Size(min = 1, max = 128)
    private String name;
}

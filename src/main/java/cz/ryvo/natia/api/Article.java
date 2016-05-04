package cz.ryvo.natia.api;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Data
public class Article implements Serializable {

    public static final Long serialVersionUID = 1L;

    @NotNull
    @Size(min = 1, max = 20)
    private String code;

    @NotNull
    @Size(min = 1, max = 128)
    private String name;
}

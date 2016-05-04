package cz.ryvo.natia.api;

import cz.ryvo.natia.validation.constraint.OutputOnly;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class CreateResult implements Serializable {

    public static final Long serialVersionUID = 1L;

    @OutputOnly
    private Long id;
}

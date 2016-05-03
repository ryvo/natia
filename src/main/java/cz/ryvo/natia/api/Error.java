package cz.ryvo.natia.api;

import cz.ryvo.natia.validation.constraint.OutputOnly;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import static java.util.Objects.requireNonNull;

@OutputOnly
@ApiModel(description = "Error, which is returned if something went wrong.")
@Data
@NoArgsConstructor
@EqualsAndHashCode(of = "code")
public class Error implements Serializable {

    public static final long serialVersionUID = 1L;

    @ApiModelProperty(notes = "Error code.", required = true)
    private String code;

    @ApiModelProperty(notes = "Full error description.", required = true)
    private String desc;

    @ApiModelProperty(notes = "Error descriptions with placeholders.")
    private String rawDesc;

    @ApiModelProperty(notes = "Map of placeholder arguments.")
    @Setter(onMethod = @__({@NonNull}))
    private Map<String, Object> args = new HashMap<>(0);

    public static ErrorBuilder builder() {
        return new ErrorBuilder();
    }

    @ToString
    public static class ErrorBuilder {

        private Error error = new Error();

        public Error.ErrorBuilder code(String code) {
            this.error.code = code;
            return this;
        }

        public Error.ErrorBuilder desc(String desc) {
            this.error.desc = desc;
            return this;
        }

        public Error.ErrorBuilder rawDesc(String rawDesc) {
            this.error.rawDesc = rawDesc;
            return this;
        }

        public Error.ErrorBuilder args(Map<String, Object> args) {
            this.error.args = args;
            return this;
        }

        public Error build() {
            requireNonNull(error.code, "code must be set");
            return this.error;
        }
    }
}

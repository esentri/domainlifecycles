package nitrox.dlc.validation.extend.jakarta;

import jakarta.validation.constraints.NotNull;
import nitrox.dlc.domain.types.ValueObject;

public record VOStaticMethod(@NotNull Long value) implements ValueObject {

    public static void calculate(Long att){

    }
}

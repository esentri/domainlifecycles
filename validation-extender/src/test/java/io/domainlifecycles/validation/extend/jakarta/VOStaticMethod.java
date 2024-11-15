package io.domainlifecycles.validation.extend.jakarta;

import jakarta.validation.constraints.NotNull;
import io.domainlifecycles.domain.types.ValueObject;

public record VOStaticMethod(@NotNull Long value) implements ValueObject {

    public static void calculate(Long att) {

    }
}

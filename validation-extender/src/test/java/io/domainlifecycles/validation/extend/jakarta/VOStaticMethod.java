package io.domainlifecycles.validation.extend.jakarta;

import io.domainlifecycles.domain.types.ValueObject;
import jakarta.validation.constraints.NotNull;

public record VOStaticMethod(@NotNull Long value) implements ValueObject {

    public static void calculate(Long att) {

    }
}

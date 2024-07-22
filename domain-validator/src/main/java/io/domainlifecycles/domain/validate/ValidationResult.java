package io.domainlifecycles.domain.validate;

import io.domainlifecycles.mirror.api.DomainTypeMirror;

public record ValidationResult(
    String msg,
    DomainTypeMirror validatedMirror,
    ValidationResultType resultType
) {
}

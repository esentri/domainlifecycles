package io.domainlifecycles.domain.validate;

import io.domainlifecycles.mirror.api.DomainType;
import io.domainlifecycles.mirror.api.DomainTypeMirror;

public interface DomainValidationRule<T extends DomainTypeMirror> {

    String ruleId();

    ValidationResult apply(T domainTypeMirror);

    DomainType applicableOn();

}

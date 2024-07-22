package io.domainlifecycles.domain.validate.rules;

import io.domainlifecycles.domain.validate.DomainValidationRule;

public enum DefaultRules {
    ID_TYPE(new IdValueType());

    private DomainValidationRule rule;

    DefaultRules(DomainValidationRule rule) {
        this.rule = rule;
    }
}

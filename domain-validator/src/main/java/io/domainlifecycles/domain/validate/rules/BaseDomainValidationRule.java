package io.domainlifecycles.domain.validate.rules;

import io.domainlifecycles.domain.validate.DomainValidationRule;
import io.domainlifecycles.domain.validate.ValidationResult;
import io.domainlifecycles.domain.validate.ValidationResultType;
import io.domainlifecycles.mirror.api.DomainType;
import io.domainlifecycles.mirror.api.DomainTypeMirror;

import java.util.Objects;

public abstract class BaseDomainValidationRule<T extends DomainTypeMirror> implements DomainValidationRule<T> {
    private final String notOKResultMessage;
    private final ValidationResultType notOKResultType;

    protected BaseDomainValidationRule(String notOKResultMessage, ValidationResultType notOKResultType) {
        this.notOKResultMessage = Objects.requireNonNull(notOKResultMessage);
        this.notOKResultType = Objects.requireNonNull(notOKResultType);
    }

    @Override
    public String ruleId() {
        return this.getClass().getSimpleName();
    }

    protected abstract boolean ruleFailedWhen(T domainTypeMirror);

    @Override
    public ValidationResult apply(T domainTypeMirror) {
        if(ruleFailedWhen(domainTypeMirror)){
            return notOKValidationResult(domainTypeMirror);
        }else{
            return okValidationResult(domainTypeMirror);
        }
    }

    protected ValidationResult notOKValidationResult(T domainTypeMirror){
        return new ValidationResult(notOKResultMessage, domainTypeMirror, notOKResultType);
    }

    protected ValidationResult okValidationResult(T domainTypeMirror){
        return new ValidationResult("This is fine!", domainTypeMirror, ValidationResultType.OK);
    }

}

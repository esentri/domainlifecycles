package io.domainlifecycles.domain.validate.rules;

import io.domainlifecycles.domain.validate.ValidationResultType;
import io.domainlifecycles.mirror.api.DomainType;

import io.domainlifecycles.mirror.api.IdentityMirror;

public class IdValueType extends BaseDomainValidationRule<IdentityMirror> {

    protected IdValueType() {
        super("Not adequate value type assigned for Identity!", ValidationResultType.DLC_FAIL);
    }

    @Override
    protected boolean ruleFailedWhen(IdentityMirror domainTypeMirror) {
        if(domainTypeMirror.getValueTypeName().isEmpty()){
            return true;
        }else{
            var valueType = domainTypeMirror.getValueTypeName().get();
            return Object.class.getName().equals(valueType);
        }
    }

    @Override
    public DomainType applicableOn() {
        return DomainType.IDENTITY;
    }
}

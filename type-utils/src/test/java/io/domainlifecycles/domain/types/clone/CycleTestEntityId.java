package io.domainlifecycles.domain.types.clone;

import io.domainlifecycles.domain.types.base.IdentityBase;

public class CycleTestEntityId extends IdentityBase<Long> {

    public CycleTestEntityId(Long value) {
        super(value);
    }
}

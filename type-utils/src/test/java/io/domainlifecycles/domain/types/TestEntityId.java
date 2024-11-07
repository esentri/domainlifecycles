package io.domainlifecycles.domain.types;

import io.domainlifecycles.domain.types.base.IdentityBase;

public class TestEntityId extends IdentityBase<Long> {
    /**
     * Constructs an typed id.
     *
     * @param value associated with identity.
     */
    public TestEntityId(Long value) {
        super(value);
    }
}

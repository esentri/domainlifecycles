package io.domainlifecycles.jdbc.nestedidentity;

import io.domainlifecycles.domain.types.Identity;
import io.domainlifecycles.domain.types.base.AggregateRootBase;

public class NestedIdentityRoot extends AggregateRootBase<NestedIdentityRoot.NestedIdentityRootId> {

    private final NestedIdentityRootId id;

    public NestedIdentityRoot(NestedIdentityRootId id, long concurrencyVersion) {
        super(concurrencyVersion);
        this.id = id;
    }

    public NestedIdentityRootId getId() {
        return id;
    }

    public record NestedIdentityRootId(Long value) implements Identity<Long> {
    }
}

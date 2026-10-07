package io.domainlifecycles.domain.types.clone;

import io.domainlifecycles.domain.types.base.EntityBase;
import lombok.Builder;
import lombok.Getter;

import java.util.Optional;

/**
 * Self-referencing entity fixture for {@link EntityCloner} regression tests, covering
 * {@code Optional}-typed cyclic entity references - following this project's established
 * "raw constructor/setter parameter, wrapped internally" convention for {@code Optional}-typed fields (see
 * e.g. {@code tests.shared.persistence.domain.optional.OptionalAggregate#setOptionalEntity}).
 */
@Getter
public class CycleTestEntity extends EntityBase<CycleTestEntityId> {

    private CycleTestEntityId id;
    private Optional<CycleTestEntity> self;

    @Builder(setterPrefix = "set")
    public CycleTestEntity(CycleTestEntityId id, long concurrencyVersion, CycleTestEntity self) {
        super(concurrencyVersion);
        this.id = id;
        setSelf(self);
    }

    public void setSelf(CycleTestEntity self) {
        this.self = Optional.ofNullable(self);
    }
}

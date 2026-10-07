package io.domainlifecycles.domain.types.clone;

import io.domainlifecycles.domain.types.base.EntityBase;
import lombok.Builder;

import java.util.List;

/**
 * Entity fixture for {@link EntityCloner} regression tests, covering the two mutable basic-typed field
 * shapes {@code EntityCloner} needs to deep-copy rather than reference-copy: a {@link List} and an array.
 */
public class CloneTestEntity extends EntityBase<CloneTestEntityId> {

    public CloneTestEntityId id;
    public List<String> tags;
    public byte[] payload;

    @Builder(setterPrefix = "set")
    public CloneTestEntity(CloneTestEntityId id, long concurrencyVersion, List<String> tags, byte[] payload) {
        super(concurrencyVersion);
        this.id = id;
        this.tags = tags;
        this.payload = payload;
    }
}

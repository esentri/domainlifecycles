package io.domainlifecycles.domain.types;


import io.domainlifecycles.assertion.DomainAssertionException;
import io.domainlifecycles.domain.types.base.EntityBase;
import lombok.Builder;

public class TestEntity extends EntityBase<TestEntityId> {

    public TestEntityId id;

    @Builder(setterPrefix = "set")
    public TestEntity(TestEntityId id, long concurrencyVersion) {
        super(concurrencyVersion);
        this.id = id;

    }

    @Override
    public void validate() throws DomainAssertionException {
        System.out.println("Validate Called!");
    }

}

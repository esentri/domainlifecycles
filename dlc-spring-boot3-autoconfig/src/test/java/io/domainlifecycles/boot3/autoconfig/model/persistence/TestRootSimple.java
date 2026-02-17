package io.domainlifecycles.boot3.autoconfig.model.persistence;

import io.domainlifecycles.assertion.DomainAssertions;
import io.domainlifecycles.domain.types.base.AggregateRootBase;
import lombok.Builder;
import lombok.Getter;

@Getter
public class TestRootSimple extends AggregateRootBase<TestRootSimpleId> {

    private TestRootSimpleId id;
    private String name;

    @Builder(setterPrefix = "set")
    public TestRootSimple(TestRootSimpleId id,
                          long concurrencyVersion,
                          String name

    ) {
        super(concurrencyVersion);
        this.id = id;
        DomainAssertions.isNotNull(id, "Eine Root ID muss angegeben sein!");
        setName(name);
    }

    public void setName(String name) {
        this.name = name;
    }

}

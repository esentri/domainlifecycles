package test.domain;

import io.domainlifecycles.domain.types.DomainService;

public abstract class MyBaseService implements DomainService {
    // base implementation - present in the mirror
    public void process(MyDomainCommand command) {
        // base domain logic
    }
}

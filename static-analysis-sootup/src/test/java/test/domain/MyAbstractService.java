package test.domain;

import io.domainlifecycles.domain.types.DomainService;

public abstract class MyAbstractService implements DomainService {
    // abstract domain method - no body
    public abstract void abstractOperation(MyDomainCommand command);
}

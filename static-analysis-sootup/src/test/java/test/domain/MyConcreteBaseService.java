package test.domain;

import io.domainlifecycles.domain.types.DomainService;

public class MyConcreteBaseService implements DomainService {

    // Case Q: a base type that is instantiable AND overridable. A call written against this
    // type can run here or in a subclass - unlike an abstract base, where only the subclass can
    // ever run.
    public void execute(MyDomainCommand command) {
        // base domain logic
    }
}

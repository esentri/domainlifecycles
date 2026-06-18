package test.domain;

import io.domainlifecycles.domain.types.DomainService;

public interface MyPlainInterface extends DomainService {
    // interface method without default - no body
    void declaredOnly(MyDomainCommand command);
}

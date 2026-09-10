package test.domain;

import io.domainlifecycles.domain.types.DomainService;

public interface MyPlainInterface extends DomainService {
    // interface method without default - no body
    void declaredOnly(MyDomainCommand command);

    // Case O: a default method DOES have a body, and that body is inherited by every
    // implementation - through the interface, not through a superclass. Its call is an
    // invokeinterface on `this`, so it dispatches to the implementation at runtime.
    default void defaultOperation(MyDomainCommand command) {
        declaredOnly(command);
    }
}

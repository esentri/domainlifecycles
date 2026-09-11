package test.domain;

import io.domainlifecycles.domain.types.DomainService;

public abstract class MyBaseService implements DomainService {
    // base implementation - present in the mirror
    public void process(MyDomainCommand command) {
        // base domain logic
    }

    // Case N: template method. The body lives here and is inherited unchanged by both
    // subclasses, but process(...) is an invokevirtual on `this` - so at runtime it dispatches
    // to the override of whichever subclass owns the instance. Analyzing the body once per
    // signature would attribute the call to MyBaseService.process for everyone.
    public void baseTemplate(MyDomainCommand command) {
        process(command);
        // the same dispatch once more, but from inside a lambda of the inherited body. The
        // lambda runs on the very same instance, so it has to resolve to the concrete owner
        // too - descending into it must not fall back to the declaring class.
        Runnable deferred = () -> process(command);
        deferred.run();
    }
}

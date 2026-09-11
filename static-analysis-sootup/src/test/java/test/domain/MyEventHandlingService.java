package test.domain;

import io.domainlifecycles.domain.types.DomainEventListener;
import io.domainlifecycles.domain.types.DomainService;
import io.domainlifecycles.domain.types.Publishes;

public class MyEventHandlingService implements DomainService {

    private final MyRepository repository;

    public MyEventHandlingService(MyRepository repository) {
        this.repository = repository;
    }

    // The second listener of MyDomainEvent. Publishing the event branches the flow: this method
    // and MyDomainService.onMyDomainEvent both run, independently of each other and of the
    // publisher. findById is called by both branches, doSomethingNoArg only by this one.
    @DomainEventListener
    public void onMyDomainEvent(MyDomainEvent event) {
        var agg = repository.findById(event.id());
        agg.ifPresent(a -> a.doSomethingNoArg());
        republish();
    }

    // Case P: handling the event publishes it again. The flow closes over the event join, not
    // over a method call - it has to be reported as a cycle and stop there, or the traversal
    // would never terminate.
    @Publishes(domainEventTypes = MyDomainEvent.class)
    public void republish() {
        // the publishing itself is not modelled as a call, the mirror carries it
    }
}

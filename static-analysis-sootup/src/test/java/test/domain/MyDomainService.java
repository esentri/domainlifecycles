package test.domain;

import io.domainlifecycles.domain.types.DomainEventListener;
import io.domainlifecycles.domain.types.DomainService;

public class MyDomainService implements DomainService {

    private final MyOutboundService outboundService;
    private final MyRepository repository;

    public MyDomainService(MyOutboundService outboundService, MyRepository repository) {
        this.outboundService = outboundService;
        this.repository = repository;
    }

    @DomainEventListener
    public void onMyDomainEvent(MyDomainEvent event) {
        var agg = repository.findById(event.id());
        outboundService.doSomething(agg.orElse(null));
    }

    public static boolean validate(MyAggregateRoot a) {
        return true;
    }
}

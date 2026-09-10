package test.domain;

import io.domainlifecycles.domain.types.OutboundService;

public interface MyOutboundService extends OutboundService {

    public void doSomething(MyAggregateRoot root);
}

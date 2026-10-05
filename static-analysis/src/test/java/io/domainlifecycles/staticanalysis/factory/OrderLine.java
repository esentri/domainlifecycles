package io.domainlifecycles.staticanalysis.factory;

import io.domainlifecycles.domain.types.Entity;

public class OrderLine implements Entity<OrderLineId> {

    private final OrderLineId id;

    public OrderLine(OrderLineId id) {
        this.id = id;
    }

    @Override
    public OrderLineId id() {
        return id;
    }

    @Override
    public long concurrencyVersion() {
        return 0;
    }
}

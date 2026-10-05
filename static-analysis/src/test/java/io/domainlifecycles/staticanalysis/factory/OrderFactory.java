package io.domainlifecycles.staticanalysis.factory;

import io.domainlifecycles.domain.types.Factory;

public class OrderFactory implements Factory {

    public Order create() {
        return new Order(new OrderId(1L));
    }

    // a factory method providing a read model: it leads to it as provider only
    public OrderSummary summarize(Order order) {
        return new OrderSummary(0);
    }
}

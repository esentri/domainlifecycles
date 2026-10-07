package tests.factory;

import io.domainlifecycles.domain.types.AggregateRoot;
import io.domainlifecycles.domain.types.FactoryMethod;

public class Order implements AggregateRoot<OrderId> {

    private final OrderId id;

    public Order(OrderId id) {
        this.id = id;
    }

    @FactoryMethod
    public Order copy(OrderId newId) {
        return new Order(newId);
    }

    @Override
    public OrderId id() {
        return id;
    }

    @Override
    public long concurrencyVersion() {
        return 0;
    }
}

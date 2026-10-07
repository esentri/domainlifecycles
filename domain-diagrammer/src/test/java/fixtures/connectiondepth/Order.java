package fixtures.connectiondepth;

import io.domainlifecycles.domain.types.Publishes;
import io.domainlifecycles.domain.types.base.AggregateRootBase;

public class Order extends AggregateRootBase<OrderId> {

    private final OrderId id;

    public Order(OrderId id, long concurrencyVersion) {
        super(concurrencyVersion);
        this.id = id;
    }

    @Publishes(domainEventTypes = OrderPlaced.class)
    public void confirm() {
    }
}

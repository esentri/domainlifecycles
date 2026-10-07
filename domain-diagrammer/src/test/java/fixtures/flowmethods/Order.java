package fixtures.flowmethods;

import io.domainlifecycles.domain.types.base.AggregateRootBase;

import java.util.List;

public class Order extends AggregateRootBase<OrderId> {

    private final OrderId id;

    private List<OrderLine> lines;

    public Order(OrderId id, long concurrencyVersion) {
        super(concurrencyVersion);
        this.id = id;
    }

    public void confirm() {
    }

    public void cancel() {
    }
}

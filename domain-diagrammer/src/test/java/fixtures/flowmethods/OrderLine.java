package fixtures.flowmethods;

import io.domainlifecycles.domain.types.base.EntityBase;

// part of the Order aggregate, reached by no flow
public class OrderLine extends EntityBase<OrderLineId> {

    private final OrderLineId id;

    public OrderLine(OrderLineId id, long concurrencyVersion) {
        super(concurrencyVersion);
        this.id = id;
    }

    public void increase() {
    }

    public void decrease() {
    }
}

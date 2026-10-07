package fixtures.inlinedvalueobjects;

import io.domainlifecycles.domain.types.base.AggregateRootBase;

import java.util.List;

public class Order extends AggregateRootBase<OrderId> {

    private final OrderId id;

    private Name name;

    private Money price;

    private List<Money> discounts;

    private Address address;

    private PriceRange priceRange;

    private Location location;

    public Order(OrderId id, long concurrencyVersion) {
        super(concurrencyVersion);
        this.id = id;
    }
}

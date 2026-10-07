package io.domainlifecycles.staticanalysis.factory;

import io.domainlifecycles.domain.types.AggregateRoot;
import io.domainlifecycles.domain.types.FactoryMethod;

import java.util.ArrayList;
import java.util.List;

public class Order implements AggregateRoot<OrderId> {

    private final OrderId id;

    private final List<OrderLine> lines = new ArrayList<>();

    public Order(OrderId id) {
        this.id = id;
    }

    // creates its own instances: leads nowhere
    @FactoryMethod
    public static Order open(OrderId id) {
        return new Order(id);
    }

    // creates an entity of its aggregate
    @FactoryMethod
    public OrderLine addLine() {
        var line = new OrderLine(new OrderLineId((long) lines.size()));
        lines.add(line);
        return line;
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

package tests.factory;

import io.domainlifecycles.domain.types.Factory;

public class OrderFactory implements Factory {

    public Order create(OrderId id) {
        return new Order(id);
    }
}

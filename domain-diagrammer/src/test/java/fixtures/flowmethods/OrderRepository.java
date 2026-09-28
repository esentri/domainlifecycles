package fixtures.flowmethods;

import io.domainlifecycles.domain.types.Repository;

public interface OrderRepository extends Repository<OrderId, Order> {

    Order findOrder(OrderId id);

    void store(Order order);

    void remove(OrderId id);
}

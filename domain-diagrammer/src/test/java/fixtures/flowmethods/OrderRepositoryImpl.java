package fixtures.flowmethods;

import java.util.Optional;

public class OrderRepositoryImpl implements OrderRepository {

    @Override
    public Order findOrder(OrderId id) {
        return null;
    }

    @Override
    public void store(Order order) {
    }

    @Override
    public void remove(OrderId id) {
    }

    @Override
    public Optional<Order> findById(OrderId id) {
        return Optional.empty();
    }

    @Override
    public Order insert(Order aggregateRoot) {
        return aggregateRoot;
    }

    @Override
    public Order update(Order aggregateRoot) {
        return aggregateRoot;
    }

    @Override
    public Optional<Order> deleteById(OrderId id) {
        return Optional.empty();
    }
}

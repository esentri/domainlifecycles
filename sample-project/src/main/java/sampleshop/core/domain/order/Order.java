/*
 *
 *     ___
 *     │   ╲                 _
 *     │    ╲ ___ _ __  __ _(_)_ _
 *     |     ╲ _ ╲ '  ╲╱ _` │ │ ' ╲
 *     |_____╱___╱_│_│_╲__,_│_│_||_|
 *     │ │  (_)╱ _│___ __ _  _ __│ |___ ___
 *     │ │__│ │  _╱ -_) _│ ││ ╱ _│ ╱ -_|_-<
 *     │____│_│_│ ╲___╲__│╲_, ╲__│_╲___╱__╱
 *                      |__╱
 *
 *  Copyright 2019-2024 the original author or authors.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       https://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package sampleshop.core.domain.order;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Getter;
import io.domainlifecycles.assertion.DomainAssertions;
import io.domainlifecycles.domain.types.Identity;
import io.domainlifecycles.domain.types.Publishes;
import io.domainlifecycles.domain.types.base.AggregateRootBase;
import io.domainlifecycles.events.api.DomainEvents;
import sampleshop.core.domain.Price;
import sampleshop.core.domain.customer.Customer;
import sampleshop.core.domain.product.Product;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.stream.Stream;

/**
 * Order aggregate root.
 *
 * @author Tobias Herb
 * @author Mario Herb
 */
@Getter
public final class Order extends AggregateRootBase<Order.OrderId> {

    /**
     * The "typed" OrderId as inner class (record) of the {@code Order} class.
     */
    public record OrderId(@NotNull Long value) implements Identity<Long> {
    }

    private final OrderId id;

    @NotNull
    private final Customer.CustomerId customerId;

    private final Instant creation;

    @NotNull
    private final List<OrderItem> items;

    @NotNull
    private OrderStatus status;

    @Builder(setterPrefix = "set")
    private Order(final long concurrencyVersion,
                  final Order.OrderId id,
                  final Customer.CustomerId customerId,
                  final OrderStatus status,
                  final Instant creation,
                  final List<OrderItem> items) {
        super(concurrencyVersion);
        this.id = id;
        this.customerId = customerId;
        this.creation = creation;
        this.status = status;
        this.items = items;
    }

    public Price totalPrice() {
        return items()
            .map(OrderItem::itemPrice)
            .reduce(new Price(BigDecimal.ZERO), Price::add);
    }

    /// ORDER ITEMS

    /**
     * Returns a {@link Stream} of contained {@link OrderItem}s.
     * <p>
     * NOTE: We think, it is better to avoid returning the direct reference to
     * the list of order items, because this list can be manipulated
     * arbitrarily (by mistake) without conforming to the constraints
     * and rules of the AggregateRoot.
     */
    public Stream<OrderItem> items() {
        return items.stream();
    }

    /**
     * Either creates a new order item for the given product or increases
     * the quantity if already present as order item.
     *
     * @param id of the product to be added
     * @return this Order.
     */
    public Order add(final Product.ProductId id) {
        return addItem(id, 1);
    }

    /**
     * Either creates a new order item for the given product or increases
     * the quantity if already present as order item.
     *
     * @param id       of the product to be added
     * @param quantity of the product to be ordered
     * @return this Order.
     */
    public Order addItem(@NotNull final Product.ProductId id, @Positive final int quantity) {
        items().filter(item -> item.getProductId().equals(id))
            .findFirst().ifPresentOrElse(
                item -> item.addQuantity(quantity),
                () -> items.add(OrderItem.builder().setProductId(id).setQuantity(quantity).build())
            );
        return this;
    }

    /// LIFECYCLE

    /**
     * Determines whether this Order has the given {@link OrderStatus}.
     */
    public boolean hasStatus(final OrderStatus status) {
        return this.status == status;
    }


    /**
     * Transitions processing status of this Order to {@link OrderStatus#CANCELED}.
     */
    @Publishes(domainEventTypes = {OrderCanceled.class})
    public Order cancel() {
        transition(OrderStatus.PENDING, OrderStatus.CANCELED,
            "Can't cancel an order that is not pending.");
        DomainEvents.publish(new OrderCanceled(this));
        return this;
    }

    /**
     * Transitions processing status of this Order to {@link OrderStatus#SHIPPED}.
     */
    @Publishes(domainEventTypes = {OrderShipped.class})
    public Order ship() {
        transition(OrderStatus.PENDING, OrderStatus.SHIPPED,
            "Can't mark as shipped an order that is not pending.");
        DomainEvents.publish(new OrderShipped(this));
        return this;
    }

    private Order transition(final OrderStatus required, final OrderStatus next, final String errorMessage) {
        DomainAssertions.notEquals(this.status, required, errorMessage);
        this.status = next;
        return this;
    }

}

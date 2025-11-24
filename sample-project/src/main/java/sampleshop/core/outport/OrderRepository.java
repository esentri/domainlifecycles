/*
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

package sampleshop.core.outport;

import io.domainlifecycles.domain.types.Repository;
import java.util.List;
import sampleshop.core.domain.order.Order;
import sampleshop.core.domain.order.OrderItem;
import sampleshop.core.domain.order.OrderStatus;

import java.util.Optional;
import java.util.stream.Stream;

/**
 * Defines {@link Order} persistence operations.
 *
 * @author Tobias Herb
 * @author Mario Herb
 */
public interface OrderRepository extends Repository<Order.OrderId, Order> {

    /**
     * Produces a fresh {@link Order.OrderId}.
     */
    Order.OrderId newOrderId();

    /**
     * Produces a fresh {@link Order.OrderId}.
     */
    OrderItem.OrderItemId newOrderItemId();

    /**
     * @param offset
     * @param limit
     * @param orderStatus
     * @return @return stream of {@code Order}s
     */
    List<Order> find(int offset, int limit, OrderStatus... orderStatus);

    /**
     * Stores an new {@code Order} into this repository.
     *
     * @param entity to to be stored
     * @return updated {@code entity} instance
     */
    @Override
    Order insert(Order entity);

    /**
     * Updates an existent {@code Order} in this repository.
     *
     * @param entity to to be updated
     * @return updated {@code entity} instance
     */
    @Override
    Order update(Order entity);

    /**
     * Finds the order with the specified orderId.
     *
     * @param orderId the ID of the order to find
     * @return an Optional containing the order if found, or empty if not found
     */
    @Override
    Optional<Order> findById(Order.OrderId orderId);

    /**
     * Deletes an order from the repository based on the specified order ID.
     *
     * @param orderId the ID of the order to delete
     * @return an Optional containing the deleted order if found and deleted, or an empty Optional if not found
     */
    @Override
    Optional<Order> deleteById(Order.OrderId orderId);
}

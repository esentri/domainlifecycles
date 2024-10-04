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

import io.domainlifecycles.domain.types.DomainService;
import io.domainlifecycles.domain.types.Publishes;
import io.domainlifecycles.events.api.DomainEvents;
import sampleshop.core.domain.product.Product;
import sampleshop.core.outport.OrderRepository;
import sampleshop.core.outport.ProductRepository;

import java.time.Instant;
import java.util.stream.Collectors;

/**
 * {@inheritDoc}
 *
 * @author Mario Herb
 */
public final class OrderPlacementService implements DomainService {

    private final OrderRepository orderRepository;

    private final ProductRepository productRepository;

    public OrderPlacementService(final OrderRepository orderRepository,
                                 final ProductRepository productRepository) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
    }

    /**
     * {@inheritDoc}
     */
    @Publishes(domainEventTypes = {NewOrderPlaced.class})
    public Order placeOrder(final PlaceOrder placeOrder) {
        var placed = orderRepository.insert(
            Order.builder()
                .setId(orderRepository.newOrderId())
                .setCustomerId(placeOrder.customerId())
                .setStatus(OrderStatus.PENDING)
                .setCreation(Instant.now())
                .setItems(
                    placeOrder.items()
                        .stream()
                        .map(item ->
                            OrderItem
                                .builder()
                                .setId(orderRepository.newOrderItemId())
                                .setProductId(item.productId())
                                .setQuantity(item.quantity())
                                .setProductPrice(
                                    productRepository
                                        .findById(item.productId())
                                        .map(Product::getPrice)
                                        .orElseThrow(() -> new IllegalArgumentException(
                                            String.format("ProductId '%s' is not present in database!",
                                                item.productId()))
                                        )
                                )
                                .build()
                        ).collect(Collectors.toList()))
                .build()
        );
        DomainEvents.publish(new NewOrderPlaced(placed));
        return placed;
    }

}

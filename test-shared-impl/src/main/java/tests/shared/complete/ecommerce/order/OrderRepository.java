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

package tests.shared.complete.ecommerce.order;

import io.domainlifecycles.domain.types.Publishes;
import io.domainlifecycles.domain.types.Repository;
import jakarta.validation.constraints.NotNull;

import java.util.Optional;
import java.util.stream.Stream;


public class OrderRepository implements Repository<OrderIdBv3, OrderBv3> {


    @Override
    public Optional<OrderBv3> findById(@NotNull OrderIdBv3 id) {
        return Optional.empty();
    }

    @Override
    public OrderBv3 insert(OrderBv3 aggregateRoot) {
        return null;
    }

    @Override
    public OrderBv3 update(OrderBv3 aggregateRoot) {
        return null;
    }

    @Override
    public Optional<OrderBv3> deleteById(OrderIdBv3 orderIdBv3) {
        return Optional.empty();
    }

    @Publishes(domainEventTypes = NewOrder.class)
    public OrderIdBv3 create(@NotNull OrderBv3 order) {
        return order.getId();
    }

    public Stream<OrderBv3> findByStatus(@NotNull OrderStatusCodeEnumBv3 status) {
        return Stream.empty();
    }

}

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

package sampleshop.outbound;

import java.util.List;
import sampleshop.outbound.event.SpringPersistenceEventPublisher;
import io.domainlifecycles.jooq.imp.JooqAggregateRepository;
import io.domainlifecycles.jooq.imp.provider.JooqDomainPersistenceProvider;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;
import sampleshop.Sequences;
import sampleshop.core.domain.order.Order;
import sampleshop.core.domain.order.OrderItem;
import sampleshop.core.domain.order.OrderStatus;
import sampleshop.core.outport.OrderRepository;

import java.util.Arrays;
import java.util.stream.Stream;

import static org.jooq.impl.DSL.trueCondition;
import static sampleshop.Tables.ORDER;

/**
 * PersistenceActionPublishingRepository implementation for the {@link Order}.
 *
 * @author Mario Herb
 * @author Tobias Herb
 */
@Repository
class JooqOrderRepository extends JooqAggregateRepository<Order, Order.OrderId> implements OrderRepository {

    public JooqOrderRepository(DSLContext dslContext,
                               JooqDomainPersistenceProvider domainPersistenceProvider,
                               SpringPersistenceEventPublisher persistenceEventPublisher) {
        super(Order.class, dslContext, domainPersistenceProvider, persistenceEventPublisher);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Order.OrderId newOrderId() {
        return new Order.OrderId(dslContext.nextval(Sequences.ORDER_ID_SEQ));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public OrderItem.OrderItemId newOrderItemId() {
        return new OrderItem.OrderItemId(dslContext.nextval(Sequences.ORDER_ITEM_ID_SEQ));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Order> find(int offset, int limit, OrderStatus... orderStatuses) {
        Condition condition = trueCondition();
        if (orderStatuses != null && orderStatuses.length > 0) {
            condition = ORDER.STATUS.in(Arrays.stream(orderStatuses).map(s -> s.name()).toList());
        }
        return dslContext
            .selectFrom(ORDER)
            .where(condition)
            .orderBy(ORDER.ID)
            .offset(offset)
            .limit(limit)
            .fetch()
            .stream()
            .map(or -> getFetcher().fetchDeep(or).resultValue().orElseThrow())
            .toList();
    }
}

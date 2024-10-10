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

package sampleshop.outbound;

import org.jooq.DSLContext;

import org.springframework.stereotype.Service;
import sampleshop.core.domain.order.OrderStatus;
import sampleshop.core.outport.OrdersByCustomer;
import sampleshop.core.outport.OrdersByCustomerQueryClient;

import java.util.List;

import static org.jooq.impl.DSL.field;
import static sampleshop.Tables.CUSTOMER;
import static sampleshop.Tables.ORDER;

/**
 * QueryClient implementation to demonstrate 'lightweight' ReadModels {@link OrdersByCustomer}.
 *
 * @author Mario Herb
 */
@Service
public class JooqOrdersByCustomerQueryClient implements OrdersByCustomerQueryClient {

    private final DSLContext dslContext;

    public JooqOrdersByCustomerQueryClient(DSLContext dslContext) {
        this.dslContext = dslContext;
    }

    /**
     * Query returning the {@link OrdersByCustomer} read model.
     */
    @Override
    public List<OrdersByCustomer> listAll(String customerNameFilter, int offset, int limit) {
        return dslContext.select(
                CUSTOMER.USER_NAME,
                field(dslContext.selectCount()
                    .from(ORDER).where(
                        ORDER.CUSTOMER_ID.eq(CUSTOMER.ID).and(ORDER.STATUS.eq(OrderStatus.PENDING.name()))
                    )
                ).as("pending_orders"),
                field(dslContext.selectCount()
                    .from(ORDER).where(
                        ORDER.CUSTOMER_ID.eq(CUSTOMER.ID).and(ORDER.STATUS.eq(OrderStatus.SHIPPED.name()))
                    )
                ).as("shipped_orders"),
                field(dslContext.selectCount()
                    .from(ORDER).where(
                        ORDER.CUSTOMER_ID.eq(CUSTOMER.ID).and(ORDER.STATUS.eq(OrderStatus.CANCELED.name()))
                    )
                ).as("cancelled_orders")
            )
            .from(CUSTOMER)
            .where(CUSTOMER.USER_NAME.like("%" + customerNameFilter + "%"))
            .orderBy(CUSTOMER.USER_NAME.desc())
            .offset(offset)
            .limit(limit)
            .fetch()
            .stream()
            .map(r -> new OrdersByCustomer(r.value1(), r.value2(), r.value3(), r.value4()))
            .toList();
    }
}

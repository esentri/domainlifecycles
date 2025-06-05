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

import io.domainlifecycles.autoconfig.configurations.event.SpringPersistenceEventPublisher;
import io.domainlifecycles.jooq.imp.JooqAggregateRepository;
import io.domainlifecycles.jooq.imp.provider.JooqDomainPersistenceProvider;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;
import sampleshop.Sequences;
import sampleshop.core.domain.customer.Customer;
import sampleshop.core.outport.CustomerRepository;

import java.util.stream.Stream;

import static sampleshop.Tables.CUSTOMER;

/**
 * PersistenceActionPublishingRepository implementation for the {@link Customer}.
 *
 * @author Mario Herb
 * @author Tobias Herb
 */
@Repository
class JooqCustomerRepository extends JooqAggregateRepository<Customer, Customer.CustomerId> implements CustomerRepository {

    public JooqCustomerRepository(DSLContext dslContext,
                                  JooqDomainPersistenceProvider domainPersistenceProvider,
                                  SpringPersistenceEventPublisher persistenceEventPublisher) {
        super(Customer.class, dslContext, domainPersistenceProvider, persistenceEventPublisher);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Customer.CustomerId newCustomerId() {
        return new Customer.CustomerId(dslContext.nextval(Sequences.CUSTOMER_ID_SEQ));
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public Stream<Customer> find(int offset, int limit) {
        return dslContext
            .selectFrom(CUSTOMER)
            .orderBy(CUSTOMER.ID.desc())
            .offset(offset)
            .limit(limit)
            .fetch()
            .stream()
            .map(r -> getFetcher().fetchDeep(r).resultValue().orElseThrow());
    }
}

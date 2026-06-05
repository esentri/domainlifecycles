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

package sampleshop.inbound.driver;

import io.domainlifecycles.domain.types.Publishes;
import io.domainlifecycles.events.api.DomainEvents;
import org.springframework.transaction.annotation.Transactional;
import sampleshop.core.domain.customer.AddNewCustomer;
import sampleshop.core.domain.customer.ChangeCreditCard;
import sampleshop.core.domain.customer.ChangeCustomerAddress;
import sampleshop.core.domain.customer.Customer;
import sampleshop.core.domain.customer.NewCustomerAdded;
import sampleshop.core.inport.CustomerDriver;
import sampleshop.core.outport.CustomerRepository;
import sampleshop.core.outport.OrdersByCustomer;
import sampleshop.core.outport.OrdersByCustomerQueryHandler;

import java.util.List;
import java.util.Optional;

/**
 * The CustomerService implements the CustomerDriver.
 *
 * @author Mario Herb
 * @author Tobias Herb
 */
@Transactional
public class CustomerService implements CustomerDriver {

    private final CustomerRepository repository;

    private final OrdersByCustomerQueryHandler ordersByCustomerQueries;


    public CustomerService(CustomerRepository repository, OrdersByCustomerQueryHandler ordersByCustomerQueries) {
        this.repository = repository;
        this.ordersByCustomerQueries = ordersByCustomerQueries;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Publishes(domainEventTypes = {NewCustomerAdded.class})
    public Customer add(AddNewCustomer addNewCustomer) {
        var addedCustomer = repository.insert(Customer
            .builder()
            .id(repository.newCustomerId())
            .userName(addNewCustomer.userName())
            .address(addNewCustomer.address())
            .creditCard(addNewCustomer.creditCard().orElse(null))
            .blocked(false)
            .build()
        );
        DomainEvents.publish(new NewCustomerAdded(addedCustomer));
        return addedCustomer;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<Customer> changeAddress(ChangeCustomerAddress changeCustomerAddress) {
        return repository
            .findById(changeCustomerAddress.customerId())
            .map(customer -> repository.update(customer.setAddress(changeCustomerAddress.address()))
            );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<Customer> changeCreditCard(ChangeCreditCard changeCreditCard) {
        var changedCustomer = repository
            .findById(changeCreditCard.customerId())
            .map(customer -> repository.update(customer.setCreditCard(changeCreditCard.creditCard()))
            );
        return changedCustomer;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Customer> find(int offset, int limit) {
        return repository.find(offset, limit).toList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<OrdersByCustomer> reportOrders(String customerFilter, int offset, int limit) {
        return ordersByCustomerQueries.listAll(customerFilter, offset, limit);
    }

}

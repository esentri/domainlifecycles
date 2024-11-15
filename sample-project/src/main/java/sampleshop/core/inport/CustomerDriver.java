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

package sampleshop.core.inport;

import io.domainlifecycles.domain.types.Driver;
import sampleshop.core.domain.customer.AddNewCustomer;
import sampleshop.core.domain.customer.ChangeCreditCard;
import sampleshop.core.domain.customer.ChangeCustomerAddress;
import sampleshop.core.domain.customer.Customer;
import sampleshop.core.outport.OrdersByCustomer;

import java.util.List;
import java.util.Optional;

/**
 * The OrderDriver contains all operations driving the application
 * in association with {@link Customer}s.
 *
 * @author Mario Herb
 */
public interface CustomerDriver extends Driver {

    /**
     * Add a new Customer
     */
    Customer add(AddNewCustomer addNewCustomer);

    /**
     * Change address of existing Customer
     */
    Optional<Customer> changeAddress(ChangeCustomerAddress changeCustomerAddress);

    /**
     * Change credit card information of existing Customer
     */
    Optional<Customer> changeCreditCard(ChangeCreditCard changeCreditCard);

    /**
     * Find list of existing Customers
     */
    List<Customer> find(int offset, int limit);

    /**
     * Find list of existing Customers with filter option
     */
    List<OrdersByCustomer> reportOrders(String customerFilter, int offset, int limit);
}

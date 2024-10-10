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

package sampleshop.core.outport;

import io.domainlifecycles.domain.types.Repository;
import sampleshop.core.domain.customer.Customer;

import java.util.Optional;
import java.util.stream.Stream;

/**
 * Defines {@link Customer} persistence operations.
 *
 * @author Tobias Herb
 * @author Mario Herb
 */
public interface CustomerRepository extends Repository<Customer.CustomerId, Customer> {

    /**
     * Produces a new {@link Customer.CustomerId}.
     */
    Customer.CustomerId newCustomerId();

    /**
     * Stores an new {@code Customer} into this repository.
     *
     * @param entity customer to to be stored
     * @return updated {@code entity} instance
     */
    @Override
    Customer insert(Customer entity);

    /**
     * Updates an existent {@code Customer} in this repository.
     *
     * @param entity customer to to be updated
     * @return updated {@link Customer} instance
     */
    @Override
    Customer update(Customer entity);

    /**
     * Finds {@code Customer}s paginated in this repository.
     *
     * @param offset pagination offset
     * @param limit  pagination limit
     * @return Stream of {@link Customer} instances
     */
    Stream<Customer> find(int offset, int limit);


    /**
     * Retrieves a customer by its ID.
     *
     * @param customerId the ID of the customer to retrieve
     * @return an Optional containing the customer, or an empty Optional if the customer does not exist
     */
    @Override
    Optional<Customer> findById(Customer.CustomerId customerId);

    /**
     * Deletes a customer by their ID.
     *
     * @param customerId the ID of the customer to delete
     * @return an Optional containing the deleted customer, or an empty Optional if the customer does not exist
     */
    @Override
    Optional<Customer> deleteById(Customer.CustomerId customerId);
}

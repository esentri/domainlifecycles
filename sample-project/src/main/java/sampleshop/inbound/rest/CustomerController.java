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

package sampleshop.inbound.rest;

import io.domainlifecycles.spring.http.ResponseEntityBuilder;
import io.domainlifecycles.spring.http.ResponseObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import sampleshop.core.domain.customer.AddNewCustomer;
import sampleshop.core.domain.customer.Address;
import sampleshop.core.domain.customer.ChangeCreditCard;
import sampleshop.core.domain.customer.ChangeCustomerAddress;
import sampleshop.core.domain.customer.CreditCard;
import sampleshop.core.domain.customer.Customer;
import sampleshop.core.outport.OrdersByCustomer;
import sampleshop.inbound.driver.CustomerService;

import java.util.List;

/**
 * Implementation of the REST {@link CustomerAPI} web controller
 *
 * @author Mario Herb
 * @author Tobias Herb.
 */
@RestController()
public class CustomerController implements CustomerAPI {

    private final CustomerService customerDriver;
    private final ResponseEntityBuilder responseEntityBuilder;

    CustomerController(CustomerService customerDriver, ResponseEntityBuilder responseEntityBuilder) {
        this.customerDriver = customerDriver;
        this.responseEntityBuilder = responseEntityBuilder;
    }

    /**
     * Returns a List of {@link Customer}s. Enables paging.
     */
    @Override
    public ResponseEntity<ResponseObject<List<Customer>>> find(
        @RequestParam(value = "offset", defaultValue = "0") int offset,
        @RequestParam(value = "limit", defaultValue = "10") int limit) {
        return responseEntityBuilder.build(customerDriver.find(offset, limit), HttpStatus.OK);
    }

    /**
     * Add a new Customer
     */
    @Override
    public ResponseEntity<ResponseObject<Customer>> addNewCustomer(
        @RequestBody AddNewCustomer addNewCustomer) {
        return responseEntityBuilder.build(customerDriver.add(addNewCustomer), HttpStatus.CREATED);
    }

    /**
     * Change the address of an existing customer
     */
    @Override
    public ResponseEntity<ResponseObject<Customer>> changeAddress(
        @PathVariable("id") Customer.CustomerId id,
        @RequestBody Address address
    ) {
        return responseEntityBuilder.build(
            customerDriver.changeAddress(new ChangeCustomerAddress(id, address)).orElse(null), HttpStatus.OK);
    }

    /**
     * Change the credit card information of an existing customer
     */
    @Override
    public ResponseEntity<ResponseObject<Customer>> changeCreditCard(
        @PathVariable("id") Customer.CustomerId id,
        @RequestBody CreditCard creditCard
    ) {
        return responseEntityBuilder.build(
            customerDriver.changeCreditCard(new ChangeCreditCard(id, creditCard)).orElse(null), HttpStatus.OK);
    }


    /**
     * Returns a List of {@link OrdersByCustomer} read models. Enables paging.
     */
    @Override
    public ResponseEntity<ResponseObject<List<OrdersByCustomer>>> reportOrders(
        @RequestParam(value = "customerNameFilter") String customerNameFilter,
        @RequestParam(value = "offset", defaultValue = "0") int offset,
        @RequestParam(value = "limit", defaultValue = "10") int limit) {
        return responseEntityBuilder.build(customerDriver.reportOrders(customerNameFilter, offset, limit),
            HttpStatus.OK);
    }
}

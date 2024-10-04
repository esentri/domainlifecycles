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

package sampleshop;

import io.domainlifecycles.domain.types.AggregateDomainEvent;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import io.domainlifecycles.events.api.DomainEvents;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import sampleshop.core.domain.customer.Customer;
import sampleshop.core.domain.customer.FraudDetected;
import sampleshop.core.domain.order.Order;
import sampleshop.core.domain.order.OrderItem;
import sampleshop.core.domain.order.OrderStatus;
import sampleshop.core.domain.product.Product;
import sampleshop.core.outport.CustomerRepository;
import sampleshop.core.outport.OrderRepository;
import sampleshop.core.domain.customer.CustomerNotificationService;


import java.time.Instant;
import java.util.ArrayList;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest(properties = {"spring.datasource.url=jdbc:h2:./build/h2-db/test;AUTO_SERVER=TRUE"})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@AutoConfigureMockMvc
@Slf4j
public class DefaultTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Test
    public void applicationStarted() {
    }

    /**
     * This test demonstrates the usage of an {@link AggregateDomainEvent} in a transactional setup.
     * The FraudDetected event is published within this application. DLC recognizes the event, start a new
     * transaction (if needed),
     * loads the target aggregate and calls the event handler directly on the aggregate and finally persists the
     * updates on the aggregate
     * and commits the transaction.
     * <br/>
     * This way the domain event is automatically part of the aggregate interface. A lot of boilerplate code for the
     * "purely technical" event handling is reduced.
     * <br/>
     * Have a look at the log out put, when running this test, to inspect the details!
     */
    @Test
    public void fraudDetected() {
        var fraud = new FraudDetected(new Customer.CustomerId(1L));
        DomainEvents.publish(fraud);

        var customer = customerRepository.findById(new Customer.CustomerId(1L));
        assertThat(customer).isPresent();
        assertThat(customer.get().isBlocked()).isTrue();
    }

    /**
     * This test demonstrates several features of DLC:
     * <br/>
     * The JSON mapping extension for Jackson automatically translates the numerical id values of the request
     * into typed ids expected on the Java side {@link Customer.CustomerId} or
     * {@link sampleshop.core.domain.product.Product.ProductId}.
     * <br/>
     * The DLC persistence extension maps the new {@link Order} into records and applies several
     * INSERT statements to insert them into the database, without having a programmer needing to declare or specify
     * anything for the
     * persistence behaviour.
     * <br/>
     * It also demonstrates the transactional behaviour of the DLC domain event handling. The event handler defined in
     * {@link CustomerNotificationService} is running after the transaction of the incoming new order request committed.
     * If the event publishing transaction failed, the event handler would not be notified.
     * Have a look at the log output to inspect that behaviour. You should find the event handler logging something
     * like 'Notification sent to "john_doe@whitehouse.com" with...'.
     * <br/>
     * Afterwards for the selection of the all orders that belong to the customer with the id 1, the DLC persistence
     * extension
     * (via fetcher) issues additional select statements to select the records for {@link OrderItem} instances, that
     * belong to the selected orders.
     * All selected records were mapped into complete and fully loaded {@link Order} aggregates without any heavy
     * boilerplate mapping code.
     * <br/>
     * We delete the newly created order afterwards, in order to get a 'replayable' test database. The default DLC
     * repository implementation provides
     * a 'deleteById' method that issues SQL DELETE statements for each domain object contained in the aggregate.
     */
    @Test
    public void newOrder() throws Exception {
        final var body = "{" +
            "\"customerId\":1," +
            "\"items\":[ " +
            "{\"productId\":2, " +
            "\"quantity\":3}" +
            "]}";

        mockMvc.perform(
                MockMvcRequestBuilders
                    .post("/api/orders/command/place")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(body)
            )
            .andExpect(status().isCreated())
            .andReturn();

        var orders = orderRepository.find(0, 10, OrderStatus.PENDING).collect(Collectors.toList());
        assertThat(orders).hasSize(1);

        var order = orderRepository.deleteById(orders.get(0).getId());
        assertThat(order).isPresent();
    }

    /**
     * This test demonstrates the DLC validation extension. The extension validates fields of domain objects or
     * return values or parameters of their methods
     * with bean validation annotations directly upon object creation or when the values are set. That helps keeping
     * domain objects "always-valid" as well simplifying
     * the declaration invariants/business rules on domain objects.
     */
    @Test
    @Transactional
    public void validation() {
        var order = orderRepository.findById(new Order.OrderId(1L)).orElseThrow();
        //the quantity of an order item must be > 0
        var t = catchThrowable(() ->
            order.addItem(
                new Product.ProductId(1L),
                0));
        assertThat(t).hasMessageContaining("addItem.quantity");
        log.info("Exception message: " + t.getMessage());


        //order without a status is invalid
        var t2 = catchThrowable(() ->
            Order.builder()
                .setCustomerId(new Customer.CustomerId(1L))
                .setId(new Order.OrderId(2L))
                .setItems(new ArrayList<>())
                .setCreation(Instant.now())
                .build()
        );

        assertThat(t2).hasMessageContaining("status");
        log.info("Exception message: " + t2.getMessage());
    }

    @Test
    public void report() throws Exception {
        final MvcResult response = mockMvc.perform(
                MockMvcRequestBuilders
                    .get("/api/customers/orders-overview")
                    .contentType(MediaType.APPLICATION_JSON)
                    .queryParam("customerNameFilter", "doe")
            )
            .andExpect(status().isOk())
            .andReturn();
        var content = response.getResponse().getContentAsString();
        log.info("Response=" + content);
        assertThat(content).contains("doe");
    }
}

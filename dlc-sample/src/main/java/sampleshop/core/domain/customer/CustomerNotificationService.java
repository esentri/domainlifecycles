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

package sampleshop.core.domain.customer;

import nitrox.dlc.domain.types.DomainService;
import nitrox.dlc.domain.types.ListensTo;
import org.springframework.stereotype.Service;
import sampleshop.core.domain.order.NewOrderPlaced;
import sampleshop.core.domain.order.OrderCanceled;
import sampleshop.core.domain.order.OrderShipped;
import sampleshop.core.outport.CustomerRepository;
import sampleshop.core.outport.EmailNotifierService;

/**
 * The CustomerNotificationService class is responsible for notifying customers about various events related to their orders.
 * It listens to specific domain events and uses the EmailNotifierService to send email notifications to the customers.
 *
 * @author Mario Herb
 */
@Service
public class CustomerNotificationService implements DomainService {

    private final EmailNotifierService emailNotifierService;

    private final CustomerRepository customerRepository;

    public CustomerNotificationService(EmailNotifierService emailNotifierService, CustomerRepository customerRepository) {
        this.emailNotifierService = emailNotifierService;
        this.customerRepository = customerRepository;
    }

    /**
     * Notifies Customers about a shipped order.
     *
     * @param orderShipped the DomainEvent representing the fact that an order was shipped
     */
    @ListensTo(domainEventType = OrderShipped.class)
    public void notifyOrderShipped(OrderShipped orderShipped){
        var customer = customerRepository.findById(orderShipped.order().getCustomerId())
            .orElseThrow();
        emailNotifierService.notify(customer.getUserName(), String.format("Your Order with the ID %s was shipped!", orderShipped.order().getId().value()));
    }

    /**
     * Notifies Customers about the cancellation of an order.
     *
     * @param orderCanceled the DomainEvent representing the fact that an order was canceled
     */
    @ListensTo(domainEventType = OrderCanceled.class)
    public void notifyOrderCanceled(OrderCanceled orderCanceled){
        var customer = customerRepository.findById(orderCanceled.order().getCustomerId())
            .orElseThrow();
        emailNotifierService.notify(customer.getUserName(), String.format("Your Order with the ID %s was canceled!", orderCanceled.order().getId().value()));
    }

    /**
     * Notifies Customers about a new order placed event.
     *
     * @param newOrderPlaced the DomainEvent representing the fact that a new order was successfully placed
     */
    @ListensTo(domainEventType = NewOrderPlaced.class)
    public void notifyNewOrderPlaced(NewOrderPlaced newOrderPlaced){
        var customer = customerRepository.findById(newOrderPlaced.order().getCustomerId())
            .orElseThrow();
        emailNotifierService.notify(customer.getUserName(), String.format("We received your new Order with the ID '%s'!", newOrderPlaced.order().getId().value()));
    }
}

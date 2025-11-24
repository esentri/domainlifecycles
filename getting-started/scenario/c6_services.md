# Chapter 6 – Customer Services and Domain Commands

---

**Covered topics:** Application services, DomainCommands, Repository usage, DomainEvents, Event listeners

---

Emma’s webshop can now store customers in the database — but so far, there’s no *use-case* to actually **add** new ones.
That’s the job of the **application layer**: to coordinate domain objects, persistence, and events for concrete business actions.

In DDD & DLC, this is typically done through **DomainCommands** and **service classes**.

---

## Step 1 – Introducing a DomainCommand

In Domain-Driven Design, a command expresses an **intention to change the system**.
For example, “Add a new customer” or “Change a customer’s address.”

Let’s define a command that captures the intent of adding a new customer:

<details>
<summary><img style="height: 12px" src="../icons/java.svg" alt="java"><b>AddNewCustomer.java</b></summary>

```java
package com.shop.domain.customer;

import io.domainlifecycles.domain.types.DomainCommand;
import java.util.Optional;

public record AddNewCustomer(
    String userName,
    Address address,
    Optional<CreditCard> creditCard
) implements DomainCommand { }
```

</details>

The command is simple and expressive.
It carries all the data needed to fulfill Emma’s request: who the customer is and how they can pay.

---

## Step 2 – Creating the Customer Service

We’ll now implement a service that handles this command. The service we will build in fact is an application-service, 
since it's only there for orchestration and connecting our web and persistence layer.
This service belongs to the **application layer** and acts as the *driver* for customer-related use-cases.

Create a new class in `src/main/java/com/shop/inbound/driver`:

<details>
<summary><img style="height: 12px" src="../icons/java.svg" alt="java"><b>CustomerService.java</b></summary>

```java
package com.shop.inbound.driver;

import io.domainlifecycles.domain.types.Publishes;
import io.domainlifecycles.events.api.DomainEvents;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.shop.domain.customer.AddNewCustomer;
import com.shop.domain.customer.Customer;
import com.shop.domain.customer.NewCustomerAdded;
import com.shop.outport.CustomerRepository;

@Transactional
@Service
public class CustomerService {

    private final CustomerRepository repository;

    public CustomerService(CustomerRepository repository) {
        this.repository = repository;
    }

    @Publishes(domainEventTypes = {NewCustomerAdded.class})
    public Customer add(AddNewCustomer addNewCustomer) {
        var addedCustomer = repository.insert(Customer.builder()
            .id(repository.newCustomerId())
            .userName(addNewCustomer.userName())
            .address(addNewCustomer.address())
            .creditCard(addNewCustomer.creditCard())
            .blocked(false)
            .build());

        DomainEvents.publish(new NewCustomerAdded(addedCustomer));
        return addedCustomer;
    }
}
```

</details>

Let’s unpack what’s happening here:

1. `@Service` makes the class a Spring-managed bean.
2. `@Transactional` ensures that adding a customer and saving it to the database happen atomically.
3. `@Publishes` declares that the method emits the `NewCustomerAdded` domain event.
4. Inside the method, we call our repository’s `insert()` method — which we implemented in **Chapter 5**.

The `CustomerRepository` handles all the persistence details, so the service remains focused on business intent.

---

## Step 3 - Event listeners
See the event we are publishing in the `add()` function above? Also, do you remember the [events we published in chapter four](./c4_order.md#step-3-introducing-domain-events)?
Nobody's listening to them yet!
Let's take a look at how DLC handles event listeners.

```java
@Log
@Service
public class CustomerNotificationService implements DomainService {
    
    private final CustomerRepository customerRepository;

    public CustomerNotificationService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    /**
     * Notifies Customers about a new order placed event.
     *
     * @param newOrderPlaced the DomainEvent representing the fact that a new order was successfully placed
     */
    @ListensTo(domainEventType = NewOrderPlaced.class)
    public void notifyNewOrderPlaced(NewOrderPlaced newOrderPlaced) {
        var customer = customerRepository.findById(newOrderPlaced.order().getCustomerId())
            .orElseThrow();
        log.info(customer.getUserName(),
            String.format("We received your new Order with the ID '%s'!", newOrderPlaced.order().getId().value()));
    }
}
```

Note the annotation `@ListensTo(domainEventType = NewOrderPlaced.class)`. That's how you create event-listeners with DLC.
Simple as that. 
It may be beneficial to write your event-listeners in a separate service, as we did with this example, to ensure loose coupling
and a clear architecture.

--- 

## Step 4 – Connecting It All Together

Now that Emma can officially “add a new customer,” let’s simulate this workflow inside a test or controller.

```java

var command = new AddNewCustomer(
    "emma_shop_fan",
    new Address("Main Street 1", "London", "E1 6AN"),
    Optional.empty()
);

Customer added = customerService.add(command);
System.out.println("Added new customer with id: " + added.getId().value());

```

When this runs:

1. The service creates a new `Customer` aggregate using the builder.
2. The repository inserts it into the database.
3. A `NewCustomerAdded` domain event is published, allowing other components (like notifications) to react.

---

## Step 5 – Beyond Adding Customers

The same pattern applies to other use-cases:

* `ChangeCustomerAddress` → updates address and emits `CustomerAddressChanged`.
* `ChangeCreditCard` → updates payment details and emits `CustomerCreditCardChanged`.
* `BlockCustomer` → responds to domain events like `FraudDetected`.

Each of these can be modeled as small, composable commands, handled by the same service or dedicated handlers.

---

## Conclusion

You now know how Domain-Commands are created and handled with DLC in an application service.
For our next and last chapter, we will look at how DLC simplifies OpenAPI documentation and how 
to finally make Emma's webshop accessible to the internet via REST-Controllers, so that her customers
can interact with it.

---

|   **Chapter 5 - Customer**    | **Chapter 7 - REST**  |
|:-----------------------------:|:---------------------:|
| [<< Previous](c5_customer.md) | [Next >>](c7_rest.md) |
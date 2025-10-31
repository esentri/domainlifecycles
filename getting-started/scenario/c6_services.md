# Chapter 6 – Customer Services and Domain Commands

---

**Covered topics:** Application services, DomainCommands, Repository usage, DomainEvents

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

We’ll now implement a service that handles this command.
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
            .creditCard(addNewCustomer.creditCard().orElse(null))
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

## Step 3 – Connecting It All Together

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

✅ **Checkpoint**

You now know how to:

* Create and use **DomainCommands** to express business intent.
* Build **application services** that coordinate repositories and domain events.
* Keep your domain model clean while DLC handles persistence and event wiring.

---

In the next chapter, we’ll expose these use-cases to the outside world through **REST controllers**, letting Emma and her customers interact with the webshop.

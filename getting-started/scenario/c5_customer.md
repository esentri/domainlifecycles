# Chapter 5 – Persisting Customers and Working with Repositories

--- 

Covered topics: Aggregate persistence, Repositories, JooqAggregateRepository, Database fetching

--- 

Emma’s shop has products and orders - now she needs a way to manage her customers.
Up until now, we’ve stayed inside the domain layer. But to make the webshop real, our aggregates need to live in a database.

That’s where repositories come in.

In this chapter, we’ll build the Customer aggregate and connect it to a persistence layer using DLC’s repository support.

## Step 1: Creating the Customer Aggregate

The Customer is our last missing aggregate. We’ll keep the implementation short and focus on what matters: it’s an aggregate root that represents the shop user.

<details> <summary><img style="height: 12px" src="../icons/java.svg" alt="java"><b>Customer.java</b></summary>

```java
package com.shop.domain.customer;

import io.domainlifecycles.domain.types.Identity;
import io.domainlifecycles.domain.types.base.AggregateRootBase;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import java.util.Optional;

@Getter
public final class Customer extends AggregateRootBase<Customer.CustomerId> {

    public record CustomerId(@NotNull Long value) implements Identity<Long> { }

    private final CustomerId id;
    @NotEmpty @Size(max = 100)
    private String userName;
    @NotNull
    private Address address;
    private Optional<CreditCard> creditCard;
    private boolean blocked;

    @Builder
    public Customer(CustomerId id,
                    long concurrencyVersion,
                    String userName,
                    Address address,
                    CreditCard creditCard,
                    boolean blocked) {
        super(concurrencyVersion);
        this.id = id;
        this.userName = userName;
        this.address = address;
        this.creditCard = Optional.ofNullable(creditCard);
        this.blocked = blocked;
    }
}
```
</details>

We’ll skip complex event handling here for now. The goal is to understand how a Customer is stored and retrieved from the database.

## Step 2: Defining the Customer Repository Interface

DDD teaches us that aggregates should only be loaded and stored via repositories.
In DLC, this is made easy: you just extend the generic Repository interface.

Let’s create the CustomerRepository that will handle fetching and persisting our customers.

<details> <summary><img style="height: 12px" src="../icons/java.svg" alt="java"><b>CustomerRepository.java</b></summary>

```java
package com.shop.outport;

import io.domainlifecycles.domain.types.Repository;
import com.shop.domain.customer.Customer;
import java.util.Optional;
import java.util.stream.Stream;

public interface CustomerRepository extends Repository<Customer.CustomerId, Customer> {

    Customer.CustomerId newCustomerId();

    @Override
    Customer insert(Customer entity);

    @Override
    Customer update(Customer entity);

    @Override
    Optional<Customer> findById(Customer.CustomerId customerId);

    @Override
    Optional<Customer> deleteById(Customer.CustomerId customerId);

    Stream<Customer> find(int offset, int limit);
}
```
</details>

This interface defines everything we need:
- A method to create new IDs (newCustomerId()),
- Methods to insert, update, find, and delete customers,
- A paginated finder (find(offset, limit)).

## Step 3: Creating the JOOQ repository

Usually, you'd need to implement all the methods needed to allow for persisting/fetching data from a database.
DLC's persistence module however does the heavy lifting for you and already comes with lots of the predefined methods
for common database operations like inserting, updating, fetching etc.
This means that just the following basic definition of a repository makes simple CRUD operations possible already:

<details> <summary><img style="height: 12px" src="../icons/java.svg" alt="java"><b>JooqCustomerRepository.java</b></summary>

```java
package com.shop.outbound;

import io.domainlifecycles.jooq.imp.JooqAggregateRepository;
import io.domainlifecycles.jooq.imp.provider.JooqDomainPersistenceProvider;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;
import com.shop.domain.customer.Customer;

@Repository
class JooqCustomerRepository
extends JooqAggregateRepository<Customer, Customer.CustomerId> {

    public JooqCustomerRepository(DSLContext dslContext,
                                  JooqDomainPersistenceProvider domainPersistenceProvider,
                                  SpringPersistenceEventPublisher persistenceEventPublisher) {
        super(Customer.class, dslContext, domainPersistenceProvider, persistenceEventPublisher);
    }
}
```
</details>

By making our repository implementation extend the `JooqAggregateRepository`, we get access to the bespoke CRUD operations already.

But now you might wonder, how can we make our `JooqCustomerRepository` implement the interface we defined before? 
What's still left to do for us?

Before we continue, please have a look at the [following guide](additional/jooq-setup.md) and follow through. This is necessary to setup DLC's persistence
and JOOQ, the driving force behind it, accordingly. As soon as you are finished, please return to this guide and continue with
the next step.

## Step 4: Implementing the CustomerRepository and gain full functionality

Great!
You've setup JOOQ which means we are now ready to finalize the development of our repository class.

The only methods we need to override to implement our `CustomerRepository` are:
- `newCustomerId()`
- `find(int offset, int limit)`

We don't need to worry about the rest of the methods since they already come with the extension of the `JooqAggregateRepository`.

So, let's look at the implementation then:

<details> <summary><img style="height: 12px" src="../icons/java.svg" alt="java"><b>JooqCustomerRepository.java</b></summary>

```java
package sampleshop.outbound;

import sampleshop.outbound.event.SpringPersistenceEventPublisher;
import io.domainlifecycles.jooq.imp.JooqAggregateRepository;
import io.domainlifecycles.jooq.imp.provider.JooqDomainPersistenceProvider;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;
import sampleshop.Sequences;
import sampleshop.core.domain.customer.Customer;
import sampleshop.core.outport.CustomerRepository;

import java.util.stream.Stream;

import static sampleshop.Tables.CUSTOMER;

@Repository
class JooqCustomerRepository extends JooqAggregateRepository<Customer, Customer.CustomerId> implements CustomerRepository {

    public JooqCustomerRepository(DSLContext dslContext,
                                  JooqDomainPersistenceProvider domainPersistenceProvider,
                                  SpringPersistenceEventPublisher persistenceEventPublisher) {
        super(Customer.class, dslContext, domainPersistenceProvider, persistenceEventPublisher);
    }
    
    @Override
    public Customer.CustomerId newCustomerId() {
        return new Customer.CustomerId(dslContext.nextval(Sequences.CUSTOMER_ID_SEQ));
    }
    
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
```
</details>

That's a lot of code!

Let's walk through it step by step.

### Custom queries
More complex queries, like our paginated `find()` can be excellently defined through the interaction between DLC and JOOQ.
The `fetcher` is often used here, which enables simplified loading of the aggregates from the database.
'CUSTOMER' is the JOOQ representation of the customer table and the corresponding JOOQ records of the database.
The fetcher uses `fetchDeep` to load further JOOQ records from the tables that make up the aggregate.
It also performs the mapping to the desired aggregate structure at Java level.
`.offset()` and `.limit()` are the (self-explanatory) options JOOQ provides for pagination.

With a combination of these options, you are able to write the most complex queries you can think of - All while
being integrated in DLC and using the standard JOOQ pattern.


## Conclusion

Congratulations!

You've set up your first repository and are now able to handle data with a relational database and make use of 
DLC's persistence features.

However: The data is nowhere retrieved yet! In the next chapter, we will integrate everything we've built so far by 
creating service classes and making our webshop finally functional. At the same time, you will learn about how to 
make use of the command-pattern in DLC.


---

|    **Chapter 4 - Order**     | **Chapter 6 - Services**  |
|:----------------------------:|:-------------------------:|
| [<< Previous](c3_product.md) | [Next >>](c5_customer.md) |
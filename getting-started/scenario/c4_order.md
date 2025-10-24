# Chapter 4 – Modeling the Order Aggregate

---

Covered topics: Aggregate Roots, Domain Events, Entities, Domain State Transitions

---

Emma’s webshop is up and running — customers can browse and view her handmade products.
Now it’s time for the next big step: letting them place orders.

This brings us to our second Aggregate Root — the `Order`.
While the `Product` focused on static data (name, price, image), an order represents business activity, something that happens in the system.
That’s where Domain Events come into play. 
They can be placed, shipped, or canceled - and these changes need to be clearly represented in the domain model.

## Step 1: Defining the Order Aggregate

As always, start by creating a new package for the domain area:

```src/main/java/com/shop/core/domain/order```

The `Order` will be our second Aggregate Root.
Let’s begin with a clean, minimal version — no events or transitions yet, just the core data.

<details> <summary><img style="height: 12px" src="../icons/java.svg" alt="java"><b>Order.java</b></summary>

```java
package com.shop.domain.order;

import io.domainlifecycles.domain.types.Identity;
import io.domainlifecycles.domain.types.base.AggregateRootBase;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import sampleshop.core.domain.Price;
import sampleshop.core.domain.customer.Customer;
import sampleshop.core.domain.product.Product;

import java.time.Instant;
import java.util.List;
import java.util.stream.Stream;

@Getter
public final class Order extends AggregateRootBase<Order.OrderId> {

    public record OrderId(@NotNull Long value) implements Identity<Long> { }

    private final OrderId id;
    @NotNull private final Customer.CustomerId customerId;
    private final Instant creation;
    @NotNull private final List<OrderItem> items;
    @NotNull private OrderStatus status;

    @Builder
    private Order(long concurrencyVersion,
                  OrderId id,
                  Customer.CustomerId customerId,
                  OrderStatus status,
                  Instant creation,
                  List<OrderItem> items) {
        super(concurrencyVersion);
        this.id = id;
        this.customerId = customerId;
        this.creation = creation;
        this.status = status;
        this.items = items;
    }

    public Stream<OrderItem> items() {
        return items.stream();
    }

    public Price totalPrice() {
        return items()
            .map(OrderItem::itemPrice)
            .reduce(new Price(java.math.BigDecimal.ZERO), Price::add);
    }
}

```
</details>

You can see that we don't store a `Product` in an `Order`, but rather an `OrderItem`.
Since this represents an entity in a DDD context, it only makes sense to define this as a separate class.
As for every other DDD building block, there is once again a superclass which you can simply extend to define a new entity.
Here's how this would look for the `OrderItem` in particular:

<details> <summary><img style="height: 12px" src="../icons/java.svg" alt="java"><b>OrderItem.java</b></summary>

```java
package com.shop.domain.order;

import io.domainlifecycles.domain.types.Identity;
import io.domainlifecycles.domain.types.base.EntityBase;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Getter;
import com.shop.domain.Price;
import com.shop.domain.product.Product;

@Getter
public final class OrderItem extends EntityBase<OrderItem.OrderItemId> {

    public record OrderItemId(@NotNull Long value) implements Identity<Long> { }

    private final OrderItemId id;
    @NotNull private final Product.ProductId productId;
    @NotNull private final Price productPrice;
    @Positive private int quantity;

    @Builder
    private OrderItem(long concurrencyVersion,
                      OrderItemId id,
                      Product.ProductId productId,
                      Price productPrice,
                      int quantity) {
        super(concurrencyVersion);
        this.id = id;
        this.productId = productId;
        this.productPrice = productPrice;
        this.quantity = quantity;
    }

    public Price itemPrice() {
        return productPrice.multiply(quantity);
    }

    public OrderItem addQuantity(int quantity) {
        this.quantity += quantity;
        return this;
    }
}
```
</details>

Now we've modeled the order and contained products in a way congruent with DDD.

At this point, we’ve captured the core concept:

- An order has a unique identity (OrderId).
- It belongs to a customer.
- It contains items and a total price.
- It tracks its status in the ordering process.

This structure alone already expresses much of Emma’s domain language.

## Step 2: Adding Domain Behavior

Now, let’s make the Order do something.
Orders should allow customers to add items — either creating a new OrderItem or increasing the quantity if it already exists.

Add this method inside `Order`:

```java
public Order addItem(@NotNull final Product.ProductId id, int quantity) {
    items()
        .filter(item -> item.getProductId().equals(id))
        .findFirst()
        .ifPresentOrElse(item -> 
            item.addQuantity(quantity),
            () -> items.add(OrderItem.builder().productId(id).quantity(quantity).build()));
    
    return this;
}
```

Now Emma’s customers can fill their virtual shopping carts — and the model clearly represents that process.

## Step 3: Introducing Domain Events

In a real system, certain domain actions are worth announcing.
When an order is shipped or canceled, other components — such as inventory or notification services — might need to react.
Instead of hard-coding these dependencies, DLC uses Domain Events.

A `DomainEvent` is a simple, immutable object describing a fact that has occurred in the domain.
You define your own by simply implementing the Interface, as you might have already guessed. It's recommended to use
immutable classes, so a Java record fits perfectly here.

<details> <summary><img style="height: 12px" src="../icons/java.svg" alt="java"><b>OrderCanceled.java</b></summary>

```java
package com.shop.domain.order;

import io.domainlifecycles.domain.types.DomainEvent;
import jakarta.validation.constraints.NotNull;

public record OrderCanceled(@NotNull Order order) implements DomainEvent { }
```
</details> <details> <summary><img style="height: 12px" src="../icons/java.svg" alt="java"><b>OrderShipped.java</b></summary>

```java
package com.shop.domain.order;

import io.domainlifecycles.domain.types.DomainEvent;
import jakarta.validation.constraints.NotNull;

public record OrderShipped(@NotNull Order order) implements DomainEvent { }
```
</details>

These events don’t do anything by themselves — they simply tell the system that something happened.

## Step 4: Publishing Events in the Aggregate

Now we’ll enrich the Order with two lifecycle transitions.
Each transition changes the order’s state and publishes a corresponding event.

Add the following methods inside your Order class:

```java
@Publishes(domainEventTypes = {OrderCanceled.class})
public Order cancel() {
    DomainAssertions.isTrue(status == OrderStatus.PENDING,
        "Only pending orders can be canceled.");
    status = OrderStatus.CANCELED;
    DomainEvents.publish(new OrderCanceled(this));
    return this;
}

@Publishes(domainEventTypes = {OrderShipped.class})
public Order ship() {
    DomainAssertions.isTrue(status == OrderStatus.PENDING,
        "Only pending orders can be shipped.");
    status = OrderStatus.SHIPPED;
    DomainEvents.publish(new OrderShipped(this));
    return this;
}
```

The annotation `@Publishes(domainEventTypes = {})` and the call of the static method `DomainEvents.publish()` are relevant here.

With these few lines:
- The aggregate now controls its own lifecycle.
- It emits Domain Events to inform the outside world.
- Validation remains inside the domain model.

## Conclusion
Now the webshop is starting to take on some shape. 
Emma's webshop can now take on orders. But what happens next? Our orders are telling the system: "I'm cancelled" or "I'm shipped",
but nobody's listening.
That's what we're going to cover in the next chapter: Event-Listeners, Domain-Commands and Service classes in general.

---

|   **Chapter 3 - Product**    | **Chapter 5 - Customer**  |
|:----------------------------:|:-------------------------:|
| [<< Previous](c3_product.md) | [Next >>](c5_customer.md) |
# Chapter 3 – Modeling the Product Aggregate

---

**Covered topics:** Aggregate-Roots, Identities, Value-Objects, Validation-Support

---

Emma is excited — her webshop is finally starting to take shape.
The first thing she wants to sell are her handmade products.
For you as a developer, that means it’s time to design the core domain model: the Product.

In DDD terms, a Product is an Aggregate Root — the main entry point for a group of related objects that represent a consistent unit in the domain.

---

## Step 1: Define the Domain Package

Inside your project, create a new package for the domain layer and inside for the product domain:

`src/main/java/com/shop/core/domain/product`

This is where all product-related classes will live: entities, value objects, and the aggregate itself.

---

## Step 2: Create the Aggregate Root

Let’s start simple.
To define a new AggregateRoot with DLC, you simply make your class extend the `AggregateRootBase` superclass.

<details> <summary><img style="height: 12px" src="../icons/java.svg" alt="java"><b>Product.java</b></summary>

```java
package com.shop.domain.product;

import io.domainlifecycles.domain.types.Identity;
import io.domainlifecycles.domain.types.base.AggregateRootBase;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

import java.net.URI;
import java.util.Optional;

@Getter
public final class Product extends AggregateRootBase<Product.ProductId> {
    
    public record ProductId(@NotNull Long value) implements Identity<Long> { }
    
    private final ProductId id;

    @Builder
    private Product(final long concurrencyVersion,
                    final Product.ProductId id) {
        super(concurrencyVersion);
        this.id = id;
    }
}
```
</details>

Notice how the `AggregateRootBase` is a generic class? It expects you to provide the ID of your class, which has to implement
the `Identity` interface. For sake of simplicity, you can just define it as a local record in your `Product` class.
Last but not least, for every entity you define with the DLC framework, you need it to accept a `concurrencyVersion` for 
initializing. The `concurrencyVersion` builds the foundation for optimistic locking and is necessary for DLC to work. 
More on `concurrencyVersion` can be found [here](../../types/src/main/java/io/domainlifecycles/domain/types/internal/ConcurrencySafe.java)

---

## Step 3: Make your product alive
"What makes a product?", Emma asks herself. 
Well, first of all it has a price attached to it. Maybe a name, and a description as well.
Your webshop should also be able to display an image of the given product, so we need an Image-URL, too.
But before we add all the attributes to the products, let's first have a look at the price.
The price is a great candidate to be implemented as a Value-Object in the DDD world, rather than a simple primitive type.

Similar to defining an AggregateRoot with DLC, to create a ValueObject you simply make it implement the 
`ValueObject` interface. A possible implementation for the price could look like this:

<details> <summary><img style="height: 12px" src="../icons/java.svg" alt="java"><b>Price.java</b></summary>

```java
package com.shop.domain.product;

import io.domainlifecycles.domain.types.ValueObject;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Builder;

import java.math.BigDecimal;

@Builder(setterPrefix = "set")
public record Price(
    @NotNull @PositiveOrZero @Digits(integer = 10, fraction = 2) BigDecimal amount
) implements ValueObject {
    
    public Price add(Price another) {
        return new Price(amount.add(another.amount));
    }

    public Price multiply(int factor) {
        return new Price(amount.multiply(BigDecimal.valueOf(factor)));
    }

}
```
</details>

Simple as that.
Now we are equipped with everything we need. Let's complete the Product!

<details> <summary><img style="height: 12px" src="../icons/java.svg" alt="java"><b>Product.java</b></summary>

```java
package com.shop.domain.product;

import io.domainlifecycles.domain.types.Identity;
import io.domainlifecycles.domain.types.base.AggregateRootBase;
import lombok.Builder;
import lombok.Getter;
import sampleshop.core.domain.Price;

import java.net.URI;
import java.util.Optional;

@Getter
public final class Product extends AggregateRootBase<Product.ProductId> {

    public record ProductId(Long value) implements Identity<Long> { }

    private final ProductId id;

    private String description;
    
    private String name;
    
    private URI image;
    
    private Price price;

    @Builder
    private Product(final long concurrencyVersion,
                    final Product.ProductId id,
                    final String description,
                    final String name,
                    final URI image,
                    final Price price) {
        super(concurrencyVersion);
        this.id = id;
        this.description = description;
        this.name = name;
        this.image = image;
        this.price = price;
    }
}

```
</details>

Now we enhanced our product with all the values we need to list it on our webshop!

---

## Step 4: Validation & Business rules / Invariants
Emma is thinking to herself: "Well the shop owner can now list products on his site. But what if he makes a mistake, 
and leaves the price empty? Customers will be confused. And what if he forgets to name his product? How can I tackle this?"

This may sound familiar to you. If you have ever implemented some form of business rules into your project, you know the struggle.
Things can get messy fast, you lose track of all the rules you need and end up with long, nested if/else statements in your
services. 

### First option: Bean validations
DLC offers extended support of
Java Bean Validation Annotations within a DomainObject to implement invariants or pre/post/conditions on methods.
You can use them as always by just annotating your fields with the wanted annotation. 
An example for the products in Emma's webshop could look like this:

<details> <summary><img style="height: 12px" src="../icons/java.svg" alt="java"><b>Product.java</b></summary>

```java
package com.shop.domain.product;

import io.domainlifecycles.assertion.DomainAssertions;
import io.domainlifecycles.domain.types.Identity;
import io.domainlifecycles.domain.types.base.AggregateRootBase;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import sampleshop.core.domain.Price;

import java.net.URI;
import java.util.Optional;

@Getter
public final class Product extends AggregateRootBase<Product.ProductId> {

    public record ProductId(@NotNull Long value) implements Identity<Long> { }

    private final ProductId id;

    private @Size(max = 1000) String description;
    
    @NotEmpty
    @Size(max = 200)
    private String name;
    
    private URI image;
    
    @NotNull
    private Price price;

    @Builder
    private Product(final long concurrencyVersion,
                    final Product.ProductId id,
                    final String description,
                    final String name,
                    final URI image,
                    final Price price) {
        super(concurrencyVersion);
        this.id = id;
        this.description = description;
        this.name = name;
        this.image = image;
        this.price = price;
    }
}
```
</details>

In order to make them work however, you need to activate the byte-code extension.

### Byte-Code extension
To simplify the implementation of an "Always-Valid-Strategy", DLC offers a ByteCode extension.
"Always-Valid" means that all domain objects can only be created with their validations adhered to at all times.
The ByteCode extension, for example, adds corresponding validation calls to all relevant constructors.

The extension however is not activated per default when using DLC's autoconfig feature.
You have to activate it explicitly like this in your `Application` class:

```Java
@SpringBootApplication
@EnableDlc(dlcDomainBasePackages = "com.shop.domain")
public class ShopApplication {
    
    static void main(String[] args) {
        SpringApplication.run(ShopApplication.class, args);
    }

    @PostConstruct
    public void postConstruct() {
        ValidationDomainClassExtender.extend("sampleshop");
    }
}
```

But bean validation annotations can often be quite limiting to your use cases. What if you want to write more specific
and unique invariants? That's where DLC's DomainAssertions come into play.

### Second option: DomainAssertions-API
By making use of DLC's `DomainAssertions`-API, Emma can easily implement any rule she can think of for her product.

Let's take a look on how she does that.

<details> <summary><img style="height: 12px" src="../icons/java.svg" alt="java"><b>Product.java</b></summary>

```java
package com.shop.domain.product;

import io.domainlifecycles.assertion.DomainAssertions;
import io.domainlifecycles.domain.types.Identity;
import io.domainlifecycles.domain.types.base.AggregateRootBase;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import sampleshop.core.domain.Price;

import java.net.URI;
import java.util.Optional;

@Getter
public final class Product extends AggregateRootBase<Product.ProductId> {

    public record ProductId(@NotNull Long value) implements Identity<Long> { }

    private final ProductId id;

    private @Size(max = 1000) String description;
    
    @NotEmpty
    @Size(max = 200)
    private String name;
    
    private URI image;
    
    @NotNull
    private Price price;

    @Builder
    private Product(final long concurrencyVersion,
                    final Product.ProductId id,
                    final String description,
                    final String name,
                    final URI image,
                    final Price price) {
        super(concurrencyVersion);
        this.id = id;
        this.description = description;
        this.name = name;
        this.image = image;
        this.price = price;
    }
    
    @Override
    public void validate() {
        image.ifPresent(uri ->
            DomainAssertions.hasLength(
                uri.toString(),
                0,
                1000,
                "The product image URI must have less than 1000 characters."
            )
        );
    }
}
```
</details>

With `DomainAssertions`, you can write custom assertions that are not supported out of the box by using plain 
bean validations. In our case, we make use of `DomainAssertions` by overriding the `validate()` method of our AggregateRootBase.
`DomainAssertions` enable you to execute any type of assertion you can think of. In this case, we want the URI to have
a maximum number of characters.
You can use `DomainAssertions` anywhere you want in your code. Most often, you will probably use them in your constructor
or, like in this case, in the `validate()` method in classes which implement the 
`io.domainlifecycles.domain.types.Validatable` interface (as most of the marker interfaces do).

Now we are safe and the webshop will guide the webshop owner to only listing valid products!

---

## Step 5: Your first Aggregate in action!

You can test your `Product` and especially its constraints like the following:

<details> <summary><img style="height: 12px" src="../icons/java.svg" alt="java"><b>Product.java</b></summary>

```java
package com.shop.domain.product;

import java.math.BigDecimal;
import java.util.Optional;

class ProductTest {

    @Test
    void testSuccessfulInit() {
        assertThatNoException().isThrownBy(() ->
            Product.builder()
                .id(ProductId.builder().id(1L).build())
                .description("A red handbag.")
                .name("Red handbag")
                .price(Price.builder().amount(BigDecimal.TEN).build())
                .build());
    }

    @Test
    void testEmptyName() {
        assertThatThrownBy(() ->
            Product.builder()
                .id(ProductId.builder().id(1L).build())
                .description("A red handbag.")
                .price(Price.builder().amount(BigDecimal.TEN).build())
                .build())
            .isInstanceOf(DomainAssertionException.class);
    }
}
```
</details>

---

## Conclusion
So far, we’ve kept things simple — but this small model already shows the DDD mindset:
- The domain model is expressive and free from technical concerns.
- The business rules (like checking stock) live inside the aggregate, not in a service.

In other words: the code speaks Emma’s business language.

In the next chapter, we will implement a second AggregateRoot, the Order, to enhance our webshop and create 
a first bit of functionality by using `DomainCommands` and `DomainEvents`.

---

| **Chapter 2 - Configuration** | **Chapter 4 - Order**  |
|:-----------------------------:|:----------------------:|
|  [<< Previous](c2_config.md)  | [Next >>](c4_order.md) |
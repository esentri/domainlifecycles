# Chapter 7 – Exposing Customers via REST

---

**Covered topics:** REST controllers, DLC auto-configuration, JSON serialization, OpenAPI integration

---

Emma’s webshop can now create customers and store them in the database — but she still can’t *talk to* the system.
To make that possible, we’ll expose our application layer through a **REST API**.

This chapter will show you how to connect your `CustomerService` from Chapter 6 to the outside world and how **DLC’s autoconfiguration** takes care of the heavy lifting for serialization and documentation.

---

## Step 1 – DLC Web & OpenAPI Auto-Configuration

When you annotated your Spring Boot application with:

```java
@EnableDlc(dlcDomainBasePackages = "com.shop.domain")
@SpringBootApplication
public class ShopApplication {
    static void main(String[] args) {
        SpringApplication.run(ShopApplication.class, args);
    }
}
```

you already enabled more than just the core DLC features.

DLC’s autoconfiguration automatically provides:

* **OpenAPI integration**: generates a full OpenAPI/Swagger specification that includes your DLC domain types, complete with accurate schemas and example structures.
* **Jackson / ObjectMapper configuration**: automatically registers formatters and serializers so that DLC types (`AggregateId`, `ValueObject`, `DomainCommand`, etc.) are correctly serialized and deserialized in JSON.
* **REST-friendly defaults**: content negotiation, validation, and error handling configured out-of-the-box.

That means your domain objects “just work” in REST controllers and appear correctly in generated OpenAPI docs — no manual JSON adapters or schema definitions required.

---

## Step 2 – Creating the REST Controller

Next, let’s expose the `CustomerService` through a controller.

Create a new class in
`src/main/java/com/shop/inbound/rest/CustomerController.java`.

We’ll start with just the **addNewCustomer** endpoint for clarity:

<details>
<summary><img style="height: 12px" src="../icons/java.svg" alt="java"><b>CustomerController.java</b></summary>

```java
package com.shop.inbound.rest;

import io.domainlifecycles.spring.http.ResponseEntityBuilder;
import io.domainlifecycles.spring.http.ResponseObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.shop.domain.customer.AddNewCustomer;
import com.shop.domain.customer.Customer;
import com.shop.inbound.driver.CustomerService;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    private final CustomerService customerService;
    private final ResponseEntityBuilder responseEntityBuilder;

    public CustomerController(CustomerService customerService,
                              ResponseEntityBuilder responseEntityBuilder) {
        this.customerService = customerService;
        this.responseEntityBuilder = responseEntityBuilder;
    }

    /**
     * Add a new customer to Emma's webshop.
     */
    @PostMapping
    public ResponseEntity<ResponseObject<Customer>> addNewCustomer(
            @RequestBody AddNewCustomer addNewCustomer) {
        var created = customerService.add(addNewCustomer);
        return responseEntityBuilder.build(created, HttpStatus.CREATED);
    }
}
```

</details>

Let’s break this down:

* `@RestController` marks the class as a web endpoint.
* `@RequestBody AddNewCustomer` automatically deserializes the incoming JSON payload into the domain command.
* `ResponseEntityBuilder` is a small utility from DLC that wraps results into a standardized response format (`ResponseObject`), making it easy to build consistent API responses.
* Thanks to DLC’s autoconfigured **ObjectMapper**, `Customer`, `AddNewCustomer`, and all related value objects are properly serialized without extra effort.

---

## Step 3 – Trying It Out

Start your application and send a POST request:

```bash
POST /api/customers
Content-Type: application/json

{
  "userName": "emma_shop_fan",
  "address": {
    "street": "Main Street 1",
    "city": "London",
    "zipCode": "E1 6AN"
  },
  "creditCard": null
}
```

You should receive a `201 Created` response with a JSON body like:

```json
{
  "data": {
    "id": 42,
    "userName": "emma_shop_fan",
    "address": {
      "street": "Main Street 1",
      "city": "London",
      "zipCode": "E1 6AN"
    },
    "blocked": false
  },
  "status": "SUCCESS"
}
```

Open your browser at
`http://localhost:8080/swagger-ui.html`
and you’ll see the endpoint automatically documented — complete with type schemas generated from your DLC domain model.

---

## Step 4 – What We’ve Achieved

At this point, your webshop:

* Accepts **DomainCommands** (`AddNewCustomer`) over REST.
* Automatically serializes/deserializes complex domain objects.
* Publishes domain events when a new customer is added.
* Generates a full **OpenAPI definition** with no extra configuration.

---

## Conclusion

The webshop now accepts **DomainCommands** (`AddNewCustomer`) over REST, automatically serializes/deserializes complex domain objects, 
publishes domain events when a new customer is added and generates a full **OpenAPI definition** with no extra configuration.

With this last missing piece implemented, Emma's online-shop is now ready to be served to customers.
Congratulations!

For now, we've only covered the absolute basics to make it work. If you want to make the shop more enhanced,
have a look at the [sample-project](../../sample-project), where you will find the webshop fully implemented.

---

|   **Chapter 6 - Services**    |
|:-----------------------------:|
| [<< Previous](c6_services.md) |
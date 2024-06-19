## Domain Lifecycles DomainEvents

DLC Domain Events provides functionality to simplify some of the technical aspects regarding DomainEvents:
- In order to keep the core implementation of a bounded context free of technical concerns DLC Domain Events provides two simple
interfaces for publishing or listening on Domain Events.
- DLC provides rich configuration options 
  - to align DomainEvent handling according to transactional boundaries
  - to plug in technical message bus providers

### Provided API

#### Publishing Domain Events

Domain Events could be published from anywhere within the whole program by using a central static interface 
``DomainEvents.publish(domainEvent);``. That way it's not needed to inject or provide implementations of a publisher 
interface in any services (especially DomainServices or ApplicationServices). Furthermore it's also straight forward 
to publish events directly from an Aggregate.

```Java
public final class OrderPlacementService implements OrderPlacementOperations {
    
    @Publishes(domainEventTypes = {NewOrderPlaced.class})
    public Order placeOrder(final PlaceOrder placeOrder) {
        ...
        DomainEvents.publish(new NewOrderPlaced(placed));
        return placed;
    }

}
```

```Java
public final class Order extends AggregateRootBase<Order.OrderId> {

    ...
    
    @Publishes(domainEventTypes = {OrderCanceled.class})
    public Order cancel() {
        transition(OrderStatus.PENDING, OrderStatus.CANCELED,
            "Can't cancel an order that is not pending.");
        DomainEvents.publish(new OrderCanceled(this));
        return this;
    }
    
    @Publishes(domainEventTypes = {OrderShipped.class})
    public Order ship() {
        transition(OrderStatus.PENDING, OrderStatus.SHIPPED,
            "Can't mark as shipped an order that is not pending.");
        DomainEvents.publish(new OrderShipped(this));
        return this;
    }

}
```

DLC DomainEvents also provides options to customize or enhance to technical event publishing mechanisms hidden behind
this simple interface, see [Configuration](#Configuration).

Additionally, DLC provides the option to add metadata annotations on methods that publish DomainEvents (``@Publishes``, see io.domainlifecycles.domain.types.Publishes). 
Those annotations have no functional impact, but they currently make it more transparent to developers, where DomainEvents are published. The ``@Publishes`` metadata 
information is also rendered in DLC domain diagrams (see [DLC Domain Diagrammer](./readme_diagrammer.md))  

#### Listening to DomainEvents
Domain Events are typically listened to by ApplicationServices, DomainServices, Repositories, OutboundServices, QueryClients or directly by Aggregate instances. 
DLC reduces the technical boilerplate normally needed to route consumed DomainEvents to the addressed handler methods.
Therefor the annotation ``@ListensTo`` (see io.domainlifecycles.domain.types.ListensTo) is used.

Every method (inside an ApplicationService, a DomainService, an OutboundService, a QueryClient or a Repository) that has only one parameter (the consumed DomainEvent) and which is annotated with
```@ListensTo``` is automatically called, when a new DomainEvent is consumed by the central DLC event handler (see ``io.domainlifecycles.events.receive.ReceivingDomainEventHander``).

Depending on the transaction configuration of DLC DomainEvents, every handler call might also be automatically wrapped in an independent transaction.

Example of a service event handler methods (applicable on ApplicationServices (aka Drivers), DomainServices, OutboundServices, QueryClients or Repositories):
```Java
public class CustomerNotificationDriver implements Driver {
    ...
    @ListensTo(domainEventType = OrderShipped.class)
    public void notifyOrderShipped(OrderShipped orderShipped){
        ...
    }

    @ListensTo(domainEventType = OrderCanceled.class)
    public void notifyOrderCanceled(OrderCanceled orderCanceled){
        ...
    }

    @ListensTo(domainEventType = NewOrderPlaced.class)
    public void notifyNewOrderPlaced(NewOrderPlaced newOrderPlaced){
        ...
    }
}
```

To route DomainEvents directly to an Aggregate instance, the DomainEvent must implement a special interface ```io.domainlifecycles.domain.types.AggregateDomainEvent```.
The interface requires the implementor to provide the ``targetId`` of the target AggregateRoot. In this case fetching the AggregateRoot instance by the specified targetId 
from it's corresponding Repository and calling the event handler method directly on the AggregateRoot instance is done directly by the DLC framework in the background.
The complete described operation is done in a separate transaction. This case requires a transactional setup of DLC DomainEvents!

Example of an AggregateDomainEvent:
```Java
public record FraudDetected(
    Customer.CustomerId customerId
) implements AggregateDomainEvent<Customer.CustomerId, Customer> {

    @Override
    public Customer.CustomerId targetId() {
        return customerId;
    }
}
```

Example of it's corresponding handler implementation:
```Java
public final class Customer extends AggregateRootBase<Customer.CustomerId> {
    ...
    @ListensTo(domainEventType = FraudDetected.class)
    public void onFraudDetected(FraudDetected fraudDetected){
        block();
    }

    public Customer block(){
        this.blocked = true;
        return this;
    }
    
}
```

### Configuration
DomainEvent handling provides a rich set of configuration options. There are several preconfigured options that are provided out-of-the-box (especially with transactional support for Spring/Spring Boot 3.0).
But it's also possible to override some of the interfaces to add customized publishing or listening behaviour.

The configuration typically is done via creating an instance of the ``io.domainlifecycles.events.api.DomainEventsConfiguration``class. 
At least for the automatic routing functionality an instance of a ``io.domainlifecycles.services.ServiceProvider`` must be provided (typically ``io.domainlifecycles.services.Services`` is used).

##### Out-of-the-box configuration options

The out-of-the-box configurations of DLC DomainEvents are:

- [Non-transactional handling](#non-transactional) (not recommended for real world use cases)
- [Transactional setup without a transactional outbox](#transactional-without-outbox)
- [Transactional setup including a transactional outbox](#transactional-with-outbox)

All these out-of-the-box configurations do not include an external message bus. Domain Events are only handled "in-process". 
The transactional outbox solution might still provide some guarantees. Third-party message busses might be plugged in 
as described below ([Typical customizations](#typical-customizations)).

<a name="non-transactional"></a>
###### Non-transactional handling
DomainEvents are simply routed in process to the corresponding handlers. DomainEvent loss and DomainEvent ghost messages are possible due to the missing 
transaction support. The consuming handlers are executed asynchronously in separate transactions.
Processing failures in the handlers are reported via log (SLF4J). No automatic retry is in place.

Example setup:
```Java
var services = new Services();
services.registerDomainServiceInstance(domainService);
services.registerRepositoryInstance(repository);
services.registerApplicationServiceInstance(applicationService);
services.registerQueryClientInstance(queryClient);
services.registerOutboundServiceInstance(outboundService);

var configBuilder = new DomainEventsConfiguration.DomainEventsConfigurationBuilder();
configBuilder.withServiceProvider(services);

configBuilder.make();
```

<a name="transactional-without-outbox"></a>
###### Transactional setup without a transactional outbox
In this case DomainEvents are published "after commit". So "ghost messages" are avoided. Ghost messages are DomainEvents, 
that are published but the transaction of the operation that issued the DomainEvent might still be rolled back. 
The loss of DomainEvents using this implementation is are rare case but technically possible. This setup routes 
DomainEvents directly to consuming handlers. The consuming handlers are executed asynchronously in separate transactions.
Processing failures in the handlers are reported via log (SLF4J). No automatic retry is in place.

Example setup with Spring transactions:
``` Java
var services = new Services();
...
var config =  new DomainEventsConfiguration.DomainEventsConfigurationBuilder()
                .withSpringPlatformTransactionManager(transactionManager)
                .withServiceProvider(serviceProvider)
                .make();
```

Example setup with JTA provided transactions:
``` Java
var services = new Services();
...
var config =  new DomainEventsConfiguration.DomainEventsConfigurationBuilder()
                .withJtaTransactionManager(userTransactionManager);
                .withServiceProvider(serviceProvider)
                .make();
```
<a name="transactional-with-outbox"></a>
###### Transactional setup with a transactional outbox
Adding a transactional outbox avoids DomainEvent loss on the publishing side. The setup involves an "outbox" database table, but it's a reliable way of not loosing any DomainEvents 
and comes into play if a relational database is involved as a part of the main technical infrastructure.
The main disadvantage of that setup is a reduced throughput in high volume scenarios due to the database involvement. 
DLC provides an interface for custom Outbox implementations ``io.domainlifecycles.events.publish.outbox.api.TransactionalOutbox``.

For more information on the "transactional outbox pattern" have a look at [Transactional Outbox Pattern](https://microservices.io/patterns/data/transactional-outbox.html)

A tested implementation is available especially for Spring based on Spring transactions, Spring JDBC and Jackson, see ``io.domainlifecycles.events.publish.outbox.impl.SpringJdbcOutbox``.

Example setup with the DLC provided Spring based outbox implementation is shown below. This setup routes polled DomainEvents from the outbox directly to consuming handlers 
within the pollers process, the consuming handlers are executed asynchronously in separate transactions. If DomainEvents were processed successfully by the handlers,
this is acknowledged to the outbox directly. So monitoring the state of event delivery is possible on the outbox. No automatic retry is in place.
Processing failures in the handlers are also reported via log (SLF4J).
``` Java
@SpringBootApplication
public class OutboxTestApplication {

   ...

    @Bean
    public ServiceProvider serviceProvider(
        List<Repository<?,?>> repositories, 
        List<ApplicationService> applicationServices, 
        List<DomainService> domainServices,
        List<QueryClient<?>> queryClients,
        List<OutboundService> outboundServices
    ){
        var services = new Services();
        repositories.forEach(services::registerRepositoryInstance);
        applicationServices.forEach(services::registerApplicationServiceInstance);
        domainServices.forEach(services::registerDomainServiceInstance);
        queryClients.forEach(services::registerQueryClientInstance);
        outboundServices.forEach(services::registerOutboundServiceInstance);
        return services;
    }

    @Bean
    public DomainEventsConfiguration domainEventsConfiguration(TransactionalOutbox transactionalOutbox, ServiceProvider serviceProvider, PlatformTransactionManager transactionManager) {
        var config =  new DomainEventsConfiguration.DomainEventsConfigurationBuilder()
            .withSpringPlatformTransactionManager(transactionManager)
            .withServiceProvider(serviceProvider)
            .withTransactionalOutbox(transactionalOutbox)
            .make();
        return config;
    }

    @Bean
    public TransactionalOutbox transactionalOutbox(DataSource dataSource, ObjectMapper objectMapper, PlatformTransactionManager platformTransactionManager){
        return new SpringJdbcOutbox(dataSource, objectMapper, platformTransactionManager);
    }

}
```

Using this implementation a outbox table must be provided in the database like that (example is for H2 database, a corresponding schema must be provided by the developers for other database technologies):
```SQL
CREATE TABLE IF NOT EXISTS outbox ( 
    id                  VARCHAR(36) PRIMARY KEY,
    domain_event        VARCHAR(8000) NOT NULL,
    inserted            TIMESTAMP NOT NULL,
    batch_id            VARCHAR(36) NULL,
    processing_result   VARCHAR(30) NULL,
    delivery_started    TIMESTAMP NULL
);
```

The outbox table name might be customized via ``setOutboxTableName(String tableName)`` of ``io.domainlifecycles.events.publish.outbox.impl.SpringJdbcOutbox``. 
Have a look at the implementation and Javadoc of this class to gain more insights in other configuration options (timeout settings, batch sizes, etc).
Performance improvements might be gained by:
- ``outbox.setStrictBatchOrder(false);`` 
- or adjusting the polling configuration ``outboxPoller.setPeriod(x);`` or ``outboxPoller.setMaxBatchSize(y);``

<a name="typical-customizations"></a>
##### Typical customizations
A typical customization might consist of plugging in a third-party message bus for DomainEvent transport in a distributed setup (e.g. microservices).

###### Third party message bus DomainEvent publishing
A third party event bus might be used with or without an Outbox involved. If the event bus supports 2PC / XA transactions, the guarantees of an outbox can be achieved by just using 2PC.

To route DomainEvents from an Outbox to a message bus, consider extending ``io.domainlifecycles.events.publish.outbox.poll.AbstractOutboxPoller``.
To route DomainEvents without using an Outbox to a message bus, consider implementing a custom ``io.domainlifecycles.events.publish.DomainEventPublisher``. 
Those custom implementations must be configured via ``io.domainlifecycles.events.api.DomainEventsConfiguration`` to activate them.

###### Third party message bus Domain Event consumption

To consume DomainEvents from a third party event bus and route them directly to handler services or Aggregates as described before, consider implementing a 
message bus technology dependent consumer service and then provide those events directly to the configured ``io.domainlifecycles.events.receive.execution.ReceivingDomainEventHandler`` via it's
``handleReceived()`` method. The ReceivingDomainEventHandler instance could be acquired via a ``io.domainlifecycles.events.api.DomainEventsConfiguration`` instance. 

#### Typical Spring Boot setup

```Java
@Configuration
public class DLCDomainEventsConfiguration {

    @Bean
    public ServiceProvider serviceProvider(
            List<Repository<?,?>> repositories, 
            List<ApplicationService> applicationServices, 
            List<DomainService> domainServices,
            List<QueryClient<?>> queryClients,
            List<OutboundService> outboundServices
    ){
        var services = new Services();
        repositories.forEach(services::registerRepositoryInstance);
        applicationServices.forEach(services::registerApplicationServiceInstance);
        domainServices.forEach(services::registerDomainServiceInstance);
        queryClients.forEach(services::registerQueryClientInstance);
        outboundServices.forEach(services::registerOutboundServiceInstance);
        return services;
    }

    @Bean
    public DomainEventsConfiguration domainEventsConfiguration(ServiceProvider serviceProvider, PlatformTransactionManager transactionManager) {
        return new DomainEventsConfiguration.DomainEventsConfigurationBuilder()
            .withServiceProvider(serviceProvider)
            .withSpringPlatformTransactionManager(transactionManager)
            .make();
    }

}
```






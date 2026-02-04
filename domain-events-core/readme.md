## DLC DomainEvents

DLC Domain Events provides functionality to simplify some of the technical aspects regarding DomainEvents:
- In order to keep the core implementation of a bounded context free of technical concerns DLC Domain Events provides two simple
interfaces for publishing or listening on Domain Events.
- DLC provides rich configuration options 
  - to align DomainEvent handling according to transactional boundaries
  - to plug in technical message bus providers
  - to route different Domain Event types via different technical message bus implementations

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

DLC DomainEvents also provides options to customize or enhance to technical event publishing mechanisms, see [Configuration](#Configuration).

Additionally, DLC provides the option to add metadata annotations on methods that publish DomainEvents (``@Publishes``, see io.domainlifecycles.domain.types.Publishes). 
Those annotations have no functional impact, but they currently make it more transparent to developers, where DomainEvents are published. The ``@Publishes`` metadata 
information is also rendered in DLC domain diagrams (see [DLC Domain Diagrammer](../readme_diagrammer.md))  

<a name="listening"></a>
#### Listening to DomainEvents
Domain Events are typically listened to by ApplicationServices, DomainServices, Repositories, OutboundServices, QueryHandlers or directly by Aggregate instances. 
DLC reduces the technical boilerplate normally needed to route consumed DomainEvents to the addressed handler methods.
Therefor the annotation ``@DomainEventListener`` (see ``io.domainlifecycles.domain.types.DomainEventListener``) is used.

Every method (inside an ApplicationService, a DomainService, an OutboundService, a QueryHandler or a Repository) that has only one parameter (the consumed DomainEvent) and which is annotated with
```@DomainEventListener``` is automatically called.

Depending on the transaction configuration of DLC DomainEvents, every handler call might also be automatically wrapped in an independent transaction.

Example of a service event handler methods (applicable on ApplicationServices (aka Drivers), DomainServices, OutboundServices, QueryHandlers or Repositories):
```Java
public class CustomerNotificationDriver implements Driver {
    ...
    @DomainEventListener
    public void notifyOrderShipped(OrderShipped orderShipped){
        ...
    }

    @DomainEventListener
    public void notifyOrderCanceled(OrderCanceled orderCanceled){
        ...
    }

    @DomainEventListener
    public void notifyNewOrderPlaced(NewOrderPlaced newOrderPlaced){
        ...
    }
}
```

To route DomainEvents directly to an Aggregate instance, the DomainEvent must implement a special interface ``io.domainlifecycles.domain.types.AggregateDomainEvent``.
The interface requires the implementor to provide the ``targetId`` of the target AggregateRoot. In this case fetching the AggregateRoot instance by the specified targetId 
from it's corresponding Repository and calling the event handler method directly on the AggregateRoot instance is done directly by the DLC framework in the background.
The complete described operation is done in a separate transaction and the Aggregate instance is finally updated using its repository. 

This case requires a transactional setup of DLC DomainEvents!

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
    @DomainEventListener
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

#### Spring Integration
The default configuration of DLC DomainEvents in a Spring application uses the Spring event bus for publishing and consuming DomainEvents.
See [DLC Spring Bus Domain Events integration](../domain-events-spring-bus/readme.md) 

The other configuration option supported by [DLC Spring Boot autoconfiguration](../dlc-spring-boot-autoconfig/readme.md) uses the DLC in memory event channel.

#### Other advanced Configuration Options
The configuration of the technical messaging infrastructure typically is done by creating an instance of the ``io.domainlifecycles.events.api.ChannelFactory``class. 
The Channel Factory provides methods for creating Channels (see below [Channels](#Channels))
Domain Events of different types could be processed by different channels. 
On the publishing side an implementation of ``io.domainlifecycles.events.api.PublishingRouter`` decides, which Domain Event is published to which Channel.
The default implementation ``io.domainlifecycles.events.api.DomainEventTypeBasedRouter`` decides this based on the published Domain Event type.

A typical configuration might look like this (Spring based example):
```Java

    @Bean
    public ChannelRoutingConfiguration channelConfiguration(PlatformTransactionManager platformTransactionManager, ServiceProvider serviceProvider){
        //The channel created by this factory publishes Domain Events bound to Spring transactions on the publisher side.
        //Domain Events are published before commit only if the transaction is ready to commit (not if it fails before)
        //Domain Events are provided in memory to the listeners (in this case no external event bus is used)
        var channel = new SpringTxInMemoryChannelFactory(platformTransactionManager, serviceProvider, false).processingChannel("c1");

        var router = new DomainEventTypeBasedRouter(List.of(channel));
        //The channel named 'c1' is the default channel. All Domain Events in the application are processed by this routing configuration
        router.defineDefaultChannel("c1");
        return new ChannelRoutingConfiguration(router);
    }
```

For the automatic routing functionality on the listener side an instance of 
a ``io.domainlifecycles.services.ServiceProvider`` must be provided 
(typically ``io.domainlifecycles.services.Services`` is used).

##### Channels

A ChannelFactory can typically create 3 types of channels:
- <b>PublishingOnlyChannel</b>: Domain Events processed by that type of channel are typically routed to an external message bus, 
  where another application (another deployment unit) consumes these Domain Events from
- <b>ConsumingOnlyChannel</b>: This is a channel configuration which only takes care of consuming of externally 
  provided Domain Events
- <b>ProcessingChannel</b>: A processing channel handles the publishing and consuming of Domain Events processed within
  the same application (deployment unit) 

It is possible to define multiple different channels, which use a different technical messaging infrastructure.
For example, we could have a situation where some of the Domain Events are processed within the same application 
(so-called internal Domain Events) and others, that are especially targeted to inform
another application or for example a specific microservice (so-called external Domain Events). 
To have a reliable handling of internal Domain Events, one might decide to use the transactional outbox 
implementation provided by [Gruelbox](https://github.com/gruelbox/transaction-outbox) or [Spring Modulith Events](https://github.com/spring-projects/spring-modulith/tree/main/spring-modulith-events).
To publish externally handled Domain Events, one might define a dedicated channel processing those events 
using a JMS compliant message broker.

In order to use multiple channels, a channel routing configuration is needed to decide which channel should be used in which case.
DLC provides a ```DomainEventTypeBasedRouter```, that decides the channel, that domain event are published to by checking 
the DomainEvent type (see example configuration below).

An example for event processing with multiple channels using Spring:
```Java
    @Bean
    public ProcessingChannel inMemoryChannel(InMemoryChannelFactory factory){
        return factory.processingChannel("inMemory");
    }
    
    @Bean
    public PublishingChannel springChannel(SpringApplicationEventsPublishingChannelFactory factory){
        return factory.publishOnlyChannel("springTx");
    }
    
    @Bean
    public PublishingRouter router(List<PublishingChannel> channels ){
        var router = new DomainEventTypeBasedRouter(channels);
        router.defineDefaultChannel("inMemory");
        router.defineExplicitRoute(AnAggregateDomainEvent.class, "springTx");
        return router;
    }
```

<a name="non-transactional"></a>
###### Non-transactional handling (in-memory only)
DomainEvents are simply routed in process to the corresponding handlers. DomainEvent loss and DomainEvent ghost messages are possible due to the missing 
transaction support. The consuming handlers are synchronously joining the event publishers transaction.
Processing failures in the handlers are reported via log (SLF4J). No automatic retry is in place.

Example setup:
```Java
    //The Services instance must register all services that contain event handler methods
    var services = new Services();
    services.registerServiceKindInstance(domainService);
    services.registerServiceKindInstance(repository);
    services.registerServiceKindInstance(applicationService);
    services.registerServiceKindInstance(queryHandler);
    services.registerServiceKindInstance(outboundService);
    
    //In memory only a processing channel can be defined
    var inMemoryChannel = new InMemoryChannelFactory(services).processingChannel("default");
    var router = new DomainEventTypeBasedRouter(List.of(inMemoryChannel));
    //all events, that have no special routing declaration are routed to the default channel
    router.defineDefaultChannel("default");
    // by instantiating this routing configuration, the channel is active
    new ChannelRoutingConfiguration(router);
```

Using Spring there is an auto-configuration provided for the configuration above (see [below](#spring-auto-configuration)).

##### Advanced Configuration options
The advanced configurations of DLC DomainEvents are:
- [Transactional setup using a transactional outbox by Gruelbox](../domain-events-gruelbox/readme.md)
- [Using an external ActiveMQ5 message broker](../domain-events-activemq-classic5/readme.md)
- [Using a Jakarta JMS 3.0 message broker](../domain-events-jakarta-jms/readme.md)
- [Transactional setup using JTA](../domain-events-jakarta-jta/readme.md)
Those are not supported by DLCs Sprng Boot auto-configuration or the DLC Spring Boot starter.

<a name="spring-auto-configuration"></a>
##### Spring Auto-Configuration
For DLC Domain Events there 2 [auto configurations](../dlc-spring-boot-autoconfig/readme.md) available:
- io.domainlifecycles.autoconfig.configurations.DlcNoTxInMemoryDomainEventsAutoConfiguration:
- io.domainlifecycles.autoconfig.configurations.DlcSpringBusDomainEventsAutoConfiguration
  - Enabled by default, disable by using ``@EnableDlc(exclude="DlcSpringBusDomainEventsAutoConfiguration.class")``


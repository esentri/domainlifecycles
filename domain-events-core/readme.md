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
Therefor the annotation ``@ListensTo`` (see io.domainlifecycles.domain.types.ListensTo) is used.

Every method (inside an ApplicationService, a DomainService, an OutboundService, a QueryHandler or a Repository) that has only one parameter (the consumed DomainEvent) and which is annotated with
```@ListensTo``` is automatically called, when a new DomainEvent is consumed by the central DLC event consumer (see ``io.domainlifecycles.events.consume.DomainEventConsumer``).

Depending on the transaction configuration of DLC DomainEvents, every handler call might also be automatically wrapped in an independent transaction.

Example of a service event handler methods (applicable on ApplicationServices (aka Drivers), DomainServices, OutboundServices, QueryHandlers or Repositories):
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

#### Channels

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
implementation provided by [Gruelbox](https://github.com/gruelbox/transaction-outbox).
To publish externally handled Domain Events, one might define a dedicated channel processing those events 
using a JMS compliant message broker.

As in case of Gruelbox or MQ based Domain Event processing, some channel rely on external resources 
(database tables, a polling thread or message brokers). So these channels offer a ```shutdown()``` hook 
for proper releasing of those resources. See the configuration example below using the Spring bean destroy method configured as 
```shutdown```.

An example above described event processing is configured like that:
```Java

    // We need a DomainEventsInstantiator for the outbox
    @Bean
    public DomainEventsInstantiator domainEventsInstantiator(){
        return new DomainEventsInstantiator();
    }
    
    //This is an outbox configuration, which not DLC specific for the most parts
    // DLC specific is only the DomainEventsInstantiator provided
    @Bean
    public TransactionOutbox transactionOutbox(
        SpringTransactionManager springTransactionManager,
        ObjectMapper objectMapper,
        DomainEventsInstantiator domainEventsInstantiator
    ) {
        return TransactionOutbox.builder()
            .instantiator(domainEventsInstantiator)
            .transactionManager(springTransactionManager)
            .blockAfterAttempts(3)
            .persistor(DefaultPersistor.builder()
                           .serializer(JacksonInvocationSerializer.builder().mapper(objectMapper).build())
                           .dialect(Dialect.H2)
                           .build())
            .build();
    }

    //The channel factory used to create the 'internal' channel used for all internal Domain Events
    @Bean
    public GruelboxChannelFactory gruelboxChannelFactory(
        ServiceProvider serviceProvider,
        TransactionOutbox transactionOutbox,
        TransactionalHandlerExecutor transactionalHandlerExecutor,
        DomainEventsInstantiator domainEventsInstantiator
    ){
        return new GruelboxChannelFactory(
            serviceProvider,
            transactionOutbox,
            transactionalHandlerExecutor,
            domainEventsInstantiator
        );
    }

    //The internal channel uses the outbox as technical infrastructure
    //The outbox ensures proper transactional event processing, 
    // avoiding ghost events or lost events 
    @Bean(destroyMethod = "close")
    public GruelboxProcessingChannel internalChannel(
        GruelboxChannelFactory gruelboxChannelFactory
    ){
        return gruelboxChannelFactory.processingChannel("internal");
    }

    //This channel factory is used to create the external channel, routing event via JMS 
    // (using the Active MQ implementation of a Jakarta JMS compliant message broker )
    @Bean
    @DependsOn("domainModel") 
    //@DependsOn: Depending on the rest of the application config it's sometimes necessary to make sure the 
    //Domain mirror is initialized before
    public SpringtransactionJakartaJmsChannelFactory springtransactionJakartaJmsChannelFactory(
            ActiveMQConnectionFactory jmsConnectionFactory,
            ObjectMapper objectMapper
    ){
        return new SpringtransactionJakartaJmsChannelFactory(
                jmsConnectionFactory,
                objectMapper
        );
    }

    //The external channel uses a JMS broker as technical infrastructure
    @Bean(destroyMethod = "close")
    public PublishingChannel externalChannel(
            SpringtransactionJakartaJmsChannelFactory springtransactionJakartaJmsChannelFactory
    ){
        return springtransactionJakartaJmsChannelFactory.publishOnlyChannel("external");
    }

    //By this routing configuration is declared which event type is routed to which channel and 
    //its underlying messaging infrastructure
    @Bean
    public ChannelRoutingConfiguration channelConfiguration(List<PublishingChannel> publishingChannels){
        var router = new DomainEventTypeBasedRouter(publishingChannels);
        router.defineDefaultChannel("internal");
        router.defineExplicitRoute( FirstPublicDomainEvent.class, "external");
        router.defineExplicitRoute( SecondPublicDomainEvent.class, "external");
        return new ChannelRoutingConfiguration(router);
    }
    
    //The transactional handler executor is used to wrap event handler in separate transaction, so that the result of each
    //handler execution is transactionally independent of each other
    @Bean
    public TransactionalHandlerExecutor transactionalHandlerExecutor(PlatformTransactionManager platformTransactionManager){
        return new SpringTransactionalHandlerExecutor(platformTransactionManager);
    }
```

##### Out-of-the-box configuration options

The out-of-the-box configurations of DLC DomainEvents are:

- [Non-transactional handling](#non-transactional) 
- [Transactional setup without a transactional outbox](#transactional-without-outbox)
- [Transactional setup using a transactional outbox](#transactional-with-outbox)
- [Transactional setup using an external message broker](#transactional-message-broker)
- [Transactional setup using a transactional outbox proxying an external message broker](#transactional-outbox-proxying-message-broker)

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

<a name="transactional-without-outbox"></a>
###### Transactional setup without a transactional outbox
In this case DomainEvents are published "after commit". So "ghost messages" are avoided. Ghost messages are DomainEvents, 
that are published, but the transaction of the operation that issued the DomainEvent might still be rolled back. 
The loss of DomainEvents using this implementation are rare cases but technically possible. This setup routes 
DomainEvents directly to consuming handlers. The consuming handlers are executed asynchronously in separate transactions.
Processing failures in the handlers are reported via log (SLF4J). No automatic retry is in place.

Example setup with Spring transactions:
```Java
    var services = new Services();
    ...
    var channel = new SpringTxInMemoryChannelFactory(
        platformTransactionManager, 
        services,
        true
        )
        .processingChannel("c1");
    
    var router = new DomainEventTypeBasedRouter(List.of(channel));
    router.defineDefaultChannel("c1");
    new ChannelRoutingConfiguration(router);
```

Example setup with JTA provided transactions:
```Java
    var services = new Services();
    ...
    var channel = new JtaInMemoryChannelFactory(
        userTransactionManager, 
        services,
        true
        )
        .processingChannel("c1");
    
    var router = new DomainEventTypeBasedRouter(List.of(channel));
    router.defineDefaultChannel("c1");
    new ChannelRoutingConfiguration(router);
```
<a name="transactional-with-outbox"></a>
###### Transactional setup with a transactional outbox
Adding a transactional outbox avoids DomainEvent loss as well as ghost events on the publishing side. 
The setup involves an "outbox" database table, but it's a reliable way of not loosing any DomainEvents 
and comes into play, if a relational database is involved as a part of the main technical infrastructure.
The main disadvantage of that setup is a reduced throughput in high volume scenarios due to the database involvement. 

For more information on the "transactional outbox pattern" have a look at [Transactional Outbox Pattern](https://microservices.io/patterns/data/transactional-outbox.html)

DLC works fine with [Gruelbox](https://github.com/gruelbox/transaction-outbox), a very flexible and reliable transactional outbox implementation.

###### DLC Outbox (Deprecated, use gruelbox instead)
This implementation relies heavily on Spring transactions, Spring JDBC and Jackson, see ``io.domainlifecycles.events.publish.outbox.impl.SpringJdbcOutbox``.

Example setup with the DLC provided Spring based outbox implementation is shown below. This setup routes polled DomainEvents from the outbox directly to consuming handlers 
within the pollers process, the consuming handlers are executed asynchronously in separate transactions. If DomainEvents were processed successfully by the handlers,
this is acknowledged to the outbox directly. So monitoring the state of event delivery is possible on the outbox. No automatic retry is in place.
Processing failures in the handlers are also reported via log (SLF4J).
```Java
    @SpringBootApplication
    public class OutboxTestApplication {
       ...
        @Bean
        public ServiceProvider serviceProvider(
           ...
        ){
            ...
            return services;
        }
    
        @Bean
        public SpringTransactionalOutboxChannelFactory springOutbox(DataSource dataSource,
                                                                    ObjectMapper objectMapper,
                                                                    PlatformTransactionManager platformTransactionManager,
                                                                    ServiceProvider serviceProvider) {
            return  new SpringTransactionalOutboxChannelFactory(
                platformTransactionManager,
                objectMapper,
                dataSource,
                serviceProvider
            );
        }
    
        @Bean
        public ProcessingChannel channel(SpringTransactionalOutboxChannelFactory factory){
            return factory.processingChannel("channel");
        }
    
        @Bean
        public ChannelRoutingConfiguration channelConfiguration(List<PublishingChannel> publishingChannels){
            var router = new DomainEventTypeBasedRouter(publishingChannels);
            router.defineDefaultChannel("channel");
            return new ChannelRoutingConfiguration(router);
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

###### Gruelbox Transaction Outbox

The [Gruelbox Transaction Outbox](https://github.com/gruelbox/transaction-outbox) is integrated with DLC and support a broad spectrum of
databases, transaction management and dependency injection frameworks.

The setup is done by configuring the ``com.gruelbox.transactionoutbox.TransactionOutbox`` instance as well as 
the ``io.domainlifecycles.events.gruelbox.api.GruelboxChannelFactory``. Finally, the channel routing configuration is done like the common
DLC Domain Events configuration.

A Spring based example using Gruelbox as messaging infrastructure:
```Java
    // We need a DomainEventsInstantiator for the outbox
    // It is needed for creating service instances when processing outbox DomainEvents
    @Bean
    public DomainEventsInstantiator domainEventsInstantiator(){
        return new DomainEventsInstantiator();
    }
    
    // A TransactionalHandlerExecutor is used to wrap all listener executions in independent 
    // transactions
    @Bean
    public TransactionalHandlerExecutor transactionalHandlerExecutor(PlatformTransactionManager platformTransactionManager){
        return new SpringTransactionalHandlerExecutor(platformTransactionManager);
    }
    
    //Here we can configure the outbox
    @Bean
    public TransactionOutbox transactionOutbox(
        SpringTransactionManager springTransactionManager,
        ObjectMapper objectMapper,
        DomainEventsInstantiator domainEventsInstantiator
    ) {
        return TransactionOutbox.builder()
            .instantiator(domainEventsInstantiator)
            .transactionManager(springTransactionManager)
            .blockAfterAttempts(3)
            .persistor(DefaultPersistor.builder()
                           .serializer(JacksonInvocationSerializer.builder().mapper(objectMapper).build())
                           .dialect(Dialect.H2)
                           .build())
            .build();
    }

    //We configure the channel factory, to be able to create Channels using the
    //the outbox
    @Bean
    public GruelboxChannelFactory gruelboxChannelFactory(
        ServiceProvider serviceProvider,
        TransactionOutbox transactionOutbox,
        TransactionalHandlerExecutor transactionalHandlerExecutor,
        DomainEventsInstantiator domainEventsInstantiator
    ){
        return new GruelboxChannelFactory(
            serviceProvider,
            transactionOutbox,
            transactionalHandlerExecutor,
            domainEventsInstantiator
        );
    }
    
    //Create a ProcessingChannel named 'default'
    @Bean(destroyMethod = "close")
    public GruelboxProcessingChannel defaultChannel(GruelboxChannelFactory gruelboxChannelFactory){
        return gruelboxChannelFactory.processingChannel("default");
    }

    //Route all Domain Events of the application to the default channel
    @Bean
    public ChannelRoutingConfiguration channelConfiguration(List<PublishingChannel> publishingChannels){
        var router = new DomainEventTypeBasedRouter(publishingChannels);
        router.defineDefaultChannel("default");
        return new ChannelRoutingConfiguration(router);
    }

```

Here's another Spring based example of using Gruelbox in combination with idempotency protection.
```Java
@Configuration
@Import({SpringTransactionManager.class})
@Slf4j
public class GruelboxIntegrationIdempotencyConfig {

    //Here we can configure the outbox 
    @Bean
    public TransactionOutbox transactionOutbox(
        SpringTransactionManager springTransactionManager,
        ObjectMapper objectMapper,
        DomainEventsInstantiator domainEventsInstantiator
    ) {
        return TransactionOutbox.builder()
            .instantiator(domainEventsInstantiator)
            .transactionManager(springTransactionManager)
            .blockAfterAttempts(3)
            .persistor(DefaultPersistor.builder()
                           .serializer(JacksonInvocationSerializer.builder().mapper(objectMapper).build())
                           .dialect(Dialect.H2)
                           .build())
            .build();
    }

    //Create a ProcessingChannel named 'c1'
    @Bean(destroyMethod = "close")
    public GruelboxProcessingChannel gruelboxChannel(
        ServiceProvider serviceProvider,
        TransactionOutbox transactionOutbox,
        TransactionalHandlerExecutor transactionalHandlerExecutor,
        DomainEventsInstantiator domainEventsInstantiator,
        IdempotencyConfiguration idempotencyConfiguration
    ){
        return new GruelboxChannelFactory(
            serviceProvider,
            transactionOutbox,
            transactionalHandlerExecutor,
            domainEventsInstantiator,
            idempotencyConfiguration
        ).processingChannel("c1");
    }

    //Route all Domain Events of the application to the default channel 'c1'
    @Bean
    public ChannelRoutingConfiguration channelConfiguration(List<PublishingChannel> publishingChannels){
        var router = new DomainEventTypeBasedRouter(publishingChannels);
        router.defineDefaultChannel("c1");
        return new ChannelRoutingConfiguration(router);
    }

    // We need a DomainEventsInstantiator for the outbox
    // It is needed for creating service instances when processing outbox DomainEvents
    @Bean
    public DomainEventsInstantiator domainEventsInstantiator(){
        return new DomainEventsInstantiator();
    }

    // A TransactionalHandlerExecutor is used to wrap all listener executions in independent 
    // transactions
    @Bean
    public TransactionalHandlerExecutor transactionalHandlerExecutor(PlatformTransactionManager platformTransactionManager){
        return new SpringTransactionalHandlerExecutor(platformTransactionManager);
    }
    
    //The idempotency protection defines which listener operation should be protected, 
    // to avoid event duplicates being processed multiple times
    @Bean
    public IdempotencyConfiguration idempotencyConfiguration(){
        var config = new IdempotencyConfiguration();
        config.addConfigurationEntry(new IdempotencyConfigurationEntry(IdemProtectedListener.class, "handle", IdemProtectedDomainEvent.class, (e)-> ((IdemProtectedDomainEvent)e).id()));
        return config;
    }

}
```


<a name="transactional-message-broker"></a>
###### Transactional setup using an external message broker

In some cases we want to publish Domain Events to an external message broker. DLC supports 
ActiveMq Classic 5 (which is not fully Jakarta JMS compliant, but supported by AWS MQ) as well as Jakarta JMS 3.0 message brokers.

In both cases a transactional setup and a non-transactional setup ist supported. The transactional setup
is recommended. Domain Events in this case are published just before or after the database transaction commits.
So, the probability of ghost events or lost events is reduced. But it's not erased in all cases. To make sure to avoid 
ghost events or lost events in any case, you can use a transactional outbox proxying an external message broker (see below).

With external message brokers Domain Events get published to topics. Each Domain Event type is published to a separate topic. 
On the consumer side, multiple instances of the same kind of handlers might form a consumer group. The Domain Events on a topic 
are shared between all handlers of the same consumer group (loadbalancing). Different kinds of handlers are 
guaranteed to receive a 'copy' of each Domain Event instance of the corresponding topic. With Active MQ 5 Classic, we use the [Virtual Topic feature](https://activemq.apache.org/components/classic/documentation/virtual-destinations) to
achieve this behaviour. With Jakarta JMS 3.0 or above we use so-called [shared subscriptions](https://jakarta.ee/learn/docs/jakartaee-tutorial/current/messaging/jms-concepts/jms-concepts.html).

Currently, the transactional setup with external message brokers relies on Spring transactions.

Example setup of Active Mq 5 classic in a transactional setup:
```Java
    // Connection Factory to connect to the broker
    @Bean
    public ConnectionFactory connectionFactory() {
        return new ActiveMQConnectionFactory(...);
    }
    
    // Our channel factory requires a Class Provider
    @Bean
    public ClassProvider classProvider(){
        return new DefaultClassProvider();
    }

    // A TransactionalHandlerExecutor is used to wrap all listener executions in independent 
    // transactions
    @Bean
    public TransactionalHandlerExecutor transactionalHandlerExecutor(PlatformTransactionManager platformTransactionManager){
        return new SpringTransactionalHandlerExecutor(platformTransactionManager);
    }

    // The channel factory providing ActiveMq based channels
    @Bean
    public SpringTransactionalActiveMqChannelFactory springActiveMqChannelFactory(
        ServiceProvider serviceProvider,
        ClassProvider classProvider,
        TransactionalHandlerExecutor transactionalHandlerExecutor,
        ActiveMQConnectionFactory jmsConnectionFactory,
        ObjectMapper objectMapper
    ){
        return new SpringTransactionalActiveMqChannelFactory(
            jmsConnectionFactory,
            serviceProvider,
            classProvider,
            transactionalHandlerExecutor,
            objectMapper
        );
    }

    // Declaring the channel
    @Bean(destroyMethod = "close")
    public MqProcessingChannel channel(ActiveMqChannelFactory factory){
        return factory.processingChannel("activeMqTxChannel");
    }

    // Routing all Domain Events over the ActiveMq broker
    @Bean
    public ChannelRoutingConfiguration channelConfiguration(List<PublishingChannel> publishingChannels){
        var router = new DomainEventTypeBasedRouter(publishingChannels);
        router.defineDefaultChannel("activeMqTxChannel");
        return new ChannelRoutingConfiguration(router);
    }
    
```

Example setup of a Jakarta JMS broker in a transactional setup:
```Java

    // Our channel factory requires a Class Provider
    @Bean
    public ClassProvider classProvider(){
        return new DefaultClassProvider();
    }

    // A TransactionalHandlerExecutor is used to wrap all listener executions in independent 
    // transactions
    @Bean
    public TransactionalHandlerExecutor transactionalHandlerExecutor(PlatformTransactionManager platformTransactionManager){
        return new SpringTransactionalHandlerExecutor(platformTransactionManager);
    }
    
    // The channel factory providing Jakarta JMS based channels
    @Bean
    public SpringTransactionJakartaJmsChannelFactory springTransactionJakartaJmsChannelFactory(
        ServiceProvider serviceProvider,
        ClassProvider classProvider,
        TransactionalHandlerExecutor transactionalHandlerExecutor,
        ConnectionFactory jmsConnectionFactory,
        ObjectMapper objectMapper
    ){
        return new SpringTransactionJakartaJmsChannelFactory(
            jmsConnectionFactory,
            serviceProvider,
            classProvider,
            transactionalHandlerExecutor,
            objectMapper
        );
    }

    // Declaring the channel
    // registering a close listener for a correct tear down behaviour
    @Bean(destroyMethod = "close")
    public MqProcessingChannel channel(SpringtransactionJakartaJmsChannelFactory factory){
        return factory.processingChannel("jmsChannel");
    }

    // Routing all Domain Events over the JMS broker
    @Bean
    public ChannelRoutingConfiguration channelConfiguration(List<PublishingChannel> publishingChannels){
        var router = new DomainEventTypeBasedRouter(publishingChannels);
        router.defineDefaultChannel("jmsChannel");
        return new ChannelRoutingConfiguration(router);
    }
```


<a name="transactional-outbox-proxying-message-broker"></a>
###### Transactional setup using a transactional outbox proxying an external message broker

As described above using a message broker even in a transactional setup does not avoid ghost events or lost 
events in all cases. A solution for these kind of problems is to use a transactional outbox a proxy before 
publishing the events to an external message queue. With the outbox as a proxy, consumers are not polling the outbox, 
a dedicated outbox delivery service is reading those messages from the outbox table and sending them to the broker. 
This technique is not as performant as going directly to the broker regarding processing speed and scalability, but it is
a good tradeoff for the sake of application consistency.

Here's an example configuration for a Gruelbox based outbox proxy with a JMS broker behind the outbox:
```Java
    // Our channel factory requires a Class Provider
    @Bean
    public ClassProvider classProvider(){
        return new DefaultClassProvider();
    }

    // A TransactionalHandlerExecutor is used to wrap all listener executions in independent 
    // transactions
    @Bean
    public TransactionalHandlerExecutor transactionalHandlerExecutor(PlatformTransactionManager platformTransactionManager){
        return new SpringTransactionalHandlerExecutor(platformTransactionManager);
    }

    // Needed for wiring the outbox with the MQ domain event publisher
    @Bean
    public DomainEventsInstantiator domainEventsInstantiator(){
        return new DomainEventsInstantiator();
    }

    // the outbox 
    @Bean
    public TransactionOutbox transactionOutbox(
            SpringTransactionManager springTransactionManager,
            ObjectMapper objectMapper,
            DomainEventsInstantiator domainEventsInstantiator,
            TransactionOutboxListener transactionOutboxListener
    ) {
        return TransactionOutbox.builder()
                .instantiator(domainEventsInstantiator)
                .transactionManager(springTransactionManager)
                .blockAfterAttempts(3)
                .persistor(DefaultPersistor.builder()
                        .serializer(JacksonInvocationSerializer.builder().mapper(objectMapper).build())
                        .dialect(Dialect.H2)
                        .build())
                .listener(transactionOutboxListener)
                .build();
    }

    // The channel factory providing Jakarta JMS based channels with a Gruelbox based outbox ensuring
    // correct transactional sending behaviour
    @Bean
    public GruelboxProxyJakartaJmsChannelFactory gruelboxProxyActiveMqChannelFactory(
            ServiceProvider serviceProvider,
            ClassProvider classProvider,
            TransactionalHandlerExecutor transactionalHandlerExecutor,
            ConnectionFactory jmsConnectionFactory,
            ObjectMapper objectMapper,
            TransactionOutbox transactionOutbox,
            DomainEventsInstantiator domainEventsInstantiator
    ){
        return new GruelboxProxyJakartaJmsChannelFactory(
                serviceProvider,
                classProvider,
                transactionalHandlerExecutor,
                objectMapper,
                transactionOutbox,
                domainEventsInstantiator,
                jmsConnectionFactory
        );
    }

    // Declaring the channel
    // registering a close listener for a correct tear down behaviour
    @Bean(destroyMethod = "close")
    public MqProcessingChannel channel(GruelboxProxyJakartaJmsChannelFactory factory){
        return factory.processingChannel("gruelboxJmsChannel");
    }

    // Routing all Domain Events to the outbox and from there to the JMS broker
    @Bean
    public ChannelRoutingConfiguration channelConfiguration(List<PublishingChannel> publishingChannels){
        var router = new DomainEventTypeBasedRouter(publishingChannels);
        router.defineDefaultChannel("gruelboxJmsChannel");
        return new ChannelRoutingConfiguration(router);
    }
    
```

The Gruelbox supports also some kind of idempotency protection. Idempotency protection for Domain Events prevents
Domain Event duplicates from being processed multiple times. JMS message brokers normally only 
give at-least-once processing guarantees. That means, that in some cases it's possible that a message handler 
receives the same Domain Event twice or multiple times. If the handler behaves naturally idempotent, this is no problem.
Naturally idempotent means, in the end we have the same final state in our system, regardless weather the Domain Event 
was processed once or multiple times.

Whenever we have situations, where we have no naturally idempotent behaviour, we can use the Gruelbox to
enforce idempotency on specific handlers. 

Here's an example configuration, with idempotency protection applied:
```Java
@Configuration
@Slf4j
@EnableJms
@Import({SpringTransactionManager.class})
public class JakartaJmsGruelboxIdempotencyConfig {

    //The idempotency protection defines which listener operation should be protected, 
    // to avoid event duplicates being processed multiple times
    @Bean
    @DependsOn("domainModel")
    public IdempotencyConfiguration idempotencyConfiguration() {
        var idempotency = new IdempotencyConfiguration();
        idempotency
                .addConfigurationEntry(
                        new IdempotencyConfigurationEntry(
                                ADomainService.class,
                                "onDomainEvent",
                                ADomainEvent.class,
                                (e) -> ((ADomainEvent) e).message()
                        )
                );
        return idempotency;
    }

    // Needed for wiring the outbox with the MQ domain event publisher
    @Bean
    public DomainEventsInstantiator domainEventsInstantiator() {
        return new DomainEventsInstantiator();
    }

    // the outbox 
    @Bean
    public TransactionOutbox transactionOutbox(
            SpringTransactionManager springTransactionManager,
            ObjectMapper objectMapper,
            DomainEventsInstantiator domainEventsInstantiator
    ) {
        return TransactionOutbox.builder()
                .instantiator(domainEventsInstantiator)
                .transactionManager(springTransactionManager)
                .blockAfterAttempts(3)
                .persistor(DefaultPersistor.builder()
                        .serializer(JacksonInvocationSerializer.builder().mapper(objectMapper).build())
                        .dialect(Dialect.H2)
                        .build())
                .build();
    }

    //plugging in idempotency protection for configured consumers
    @Bean
    public SpringTransactionalIdempotencyAwareHandlerExecutorProxy springTransactionalIdempotencyAwareHandlerExecutorProxy(
            TransactionalHandlerExecutor transactionalHandlerExecutor,
            IdempotencyConfiguration idempotencyConfiguration,
            TransactionOutbox transactionOutbox,
            PlatformTransactionManager platformTransactionManager


    ) {
        return new SpringTransactionalIdempotencyAwareHandlerExecutorProxy(
                transactionalHandlerExecutor,
                idempotencyConfiguration,
                transactionOutbox,
                platformTransactionManager
        );

    }

    // The channel factory providing Jakarta JMS based channels with a Gruelbox based outbox ensuring
    // correct transactional sending behaviour. The outbox in this case also is used to protect against event duplicates
    // causing problem is non-idempotent consumers
    @Bean
    public GruelboxProxyJakartaJmsChannelFactory gruelboxProxyActiveMqChannelFactory(
            ServiceProvider serviceProvider,
            ClassProvider classProvider,
            TransactionalHandlerExecutor transactionalHandlerExecutor,
            ConnectionFactory jmsConnectionFactory,
            ObjectMapper objectMapper,
            TransactionOutbox transactionOutbox,
            DomainEventsInstantiator domainEventsInstantiator,
            SpringTransactionalIdempotencyAwareHandlerExecutorProxy springTransactionalIdempotencyAwareHandlerExecutorProxy
    ) {
        return new GruelboxProxyJakartaJmsChannelFactory(
                serviceProvider,
                classProvider,
                transactionalHandlerExecutor,
                objectMapper,
                transactionOutbox,
                domainEventsInstantiator,
                jmsConnectionFactory,
                springTransactionalIdempotencyAwareHandlerExecutorProxy
        );
    }

    // registering a close listener for a correct tear down behaviour
    @Bean(destroyMethod = "close")
    public MqProcessingChannel channel(GruelboxProxyJakartaJmsChannelFactory factory) {
        return factory.processingChannel("defaultChannel");
    }

    // Routing all Domain Events to the outbox and from there to the JMS broker
    @Bean
    public ChannelRoutingConfiguration channelConfiguration(List<PublishingChannel> publishingChannels) {
        var router = new DomainEventTypeBasedRouter(publishingChannels);
        router.defineDefaultChannel("defaultChannel");
        return new ChannelRoutingConfiguration(router);
    }

    // A TransactionalHandlerExecutor is used to wrap all listener executions in independent 
    // transactions
    @Bean
    public TransactionalHandlerExecutor transactionalHandlerExecutor(PlatformTransactionManager platformTransactionManager) {
        return new SpringTransactionalHandlerExecutor(platformTransactionManager);
    }

    // Our channel factory requires a Class Provider
    @Bean
    public ClassProvider classProvider() {
        return new DefaultClassProvider();
    }
}
```

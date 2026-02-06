## DLC GruelBox DomainEvents Integration
To have a reliable handling of internal Domain Events, one might decide to use the transactional outbox
implementation provided by [GruelBox](https://github.com/gruelbox/transaction-outbox).

This integration provides a factory for creation Gruelbox supported DomainEvent channels, that can be plugged in and being used via 
DLCs DomainEvent API.

### Usage
The API elements for publishing or listening to domain events are described [here](../domain-events-core/readme.md).
This integration is not included in the DLC Spring Boot starter and the [DLC Spring Boot autoconfiguration](../dlc-spring-boot-autoconfig/readme.md).

Adding a transactional outbox avoids DomainEvent loss as well as ghost events on the publishing side.
The setup involves an "outbox" database table, but it's a reliable way of not loosing any DomainEvents
and comes into play, if a relational database is involved as a part of the main technical infrastructure.
The main disadvantage of that setup is a reduced throughput in high volume scenarios due to the database involvement.

For more information on the "transactional outbox pattern" have a look at [Transactional Outbox Pattern](https://microservices.io/patterns/data/transactional-outbox.html)
### Configuration

DLC works fine with Gruelbox, a very flexible and reliable transactional outbox implementation.
The [Gruelbox Transaction Outbox](https://github.com/gruelbox/transaction-outbox) is integrated with DLC and support a broad spectrum of
databases, transaction management and dependency injection frameworks.

#### Dependency

**Gradle:**
```groovy
dependencies {
  implementation 'io.domainlifecycles:domain-events-gruelbox:3.0.0'
}
```

**Maven:**
```xml
<dependency>
    <groupId>io.domainlifecycles</groupId>
    <artifactId>domain-events-gruelbox</artifactId>
    <version>3.0.0</version>
</dependency>
```
#### Configuration

The setup is done by configuring the ``com.gruelbox.transactionoutbox.TransactionOutbox`` instance as well as
the ``io.domainlifecycles.events.gruelbox.api.GruelboxChannelFactory``. Finally, the channel routing configuration is done like the common
DLC Domain Events configuration:

1. Set up the channel factory ``GruelboxChannelFactory``
2. Configure the channel routing configuration like:
```Java
    var router = new DomainEventTypeBasedRouter(publishingChannels);
    router.defineDefaultChannel("default");
    return new ChannelRoutingConfiguration(router);
```

As in case of Gruelbox or MQ based Domain Event processing, some channel rely on external resources
(database tables, a polling thread or message brokers). So these channels offer a ```close()``` hook
for proper releasing of those resources. See the configuration example below using the Spring bean destroy method configured as
```close```.

#### Spring configuration example
A Spring based example using Gruelbox as messaging infrastructure:
```Java
@Configuration
@Import({SpringTransactionManager.class})
@Slf4j
public class GruelboxIntegrationConfig {
    // We need a DomainEventsInstantiator for the outbox
    // It is needed for creating service instances when processing outbox DomainEvents
    @Bean
    public DomainEventsInstantiator domainEventsInstantiator() {
        return new DomainEventsInstantiator();
    }

    // A TransactionalHandlerExecutor is used to wrap all listener executions in independent 
    // transactions
    @Bean
    public TransactionalHandlerExecutor transactionalHandlerExecutor(PlatformTransactionManager platformTransactionManager) {
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
                        .serializer(new DlcJacksonInvocationSerializer(objectMapper))
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
    ) {
        return new GruelboxChannelFactory(
                serviceProvider,
                transactionOutbox,
                transactionalHandlerExecutor,
                domainEventsInstantiator
        );
    }

    //Create a ProcessingChannel named 'default'
    @Bean(destroyMethod = "close")
    public GruelboxProcessingChannel defaultChannel(GruelboxChannelFactory gruelboxChannelFactory) {
        return gruelboxChannelFactory.processingChannel("default");
    }

    //Route all Domain Events of the application to the default channel
    @Bean
    public ChannelRoutingConfiguration channelConfiguration(List<PublishingChannel> publishingChannels) {
        var router = new DomainEventTypeBasedRouter(publishingChannels);
        router.defineDefaultChannel("default");
        return new ChannelRoutingConfiguration(router);
    }
}
```

Using Spring there is an auto-configuration provided for the configuration above (see [below](#spring-auto-configuration)).

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
                           .serializer(new DlcJacksonInvocationSerializer(objectMapper))
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

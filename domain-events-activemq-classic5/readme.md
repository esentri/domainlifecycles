## DLC Jakarta JMS DomainEvents Integration
To route Domain Events directly to an ActiveMQ Classic 5 broker use this integration.

This integration provides a factory for creation ActiveMQ Classic supported DomainEvent channels, that can be plugged in and being used via
DLCs DomainEvent API.

### Usage
The API elements for publishing or listening to domain events are described [here](../domain-events-core/readme.md).
This integration is not included in the DLC Spring Boot starter and the [DLC Spring Boot autoconfiguration](../dlc-spring-boot-autoconfig/readme.md).

### Configuration

#### Dependency

**Gradle:**
```groovy
dependencies {
  implementation 'io.domainlifecycles:domain-events-activemq-classic5:3.0.0'
}
```

**Maven:**
```xml
<dependency>
    <groupId>io.domainlifecycles</groupId>
    <artifactId>domain-events-activemq-classic5</artifactId>
    <version>3.0.0</version>
</dependency>
```
#### Configuration
The setup is done by configuring the ``io.domainlifecycles.events.activemq.api.ActiveMqChannelFactory``. Finally, the channel routing configuration is done like the common
DLC Domain Events configuration:

1. Set up the channel factory, e.g. ``ActiveMqChannelFactory`` or ``GruelboxProxyActiveMqChannelFactory`` or ``SpringTransactionalActiveMqChannelFactory``
2. Configure the channel routing configuration like:
```Java
    var router = new DomainEventTypeBasedRouter(publishingChannels);
    router.defineDefaultChannel("default");
    return new ChannelRoutingConfiguration(router);
```

#### Spring configuration examples
###### Transactional setup using an external message broker
DLC supports ActiveMq Classic 5 (which is not fully Jakarta JMS compliant, but supported by AWS MQ).

A transactional setup and a non-transactional setup ist supported. The transactional setup
is recommended. Domain Events in this case are published just before or after the database transaction commits.
So, the probability of ghost events or lost events is reduced. But it's not erased in all cases. To make sure to avoid
ghost events or lost events in any case, you can use a transactional outbox proxying an external message broker (see below).

With external message brokers Domain Events get published to topics. Each Domain Event type is published to a separate topic.
On the consumer side, multiple instances of the same kind of handlers might form a consumer group. The Domain Events on a topic
are shared between all handlers of the same consumer group (loadbalancing). Different kinds of handlers are
guaranteed to receive a 'copy' of each Domain Event instance of the corresponding topic. With Active MQ 5 Classic, we use the [Virtual Topic feature](https://activemq.apache.org/components/classic/documentation/virtual-destinations) to
achieve this behaviour. 

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

    @Bean
    public DomainEventSerializer domainEventSerializer(ObjectMapper objectMapper) {
        return new JacksonDomainEventSerializer(objectMapper);
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
        DomainEventSerializer domainEventSerializer
    ){
        return new SpringTransactionalActiveMqChannelFactory(
            jmsConnectionFactory,
            serviceProvider,
            classProvider,
            transactionalHandlerExecutor, 
            domainEventSerializer
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

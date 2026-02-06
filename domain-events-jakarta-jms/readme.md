## DLC Jakarta JMS DomainEvents Integration
To route Domain Events directly to a Jakarta JMS broker use this integration.

This integration provides a factory for creation JMS supported DomainEvent channels, that can be plugged in and being used via
DLCs DomainEvent API.

### Usage
The API elements for publishing or listening to domain events are described [here](../domain-events-core/readme.md).
This integration is not included in the DLC Spring Boot starter and the [DLC Spring Boot autoconfiguration](../dlc-spring-boot-autoconfig/readme.md).

### Configuration

#### Dependency

**Gradle:**
```groovy
dependencies {
  implementation 'io.domainlifecycles:domain-events-jakarta-jms:3.0.0'
}
```

**Maven:**
```xml
<dependency>
    <groupId>io.domainlifecycles</groupId>
    <artifactId>domain-events-jakarta-jms</artifactId>
    <version>3.0.0</version>
</dependency>
```
#### Configuration
The setup is done by configuring the ``io.domainlifecycles.events.jakarta.jms.api.JakartaJmsChannelFactory``. Finally, the channel routing configuration is done like the common
DLC Domain Events configuration:

1. Set up the channel factory, e.g. ``JakartaJmsChannelFactory`` or ``SpringTransactionJakartaJmsChannelFactory``
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

#### Spring configuration examples
###### Transactional setup using an external message broker
In some cases we want to publish Domain Events to an external message broker. DLC supports
Jakarta JMS 3.0 message brokers.

A transactional setup and a non-transactional setup ist supported. The transactional setup
is recommended. Domain Events in this case are published just before or after the database transaction commits.
So, the probability of ghost events or lost events is reduced. But it's not erased in all cases. To make sure to avoid
ghost events or lost events in any case, you can use a transactional outbox proxying an external message broker (see below).

With external message brokers Domain Events get published to topics. Each Domain Event type is published to a separate topic.
On the consumer side, multiple instances of the same kind of handlers might form a consumer group. The Domain Events on a topic
are shared between all handlers of the same consumer group (loadbalancing). Different kinds of handlers are
guaranteed to receive a 'copy' of each Domain Event instance of the corresponding topic. With Jakarta JMS 3.0 or above we use so-called [shared subscriptions](https://jakarta.ee/learn/docs/jakartaee-tutorial/current/messaging/jms-concepts/jms-concepts.html).

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

    @Bean
    public DomainEventSerializer domainEventSerializer(ObjectMapper objectMapper) {
        return new JacksonDomainEventSerializer(objectMapper);
    }
    
    // The channel factory providing Jakarta JMS based channels
    @Bean
    public SpringTransactionJakartaJmsChannelFactory springTransactionJakartaJmsChannelFactory(
        ServiceProvider serviceProvider,
        ClassProvider classProvider,
        TransactionalHandlerExecutor transactionalHandlerExecutor,
        ConnectionFactory jmsConnectionFactory,
        DomainEventSerializer domainEventSerializer
    ){
        return new SpringTransactionJakartaJmsChannelFactory(
            jmsConnectionFactory,
            serviceProvider,
            classProvider,
            transactionalHandlerExecutor, 
            domainEventSerializer
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
    
    @Bean
    public DomainEventSerializer domainEventSerializer(ObjectMapper objectMapper) {
        return new JacksonDomainEventSerializer(objectMapper);
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
                        .serializer(new DlcJacksonInvocationSerializer(objectMapper))
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
            DomainEventSerializer domainEventSerializer,
            TransactionOutbox transactionOutbox,
            DomainEventsInstantiator domainEventsInstantiator
    ){
        return new GruelboxProxyJakartaJmsChannelFactory(
                serviceProvider,
                classProvider,
                transactionalHandlerExecutor,
                domainEventSerializer,
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
                        .serializer(new DlcJacksonInvocationSerializer(objectMapper))
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
            DomainEventSerializer domainEventSerializer,
            TransactionOutbox transactionOutbox,
            DomainEventsInstantiator domainEventsInstantiator,
            SpringTransactionalIdempotencyAwareHandlerExecutorProxy springTransactionalIdempotencyAwareHandlerExecutorProxy
    ) {
        return new GruelboxProxyJakartaJmsChannelFactory(
                serviceProvider,
                classProvider,
                transactionalHandlerExecutor,
                domainEventSerializer,
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

    @Bean
    public DomainEventSerializer domainEventSerializer(ObjectMapper objectMapper) {
        return new JacksonDomainEventSerializer(objectMapper);
    }
}
```

## DLC Jakarta JTA DomainEvents Integration
To use DomainEvents in a JTA setup use this integration.

This integration provides a factory for creation JTA supported DomainEvent channels, that can be plugged in and being used via
DLCs DomainEvent API.

### Usage
The API elements for publishing or listening to domain events are described [here](../domain-events-core/readme.md).
This integration is not included in the DLC Spring Boot starter and the [DLC Spring Boot autoconfiguration](../dlc-spring-boot-autoconfig/readme.md).

### Configuration

#### Dependency

**Gradle:**
```groovy
dependencies {
  implementation 'io.domainlifecycles:domain-events-jakarta-jta:3.0.0'
}
```

**Maven:**
```xml
<dependency>
    <groupId>io.domainlifecycles</groupId>
    <artifactId>domain-events-jakarta-jta</artifactId>
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

#### Configuration examples

Example setup with JTA provided transactions:
```Java
    var services = new Services();
    ...
    var channel = new JtaInMemoryChannelFactory(
        userTransactionManager, 
        services,
        true
        )
        .processingChannel("default");
    
    var router = new DomainEventTypeBasedRouter(List.of(channel));
    router.defineDefaultChannel("default");
    new ChannelRoutingConfiguration(router);
```

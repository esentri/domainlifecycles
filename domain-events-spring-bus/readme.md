## DLC Spring Bus DomainEvents Integration
This integration uses Spring's ApplicationEventPublisher to publish domain events.
It is designed for Spring Boot applications and provides a seamless way to publish domain events within the Spring context.
Also DLC's way of handling domain events is integrated. Therefore listener proxies and adapters are provided.

### Usage
The API elements for publishing or listening to domain events are described [here](../domain-events-core/readme.md).
This integration is included in the DLC Spring Boot starter and the [DLC Spring Boot autoconfiguration](../dlc-spring-boot-autoconfig/readme.md).

### Provided beans
- - ``io.domainlifecycles.events.spring.SpringApplicationEventsPublishingChannelFactory``:
By creating a publishing channel using this factory bean, DomainEvents can be published via Springs ``ApplicationEventPublisher``using the static ``DomainEvents.publish()`` method of DLC.
- ``io.domainlifecycles.events.spring.listeners.AggregateDomainEventAdapter``:
Listens to aggregate domain events (``io.domainlifecycles.domain.types.AggregateDomainEvent``). It loads the target aggregate from the event's payload
  (``targetId``), executes the event's listener method on the aggregate and updates the aggregate via it's repository.
  The adapter executes in an own transaction.
- ``io.domainlifecycles.events.spring.listeners.ServiceKindListenerPostProcessor``:
Registers a proxy domain event listener for each service kind (ApplicationService, DomainService, Repository, QueryHandler or OutboundService) that has listeners methods annotated with ``@DomainEventListener``.
The proxy domain event listener delegates to the corresponding service kind bean and provides transactional handling.
Each original listener method is executed as if it was annotated with 
Springs ``@TransactionalEventListener``, ``@Async``,``@Transactional(propagation=REQUIRES_NEW)``.
If the original listener method is annotated additionally with ``@TransactionalEventListener`` or ``@EventListener`` or Spring Moduliths ``@ApplicationModuleListener``
no proxy is created and the original listener method is executed directly with the Spring annotation defined behaviour.
That way the behaviour of DLCs listener methods (``@DomainEventListener``) can be customized with Spring means.

### Spring Modulith Events compatible
This integration is Spring Modulith compatible. Just add the starter to your project and you are good to go.

[Getting Started](../index_de.md) / [Features](../guides/features_de.md) / [Domain Events](domain_events_de.md)

---

# Domain Events
Vereinfacht und kapselt einige Funktionen von Event-Publishing/-Listening von DomainEvents.

-   Deutlich weniger Publisher Boilerplate-Code durch die statische  `DomainEvents.publish()`  API
-   Weniger Listener und Event-Routing Boilerplate Code durch  `@ListensTo`  Annotation
-   Optional: Support für Spring oder JTA basiertes Transaktions-Handling
-   Optional: Support für "transactional outbox" Pattern für besseres Publishen von DomainEvents

---

## Implementierung 

### Domain-Events definieren
Ein neues Domain-Event lässt sich ganz einfach definieren, indem eine Klasse das `DomainEvent` Interface implementiert.
Der Einfachheit halber können hierfür auch genauso gut Java-Records genutzt werden:

```
public record NewCustomerAdded(@NotNull Customer customer) implements DomainEvent {}
```

### Domain-Events veröffentlichen
Ein Domain-Event kann folgendermaßen veröffentlicht werden:

```
@Publishes(domainEventTypes = {NewCustomerAdded.class})
public void publishEvent(Customer customer) {
    DomainEvents.publish(new NewCustomerAdded(addedCustomer));
}
```

Hierbei relevant sind die Annotation `@Publishes(domainEventTypes = {})` und der Aufruf der statischen
Methode `DomainEvents.publish()`.

### Auf Domain-Events hören
Eine Methode hört auf das veröffentlichen von Domain-Events, sofern sie mit der `@ListensTo(domainEventtype = ...)`
Annotation versehen wurde.
Dies könnte bspw. so aussehen:

```
@ListensTo(domainEventType = NewCustomerAdded.class)
public void listenToEvent(NewCustomerAdded newCustomerAdded) {
    return;
}
```

## Unit-Tests
```
public class InMemoryChannelTest {

    private static CustomerService customerService;

    @BeforeAll
    public static void init(){
        Domain.initialize(new ReflectiveDomainMirrorFactory("tests"));
        customerService = new CustomerService();

        var services = new Services();
        services.registerServiceKindInstance(domainService);
        services.registerServiceKindInstance(repository);
        services.registerServiceKindInstance(applicationService);
        services.registerServiceKindInstance(queryHandler);
        services.registerServiceKindInstance(outboundService);

        var inMemoryChannel = new InMemoryChannelFactory(services).processingChannel("default");
        var router = new DomainEventTypeBasedRouter(List.of(inMemoryChannel));
        router.defineDefaultChannel("default");
        new ChannelRoutingConfiguration(router);
    }

    @Test
    public void testIntegrationDomainEvent() {
        //when
        var evt = new ADomainEvent("Test");
        DomainEvents.publish(evt);
        //then
        assertThat(domainService.received).contains(evt);
        assertThat(repository.received).contains(evt);
        assertThat(applicationService.received).contains(evt);
        assertThat(queryHandler.received).contains(evt);
        assertThat(outboundService.received).contains(evt);
    }
}
```

---

|            **Persistence**             |               **Validation**                |
|:--------------------------------------:|:-------------------------------------------:|
| [<< Previous](persistence_de.md) | [Nächste Seite >>](validation_support_de.md) |

---

**DE** / [EN](../../english/features/validation_support_en.md)

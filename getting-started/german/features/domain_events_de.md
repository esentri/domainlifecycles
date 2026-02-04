[Getting Started](../index_de.md) / [Features](../guides/features_de.md) / [Domain Events](domain_events_de.md)

---

# Domain Events
Vereinfacht und kapselt einige Funktionen von Event-Publishing/-Listening von DomainEvents.
Deutlich weniger Publisher Boilerplate-Code durch die statische `DomainEvents.publish()` API, weniger Listener und 
Event-Routing Boilerplate Code durch `@DomainEventListener` Annotation und optional Support für Spring oder JTA basiertes 
Transaktions-Handling oder Support für "transactional outbox" Pattern für besseres Publishen von DomainEvents

---
## Configuration
In Memory Domain Events werden automatisch konfiguriert.
Um Domain Events Routing abzuschalten oder andere Event Bus Integrationen zu verwenden: 
[DLC Spring Boot AutoConfig](./../../../dlc-spring-boot-autoconfig/readme.md).

## Implementierung 

### Domain-Events definieren
Ein neues Domain-Event lässt sich ganz einfach definieren, indem eine Klasse das `DomainEvent` Interface implementiert.
Idealerweise werden hierfür Java-Records genutzt:

```Java
public record NewCustomerAdded(@NotNull Customer customer) implements DomainEvent {}
```

### Domain-Events veröffentlichen
Ein Domain-Event kann folgendermaßen veröffentlicht werden:

```Java
class EventPublisher {
    
    @Publishes(domainEventTypes = {NewCustomerAdded.class})
    public void publishEvent(Customer customer) {
        DomainEvents.publish(new NewCustomerAdded(addedCustomer));
    }
}
```

Hierbei relevant sind die Annotation `@Publishes(domainEventTypes = {})` und der Aufruf der statischen
Methode `DomainEvents.publish()`.

### Auf Domain-Events hören
Eine Methode hört auf veröffentlichte Domain-Events, sofern sie mit der `@ListensTo(domainEventType = ...)`
Annotation versehen wurde. Die Zustellung des Events und den Aufruf der Methode bei Event-Empfang 
wie auch die zugehörige Transaktionssteuerung übernimmt das Framework:

```Java
class EventListener {
    
    @DomainEventListener
    public void listenToEvent(NewCustomerAdded newCustomerAdded) {}
}
```

---

|             **Persistence**             |                **Validation**                |
|:---------------------------------------:|:--------------------------------------------:|
| [<< Vorherige Seite](persistence_de.md) | [Nächste Seite >>](validation_support_de.md) |
 
---

**DE** / [EN](../../english/features/validation_support_en.md)

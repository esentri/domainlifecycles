[Getting Started](../index.md) / [Features](../features.md) / [Domain Events](domain_events.md)

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


---

|            **Persistence**             |               **Validation**                |
|:--------------------------------------:|:-------------------------------------------:|
| [<< Vorherige Seite](./persistence.md) | [Nächste Seite >>](./validation_support.md) |

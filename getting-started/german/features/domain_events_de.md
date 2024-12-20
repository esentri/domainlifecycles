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
Idealerweise werden hierfür Java-Records genutzt:

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
Eine Methode hört auf veröffentlichte Domain-Events, sofern sie mit der `@ListensTo(domainEventType = ...)`
Annotation versehen wurde:

```
@ListensTo(domainEventType = NewCustomerAdded.class)
public void listenToEvent(NewCustomerAdded newCustomerAdded) {
    return;
}
```

---

|            **Persistence**             |               **Validation**                |
|:--------------------------------------:|:-------------------------------------------:|
| [<< Previous](persistence_de.md) | [Nächste Seite >>](validation_support_de.md) |

---

**DE** / [EN](../../english/features/validation_support_en.md)

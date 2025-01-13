[Getting Started](../index_en.md) / [Features](../guides/features_en.md) / [Domain Events](domain_events_en.md)

---

# Domain Events

Simplifies and encapsulates many functions of event publishing/listening of DomainEvents.
Significantly less publisher boilerplate code through the static `DomainEvents.publish()` API, less listener and event 
routing boilerplate code through `@ListensTo` annotation and optionally support for Spring or JTA based transaction 
handling and support for “transactional outbox” pattern for better publishing of DomainEvents.

---

## Implementation 

### Define new domain events
Ein neues Domain-Event lässt sich ganz einfach definieren, indem eine Klasse das `DomainEvent` Interface implementiert.
Idealerweise werden hierfür Java-Records genutzt:

```Java
public record NewCustomerAdded(@NotNull Customer customer) implements DomainEvent {}
```

### Publish domain events
A domain event can be published as follows:

```Java
class EventPublisher {
    
    @Publishes(domainEventTypes = {NewCustomerAdded.class})
    public void publishEvent(Customer customer) {
        DomainEvents.publish(new NewCustomerAdded(addedCustomer));
    }
}
```

The annotation `@Publishes(domainEventTypes = {})` and the call of the static method `DomainEvents.publish()` are relevant here.


### Listening to domain events
A method listens for published domain events if it has been annotated with `@ListensTo(domainEventType = ...)`.
The framework handles the delivery of the event, the invocation of the method upon event reception, as well as the associated transaction management:

```Java
class EventListener {
    
    @ListensTo(domainEventType = NewCustomerAdded.class)
    public void listenToEvent(NewCustomerAdded newCustomerAdded) {}
}
```

---

|           **Persistence**           |            **Validation**             |
|:-----------------------------------:|:-------------------------------------:|
|  [<< Previous](persistence_en.md)   |  [Next >>](validation_support_en.md)  |

---

**EN** / [DE](../../german/features/domain_events_de.md)

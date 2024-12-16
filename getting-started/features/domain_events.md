[Getting Started](../index.md) / [Features](../features.md) / [Domain Events](domain_events.md)

<hr/>

# Domain Events
Vereinfacht und kapselt einige Funktionen von Event-Publishing/-Listening von DomainEvents.

-   Deutlich weniger Publisher Boilerplate-Code durch die statische  `DomainEvents.publish()`  API
-   Weniger Listener und Event-Routing Boilerplate Code durch  `@ListensTo`  Annotation
-   Optional: Support für Spring oder JTA basiertes Transaktions-Handling
-   Optional: Support für "transactional outbox" Pattern für besseres Publishen von DomainEvents

<hr/>

## Implementierung 

### Domain-Events definieren

## Unit-Tests

<hr/>

|            **Persistence**             |               **Validation**                |
|:--------------------------------------:|:-------------------------------------------:|
| [<< Vorherige Seite](./persistence.md) | [Nächste Seite >>](./validation_support.md) |

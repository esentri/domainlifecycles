[Getting Started](../index.md) / [Features](../features.md) / [Domain-Objects Builders](domainobject_builders.md)

---

# Domain-Object Builders

Das Builder-Modul ermöglicht Support für das DDD-Factory-Pattern und bietet optional
noch Lombok Builder Kompatibilität.

---

## Implementierung
DLC baut stark auf dem Builder-Pattern auf. Verpflichtend ist hierfür zuerst einmal die Bereitstellung einer 
Konfiguration für den Default-Builder, welche bereits unter 
[Projekt erstellen](../configuration.md#DomainObjectBuilderProvider) erläutert wurde.

Für den Builder lässt sich hier am einfachsten Lombok nutzen:
```
public class Customer extends AggregateRootBase<CustomerId> {
    private final CustomerId id;
    private final int someProperty;
    private String anotherProperty;
    
    @Builder(setterPrefix = "set")
    public Customer(final CustomerId id,
                    final long concurrencyVersion,
                    final int someProperty,
                    final String anotherProperty) {
        super(concurrencyVersion);
        this.id = id;
        this.immutableProperty = immutableProperty;
        this.anotherImmutableProperty = anotherImmutableProperty;
    }
}
```

---

|            **Domain Mirror**             |           **JSON-Mapping**            |
|:----------------------------------------:|:-------------------------------------:|
| [<< Vorherige Seite](./domain_mirror.md) | [Nächste Seite >>](./json_mapping.md) |

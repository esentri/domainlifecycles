[Getting Started](../index_de.md) / [Features](../guides/features_de.md) / [Domain-Objects Builders](domainobject_builders_de.md)

---

# Domain-Object Builders

Das Builder-Modul ermöglicht Support für das DDD-Factory-Pattern und bietet optional
noch Lombok Builder Kompatibilität.

---

## Implementierung
DLC baut stark auf dem Builder-Pattern auf. Verpflichtend ist hierfür zuerst einmal die Bereitstellung einer 
Konfiguration für den Default-Builder, welche bereits unter 
[Konfiguration](../guides/configuration_de.md#DomainObjectBuilderProvider) erläutert wurde.

Für den Builder lässt sich hier am einfachsten Lombok nutzen:
```Java
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

|             **Domain Mirror**             |            **JSON-Mapping**            |
|:-----------------------------------------:|:--------------------------------------:|
| [<< Vorherige Seite](domain_mirror_de.md) | [Nächste Seite >>](json_mapping_de.md) |

---

**DE** / [EN](../../english/features/domainobject_builders_en.md)

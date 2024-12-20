[Getting Started](../index_en.md) / [Features](../guides/features_en.md) / [Domain-Objects Builders](domainobject_builders_en.md)

---

# Domain-Object Builders

Das Builder-Modul ermöglicht Support für das DDD-Factory-Pattern und bietet optional
noch Lombok Builder Kompatibilität.

---

## Implementation
DLC baut stark auf dem Builder-Pattern auf. Verpflichtend ist hierfür zuerst einmal die Bereitstellung einer 
Konfiguration für den Default-Builder, welche bereits unter 
[Projekt erstellen](../guides/configuration_en.md#DomainObjectBuilderProvider) erläutert wurde.

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

Möchte man jedoch auf Lombok verzichten, geht man **folgendermaßen** vor:

Für alle Fields werden Setter benötigt, ebenfalls statischer Zugriff
auf den Builder und eine statische `build()` Methode.

```
public class Customer extends AggregateRootBase<CustomerId> {

    private final CustomerId id;
    private final int someProperty;
    private String anotherProperty;

    private Customer(final CustomerId id,
                    final long concurrencyVersion,
                    final int someProperty,
                    final String anotherProperty) {
        super(concurrencyVersion);
        this.id = id;
        this.immutableProperty = immutableProperty;
        this.anotherImmutableProperty = anotherImmutableProperty;
    }

    public static CustomerBuilder builder() {
        return new CustomerBuilder();
    }

    public static class CustomerBuilder {
        private final CustomerId id;
        private final int someProperty;
        private String anotherProperty;

        SampleClassBuilder() {
        }

        public SampleClassBuilder setId(CustomerId id) {
            this.id = id;
            return this;
        }

        // ... other setters are ommitted for previty

        public Customer build() {
            return new Customer(this.id, this.someProperty, this.anotherProperty);
        }
    }
}
```

## Unit-Tests
Sinnvolle Unit-Tests würden hierbei die Setter, und die `build()` Methode des Builders umfassen,
bzw. damit auch den Konstruktor des jeweiligen Domain-Objects. 
Diese wurden hier der Einfachheit halber ausgelassen.

---

|            **Domain Mirror**             |           **JSON-Mapping**            |
|:----------------------------------------:|:-------------------------------------:|
| [<< Previous](domain_mirror_en.md) | [Next >>](json_mapping_en.md) |

---

**EN** / [DE](../../german/features/domainobject_builders_de.md)
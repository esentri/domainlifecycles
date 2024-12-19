[Getting Started](../index.md) / [Features](../features.md) / [Domain-Objects Builders](domainobject_builders.md)

<hr/>

# Domain-Object Builders

Support für das Builder-Pattern.
-   Ermöglicht das DDD Factory-Pattern
-   Optional: Lombok Builder Kompatibilität

<hr/>

## Implementierung
DLC baut stark auf dem Builder-Pattern auf.
Verpflichtend ist hierfür zuerst einmal die Bereitrstellung
einer Konfiguration für den Default-Builder,
welche bereits unter [Projekt erstellen](../configuration.md#DomainObjectBuilderProvider) erläutert wurde.

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

Andernfalls geht man folgendermaßen vor:
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

<hr/>

|            **Domain Mirror**             |           **JSON-Mapping**            |
|:----------------------------------------:|:-------------------------------------:|
| [<< Vorherige Seite](./domain_mirror.md) | [Nächste Seite >>](./json_mapping.md) |

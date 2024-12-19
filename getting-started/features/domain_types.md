[Getting Started](../index.md) / [Features](../features.md) / [Domain Types](domain_types.md)

<hr/>

# Domain Types
Implementierung von DDD Bausteinen mittels DLC Interfaces oder Base-Klassen.
-   DDD Bausteine im Java-Code kennzeichnen: Transparenteres Design
-   Grundlage für Java Typisierung Meta-Daten (siehe Domain Mirror)

<hr/>

## Implementierung
DLC bietet grundsätzlich verschiedene abstrakte Klassen und Interfaces, welche extended bzw. 
implementiert werden können, um entsprechende Domain Types zu definieren.

Im Folgenden ein Beispiel, um ein neues Aggregate zu definieren:
```
public class Customer extends AggregateRootBase<CustomerId> {
    private final CustomerId id;
    
    public Customer(final CustomerId id,
                    final long concurrencyVersion) {
        super(concurrencyVersion);
        this.id = id;
    }
}
```

Da für ein Aggregate auch zwingend eine ID benötigt wird, muss diese folgendermaßen im Voraus
oder als innere Klasse des Aggregates definiert sein:
```
public record CustomerId(@NotNull Long value) implements Identity<Long> {}
```


## Unit-Tests

<hr/>

|           **Domain Mirror**            |
|:--------------------------------------:|
| [Nächste Seite >>](./domain_mirror.md) |


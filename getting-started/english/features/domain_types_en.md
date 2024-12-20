[Getting Started](../index_en.md) / [Features](../guides/features_en.md) / [Domain Types](domain_types_en.md)

---

# Domain Types
The Domain Types module enables the implementation (and therefore also the labeling) of DDD blocks by making use of 
the DLC interfaces or base classes, which can be extended later.

---

## Implementation
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
Eine Identity gehört hierbei genauso zu den Domain Types, wie ein Aggregate auch. Eine aushürlichere Auflistung 
und Erläuterung aller Domain Types findet sich [hier](../../../readme_building_blocks.md).

## Unit-Tests
Unit-Tests zu den Domain-Types würden vielmehr Erfüllung der Validierungen und der Funktionsweise
des Builders dienen, weshalb auf diese in den jeweiligen Kapiteln erst eingegangen wird.

---

|        **Domain Mirror**        |
|:-------------------------------:|
| [Next >>](domain_mirror_en.md)  |

---

**EN** / [DE](../../german/features/domain_types_de.md)


[Getting Started](../index_de.md) / [Features](../guides/features_de.md) / [Domain Types](domain_types_de.md)

---

# Domain Types
Das Domain Types Modul ermöglicht die Implementierung (und damit auch Kennzeichnung) von DDD Bausteinen mittels 
DLC Interfaces oder Base-Klassen, welche später extended werden können.

---

## Implementierung
DLC bietet grundsätzlich verschiedene abstrakte Klassen und Interfaces, welche extended bzw. 
implementiert werden können, um entsprechende Domain Types zu definieren.

Im Folgenden ein Beispiel, um ein neues Aggregate zu definieren:
```Java
public class Customer extends AggregateRootBase<CustomerId> {
    private final CustomerId id;
    
    public Customer(final CustomerId id,
                    final long concurrencyVersion) {
        super(concurrencyVersion);
        this.id = id;
    }
}
```

Da für ein Aggregate auch zwingend eine ID (Identity) benötigt wird, muss diese folgendermaßen im Voraus
oder als innere Klasse des Aggregates definiert sein:
```
public record CustomerId(@NotNull Long value) implements Identity<Long> {}
```
Eine Identity gehört hierbei genauso zu den Domain Types, wie ein Aggregate auch. Eine ausführlichere Auflistung 
und Erläuterung aller Domain Types findet sich [hier](../../../readme_building_blocks.md).

---

|           **Domain Mirror**            |
|:--------------------------------------:|
|[Nächste Seite >>](domain_mirror_de.md) |

---

**DE** / [EN](../../english/features/domain_types_en.md)

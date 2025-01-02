[Getting Started](../index_en.md) / [Features](../guides/features_en.md) / [Domain Types](domain_types_en.md)

---

# Domain Types
The Domain Types module enables the implementation (and therefore also the labeling) of DDD blocks by making use of 
the DLC interfaces or base classes, which can be extended later.

---

## Implementation
DLC offers various abstract classes and interfaces that can be extended or implemented in order to define corresponding domain types.

The following is an example of how to define a new aggregate:

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

Since an ID (identity) is also required for an aggregate, this must be defined in advance or as an inner class of the 
aggregate as follows:

```Java
public record CustomerId(@NotNull Long value) implements Identity<Long> {}
```
An identity belongs to the domain types in the same way as an aggregate. A more detailed list
and explanation of all domain types can be found [here](../../../readme_building_blocks.md).
---

|        **Domain Mirror**        |
|:-------------------------------:|
| [Next >>](domain_mirror_en.md)  |

---

**EN** / [DE](../../german/features/domain_types_de.md)


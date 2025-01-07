[Getting Started](../index_en.md) / [Features](../guides/features_en.md) / [Domain-Objects Builders](domainobject_builders_en.md)

---

# Domain-Object Builders

The Builder module enables support for the DDD Factory pattern and offers optional
Lombok Builder compatibility.

---

## Implementation
DLC is strongly based on the builder pattern. The first mandatory requirement for this is the provision of a
configuration for the default builder, which can be found under
[Configuration](../guides/configuration_en.md#DomainObjectBuilderProvider).


Lombok is the easiest option for the builder:
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

|          **Domain Mirror**          |        **JSON-Mapping**         |
|:-----------------------------------:|:-------------------------------:|
| [<< Previous](domain_mirror_en.md)  |  [Next >>](json_mapping_en.md)  |

---

**EN** / [DE](../../german/features/domainobject_builders_de.md)

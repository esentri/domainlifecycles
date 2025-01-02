[Getting Started](../index_en.md) / [Features](../guides/features_en.md) / [Domain Mirror](domain_mirror_en.md)

---

# Domain Mirror
The domain mirror mirrors the meta-model of the design/project structures within a bounded context.
This enables structured queries and navigation through the model within a bounded context.

---

## Implementation
The domain mirror forms the basis for all DLC features and must be initialized before all other configurations.
This can be done using either a static constructor or with a Spring configuration bean, on which all other DLC-specific
configuration beans then depend (via Spring configuration with `@DependsOn`):

```Java
@SpringBootApplication
public class SampleApplication {

    static {
        Domain.initialize(new ReflectiveDomainMirrorFactory("sampleapp"));
    }

    public static void main(String[] args) {
        //...
    }
}
```

---

|          **Domain Types**          |       **Domain-Object Builders**        |
|:----------------------------------:|:---------------------------------------:|
| [<< Previous](domain_types_en.md)  | [Next >>](domainobject_builders_en.md)  |

---

**EN** / [DE](../../german/features/domain_mirror_de.md)

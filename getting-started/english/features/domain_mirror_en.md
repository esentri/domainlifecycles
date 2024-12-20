[Getting Started](../index_en.md) / [Features](../guides/features_en.md) / [Domain Mirror](domain_mirror_en.md)

---

# Domain Mirror
The domain mirror mirrors the meta-model of the design/project structures within a bounded context.
This enables structured queries and navigation within a bounded context and also simplified access to the values 
of `Aggregates`, `Entities` and `ValueObjects` with the use of reflection.

---

## Implementation
Um den Domain-Mirror zu nutzen, muss dieser zuerst initialisiert werden:
```
@SpringBootApplication
public class SampleApplication {

    static {
        Domain.initialize(new ReflectiveDomainMirrorFactory("sampleapp"));
    }

    public static void main(String[] args) {
        ...
    }
}
```
Anschließend können über die statischen Methoden des Interface `io.domainlifecycles.mirror.api.Domain`
die DDD-Strukturen des Projekts auf verschiedene Arten nachvollzogen werden.
Ein Beispiel hierfür wäre:
```
Customer customer = Domain.typeMirror("io.sampleapp.customer");
```

## Unit-Tests

Möchten man Funktionen seines Codes testen, welche sich auf den DLC-Mirror stützen,
ist es wichtig diesen in der Test-Klasse auch zu initialisieren mit dem korrekten Context-Package:

```
public class SomeDomainMirrorTest {

    @BeforeAll
    public static void init() {
        ReflectiveDomainMirrorFactory factory = new ReflectiveDomainMirrorFactory("tests");
        Domain.initialize(factory);
    }
}
```

---

|            **Domain Types**             |           **Domain-Object Builders**           |
|:---------------------------------------:|:----------------------------------------------:|
| [<< Previous](domain_types_en.md) | [Next >>](domainobject_builders_en.md) |

---

**EN** / [DE](../../german/features/domain_mirror_de.md)

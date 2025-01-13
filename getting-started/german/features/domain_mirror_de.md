[Getting Started](../index_de.md) / [Features](../guides/features_de.md) / [Domain Mirror](domain_mirror_de.md)

---

# Domain Mirror
Der Domain-Mirror spiegelt das Meta-Modell der DDD Building Blocks innerhalb eines Bounded-Contexts wider.
Damit ermöglicht er strukturierte Queries und Navigation über das Modell innerhalb eines Bounded-Context.

---

## Implementierung
Der Domain-Mirror bildet die Grundlage für alle DLC Features und muss vor allen anderen Konfigurationen initialisiert werden. 
Dies kann per static Konstruktor geschehen, oder aber auch als Spring Configuration Bean, von welchem dann alle anderen DLC spezifischen 
Configuration-Beans abhängen (per Spring-Configuration mit `@DependsOn`):

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

|             **Domain Types**              |           **Domain-Object Builders**            |
|:-----------------------------------------:|:-----------------------------------------------:|
| [<< Vorherige Seite](domain_types_de.md)  | [Nächste Seite >>](domainobject_builders_de.md) |

---

**DE** / [EN](../../english/features/domain_mirror_en.md)

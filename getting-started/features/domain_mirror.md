[Getting Started](../index.md) / [Features](../features.md) / [Domain Mirror](domain_mirror.md)

<hr/>

# Domain Mirror
Mirrored das Meta-Modell der Design-Strukturen innerhalb eines Bounded-Contexts.
-   Liefert strukturierte Queries and Navigation innerhalb eines Bounded-Context's strukturierten Meta-Models sowie 
vereinfachten Zugriff auf die Werte der Domain `Aggregates`,`Entities`  und  `ValueObjects` mittels Reflection.

<hr/>

## Implementierung
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

<hr/>

|            **Domain Types**             |           **Domain-Object Builders**           |
|:---------------------------------------:|:----------------------------------------------:|
| [<< Vorherige Seite](./domain_types.md) | [Nächste Seite >>](./domainobject_builders.md) |

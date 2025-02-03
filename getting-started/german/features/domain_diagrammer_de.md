[Getting Started](../index_de.md) / [Features](../guides/features_de.md) / [Domain Diagrammer](domain_diagrammer_de.md)

---

# Domain Diagrammer
Das Domain-Diagrammer Modul ist imstande ein UML-Klassendiagramm aller DDD Building Blocks 
des Projekts zu erstellen. Das Ergebnis ist hierbei Text-basiert im [Nomnoml](https://nomnoml.com/)-Format.

---

## Implementierung

```Java
class Diagrammer {
    public void generateDiagram() {
        Domain.initialize(new ReflectiveDomainMirrorFactory(new TypeMetaResolver(), "sampleshop"));
        DomainDiagramConfig diagramConfig = DomainDiagramConfig.builder().withContextPackageName("sampleshop").build();
        DomainDiagramGenerator generator = new DomainDiagramGenerator(diagramConfig, Domain.getDomainModel());

        String diagramTextNomnoml = generator.generateDiagramText();
        File file = new File("generated_nomnoml_example.nomnoml");
        FileUtils.writeStringToFile(file, actualDiagramText, StandardCharsets.UTF_8);
    }
}
```

## Beispiel Diagramm

Nachfolgend ist ein Beispiel-Diagramm der [DLC Sample App](./../../../sample-project) zu sehen.

![What a pity you cannot see it](./../../../documentation/resources/images/sample_diagram.png "Domain Diagram")


---

|             **Spring-Web-Integration**             |
|:--------------------------------------------------:|
| [<< Vorherige Seite](spring_web_integration_de.md) |

---

**DE** / [EN](../../english/features/domain_diagrammer_en.md)

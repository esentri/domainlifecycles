[Getting Started](../index_en.md) / [Features](../guides/features_en.md) / [Domain Diagrammer](domain_diagrammer_en.md)

---

# Domain Diagrammer
The Domain Diagrammer module is able to create a UML class diagram of all DDD Building Blocks of the project. 
The result is text-based in NomNoml [Nomnoml](https://nomnoml.com/) format.

---

## Implementierung

```Java
class Diagrammer {
    public void generateDiagram() {
        Domain.initialize(new ReflectiveDomainMirrorFactory("sampleshop"));
        DomainDiagramConfig diagramConfig = DomainDiagramConfig.builder().withContextPackageName("sampleshop").build();
        DomainDiagramGenerator generator = new DomainDiagramGenerator(diagramConfig);

        String diagramTextNomnoml = generator.generateDiagramText();
        File file = new File("generated_nomnoml_example.nomnoml");
        FileUtils.writeStringToFile(file, actualDiagramText, StandardCharsets.UTF_8);
    }
}
```

---

|          **Spring-Web-Integration**           |
|:---------------------------------------------:|
| [<< Previous](spring_web_integration_en.md)   |

---

**EN** / [DE](../../german/features/domain_diagrammer_de.md)

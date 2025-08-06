[Getting Started](../index_en.md) / [Features](../guides/features_en.md) / [Domain Diagrammer](domain_diagrammer_en.md)

---

# Domain Diagrammer
The Domain Diagrammer module is able to create a UML class diagram of all DDD Building Blocks of the project. 
The result is text-based in [Nomnoml](https://nomnoml.com/) format.

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

## Sample Diagramm

Here's an example of a generated diagram for the [DLC Sample App](./../../../sample-project).

![What a pity you cannot see it](./../../../documentation/resources/images/sample_diagram.png "Domain Diagram")



---

|         **Spring-Web-Integration**          |        **Autoconfiguration** |
|:-------------------------------------------:|-----------------------------:|
| [<< Previous](spring_web_integration_en.md) | [Next  >>](autoconfig_en.md) |

---

**EN** / [DE](../../german/features/domain_diagrammer_de.md)

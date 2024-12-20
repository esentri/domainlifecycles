[Getting Started](../index_en.md) / [Features](../guides/features_en.md) / [Domain Diagrammer](domain_diagrammer_en.md)

---

# Domain Diagrammer
The Domain Diagrammer module is able to create an UMl class diagram of all the DDD components in the project.
The result is text-based in [Nomnoml](https://nomnoml.com/) format.

---

## Implementation

```
Domain.initialize(new ReflectiveDomainMirrorFactory("sampleshop"));
DomainDiagramConfig diagramConfig = DomainDiagramConfig.builder().withContextPackageName("sampleshop").build();
DomainDiagramGenerator generator = new DomainDiagramGenerator(diagramConfig);

String diagramTextNomnoml = generator.generateDiagramText();
File file = new File("generated_nomnoml_example.nomnoml");
FileUtils.writeStringToFile(file, actualDiagramText, StandardCharsets.UTF_8);

```

## Unit-Tests
Ein geeigneter Unit-Test wäre hierbei der Abgleich des generierten Nomnomls und der gewollten
"Soll"-Version des Nomnomls, worauf aber hier der Einfachheit halber nicht näher eingegangen wird.

---

|            **Spring-Web-Integration**             |
|:-------------------------------------------------:|
| [<< Previous](spring_web_integration_en.md) |

---

**EN** / [DE](../../german/features/domain_diagrammer_de.md)

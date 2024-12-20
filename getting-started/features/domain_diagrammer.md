[Getting Started](../index.md) / [Features](../features.md) / [Domain Diagrammer](domain_diagrammer.md)

---

# Domain Diagrammer
Das Domain-Diagrammer Modul ist imstande ein UML-Klassendiagramm aller DDD Building Blocks 
des Projekts zu erstellen. Das Ergebnis ist hierbei Text-basiert im NomNoml [Nomnoml](https://nomnoml.com/)-Format.

---

## Implementierung

```
Domain.initialize(new ReflectiveDomainMirrorFactory("sampleshop"));
DomainDiagramConfig diagramConfig = DomainDiagramConfig.builder().withContextPackageName("sampleshop").build();
DomainDiagramGenerator generator = new DomainDiagramGenerator(diagramConfig);

String diagramTextNomnoml = generator.generateDiagramText();
File file = new File("generated_nomnoml_example.nomnoml");
FileUtils.writeStringToFile(file, actualDiagramText, StandardCharsets.UTF_8);

```

---

|            **Spring-Web-Integration**             |
|:-------------------------------------------------:|
| [<< Vorherige Seite](./spring_web_integration.md) |

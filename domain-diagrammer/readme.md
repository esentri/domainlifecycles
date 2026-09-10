# DomainLifeCycles Diagrammer

The DLC Diagrammer currently generates [Nomnoml](https://nomnoml.com/) diagram texts.
Nomnoml is tool for drawing UML diagrams based on a simple syntax.

The generated diagram text currently describes UML class diagram, which sets the focus on the used DDD patterns.
Most of the meta-information needed is derived from the used DLC marker interfaces for the
DDD [Building Blocks](../concepts/readme.md).

Have a look at our [sample project](../sample-project), and diagram text
generated [here](../sample-project/src/test/java/sampleshop/NomnomlDomainDiagramGeneratorTest.java)
by executing the test `generateSampleApp()` to the standard output.

There are several options to adjust the diagram settings, have a look
at `io.domainlifecycles.diagram.domain.config.DomainDiagramConfig`.

## Example how to use it

1. Add `io.domainlifecycles:domain-diagrammer` dependency to your project (in your test setup)
   Gradle setup:

```Groovy
dependencies{
    testImplementation 'io.domainlifecycles:domain-diagrammer:3.2.0'
}
```

Maven setup:

```XML
<dependency>
    <groupId>io.domainlifecycles</groupId>
    <artifactId>domain-diagrammer</artifactId>
    <version>3.2.0</version>
    <scope>test</scope>
</dependency>
```

2. Add a test like the following

```Java
class NomnomlDomainDiagramGeneratorTest {
    
    @Test
    void generateSampleApp() throws Exception {
        var factory = new ReflectiveDomainMirrorFactory("yourdomain");
        factory.setGenericTypeResolver(new TypeMetaResolver());
        Domain.initialize(factory);
        var trim = DiagramTrimSettings.builder()
                .withExplicitlyIncludedPackageNames(List.of("yourdomain.specific"))
                .build();
        DomainDiagramConfig diagramConfig = DomainDiagramConfig.builder().withDiagramTrimSettings(trim).build();
        DomainDiagramGenerator generator = new DomainDiagramGenerator(
                diagramConfig, Domain.getDomainMirror());
        
        String actualDiagramText = generator.generateDiagramText();
        File file = new File("generated_nomnoml_example.nomnoml");
        FileUtils.writeStringToFile(file, actualDiagramText, StandardCharsets.UTF_8);
    }
} 
```

3. After running the test you can copy the generated output from `generated_nomnoml_example.nomnoml`
   into [`https://nomnoml.com/`](https://nomnoml.com/) to view and even edit the diagram.
    - You can also save images from there
    - On Nomnoml projects page, you can find information on how to generate images directly or to host your own Nomnoml
      drawer instance via Docker.

![What a pity you cannot see it](../documentation/resources/images/sample_diagram.png "Nomnoml based DDD class diagram")

## Restricting a diagram to a flow

A diagram can be reduced to the classes taking part in one concrete flow — everything a domain
command sets in motion, everything a domain event triggers, or everything one method reaches. The
structural relations of the mirror do not know about method calls, so this needs the result of a
[static analysis](../static-analysis) to be handed to the generator:

```Java
DomainCalls domainCalls = new SootupStaticAnalyzer()
    .analyze(Domain.getDomainMirror(), DomainClasspath.ofMirroredTypes(Domain.getDomainMirror()));

var trim = DiagramTrimSettings.builder()
    .withExplicitlyIncludedPackageNames(List.of("yourdomain"))
    .withIncludeFlowsFrom(List.of("yourdomain.order.PlaceOrder"))
    .build();
DomainDiagramConfig diagramConfig = DomainDiagramConfig.builder().withDiagramTrimSettings(trim).build();

DomainDiagramGenerator generator = new DomainDiagramGenerator(
    diagramConfig, Domain.getDomainMirror(), domainCalls);
```

A `DomainCalls` is *created* by the analyzer from the `io.domainlifecycles:static-analysis-sootup`
artifact, which has to be added next to the diagrammer.
Gradle setup:

```Groovy
dependencies{
    testImplementation 'io.domainlifecycles:static-analysis-sootup:3.2.0'
}
```

Maven setup:

```XML
<dependency>
    <groupId>io.domainlifecycles</groupId>
    <artifactId>static-analysis-sootup</artifactId>
    <version>3.2.0</version>
    <scope>test</scope>
</dependency>
```

*Reading* such a result only needs the slim `io.domainlifecycles:static-analysis`, which the
diagrammer already brings along.

Each entry of `includeFlowsFrom` is a full qualified type name, optionally followed by `#` and a
method name:

| Entry | Starts the flow of |
|---|---|
| a domain command | the methods processing that command |
| a domain event | the methods listening to that event |
| any other domain type | all methods of that type |
| `type#method` | all overloads of that method |

Several entries are united. How far a flow is followed can be adjusted with
`DomainDiagramConfig.builder().withFlowConfig(...)`, for instance
`FlowConfig.defaults().withMaxDepth(3)` to stop after three steps.

The restriction only ever narrows: a class outside the configured packages or on the
`classesBlacklist` stays out, even when the flow reaches it. Entities and value objects inside an
aggregate that survives the filter are still drawn, as with every other trim setting.

## Rendering from commandline to image

First install `nomnom-cli` via `npm`.

- $ npm install nomnoml-cli -g

Then render any nomnoml file into an image.

- $ cat graph.nomnoml | nomnoml > graph.png

More on the usage of `nomnoml-cli`: https://github.com/prantlf/nomnoml-cli

## Requirements

To render something useful, your domain implementation must implement/use the marker interfaces and annotations
from [DLC Domain types](../concepts/readme.md).  
Also we at least need the [domain mirror](../mirror/readme.md), to be able to provide all the needed domain metadata within the
rendering process.

Restricting a diagram to a flow additionally requires the result of a
[static analysis](#restricting-a-diagram-to-a-flow), which needs a compiled classpath of your
domain types. The diagram generation itself still gets by with the mirror alone.

Those dependencies are provided like:

```Groovy
dependencies{
    implementation 'io.domainlifecycles:mirror:3.2.0'
    implementation 'io.domainlifecycles:types:3.2.0'
}
```

Maven setup:

```XML
<dependency>
    <groupId>io.domainlifecycles</groupId>
    <artifactId>mirror</artifactId>
    <version>3.2.0</version>
</dependency>
<dependency>
    <groupId>io.domainlifecycles</groupId>
    <artifactId>types</artifactId>
    <version>3.2.0</version>
</dependency>
```
## Rendering domain diagrams via Maven or Gradle

See [here](./../dlc-plugins/readme.md)

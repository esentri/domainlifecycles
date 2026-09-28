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
    testImplementation 'io.domainlifecycles:domain-diagrammer:3.5.0'
}
```

Maven setup:

```XML
<dependency>
    <groupId>io.domainlifecycles</groupId>
    <artifactId>domain-diagrammer</artifactId>
    <version>3.5.0</version>
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
    testImplementation 'io.domainlifecycles:static-analysis-sootup:3.5.0'
}
```

Maven setup:

```XML
<dependency>
    <groupId>io.domainlifecycles</groupId>
    <artifactId>static-analysis-sootup</artifactId>
    <version>3.5.0</version>
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

### The backward direction: entry channels into a target

`includeFlowsFrom` answers "what does this lead to". `withIncludeFlowsTo(...)` is its backward
counterpart: "what leads into this" - all the entry channels through which a target is reached:

```Java
var trim = DiagramTrimSettings.builder()
    .withExplicitlyIncludedPackageNames(List.of("yourdomain"))
    .withIncludeFlowsTo(List.of("yourdomain.order.OrderService"))
    .build();
```

Same entry syntax as `includeFlowsFrom`:

| Entry | Reaches the entry points into |
|---|---|
| a domain event | the methods publishing that event |
| any other domain type (aggregate, service, ...) | everything calling any of its methods, plus, for an aggregate/read model, the repository/query handler structurally managing/providing it |
| `type#method` | everything calling that method |

A domain command cannot be a backward flow target (nothing in the analyzed data leads *into* a
command) - passing one throws `IllegalArgumentException`. A command can still appear *as a reached
node* in the result, when a target is reached because it processes that command.

`includeFlowsFrom` and `includeFlowsTo` can be configured together; their reached classes are
united, so a diagram can show both what a seed leads to and what leads into another (or the same)
seed at once. The rendering direction of every edge is unaffected either way - it always follows
the structural direction of the mirror data (e.g. service → repository), regardless of which
direction the flow search that kept a node visible ran in.

## Showing non-domain classes

The [mirror](../mirror/readme.md#mirroring-non-domain-classes) does not only mirror classes
implementing one of the DLC marker interfaces - every other class in the scanned domain model
packages (a mapper, helper, utility or controller class, say) is mirrored too, without needing any
marker interface. Building on that, the diagrammer can draw such a class as a node, but only when it
has a relationship (by a field, method parameter or return type, in either direction) to a service
kind - a domain service, application service, repository, query handler, outbound service or
unspecified service kind. That covers both directions: a service depending on a non-domain helper
class (e.g. a mapper it holds a field for), and a non-domain class that itself calls into a service
(e.g. a REST controller or a message listener holding an application service). Classes that have no
such relationship to any service kind never appear, even though the mirror knows about them.

This is enabled by default via `GeneralVisualSettings.isShowNonDomainClasses()`. Methods of a
non-domain class are shown by default too (e.g. so a controller's endpoint methods are visible),
while fields are hidden by default; both can be switched individually:

```Java
var general = GeneralVisualSettings.builder()
    .withShowNonDomainClasses(true)
    .withShowNonDomainClassFields(true)
    .withShowNonDomainClassMethods(false)
    .build();
DomainDiagramConfig diagramConfig = DomainDiagramConfig.builder()
    .withGeneralVisualSettings(general)
    .build();
```

To hide non-domain classes altogether, set `withShowNonDomainClasses(false)`. Their look can be
adjusted like every other kind via `StyleSettings.builder().withNonDomainClassStyle(...)`.

A domain command received by a non-domain class - e.g. a controller method taking it as parameter -
is connected to it by an `is processed by` relationship, just like to an application service
processing it. With `showOnlyTopLevelDomainCommandRelations` (the default), only the outermost of
the classes processing a command is connected to it: a controller forwarding the command to an
application service it holds gets the relationship, the application service does not. A hidden
non-domain class does not count for that, so the application service keeps its relationship then.

## Read models containing read models

A read model containing another read model - as field, `Optional` or collection - is connected to it
by a composition labeled with the field and its multiplicity (e.g. `lines 1..*` for a list annotated
`@NotEmpty`), like a value object containing another value object. The field is then not listed in
the box of the containing read model. A contained read model is shown together with the read model
containing it - like the parts of an aggregate - even if the connections or flows the diagram is
restricted to do not reach it. A blacklisted contained read model is not shown and stays a field.

## Classes sharing a name

Nomnoml identifies a node by its name. Classes sharing a simple name but living in different packages
are still drawn as nodes of their own: their name is followed by their package below the package
they have in common, e.g. `OrderService (billing.domain)` and `OrderService (shipping.domain)` for
`com.example.billing.domain.OrderService` and `com.example.shipping.domain.OrderService`. A class right
in that common package is followed by its full package. The hint only appears if the classes are
shown in the same diagram, and not with `showFullQualifiedClassNames`. An implementation drawn as its
interface (without inheritance structures shown) remains one node with it, whatever its name.

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
    implementation 'io.domainlifecycles:mirror:3.5.0'
    implementation 'io.domainlifecycles:types:3.5.0'
}
```

Maven setup:

```XML
<dependency>
    <groupId>io.domainlifecycles</groupId>
    <artifactId>mirror</artifactId>
    <version>3.5.0</version>
</dependency>
<dependency>
    <groupId>io.domainlifecycles</groupId>
    <artifactId>types</artifactId>
    <version>3.5.0</version>
</dependency>
```
## Rendering domain diagrams via Maven or Gradle

See [here](./../dlc-plugins/readme.md)

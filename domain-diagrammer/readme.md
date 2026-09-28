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

### Only the methods called in the flows

By default (`GeneralVisualSettings.isShowOnlyFlowMethods()`), a class taking part in a flow shows
only the methods called in it - e.g. only `placeOrder` of an application service offering
`placeOrder`, `cancelOrder` and `report`, if the flow starts at `placeOrder`. A method called through
an interface counts for the implementation shown in its place. A backward flow into a whole type
shows the methods of that type its callers call. Classes shown for another reason - an entity of a
shown aggregate or a read model contained in a shown read model, which no flow reaches themselves -
show their methods as in a diagram without flow. The other method settings (e.g.
`showDomainServiceMethods`) still apply. To show all methods, switch it off:

```Java
var general = GeneralVisualSettings.builder()
    .withShowOnlyFlowMethods(false)
    .build();
```

### The calls of the flows

The relationships of a diagram come from the structure of the domain model: fields, processed
commands, published and listened events, managed aggregates, provided read models. A flow follows the
method calls, though, which may connect two classes without any such structure - e.g. a class reading
a read model it got from elsewhere, holding no field of it. Switched on with
`GeneralVisualSettings.withShowFlowCallRelations(true)` (off by default), two classes calling each
other in a flow are therefore connected by a `<<calls>>` relationship, labeled with the called methods
(up to three, followed by `…`), if no other relationship connects them. A call into an aggregate
connects to its frame; calls of entities, value objects, identities and enums are left out, they are
mostly accessors of an aggregate's parts.

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

A non-domain class holding another shown non-domain class as field - e.g. a controller delegating to
a helper - is connected to it as well.

A domain command received by a non-domain class - e.g. a controller method taking it as parameter -
is connected to it by an `is processed by` relationship, just like to an application service
processing it. With `showOnlyTopLevelDomainCommandRelations` (the default), only the outermost of
the classes processing a command is connected to it: a controller forwarding the command to an
application service it holds gets the relationship, the application service does not. A hidden
non-domain class does not count for that, so the application service keeps its relationship then.

## Read models without query handler

A read model is usually provided by a query handler, which the diagram connects to it. A read model no
query handler provides is provided by the classes returning it instead - service kinds (query
handlers aside) and non-domain classes with a method returning it, directly, as `Optional` or as
collection, e.g. a driver computing it. They are connected to it by a `<<provides>>` relationship,
labeled with the providing methods. The flows follow the same relation, see the
[static analysis](../static-analysis/readme.md).

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

## Value objects shown inline

A value object with only a few fields is shown inline - as field of the class referencing it, e.g.
`price:<VO> Money` - instead of as class of its own connected by a composition. By default that
applies to value objects of up to two fields; `GeneralVisualSettings.withMaxInlinedValueObjectFields(int)`
changes the number (`1` shows only value objects of a single field inline, `0` none):

```Java
var general = GeneralVisualSettings.builder()
    .withMaxInlinedValueObjectFields(3)
    .build();
```

A field holding a value object shown inline in turn counts as one field, while a value object
containing one that is not shown inline is not shown inline itself - so no field ever disappears from
the diagram. A value object on the classes blacklist is always shown inline.

## Abstract types and inheritance structures

Whether abstract types (interfaces and abstract classes) are shown depends on the inheritance
settings of `GeneralVisualSettings`:

- With `showAllInheritanceStructures`, or the setting of their kind -
  `showInheritanceStructuresForServiceKinds`, `showInheritanceStructuresInAggregates`,
  `showInheritanceStructuresForReadModels`, `showInheritanceStructuresForDomainEvents` or
  `showInheritanceStructuresForDomainCommands` - abstract and concrete types are shown both, connected
  by their inheritance relationships.
- Otherwise (the default) an abstract type stands in for its implementations: it is hidden as long as
  one of its implementations is shown in the diagram, and shown if none is. That covers an abstract type
  without any implementation as well as one whose implementations are left out of the diagram - by the
  package filter, the blacklist, or a flow reaching the abstract type only, e.g. a read model interface
  that is implemented anonymously somewhere.

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

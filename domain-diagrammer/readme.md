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

## Restricting a diagram to connected classes

Without a static analysis, a diagram can be reduced along the structural relations of the mirror -
a service referencing another one, a command processed, an event published or listened to, a repository
managing an aggregate, a query handler providing a read model, a class creating an aggregate.
`DiagramTrimSettings` offers:

- `includeConnectedToIngoing`: the given classes and everything leading to them ("what leads to it"),
- `includeConnectedToOutgoing`: the given classes and everything they lead to ("what does it lead to"),
- `includeConnectedTo`: both directions,
- `excludeConnectedToIngoing` / `excludeConnectedToOutgoing`: leave the given classes and what leads to them,
  respectively what they lead to, out.

A class may be named for both directions - in `includeConnectedToIngoing` and `includeConnectedToOutgoing` to show
what leads to it and what it leads to, each with its own depth, or in both exclude settings. Unlike
`includeConnectedTo`, which changes direction on the way and thereby also reaches e.g. the other callers of a
repository the class uses, this follows each direction from the class only. A class must not be included and
excluded at once, nor be named in `includeConnectedTo` and in one of the directed include settings.

By default the connections are followed along the complete path. `withIncludeConnectedToIngoingDepth(int)` and
`withIncludeConnectedToOutgoingDepth(int)` limit how many steps they are followed: `1` includes the classes
directly connected, `2` also the classes connected to these, and so on. `0` or a negative depth follows the
complete path (the default). An interface and its implementations count as one step, and the classes drawn
together with a class - the entities and value objects of an aggregate - take no step:

```Java
var trim = DiagramTrimSettings.builder()
    .withIncludeConnectedToOutgoing(List.of("com.example.order.PlaceOrder"))
    .withIncludeConnectedToOutgoingDepth(2)
    .build();
```

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

Settings that need the result of a static analysis are rejected with an `IllegalArgumentException`
naming the setting, if the generator is created without a `DomainCalls`: `includeFlowsFrom`,
`includeFlowsTo` and `showFlowCallRelations` switched on. `showOnlyFlowMethods`, on by default,
only takes effect in a diagram restricted to a flow and is therefore never rejected.
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
method calls, though, which may connect two classes without any such structure - e.g. a service
calling another one it gets from elsewhere, holding no field of it. Switched on with
`GeneralVisualSettings.withShowFlowCallRelations(true)` (off by default), a service kind or non-domain
class calling another service kind or non-domain class in a flow is therefore connected to it by a
`<<calls>>` relationship, directed from the caller to the called class and labeled with the called
methods (up to three, followed by `…`), if no other relationship connects them. Two classes calling
each other get a relationship in each direction.

A service kind or non-domain class calling a read model, or an aggregate - its root or one of its
entities - is connected to the read model or the aggregate's frame as well, but only if no path of
relationships leads from the caller there yet, also via other classes: a class using the query
handler or the providing service of the read model, or the repository of the aggregate, directly or
via other classes, gets no extra relationship. An aggregate - its root or one of its entities - calling
a non-domain class, e.g. a helper collecting its errors, is connected to it from its frame, if no other
relationship connects them. Calls of value objects, identities, enums, commands and events are left
out, they are mostly accessors.

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

Exceptions and anonymous classes are never drawn as nodes. An anonymous class implementing an
interface - e.g. a read model interface implemented anonymously by a client - is represented by that
interface instead, which is shown in its place (see
[Abstract types and inheritance structures](#abstract-types-and-inheritance-structures)).

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
a helper - is connected to it as well. A non-domain class listening to a domain event - e.g. a Spring
event listener with a method annotated `@DomainEventListener` - is connected to the event by a
`notifies` relationship, like a service listening to it.

A domain command received by a non-domain class - e.g. a controller method taking it as parameter -
is connected to it by an `is processed by` relationship, just like to an application service
processing it. With `showOnlyTopLevelDomainCommandRelations` (the default), only the outermost of
the classes processing a command shown in the diagram is connected to it: a controller forwarding the
command to an application service it holds gets the relationship, the application service does not.
A class the diagram does not show - e.g. left out by a flow or hidden - does not count for that, so the
outermost of the shown ones keeps its relationship then.

## Read models without query handler

A read model is usually provided by a query handler, which the diagram connects to it. A read model no
query handler provides is provided by the classes returning it instead - service kinds (query
handlers aside) and non-domain classes with a method returning it, directly, as `Optional` or as
collection, e.g. a driver computing it. They are connected to it by a `<<provides>>` relationship,
labeled with the providing methods. The flows follow the same relation, see the
[static analysis](../static-analysis/readme.md).

## Factories and factory methods

A class implementing `Factory` (or annotated with jMolecules' `@Factory`) is drawn with the stereotype
`<<Factory>>` and a style of its own (`StyleSettings.withFactoryStyle`). Like domain services it shows its
methods but not its fields by default (`showFactories`, `showFactoryFields`, `showFactoryMethods`).

The factory methods of other classes - marked with `@FactoryMethod`, e.g. in an aggregate or a domain
service - are marked with `«factory»` in their method list. A class creating another domain type by its
factory methods is connected to it by a `<<creates>>` relationship, labeled with these methods, each
qualified by the class declaring it (e.g. `Appointment.invite`, at most three, then `…`): a factory or domain service to the frame of the aggregate it creates, an aggregate - its
root or one of its entities - from its frame to the frame of another aggregate it creates. Within an
aggregate there is no such relationship, the composition already connects its classes, and neither is
there one for a class creating instances of itself, e.g. by a static factory method. `GeneralVisualSettings.withShowFactoryRelations(false)` leaves the
relationships out (on by default). Builders are never factories.

The flows follow the factory methods to the domain types they create, and backwards from a domain type to the
factory methods creating it, see the [static analysis](../static-analysis/readme.md). A flow reaching an entity
shows its whole aggregate.

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

## Aggregates shown as frames only

For an overview of a larger domain the content of the aggregates is often more detail than needed.
`GeneralVisualSettings.withShowOnlyAggregateFrames(true)` - a central switch for all aggregates of the
diagram - draws each aggregate as its frame only, without the classes inside (aggregate root, entities,
value objects, enums, identities), the relationships between them and their notes (off by default):

```Java
var general = GeneralVisualSettings.builder()
    .withShowOnlyAggregateFrames(true)
    .build();
```

All relationships from outside an aggregate - from its repository, the domain commands it processes, the
domain events it publishes or listens to, the factories creating it, the id references from other
aggregates or the calls of the flows - connect its frame anyway, so they are still drawn.

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

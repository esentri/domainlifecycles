# DLC Static Analysis

The [DLC Domain Mirror](../mirror/readme.md) mirrors the *structure* of a bounded context: which
types exist, how they relate, which methods they declare. What it cannot know is which of those
methods actually call each other. DLC Static Analysis adds that dimension. It answers two
questions:

- which domain method is called from which other domain method, and
- which domain types and methods take part in one concrete *flow* — everything a domain command
  sets in motion, everything a domain event triggers, or everything a single method reaches.

This module holds the abstraction and the result model only: the `StaticAnalyzer` interface, the
analyzed call graph (`DomainCalls`), the flow analysis on top of it (`FlowAnalyzer`) and the
`DomainClasspath` helper. It depends on nothing but the mirror.

The analysis itself lives in `static-analysis-sootup`, implemented on top
of [SootUp](https://soot-oss.github.io/SootUp/). It is a module of its own so that *reading* an
analysis result — for instance
to [restrict a domain diagram to a flow](../domain-diagrammer/readme.md#restricting-a-diagram-to-a-flow)
— does not drag a bytecode analysis framework onto the classpath.

## Dependencies

### Producing an analysis result

The analysis works on compiled bytecode, so it typically runs in a test or in the build rather than
at application runtime. Gradle setup:

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

This brings `io.domainlifecycles:static-analysis` along transitively.

### Reading an analysis result

Consumers that only read a `DomainCalls` — or that analyze a flow on top of one — depend on this
module alone. Gradle setup:

```Groovy
dependencies{
    implementation 'io.domainlifecycles:static-analysis:3.2.0'
}
```

Maven setup:

```XML
<dependency>
    <groupId>io.domainlifecycles</groupId>
    <artifactId>static-analysis</artifactId>
    <version>3.2.0</version>
</dependency>
```

The [domain diagrammer](../domain-diagrammer/readme.md) already brings this one along.

## Analyzing the domain calls

```Java
Domain.initialize(new ReflectiveDomainMirrorFactory("yourdomain"));
DomainMirror domainMirror = Domain.getDomainMirror();

DomainCalls domainCalls = new SootupStaticAnalyzer()
    .analyze(domainMirror, DomainClasspath.ofMirroredTypes(domainMirror));
```

The mirror defines the boundary of the analysis: only calls whose target resolves to a mirrored
type end up in the result, which keeps JDK and third-party code out of it.

Because the analysis reads bytecode, it has to be told where to find it. Assembling that list by
hand is error prone — a missing entry does not fail the analysis, it silently shrinks the result —
so `DomainClasspath` derives it from the mirror itself, by asking every mirrored type where its own
class file came from. That covers the domain classes as well as the DLC base types they inherit
from. `DomainClasspath.ofTypes(...)` adds anchors for anything the mirror does not cover,
`merge(...)` combines classpaths and `parse(...)` reads the string form used on command lines and
by build plugins.

The result is a graph that is indexed in both directions:

```Java
DomainMethod placeOrder = new DomainMethod("yourdomain.order.OrderService", methodMirror);

DomainCalls.CalledMethods called = domainCalls.callsFor(placeOrder);  // what does it call?
Set<DomainMethod> callers = domainCalls.callersOf(placeOrder);        // who calls it?
```

A node of that graph — a `DomainMethod` — combines the mirrored method with the *concrete owner
type*, deliberately not with the declaring type: an inherited method is attributed to the class
that owns it, so `MyOverridingService.process` and `MyBaseService.process` stay two distinct nodes.
An edge — a `DomainCalls.CallSite` — additionally carries the type whose body contained the invoke
instruction and its source line, if the analyzed bytecode has a line number table.

### Diagnostics

A static analysis fails quietly: an incomplete classpath or a method the mirror does not hold does
not raise an error, it just produces fewer calls. Without diagnostics a consumer could not tell
"this method calls nothing" from "this method could not be analyzed", so the result carries what
could *not* be done:

```Java
if (!domainCalls.isComplete()) {
    domainCalls.diagnostics(Diagnostic.Severity.WARNING).forEach(System.out::println);
}
```

A `WARNING` means the result is likely incomplete and the cause should be fixed — most importantly
a mirrored type missing from the analyzed classpath. An `INFO` means something was deliberately
skipped, e.g. a compiler generated bridge method that has no counterpart in the mirror.

## Analyzing a flow

A flow is analyzed on top of an already computed `DomainCalls`, joined with the event and command
information of the mirror:

```Java
FlowAnalyzer analyzer = new DomainCallFlowAnalyzer(domainMirror, domainCalls);

var command = (DomainCommandMirror) domainMirror
    .getDomainTypeMirror("yourdomain.order.PlaceOrder").orElseThrow();

Flow flow = analyzer.flowFrom(command);
Set<String> reachedTypes = flow.reachedTypeNames();
```

A flow can start at a domain command (beginning with the methods processing it), at a domain event
(beginning with the methods listening to it) or at a `DomainMethod`. For a quick lookup by name,
`analyzer.flowFrom("yourdomain.order.OrderService", "placeOrder")` resolves the starting method in
the mirror and returns an empty `Optional` if the type or the method is unknown.

A flow is a graph, not a sequence: a method may call several others, each of which continues on its
own. It is represented as a flat list of `Step`s, each linking back to the step it was reached from
(`Step#from()`), so the branching structure is preserved while staying easy to iterate, filter and
render. The steps are ordered breadth first, which makes `Step#depth()` the shortest distance from
the start at which a node was discovered. Cycles are contained rather than silently dropped: a step
reaching a node that already occurs among its own predecessors is reported with `Step#cyclic()` and
not expanded further.

`Flow#toString()` renders the whole thing as an indented tree, one line per step, which for the
example above takes the shape of:

```
yourdomain.order.PlaceOrder
  COMMAND_PROCESS -> yourdomain.order.OrderService.placeOrder(yourdomain.order.PlaceOrder)
    CALL -> yourdomain.order.Orders.findById(yourdomain.order.OrderId)
      IMPLEMENTATION -> yourdomain.order.OrdersImpl.findById(yourdomain.order.OrderId)
    CALL -> yourdomain.order.Order.place()
      EVENT_PUBLISH -> yourdomain.order.OrderPlaced
        EVENT_LISTEN -> yourdomain.notification.NotificationService.onOrderPlaced(yourdomain.order.OrderPlaced)
```

### The kinds of edge a flow follows

A domain flow is not made of method calls alone, so every step names the mechanism that carried the
flow to it (`Step#kind()`):

| `StepKind`        | The step's method or event is ...                         | Taken from    |
|-------------------|-----------------------------------------------------------|---------------|
| `CALL`            | called by the predecessor                                 | `DomainCalls` |
| `IMPLEMENTATION`  | an override or implementation of the predecessor's method | the mirror    |
| `EVENT_PUBLISH`   | published by the predecessor                              | the mirror    |
| `EVENT_LISTEN`    | listening to the predecessor's event                      | the mirror    |
| `COMMAND_PROCESS` | processing the predecessor's command                      | the mirror    |

The two edges that do not come from the call graph are the interesting ones.

`EVENT_PUBLISH` / `EVENT_LISTEN` cross the asynchronous boundary of a domain event. No static call
analysis can see that edge, yet it is exactly where the domain flow continues. Several listeners of
one event branch the flow into independent continuations, all of which run.

`IMPLEMENTATION` is the runtime dispatch. A call is resolved against the static type at the call
site, so a call through a repository or an outbound service interface ends at that interface —
where there is no body, and thus no continuation. This edge is that continuation. In contrast to
several listeners, several `IMPLEMENTATION` edges from one step are alternatives: exactly one of
them is taken per execution.

### Configuring the traversal

```Java
FlowConfig config = FlowConfig.defaults()
    .withMaxDepth(3)
    .excludingAccessors();

FlowAnalyzer analyzer = new DomainCallFlowAnalyzer(domainMirror, domainCalls, config);
```

By default the depth is unlimited, event and implementation edges are followed and nothing is
filtered — a flow reports what the analysis found, accessor calls included. Real domains produce a
lot of those (`command.id()`, identity getters), which is why `excludingAccessors()` exists as the
filter most consumers will want; `withMethodFilter(...)` takes any other predicate. Setting
`withFollowImplementations(false)` keeps the flow strictly on the types written at the call sites,
answering "which types does this code name" rather than "which code can run". A flow that was cut
off by `withMaxDepth(...)` while there was still something left to expand reports
`Flow#truncated()`.

## What the analysis does not see

- **Constructors and static initializers.** The mirror does not model them as a method, so
  `new SomeAggregate(...)` and `super(...)` do not appear in the result — and are not reported as a
  gap either.
- **The runtime target of a call through an interface reference.** It is reported on the interface,
  not on the implementations it may dispatch to. That is the honest information about the code; the
  polymorphic continuation is added by the `FlowAnalyzer` as its own kind of edge instead of being
  merged into the call graph.
- **Anything not on the analyzed classpath.** Reported as a `Diagnostic` rather than guessed.

## Requirements

Your domain implementation must implement/use the marker interfaces and annotations
from [DLC Domain types](../concepts/readme.md), and the [domain mirror](../mirror/readme.md) must be
initialized — it defines which types belong to the domain.

Beyond that, the analysis needs the *compiled* domain classes: it reads bytecode, not source code
and not the mirror alone. That makes it a test time or build time feature rather than a runtime one.

## Restricting a domain diagram to a flow

The main consumer of an analysis result within DLC is
the [domain diagrammer](../domain-diagrammer/readme.md#restricting-a-diagram-to-a-flow), which can
reduce a diagram to the classes taking part in one flow.

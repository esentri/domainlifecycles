# DLC Plugins

The DLC plugins provide several functions to integrate DLC in the build phase:
- creating domain diagrams of the build domain classes within the bounded contexts
- exporting a JSON domain model
- uploading the domain model based on the current implementation to a DLC Domain Viewer instance (not available for now)

## General prerequisites
To create class diagram images as SVG the plugins we use a Kroki Docker container, 
so Docker should be available on the machine running the plugin. 
The plugin spins up the container itself.

## DLC-Gradle-Plugin

### Class-Diagram
The plugin is able to create class diagrams in various formats of your implemented domain model.

#### Configuration
An example configuration in your project could look like the following:
```groovy
plugins {
    id 'io.domainlifecycles.dlc-gradle-plugin' version '3.3.0'
}

dlcGradlePlugin {
    diagram {
        fileOutputDir = layout.buildDirectory
        diagrams {
            diagramNomnoml {
                domainModelPackages = ["io.domainlifecycles.test"]
                explicitlyIncludedPackages = ["io.domainlifecycles.test.mycontext"]
                format = "nomnoml"
                fileName = "diagram"
            }
            diagramSvg {
                domainModelPackages = ["io.domainlifecycles.test"]
                format = "svg"
                fileName = "diagram"
                aggregateRootStyle = "fill=#333333 bold"
            }
        }
    }
}
```
You need to specify an output directory, where your file will be saved to with `fileOutputDir`.
Below that you can specify as many diagram configurations as you want, with different formats and specifications
and even different packages which should be used to read the model with `domainModelPackages`.

The specified DomainModel must be complete an self-contained. All classes that the model consists of must be defined within the `domainModelPackages`.
To render only a specific part of the model, use the `explicitlyIncludedPackages`configuration option.

Currently the DLC Gradle plugin is only published to Maven Central without a gradle plugin marker artifact. 
To use it, you have to specify a special resolution strategy:
```groovy
pluginManagement {

    repositories {
        gradlePluginPortal()
        mavenCentral()
    }
    resolutionStrategy {
        eachPlugin {
            if (requested.id.id == "io.domainlifecycles.dlc-gradle-plugin") {
                useModule("io.domainlifecycles:dlc-gradle-plugin:3.3.0")
            }
        }
    }
}
```

\
You can rename `diagramNomnoml` and `diagramSvg` of course according to your needs, however it's important
to specify some name, otherwise Gradle is not able to read the configuration properly.

Currently supported formats are:
- `nomnoml`
- `svg` (you need Docker installed on the machine running the plugin)

If you create a Nomnoml file, you can use https://www.nomnoml.com/ to create a diagram manually, 
or you try the built in plugin function, that spins up a Docker container to convert nomnoml to SVG.

For all options see [below](#general-dlc-plugin-configuration-options)

#### Run
```bash
gradle createDiagram
```

### Serialize Mirror
Besides creating class diagrams, the plugin is also able to generate a JSON-File containing your domain model.

#### Configuration
An example configuration in your project could look like the following:
```groovy
dlcGradlePlugin {
    serializeMirror {
        fileOutputDir = layout.buildDirectory
        serializations{
            [
                serial {
                    fileName = "model"
                    domainModelPackages = ["io.domainlifecycles.test"]
                }
            ]
        }
    }
}
```
Similar to the diagram configuration above, you need to specify where your JSON file should be saved to and its name,
and finally, the packages where the model should be read from.
However, you can leave the `fileOutputDir` and/or the `fileName` empty. The file will then be saved to the default path
`src/main/resources/META-INF/dlc/mirror.json`.

The specified DomainModel must be complete and self-contained. 
All classes that the model consists of must be defined within the `domainModelPackages`.

#### Run
```bash
gradle serializeMirror
```

### Diagram-Viewer Integration 
If you have an instance of the Diagram-Viewer app running, whether it be on your local machine or on a hosted platform,
you are able to quickly create new or update existing projects, without the need to upload a packaged archive file of
your project in the UI.

ATTENTION: The diagram viewer is released soon! Not available for now!

#### Configuration
An example configuration in your project could look like the following:
```groovy
dlcGradlePlugin {
    domainModelUpload {
        domainModelPackages = ["io.domainlifecycles.test"]
        projectName = "test-project"
        apiKey = "<YOUR-API-KEY>"
        diagramViewerBaseUrl = "http://localhost:8090"
        runStaticAnalysis = true
        streamUpload = false
        staticAnalysisCacheSize = 500
        staticAnalysisPackages = ["io.domainlifecycles.test"]
    }
}
```
Specify the packages you want to be scanned by the Diagram-Viewer. These can later on be changed, or your diagrams 
can be specified even further.
You can generate a new API-Key by clicking on the profile tab in the Diagram-Viewer App.
All classes that the model consists of must be defined within the `domainModelPackages`.

By default (`runStaticAnalysis = true`), the plugin additionally runs a static analysis of the compiled
domain classes and uploads its result (`DomainCalls`) alongside the domain model, so the Diagram-Viewer
can offer flow based diagram filtering (see [static analysis](../static-analysis/readme.md) and
[restricting a diagram to a flow](../domain-diagrammer/readme.md#restricting-a-diagram-to-a-flow)).
Set `runStaticAnalysis = false` to upload only the domain model, skipping the analysis.

For a domain of a few hundred types the uploaded JSON (domain model plus static analysis result) can
already reach the tens of megabytes, so the upload request is gzip-compressed (`Content-Encoding: gzip`)
before being sent - your Diagram-Viewer endpoint needs to decompress the request body accordingly. The
plugin also applies a 10 second connect timeout and an overall 5 minute request timeout, so an
unreachable or slow Diagram-Viewer fails the build instead of hanging it indefinitely.

By default, the (already gzip-compressed) request body is assembled completely in memory before being
sent. Set `streamUpload = true` to instead stream it directly into the HTTP request as it is produced -
this avoids ever holding the complete JSON in memory (and, since the compressed size is then not known
upfront, sends the request with chunked transfer encoding). This is opt-in rather than the default,
since it trades a lower, flatter memory footprint for a background thread producing the body while the
request is in flight; for very large domains it can be the difference between comfortably fitting into
a build's memory budget and risking an `OutOfMemoryError`, while for smaller domains the default is
simpler and sufficiently efficient.

The static analysis itself keeps memory bounded by caching only up to `staticAnalysisCacheSize` classes
(domain, JDK and library classes alike) at a time while resolving method bodies; classes evicted from
the cache are simply re-parsed from the classpath on the next access. The default, `500`, comfortably
holds a mid-sized domain plus its immediate dependencies without evicting on every lookup. Lower it to
cap memory usage further for very large projects (at the cost of more re-parsing), or raise it if you
have memory to spare and want to avoid re-parsing.

By default, the static analysis also considers only classes in `domainModelPackages` (and their
sub-packages) - not the project's entire classpath, which for a large multi-module project can be
considerably more expensive to scan. Set `staticAnalysisPackages` to restrict (or widen) this
explicitly, e.g. to also include an infrastructure package holding the concrete implementations of
your domain's repository/outbound-service interfaces - a concrete implementation outside the analyzed
packages is not found, the same as if it were simply missing from the classpath.

#### Run
```bash
gradle domainModelUpload
```

### Troubleshooting

#### Jackson
If you get an error message while compiling one of the needed projects regarding Jackson,
try to alter your used Gradle Daemon version. Full support is provided for version **8.10.1**.

## DLC-Maven-Plugin

### Class-Diagram
The plugin is able to create class diagrams in various formats of your implemented domain model.

#### Configuration
An example configuration in your project's build plugins could look like the following:
```xml
<build>
    <plugins>
        <plugin>
            <groupId>io.domainlifecycles</groupId>
            <artifactId>dlc-maven-plugin</artifactId>
            <version>3.3.0</version>
            <executions>
                <execution>
                    <id>createDiagramNomnoml</id>
                    <phase>compile</phase>
                    <goals>
                        <goal>createDiagram</goal>
                    </goals>
                    <configuration>
                        <fileOutputDir>target</fileOutputDir>
                        <diagrams>
                            <diagram>
                                <domainModelPackages>
                                    <domainModelPackage>io.domainlifecycles.test</domainModelPackage>
                                </domainModelPackages>
                                <format>nomnoml</format>
                                <fileName>diagram</fileName>
                            </diagram>
                            <diagram>
                                <domainModelPackages>
                                    <domainModelPackage>io.domainlifecycles.test</domainModelPackage>
                                </domainModelPackages>
                                <explicitlyIncludedPackages>
                                    <explicitlyIncludedPackage>io.domainlifecycles.test.mycontext</explicitlyIncludedPackage>
                                </explicitlyIncludedPackages>
                                <format>svg</format>
                                <fileName>diagram-mycontext</fileName>
                                <aggregateRootStyle>fill=#333333 bold</aggregateRootStyle>
                            </diagram>
                        </diagrams>
                    </configuration>
                </execution>
            </executions>
        </plugin>
    </plugins>
</build>
```
You need to specify an output directory, where your file will be saved to with `fileOutputDir`.
Below that you can specify as many diagram configurations as you want, with different formats and specifications
and even different packages which should be used to read the model with `domainModelPackages`.

Currently supported formats are:
- `nomnoml`
- `svg` (you need Docker installed on the machine running the plugin)

If you create a Nomnoml file, you can use https://www.nomnoml.com/ to create a diagram manually,
or you try the built in plugin function, that spins up a Docker container to convert nomnoml to SVG.

#### Run
Depending on the Maven phase you specified:
```bash
mvn dlc:createDiagram@createDiagramNomnoml
```

### Serialize Mirror
Besides creating class diagrams, the plugin is also able to generate a JSON-File containing your domain-model.

#### Configuration
An example configuration in your project could look like the following:
```xml
<build>
    <plugins>
        <plugin>
            <groupId>io.domainlifecycles</groupId>
            <artifactId>dlc-maven-plugin</artifactId>
            <version>3.3.0</version>
            <executions>
                <execution>
                    <id>serializeMirror</id>
                    <phase>compile</phase>
                    <goals>
                        <goal>serializeMirror</goal>
                    </goals>
                    <configuration>
                        <fileOutputDir>target</fileOutputDir>
                        <serializations>
                            <serialization>
                                <fileName>model</fileName>
                                <domainModelPackages>
                                    <contextPackage>io.domainlifecycles.test</contextPackage>
                                </domainModelPackages>
                            </serialization>
                        </serializations>
                    </configuration>
                </execution>
            </executions>
        </plugin>
    </plugins>
</build>
```
Similar to the diagram configuration above, you need to specify where your JSON file should be saved to and its name,
and finally, the packages where the model should be read from.
However, you can leave the `fileOutputDir` and/or the `fileName` empty. The file will then be saved to the default path
`src/main/resources/META-INF/dlc/mirror.json`.

#### Run
Depending on the the plugin execution specified above:
```bash
mvn dlc:renderJson@renderJson
```

### Diagram-Viewer Integration
If you have an instance of the Diagram-Viewer app running, whether it be on your local machine or on a hosted platform,
you are able to quickly create new or update existing projects, without the need to upload a packaged archive file of
your project in the UI.

ATTENTION: The diagram viewer is released soon! Not available for now!

#### Configuration
An example configuration in your project could look like the following:
```xml
<build>
    <plugins>
        <plugin>
            <groupId>io.domainlifecycles</groupId>
            <artifactId>dlc-maven-plugin</artifactId>
            <version>3.3.0</version>
            <executions>
                <execution>
                    <id>upload</id>
                    <phase>compile</phase>
                    <goals>
                        <goal>domainModelUpload</goal>
                    </goals>
                    <configuration>
                    <domainModelPackages>
                        <domainModelPackage>io.domainlifecycles.test</domainModelPackage>
                    </domainModelPackages>
                    <diagramViewerBaseUrl>http://localhost:8090</diagramViewerBaseUrl>
                    <apiKey>YOUR-API-KEY</apiKey>
                    <projectName>test-project</projectName>
                    <runStaticAnalysis>true</runStaticAnalysis>
                    <streamUpload>false</streamUpload>
                    <staticAnalysisCacheSize>500</staticAnalysisCacheSize>
                    <staticAnalysisPackages>
                        <staticAnalysisPackage>io.domainlifecycles.test</staticAnalysisPackage>
                    </staticAnalysisPackages>
                    </configuration>
                </execution>
            </executions>
        </plugin>
    </plugins>
</build>
```
Specify the packages you want to be scanned by the Diagram-Viewer. These can later on be changed, or your diagrams
can be specified even further.
You can generate a new API-Key by clicking on the profile tab in the Diagram-Viewer App.
All classes that the model consists of must be defined within the `domainModelPackages`.

By default (`runStaticAnalysis` defaults to `true`), the plugin additionally runs a static analysis of the
compiled domain classes and uploads its result (`DomainCalls`) alongside the domain model, so the
Diagram-Viewer can offer flow based diagram filtering (see [static analysis](../static-analysis/readme.md)
and [restricting a diagram to a flow](../domain-diagrammer/readme.md#restricting-a-diagram-to-a-flow)).
Set `<runStaticAnalysis>false</runStaticAnalysis>` to upload only the domain model, skipping the analysis.

For a domain of a few hundred types the uploaded JSON (domain model plus static analysis result) can
already reach the tens of megabytes, so the upload request is gzip-compressed (`Content-Encoding: gzip`)
before being sent - your Diagram-Viewer endpoint needs to decompress the request body accordingly. The
plugin also applies a 10 second connect timeout and an overall 5 minute request timeout, so an
unreachable or slow Diagram-Viewer fails the build instead of hanging it indefinitely.

By default, the (already gzip-compressed) request body is assembled completely in memory before being
sent. Set `<streamUpload>true</streamUpload>` to instead stream it directly into the HTTP request as it
is produced - this avoids ever holding the complete JSON in memory (and, since the compressed size is
then not known upfront, sends the request with chunked transfer encoding). This is opt-in rather than
the default, since it trades a lower, flatter memory footprint for a background thread producing the
body while the request is in flight; for very large domains it can be the difference between
comfortably fitting into a build's memory budget and risking an `OutOfMemoryError`, while for smaller
domains the default is simpler and sufficiently efficient.

The static analysis itself keeps memory bounded by caching only up to `staticAnalysisCacheSize` classes
(domain, JDK and library classes alike) at a time while resolving method bodies; classes evicted from
the cache are simply re-parsed from the classpath on the next access. The default, `500`, comfortably
holds a mid-sized domain plus its immediate dependencies without evicting on every lookup. Lower it to
cap memory usage further for very large projects (at the cost of more re-parsing), or raise it if you
have memory to spare and want to avoid re-parsing.

By default, the static analysis also considers only classes in `domainModelPackages` (and their
sub-packages) - not the project's entire classpath, which for a large multi-module project can be
considerably more expensive to scan. Set `staticAnalysisPackages` to restrict (or widen) this
explicitly, e.g. to also include an infrastructure package holding the concrete implementations of
your domain's repository/outbound-service interfaces - a concrete implementation outside the analyzed
packages is not found, the same as if it were simply missing from the classpath.

#### Run
```bash
mvn dlc:uploadDomainModel@upload
```

## General DLC plugin configuration options

Currently supported formats are:
- `nomnoml`
- `svg` (you need Docker installed on the machine running the plugin)

Supported Diagram configuration options are
- aggregateRootStyle: e.g "fill=#333333 bold" (see [Nomnoml](https://www.nomnoml.com/) style definitions)
- aggregateFrameStyle: e.g "fill=#333333 bold" (see [Nomnoml](https://www.nomnoml.com/) style definitions)
- entityStyle: e.g "fill=#333333 bold" (see [Nomnoml](https://www.nomnoml.com/) style definitions)
- valueObjectStyle: e.g "fill=#333333 bold" (see [Nomnoml](https://www.nomnoml.com/) style definitions)
- enumStyle: e.g "fill=#333333 bold" (see [Nomnoml](https://www.nomnoml.com/) style definitions)
- identityStyle: e.g "fill=#333333 bold" (see [Nomnoml](https://www.nomnoml.com/) style definitions)
- domainEventStyle: e.g "fill=#333333 bold" (see [Nomnoml](https://www.nomnoml.com/) style definitions)
- domainCommandStyle: e.g "fill=#333333 bold" (see [Nomnoml](https://www.nomnoml.com/) style definitions)
- applicationServiceStyle: e.g "fill=#333333 bold" (see [Nomnoml](https://www.nomnoml.com/) style definitions)
- domainServiceStyle: e.g "fill=#333333 bold" (see [Nomnoml](https://www.nomnoml.com/) style definitions)
- repositoryStyle: e.g "fill=#333333 bold" (see [Nomnoml](https://www.nomnoml.com/) style definitions)
- readModelStyle: e.g "fill=#333333 bold" (see [Nomnoml](https://www.nomnoml.com/) style definitions)
- queryHandlerStyle: e.g "fill=#333333 bold" (see [Nomnoml](https://www.nomnoml.com/) style definitions)
- outboundServiceStyle: e.g "fill=#333333 bold" (see [Nomnoml](https://www.nomnoml.com/) style definitions)
- font: e.g. "Calibri", "Arial"
- direction: "right" or "down"
- ranker: network-simplex | tight-tree | longest-path, see [Nomnoml](https://www.nomnoml.com/)
- acycler: see [Nomnoml](https://www.nomnoml.com/)
- backgroundColor: e.g. #eee8d5 (default is transparent)
- classesBlacklist: list of full qualified classnames (classes are excluded)
- showFields: boolean, default true
- showFullQualifiedClassNames: boolean, default false
- showAssertions: boolean, default true
- showMethods: boolean, default true
- showOnlyPublicMethods: boolean, default true
- showDomainEvents: boolean, default true
- showDomainEventFields: boolean, default false
- showDomainEventMethods: boolean, default false
- showDomainCommands: boolean, default true
- showOnlyTopLevelDomainCommandRelations: boolean, default true
- showDomainCommandFields: boolean, default false
- showDomainCommandMethods: boolean, default false
- showDomainServices: boolean, default true
- showDomainServiceFields: boolean, default false
- showDomainServiceMethods: boolean, default true
- showApplicationServices: boolean, default true
- showApplicationServiceFields: boolean, default false
- showApplicationServiceMethods: boolean, default true
- showRepositories: boolean, default true
- showRepositoryFields: boolean, default false
- showRepositoryMethods: boolean, default true
- showReadModels: boolean, default true
- showReadModelFields: boolean, default true
- showReadModelMethods: boolean, default false
- showQueryHandlers: boolean, default true
- showQueryHandlerFields: boolean, default false
- showQueryHandlerMethods: boolean, default false
- showOutboundServices: boolean, default true
- showOutboundServiceFields: boolean, default false
- showOutboundServiceMethods: boolean, default false
- showUnspecifiedServiceKinds: boolean, default true
- showUnspecifiedServiceKindFields: boolean, default false
- showUnspecifiedServiceKindMethods: boolean, default false
- callApplicationServiceDriver: boolean, default false
- fieldBlacklist: field names to be excluded in field list, default "concurrencyVersion"  
- methodBlacklist: method names to be excluded in field list, default "builder", "validate", "concurrencyVersion", "id", "findResultById", "publish", "increaseVersion", "equals", "hashCode", "toString"
- showInheritedMembersInClasses: boolean, default true
- showObjectMembersInClasses: boolean, default true
- multiplicityInLabel: boolean, default true
- fieldStereotypes: boolean, default true
- includeConnectedTo: list of full qualified classnames (all classes connected are included)
- includeConnectedToIngoing: list of full qualified classnames (classes and ingoing connected classes are included)
- includeConnectedToOutgoing: : list of full qualified classnames (classes and outgoing connected classes are included)
- excludeConnectedToIngoing: : list of full qualified classnames (classes and ingoing connected classes are excluded)
- excludeConnectedToOutgoing: : list of full qualified classnames (classes and outgoing connected classes are excluded)
- explicitlyIncludedPackages: list of packages explicitly included in the diagram
- showAllInheritanceStructures: boolean, default false
- showInheritanceStructuresInAggregates: boolean, default true
- showInheritanceStructuresForServiceKinds: boolean, default false
- showInheritanceStructuresForReadModels: boolean, default false
- showInheritanceStructuresForDomainEvents: boolean, default false
- showInheritanceStructuresForDomainCommands: boolean, default false
- showRelationshipLabels: boolean, default true
- showRelationshipStereotypes: boolean, default true
- includeFlowsFrom: list of flow starting points (see [Restricting a diagram to a flow](#restricting-a-diagram-to-a-flow)), default none (flow-based filtering disabled)
- flowMaxDepth: integer, maximum depth a flow is followed to, default unlimited
- flowFollowEvents: boolean, whether a flow follows published DomainEvents to their listening methods, default true
- flowFollowImplementations: boolean, whether a flow follows the dispatch from an interface/abstract method into its implementations, default true
- flowExcludeAccessors: boolean, whether simple accessor methods (getters/setters) are excluded from a followed flow, default false
- staticAnalysisPackages: list of packages the static analysis (triggered by `includeFlowsFrom`) restricts itself to, default `domainModelPackages` (see [Restricting a diagram to a flow](#restricting-a-diagram-to-a-flow))

## Restricting a diagram to a flow

Besides the structural filters above (packages, blacklists, connection filters), a diagram can be restricted to the
classes taking part in one or more concrete flows through the domain. A flow starts at a domain command, a domain
event, or any other domain method, and follows method calls, the dispatch into implementations, published
DomainEvents together with the methods listening to them, and the methods processing a DomainCommand.

This is configured with `includeFlowsFrom`, a list of flow starting points. Each entry is a fully qualified type
name, optionally followed by `#methodName`:
- a DomainCommand or DomainEvent type name starts the flow(s) triggered by it
- any other domain type name starts the flows of all its methods
- `type#methodName` starts the flows of all overloads of that method

Several entries are combined (their reached classes are unioned). The restriction only ever narrows an already
configured diagram: a class outside `domainModelPackages`/`explicitlyIncludedPackages` or on the `classesBlacklist`
stays out, even when the flow reaches it.

Since determining which classes take part in a flow requires analyzing the compiled domain classes, configuring
`includeFlowsFrom` makes the plugin run a static (bytecode) analysis of your domain classes as part of diagram
generation. This is skipped whenever `includeFlowsFrom` is not configured for a diagram. `flowMaxDepth`,
`flowFollowEvents`, `flowFollowImplementations` and `flowExcludeAccessors` further tune how far/what such a flow
traversal follows; they have no effect unless `includeFlowsFrom` is also set.

The static analysis keeps memory bounded by caching only up to a fixed number of classes at a time while
resolving method bodies; classes evicted from the cache are simply re-parsed from the classpath on the
next access. Configure this via `staticAnalysisCacheSize` on the `diagram` task configuration itself
(not per diagram, since one analysis is shared by all diagrams generated in the same run) - see
[Diagram-Viewer Integration](#diagram-viewer-integration) below for the same setting on the upload task.

By default, the static analysis for a given diagram also considers only classes in that diagram's own
`domainModelPackages` (and their sub-packages) - not the project's entire classpath, which for a large
multi-module project can be considerably more expensive to scan. Set `staticAnalysisPackages` on the
individual `diagram` entry to restrict (or widen) this explicitly, e.g. to also include an
infrastructure package holding the concrete implementations of your domain's repository/outbound-service
interfaces - a concrete implementation outside the analyzed packages is not found, the same as if it
were simply missing from the classpath. Unlike `staticAnalysisCacheSize`, this is configured per diagram,
not on the surrounding `diagram` task, since different diagrams in the same run may restrict to different
flows/packages.

Gradle example, restricted to the flow of the `PlaceOrder` domain command:
```groovy
dlcGradlePlugin {
    diagram {
        fileOutputDir = layout.buildDirectory
        staticAnalysisCacheSize = 500
        diagrams {
            placeOrderFlow {
                domainModelPackages = ["io.domainlifecycles.test"]
                staticAnalysisPackages = ["io.domainlifecycles.test"]
                format = "svg"
                fileName = "place-order-flow"
                includeFlowsFrom = ["io.domainlifecycles.test.order.PlaceOrder"]
                flowMaxDepth = 5
            }
        }
    }
}
```

Maven example (`<staticAnalysisCacheSize>` goes into the surrounding `<configuration>` of the
`createDiagram` execution, alongside `<fileOutputDir>` and `<diagrams>`, not into an individual `<diagram>`;
`<staticAnalysisPackages>` goes into the individual `<diagram>` instead, alongside `<domainModelPackages>`):
```xml
<diagram>
    <domainModelPackages>
        <domainModelPackage>io.domainlifecycles.test</domainModelPackage>
    </domainModelPackages>
    <staticAnalysisPackages>
        <staticAnalysisPackage>io.domainlifecycles.test</staticAnalysisPackage>
    </staticAnalysisPackages>
    <format>svg</format>
    <fileName>place-order-flow</fileName>
    <includeFlowsFrom>
        <includeFlowFrom>io.domainlifecycles.test.order.PlaceOrder</includeFlowFrom>
    </includeFlowsFrom>
    <flowMaxDepth>5</flowMaxDepth>
</diagram>
```

For the full semantics of `includeFlowsFrom` and the flow traversal settings, see the domain-diagrammer's
["Restricting a diagram to a flow"](../domain-diagrammer/readme.md#restricting-a-diagram-to-a-flow) section, and for
background on the underlying static analysis, see the [static-analysis readme](../static-analysis/readme.md).

## How to read DLC Domain Diagrams?

A DLC Domain Diagram is a UML-like diagram generated by analyzing DLC marker interfaces.
These marker interfaces are rendered as stereotypes on the interfaces or classes that extend or implement them.
Only classes and interfaces that implement or extend these marker interfaces are included in the diagram.
Therefore, the diagrams do not represent the complete UML structure of the entire program—they show only the implemented DDD concepts.

For more details, have a look at our [concept description](./../concepts/readme.md).

### Rendered edges

The following list explains how to interpret the rendered edges:
- DomainCommand → ApplicationService:
A DomainCommand is passed to an ApplicationService. The ApplicationService provides a method to process the command.

- ApplicationService → DomainService:
An ApplicationService calls a DomainService.

- ApplicationService → OutboundService:
An ApplicationService calls an OutboundService.

- ApplicationService → Repository:
An ApplicationService calls a Repository.

- ApplicationService → QueryHandler:
An ApplicationService calls a QueryHandler.

- ApplicationService → DomainEvent:
An ApplicationService publishes a DomainEvent.

- DomainService → DomainService:
A DomainService calls another DomainService.

- DomainService → Repository:
A DomainService calls a Repository.

- DomainService → OutboundService:
A DomainService calls an OutboundService.

- DomainService → QueryHandler:
A DomainService calls a QueryHandler.

- DomainService → DomainEvent:
A DomainService publishes a DomainEvent.

- Repository → Aggregate:
A Repository provides access to an Aggregate.

- Aggregate → DomainEvent:
An Aggregate publishes a DomainEvent.

- QueryHandler → ReadModel:
A QueryHandler provides a ReadModel.

- DomainEvent → Aggregate:
An Aggregate listens for a DomainEvent.

- DomainEvent → ApplicationService:
An ApplicationService listens for a DomainEvent.

- DomainEvent → DomainService:
A DomainService listens for a DomainEvent.

- DomainEvent → OutboundService:
An OutboundService listens for a DomainEvent.

- DomainEvent → QueryHandler:
A QueryHandler listens for a DomainEvent.

### Rendering intheritance
By default, not all inheritance structures are rendered.
The diagram always shows the most concrete implementations of the analyzed classes.

If you want to render all inheritance structures, set `showAllInheritanceStructures` to `true`.
Alternatively, you can enable specific inheritance structures individually by setting their corresponding options to `true.

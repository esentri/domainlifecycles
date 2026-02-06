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
    id 'io.domainlifecycles.dlc-gradle-plugin' version '3.0.0'
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
                useModule("io.domainlifecycles:dlc-gradle-plugin:3.0.0")
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
    }
}
```
Specify the packages you want to be scanned by the Diagram-Viewer. These can later on be changed, or your diagrams 
can be specified even further.
You can generate a new API-Key by clicking on the profile tab in the Diagram-Viewer App.
All classes that the model consists of must be defined within the `domainModelPackages`.

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
            <version>3.0.0</version>
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
            <version>3.0.0</version>
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
            <version>3.0.0</version>
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

## How to read DLC Domain Diagrams?

A DLC Domain Diagram is a UML-like diagram generated by analyzing DLC marker interfaces.
These marker interfaces are rendered as stereotypes on the interfaces or classes that extend or implement them.
Only classes and interfaces that implement or extend these marker interfaces are included in the diagram.
Therefore, the diagrams do not represent the complete UML structure of the entire program—they show only the implemented DDD concepts.

For more details, have a look at our [concept description](./../concepts/readme.md).

### Rendered edges

The following list explains how to interpret the rendered edges:
- DomainCommand → ApplicationService:
A DomainCommand is passed to an ApplicationService. The ApplicationService provides a method to receive the command.

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

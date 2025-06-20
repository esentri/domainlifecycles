# DLC Plugins

The DLC plugins provide several functions to integrate DLC in the build phase:
- creating domain diagrams of the build domain classes within the bounded contexts
- exporting a JSON domain model
- uploading the domain model based on the current implementation to a DLC Domain Viewer instance

## General prerequisites
To create class diagrams the plugins use a Kroki Docker container, so Docker should be available on the machine running the plugin.

## DLC-Gradle-Plugin

### Class-Diagram
The plugin is able to create class diagrams in various formats of your implemented domain model.

#### Configuration
An example configuration in your project could look like the following:
```groovy
plugins {
    id 'io.domainlifecycles.dlc-gradle-plugin' version '2.4.0'
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
                useModule("io.domainlifecycles:dlc-gradle-plugin:2.4.0")
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
- `svg`

Supported Diagram configuration options are
- aggregateRootStyle: e.g "fill=#333333 bold"
- aggregateFrameStyle
- entityStyle
- valueObjectStyle
- enumStyle
- identityStyle
- domainEventStyle
- domainCommandStyle
- applicationServiceStyle
- domainServiceStyle
- repositoryStyle
- readModelStyle
- queryHandlerStyle
- outboundServiceStyle
- font
- direction: "right" or "down"
- ranker: see Nomnoml
- acycler: see Nomnoml
- backgroundColor
- classesBlacklist
- showFields
- showFullQualifiedClassNames
- showAssertions
- showMethods
- showOnlyPublicMethods
- showDomainEvents
- showDomainEventFields
- showDomainEventMethods
- showDomainCommands
- showOnlyTopLevelDomainCommandRelations
- showDomainCommandFields
- showDomainCommandMethods
- showDomainServices
- showDomainServiceFields
- showDomainServiceMethods
- showApplicationServices
- showApplicationServiceFields
- showApplicationServiceMethods
- showRepositories
- showRepositoryFields
- showRepositoryMethods
- showReadModels
- showReadModelFields
- showReadModelMethods
- showQueryHandlers
- showQueryHandlerFields
- showQueryHandlerMethods
- showOutboundServices
- showOutboundServiceFields
- showOutboundServiceMethods
- showUnspecifiedServiceKinds
- showUnspecifiedServiceKindFields
- showUnspecifiedServiceKindMethods
- callApplicationServiceDriver
- fieldBlacklist
- methodBlacklist
- showInheritedMembersInClasses
- showObjectMembersInClasses
- multiplicityInLabel
- fieldStereotypes
- includeConnectedTo: list of full qualified classnames (all classes connected are included)
- includeConnectedToIngoing: list of full qualified classnames (classes and ingoing connected classes are included)
- includeConnectedToOutgoing: : list of full qualified classnames (classes and outgoing connected classes are included)
- excludeConnectedToIngoing: : list of full qualified classnames (classes and ingoing connected classes are excluded)
- excludeConnectedToOutgoing: : list of full qualified classnames (classes and outgoing connected classes are excluded)
- explicitlyIncludedPackages: packages explicitly included in the diagram
- showAllInheritanceStructures: boolean, default false
- showInheritanceStructuresInAggregates: boolean, default true
- showInheritanceStructuresForServiceKinds: boolean, default false
- showInheritanceStructuresForReadModels: boolean, default false
- showInheritanceStructuresForDomainEvents: boolean, default false
- showInheritanceStructuresForDomainCommands: boolean, default false

#### Run
```bash
gradle createDiagram
```

### JSON Render
Besides creating class diagrams, the plugin is also able to generate a JSON-File containing your domain model.

#### Configuration
An example configuration in your project could look like the following:
```groovy
dlcGradlePlugin {
    jsonModel {
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
Similar to the diagram configuration above you need to specify where your JSON file should be saved to and its name,
and finally the packages where the model should be read from.

The specified DomainModel must be complete an self-contained. 
All classes that the model consists of must be defined within the `domainModelPackages`.

#### Run
```bash
gradle renderJson
```

### Diagram-Viewer Integration
If you have an instance of the Diagram-Viewer app running, whether it be on your local machine or on a hosted platform,
you are able to quickly create new or update existing projects, without the need to upload a packaged archive file of
your project in the UI.

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
            <version>2.4.0</version>
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
and even different packages which should be used to read the model with `contextPackages`.

Currently supported formats are:
- `nomnoml`
- `svg`

Supported Diagram configuration options are
- aggregateRootStyle: e.g "fill=#333333 bold"
- aggregateFrameStyle
- entityStyle
- valueObjectStyle
- enumStyle
- identityStyle
- domainEventStyle
- domainCommandStyle
- applicationServiceStyle
- domainServiceStyle
- repositoryStyle
- readModelStyle
- queryHandlerStyle
- outboundServiceStyle
- font
- direction: "right" or "down"
- ranker: see Nomnoml
- acycler: see Nomnoml
- backgroundColor
- classesBlacklist
- showFields
- showFullQualifiedClassNames
- showAssertions
- showMethods
- showOnlyPublicMethods
- showDomainEvents
- showDomainEventFields
- showDomainEventMethods
- showDomainCommands
- showOnlyTopLevelDomainCommandRelations
- showDomainCommandFields
- showDomainCommandMethods
- showDomainServices
- showDomainServiceFields
- showDomainServiceMethods
- showApplicationServices
- showApplicationServiceFields
- showApplicationServiceMethods
- showRepositories
- showRepositoryFields
- showRepositoryMethods
- showReadModels
- showReadModelFields
- showReadModelMethods
- showQueryHandlers
- showQueryHandlerFields
- showQueryHandlerMethods
- showOutboundServices
- showOutboundServiceFields
- showOutboundServiceMethods
- showUnspecifiedServiceKinds
- showUnspecifiedServiceKindFields
- showUnspecifiedServiceKindMethods
- callApplicationServiceDriver
- fieldBlacklist
- methodBlacklist
- showInheritedMembersInClasses
- showObjectMembersInClasses
- multiplicityInLabel
- fieldStereotypes
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

#### Run
Depending on the Maven phase you specified:
```bash
mvn clean compile
```

### JSON Render
Besides creating class diagrams, the plugin is also able to generate a JSON-File containing your domain-model.

#### Configuration
An example configuration in your project could look like the following:
```xml
<build>
    <plugins>
        <plugin>
            <groupId>io.domainlifecycles</groupId>
            <artifactId>dlc-maven-plugin</artifactId>
            <version>2.4.0</version>
            <executions>
                <execution>
                    <id>renderJson</id>
                    <phase>compile</phase>
                    <goals>
                        <goal>renderJson</goal>
                    </goals>
                    <configuration>
                        <fileOutputDir>target</fileOutputDir>
                        <serializations>
                            <serialization>
                                <fileName>model_1</fileName>
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
Similar to the diagram configuration above you need to specify where your JSON file should be saved to and its name,
and finally the packages where the model should be read from.

#### Run
Depending on the Maven phase you specified:
```bash
mvn clean compile
```

### Diagram-Viewer Integration
If you have an instance of the Diagram-Viewer app running, whether it be on your local machine or on a hosted platform,
you are able to quickly create new or update existing projects, without the need to upload a packaged archive file of
your project in the UI.

#### Configuration
An example configuration in your project could look like the following:
```xml
<build>
    <plugins>
        <plugin>
            <groupId>io.domainlifecycles</groupId>
            <artifactId>dlc-maven-plugin</artifactId>
            <version>2.4.0</version>
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
mvn clean compile
```

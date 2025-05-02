# DLC Plugins

The DLC plugin provide several functions to integrate DLC in the build phase:
- creating domain diagrams of the build domain classes within the bounded contexts
- exporting a JSON domain model

## General prerequisites
To create class diagrams the plugins use a Kroki Docker container, so Docker should be available on the machine running the plugin.

## DLC-Gradle-Plugin

### Class-Diagram
The plugin is able to create class diagrams in various formats of your implemented domain model.

#### Configuration
An example configuration in your project could look like the following:
```groovy
dlcGradlePlugin {
    diagram {
        fileOutputDir = layout.buildDirectory
        diagrams {
            diagramNomnoml {
                domainModelPackages = ["io.domainlifecycles.test"]
                filteredPackageNames = ["io.domainlifecycles.test.mycontext"]
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
To render only a specific part of the model, use the `filteredPackageNames`configuration option.

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
- transitiveFilterSeedDomainServiceTypeNames
- filteredPackageNames: packages explicitly included in the diagram   
- showAbstractTypes: boolean, default false
- useAbstractTypeNameForConcreteServiceKinds: boolean, default true

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
                serial{
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
            <version>2.3.0</version>
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
                                <filteredPackages>
                                    <filteredPackage>io.domainlifecycles.test.mycontext</filteredPackage>
                                </filteredPackages>
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
- transitiveFilterSeedDomainServiceTypeNames
- filteredPackageNames: packages explicitly included in the diagram
- showAbstractTypes: boolean, default false
- useAbstractTypeNameForConcreteServiceKinds: boolean, default true

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
            <version>2.3.0</version>
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

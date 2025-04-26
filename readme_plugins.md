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
                contextPackages = ["io.domainlifecycles.test"]
                format = "nomnoml"
                fileName = "diagram"
            }
            diagramSvg {
                contextPackages = ["io.domainlifecycles.test"]
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
and even different packages which should be used to read the model with `contextPackages`.
\
You can rename `diagramNomnoml` and `diagramSvg` of course according to your needs, however it's important
to specify some name, otherwise Gradle is not able to read the configuration properly.

Currently supported formats are:
- `nomnoml`
- `svg`

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
        fileName = "model"
        contextPackages = ["io.domainlifecycles.test"]
    }
}
```
Similar to the diagram configuration above you need to specify where your JSON file should be saved to and its name,
and finally the packages where the model should be read from.

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
                                <contextPackages>
                                    <contextPackage>io.domainlifecycles.test</contextPackage>
                                </contextPackages>
                                <format>nomnoml</format>
                                <fileName>diagram</fileName>
                            </diagram>
                            <diagram>
                                <contextPackages>
                                    <contextPackage>io.domainlifecycles.test</contextPackage>
                                </contextPackages>
                                <format>svg</format>
                                <fileName>diagram</fileName>
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
                                <contextPackages>
                                    <contextPackage>io.domainlifecycles.test</contextPackage>
                                </contextPackages>
                            </serialization>
                            <serialization>
                                <fileName>model_2</fileName>
                                <contextPackages>
                                    <contextPackage>io.domainlifecycles.test</contextPackage>
                                </contextPackages>
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

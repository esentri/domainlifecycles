# NitroX Domain Lifecycles Diagrammer (EXPERIMENTAL)

The NitroX DLC Diagrammer currently generates [Nomnoml](https://nomnoml.com/) diagram texts.
Nomnoml is tool for drawing UML diagrams based on a simple syntax. 

The generated diagram text currently describes UML class diagram, which sets the focus on the used DDD patterns.
Most of the meta-information needed is derived from the used Nitrox DLC marker interfaces for the DDD [Building Blocks](./readme_building_blocks.md). 

Have a look at our [sample project](./dlc-sample), and diagram text generated [here](./dlc-domain-diagrammer/src/test/java/nomnoml/generator/NomnomlDomainDiagramGeneratorTest.java) 
by executing the test `generateSampleApp()` to the standard output. 

There are several options to adjust the diagram settings, have a look at `config.domain.io.domainlifecycles.diagram.DomainDiagramConfig`.

## Example how to use it

1. Add `nitrox:dlc-domain-diagrammer` dependency to your project (in your test setup)
Gradle setup:
```Groovy
dependencies{
    testImplementation 'nitrox:dlc-domain-diagrammer:2.0.0'
}
```

Maven setup:
```XML
<dependency>
    <groupId>nitrox</groupId>
    <artifactId>dlc-domain diagrammer</artifactId>
    <version>2.0.0</version>
    <scope>test</scope>
</dependency>
```

2. Add a test like the following
```Java
class NomnomlDomainDiagramGeneratorTest {
    
    @Test
    void generateSampleApp() throws Exception {
        Domain.setGenericTypeResolver(new TypeMetaResolver());
        Domain.initialize(new ReflectiveDomainMirrorFactory("yourdomain"));
        DomainDiagramConfig diagramConfig = DomainDiagramConfig.builder().withContextPackageName("yourdomain").build();
        DomainDiagramGenerator generator = new DomainDiagramGenerator(
                diagramConfig);
        
        String actualDiagramText = generator.generateDiagramText();
        File file = new File("generated_nomnoml_example.nomnoml");
        FileUtils.writeStringToFile(file, actualDiagramText, StandardCharsets.UTF_8);
    }
} 
```
3. After running the test you can copy the generated output from `generated_nomnoml_example.nomnoml` into [`https://nomnoml.com/`](https://nomnoml.com/) to view and even edit the diagram.
    - You can also save images from there
    - On Nomnoml projects page, you can find information on how to generate images directly or to host your own Nomnoml drawer instance via Docker.

![What a pity you cannot see it](documentation/resources/images/sample_diagram.png "Nomnoml based DDD class diagram")

## Rendering from commandline to image

First install `nomnom-cli` via `npm`.
- $ npm install nomnoml-cli -g

Then render any nomnoml file into an image.
- $ cat graph.nomnoml | nomnoml > graph.png

More on the usage of `nomnoml-cli`: https://github.com/prantlf/nomnoml-cli

## Requirements

To render something useful, your domain implementation must implement/use the marker interfaces and annotations from [DLC Domain types](./dlc-types).  
Also we at least need the [domain mirror](./dlc-mirror), to be able to provide all the needed domain metadata within the rendering process.

Those dependencies are provided like:
```Groovy
dependencies{
    implementation 'nitrox:dlc-mirror:2.0.0'
    implementation 'nitrox:dlc-types:2.0.0'
}
```

Maven setup:
```XML
<dependency>
    <groupId>nitrox</groupId>
    <artifactId>dlc-mirror</artifactId>
    <version>2.0.0</version>
</dependency>
<dependency>
    <groupId>nitrox</groupId>
    <artifactId>dlc-types</artifactId>
    <version>2.0.0</version>
</dependency>
```

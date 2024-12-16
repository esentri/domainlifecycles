<a href="./_index.md">Getting Started</a> / <a href="./configuration.md">Konfiguration</a>

<hr/>

# Konfiguration

<hr/>

## Persistence
Zusätzlich zur folgenden Konfiguration finden sich <a href="./features/persistence.md">hier</a> Beispiele zur Implementierung.

### Datenbank Driver
Zunächst muss allgemein zur Nutzung einer Datenbank der jeweilige Database-Driver eingebunden werden.
Da DLC JOOQ nutzt (siehe unten), werden hierfür nahezu alle relationalen SQL basierten Datenbanken unterstützt.
Eine Auflistung aller unterstützten Datenbanken findet sich <a href="https://www.jooq.org/doc/latest/manual/reference/supported-rdbms/">hier</a>.

Im Folgenden wird für die beispielhaften Konfigurationen eine interne H2-Datenbank genutzt.

<details>
<summary><img style="height: 12px" src="icons/gradle.svg"> <b>build.gradle</b></summary>

```groovy
dependencies {
    implementation 'com.h2database:h2:2.3.232'
}
```
</details>

<details>
<summary><img style="height: 12px" src="icons/file-type-maven.svg"> <b>pom.xml</b></summary>

```xml name="index.js"
<dependencies>
    <dependency>
        <groupId>com.h2database</groupId>
        <artifactId>h2</artifactId>
        <version>2.3.232</version>
    </dependency>
</dependencies>
```
</details>

Zusätzlich zur benötigten Dependency noch die jeweilige Konfiguration:

<details>
<summary><b>application.properties</b></summary>

```properties
spring.datasource.url=jdbc:h2:./dlc-sample/build/h2-db/test;AUTO_SERVER=TRUE
spring.datasource.driver-class-name=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=
```
</details>

### JOOQ
Als JDBC-Ersatz und Code-Generator kommt bei DLC JOOQ zum Einsatz, um das Arbeiten mit SQL basierten 
relationalen Datenbanken zu erleichtern. 
Hierfür wird zusätzlich noch die Spring-Boot spezifische JOOQ Dependency gebraucht:

<details>
<summary><img style="height: 12px" src="icons/gradle.svg"> <b>build.gradle</b></summary>

```groovy
dependencies {
    implementation 'org.springframework.boot:spring-boot-starter-jooq'
}
```
</details>

<details>
<summary><img style="height: 12px" src="icons/file-type-maven.svg"> <b>pom.xml</b></summary>

```xml
<dependencies>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-jooq</artifactId>
    </dependency>
</dependencies>
```
</details>

Anschließend kann man den JOOQ Code-Generator nach Belieben konfigurieren
Im Folgenden findet sich eine beispielhafte Konfiguration, für ein bereits bestehendes Datenbank-Schema:

<details>
<summary><img style="height: 12px" src="icons/gradle.svg"> <b>build.gradle</b></summary>

```groovy
jooq {
    configurations {
        main {
            generationTool {
                jdbc {
                    driver = 'org.h2.Driver'
                    url = "jdbc:h2:file:./build/h2-db/test;NON_KEYWORDS=VALUE;AUTO_SERVER=TRUE"
                    user = 'sa'
                    password = ''
                }
                generator {
                    database {
                        name = 'org.jooq.meta.h2.H2Database'
                        includes = '.*'
                        inputSchema = "${your_input_schema_name}"
                        recordVersionFields = 'CONCURRENCY_VERSION'
                        forceIntegerTypesOnZeroScaleDecimals = true
                    }
                    generate {
                        generatedAnnotation = false
                        generatedAnnotationType = 'DETECT_FROM_JDK'
                        javaTimeTypes = true
                    }
                    target {
                        packageName = "${your_package_name}"
                    }
                }
            }
        }
    }
}
```
</details>

Die Konfiguration für Maven findet nicht in der pom.xml statt, sondern in einer separaten Konfigurations-Datei, welche
im Projekt-Ordner liegen muss.

<details>
<summary><img style="height: 12px" src="icons/file-type-maven.svg"> <b>library.xml</b></summary>

```xml
<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<configuration xmlns="http://www.jooq.org/xsd/jooq-codegen-3.12.0.xsd">
    <jdbc>
        <driver>org.h2.Driver</driver>
        <url>jdbc:h2:file:./build/h2-db/test;NON_KEYWORDS=VALUE;AUTO_SERVER=TRUE</url>
        <user>sa</user>
        <password></password>
    </jdbc>
    <generator>
        <database>
            <name>org.jooq.meta.h2.H2Database</name>
            <includes>.*</includes>
            <inputSchema>${your_input_schema_name}</inputSchema>
            <recordVersionFields>CONCURRENCY_VERSION</recordVersionFields>
            <forceIntegerTypesOnZeroScaleDecimals>true</forceIntegerTypesOnZeroScaleDecimals>
        </database>
        <generate>
            <generatedAnnotation>false</generatedAnnotation>
            <generatedAnnotationType>DETECT_FROM_JDK</generatedAnnotationType>
            <javaTimeTypes>true</javaTimeTypes>
        </generate>
        <target>
            <packageName>${your_package_name}</packageName>
        </target>
    </generator>
</configuration>
```
</details>

Weitere Informationen zur Konfiguration von JOOQ finden sich <a href="">hier</a>.

### Spring Beans
Die folgenden Spring-Beans müssen konfiguriert werden und in einer ```@Configuration``` Klasse als ```@Bean``` bereitgestellt werden:

#### DataSourceConnectionProvider
```
@Bean
public DataSourceConnectionProvider connectionProvider(DataSource dataSource) {
    return new DataSourceConnectionProvider(new TransactionAwareDataSourceProxy(dataSource));
}
```

<br/>

#### DefaultConfiguration
```
@Bean
public DefaultConfiguration configuration(DataSource dataSource) {
    final var jooqConfig = new DefaultConfiguration();
    jooqConfig.settings().setExecuteWithOptimisticLocking(true);
    jooqConfig.setConnectionProvider(connectionProvider(dataSource));
    jooqConfig.set(SQLDialect.H2);
    return jooqConfig;
}
```

<br/>

#### DefaultDSLContext
```
@Bean
public DefaultDSLContext dslContext(DataSource dataSource) {
    return new DefaultDSLContext(configuration(dataSource));
}
```

<br/>

#### JooqDomainPersistenceProvider
Der JooqDomainPersistenceProvider ermöglicht einen JOOQ spezifischen Zugriff auf alle Domain-Objekte.

```
@Bean
public JooqDomainPersistenceProvider domainPersistenceProvider(DomainObjectBuilderProvider domainObjectBuilderProvider,
                Set<RecordMapper<?, ?, ?>> customRecordMappers) {
    return new JooqDomainPersistenceProvider(
        JooqDomainPersistenceConfiguration.JooqPersistenceConfigurationBuilder
            .newConfig()
            .withDomainObjectBuilderProvider(domainObjectBuilderProvider)
            .withCustomRecordMappers(customRecordMappers)
            .withRecordPackage(JOOQ_RECORD_PKG)
            .make());
}
```

<br/>

#### EntityIdentityProvider
Der EntityIdentityProvider ermöglicht es, dass neue Entitäten oder AggregateRoots
von außerhalb (z. B. über einen REST-Controller) in die Anwendung eingebracht werden
können und dass für neue Instanzen neue IDs aus den entsprechenden Datenbanksequenzen oder anderen ID-Providern
abgerufen werden.
Wird nur benutzt in Zusammenhang mit ```DlcJacksonModule```, siehe unten.

```
@Bean
EntityIdentityProvider identityProvider(DSLContext dslContext) {
    return new JooqEntityIdentityProvider(dslContext);
}
```


<hr/>


## Domain-Object-Builders
Zusätzlich zur folgenden Konfiguration finden sich <a href="./features/domainobject_builders.md">hier</a> Beispiele zur Implementierung.

### Spring-Beans
Die folgenden Spring-Beans müssen konfiguriert werden und in einer ```@Configuration``` Klasse als ```@Bean``` bereitgestellt werden:

#### DomainObjectBuilderProvider
Der DomainObjectBuilderProvider wird benötigt um mit inner-Builders oder den Lombok-Builders zu arbeiten.

```
@Bean
DomainObjectBuilderProvider innerClassDomainObjectBuilderProvider() {
    return new InnerClassDomainObjectBuilderProvider();
}
```


<hr/>


## JSON-Mapping
Zusätzlich zur folgenden Konfiguration finden sich <a href="./features/json_mapping.md">hier</a> Beispiele zur Implementierung.

### Spring-Beans
Die folgenden Spring-Beans müssen konfiguriert werden und in einer ```@Configuration``` Klasse als ```@Bean``` bereitgestellt werden:

#### DlcJacksonModule
Benötigt für DLC-Jackson Integration.

```
@Bean
DlcJacksonModule dlcModuleConfiguration(List<? extends JacksonMappingCustomizer<?>> customizers,
                                        DomainObjectBuilderProvider domainObjectBuilderProvider,
                                        EntityIdentityProvider entityIdentityProvider
) {
    DlcJacksonModule module = new DlcJacksonModule(domainObjectBuilderProvider, entityIdentityProvider);
        customizers.forEach(c -> module.registerCustomizer(c, c.instanceType));
    return module;
}
```

## Domain-Events
Zusätzlich zur folgenden Konfiguration finden sich <a href="./features/domain_events.md">hier</a> Beispiele zur Implementierung.

### Spring-Beans
Die folgenden Spring-Beans müssen konfiguriert werden und in einer ```@Configuration``` Klasse als ```@Bean``` bereitgestellt werden:

#### SpringPersistenceEventPublisher
Wird benötigt, um DLC-Events über den Spring event bus zu veröffentlichen.

```
@Bean
public SpringPersistenceEventPublisher springPersistenceEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
    return new SpringPersistenceEventPublisher(applicationEventPublisher);
}
```

<br/>

#### ChannelRoutingConfiguration
Stellt eine Konfiguration für das Channel Routing bereit.

```
@Bean
public ChannelRoutingConfiguration channelConfiguration(PlatformTransactionManager platformTransactionManager, ServiceProvider serviceProvider){
        var channel = new SpringTxInMemoryChannelFactory(platformTransactionManager, serviceProvider, true).processingChannel("default");
        var router = new DomainEventTypeBasedRouter(List.of(channel));
        router.defineDefaultChannel("default");
        return new ChannelRoutingConfiguration(router);
}
```

## Domain-Types
Zusätzlich zur folgenden Konfiguration finden sich <a href="./features/domain_types.md">hier</a> Beispiele zur Implementierung.

### Spring-Beans
Die folgenden Spring-Beans müssen konfiguriert werden und in einer ```@Configuration``` Klasse als ```@Bean``` bereitgestellt werden:

#### ServiceProvider
Stellt einen Provider für alle benötigten ```ServiceKind``` Objekte bereit.

```
public ServiceProvider serviceProvider(List<ServiceKind> serviceKinds){
    return  new Services(serviceKinds);
}
```

<hr/>


## Open-API-Extension
Zusätzlich zur folgenden Konfiguration finden sich <a href="./features/open_api_extension.md">hier</a> Beispiele zur Implementierung.

### Spring-Beans
Die folgenden Spring-Beans müssen konfiguriert werden und in einer ```@Configuration``` Klasse als ```@Bean``` bereitgestellt werden:

#### DlcOpenApiCustomizer
Stellt die Konfiguration/Anpassungen der DLC-/OpenAPI-Integration bereit.

```
@Bean
public DlcOpenApiCustomizer openApiCustomizer(SpringDocConfigProperties springDocConfigProperties) {
    return new DlcOpenApiCustomizer(springDocConfigProperties);
}
```

<hr/>

|          **Projekt erstellen**            |             **DLC starten**              |
|:-----------------------------------------:|:----------------------------------------:|
| [<< Vorherige Seite](./create_project.md) | [Nächste Seite >>](./run_application.md) |

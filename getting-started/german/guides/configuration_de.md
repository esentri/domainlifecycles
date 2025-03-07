[Getting Started](../index_de.md) / [Konfiguration](configuration_de.md)

---

# Konfiguration

DLC bietet viele Möglichkeiten für Anpassungen und spezifische Konfiguration. Einige Konfigurationen 
müssen verpflichtend vorgenommen werden, damit der volle Funktionsumfang gewährleistet werden kann.
Im Folgenden befinden sich alle Konfigurationen (bzw. in diesem Beispiel per Spring-Beans), welche für die grundlegenden DLC-Funktionen
benötigt werden. Hierbei sind alle Beans sortiert nach dem jeweiligen DLC-Feature, welchem sie zuzuordnen sind.
Im Guide zu den [Features](features_de.md) wird an vielen Stellen nochmal näher auf einige Stellen eingegangen und 
rückverwiesen.

---

## Persistence
Das Persistence Modul ist zuständig für alle Interaktionen zwischen DLC und einer relationalen Datenbank bzgl. 
der entsprechenden Abbildung von Aggregaten und dem entsprechenden Persistenz-Zugriff per Repositories.

Zusätzlich zur folgenden Konfiguration finden sich [hier](../features/persistence_de.md) Beispiele zur Implementierung.

### Datenbank Driver
Zunächst muss allgemein zur Nutzung einer Datenbank der jeweilige Datenbank Treiber eingebunden werden.
Da DLC jOOQ nutzt (siehe unten), werden hierfür nahezu alle relationalen SQL basierten Datenbanken unterstützt.
Eine Auflistung aller unterstützten Datenbanken findet sich 
[hier](https://www.jooq.org/doc/latest/manual/reference/supported-rdbms/").

Im Folgenden wird für die beispielhaften Konfigurationen eine interne H2-Datenbank genutzt.

<details>
<summary><img style="height: 12px" src="../../icons/gradle.svg" alt="gradle"> <b>build.gradle</b></summary>

```groovy
dependencies {
    implementation 'com.h2database:h2:2.3.232'
}
```
</details>

<details>
<summary><img style="height: 12px" src="../../icons/file-type-maven.svg" alt="maven"> <b>pom.xml</b></summary>

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

Außerdem zur benötigten Dependency noch die jeweilige Konfiguration in den Spring Application Properties:

<details>
<summary><b>application.properties</b></summary>

```properties
spring.datasource.url=jdbc:h2:./dlc-sample/build/h2-db/test;AUTO_SERVER=TRUE
spring.datasource.driver-class-name=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=
```
</details>

### jOOQ
Für einfacheren und vor allem typsicheren JDBC-Zugriff kommt bei DLC, das Framework jOOQ zum Einsatz.
Das erleichtert die Arbeit mit SQL basierten relationalen Datenbanken und abstrahiert SQL Dialekt spezifische Eigenheiten.
Hierfür wird zusätzlich noch die Spring-Boot spezifische jOOQ Dependency benötigt:

<details>
<summary><img style="height: 12px" src="../../icons/gradle.svg" alt="gradle"> <b>build.gradle</b></summary>

```groovy
dependencies {
    implementation 'org.springframework.boot:spring-boot-starter-jooq'
}
```
</details>

<details>
<summary><img style="height: 12px" src="../../icons/file-type-maven.svg" alt="maven"> <b>pom.xml</b></summary>

```xml
<dependencies>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-jooq</artifactId>
    </dependency>
</dependencies>
```
</details>

Bei jOOQ werden typischerweise Java-Klassen für den Zugriff auf Datenbank-Objekte (z.B. Tabellen und Sequences) generiert.
Hierfür muss der jOOQ Code-Generator im Build-Management eingebunden werden.

Ab jOOQ Version 3.19 kann der Gradle jOOQ Codegenerator über Plugins eingebunden werden. 
Es wird dringend empfohlen, diese Versionen zu nutzen.

Zunächst müssen die Abhängigkeiten für den Generator definiert werden

<details>
<summary><img style="height: 12px" src="../../icons/gradle.svg" alt="gradle"> <b>build.gradle</b></summary>

```groovy
dependencies {
    runtimeOnly 'org.jooq:jooq-codegen:3.19:10'
    jooqCodegen 'org.jooq:jooq-meta:3.19:10'
}
```
</details>

Anschließend kann der Generator konfiguriert werden. 
Zur Veranschaulichung hier eine beispielhafte Konfiguration, für ein bereits bestehendes Datenbank-Schema:
<details>
<summary><img style="height: 12px" src="../../icons/gradle.svg" alt="gradle"> <b>build.gradle</b></summary>

```groovy
jooq {
    configuration {

        jdbc {
            driver = dbDriver
            url = dbUrl
            user = dbUsername
            password = dbPassword
        }

        generator {
            database {
                name = "org.jooq.meta.h2.H2Database"
                includes = ".*"
                inputSchema = 'SHOP_DOMAIN'
                recordVersionFields = 'CONCURRENCY_VERSION'
            }

            generate {}
            target {
                packageName = "sampleshop"
            }
        }
    }
}
```
</details>

Weiterhin ist zu beachten, dass definiert werden sollte, wann der Code-Generator ausgeführt werden soll.

<details>
<summary><img style="height: 12px" src="../../icons/gradle.svg" alt="gradle"> <b>build.gradle</b></summary>

```groovy
tasks.named("compileJava") {
    dependsOn(tasks.named("jooqCodegen"))
}
```
</details>

Werden Tools wie Flyway o.Ä. genutzt, muss auf die richtige Ausführung des Code-Generators von jOOQ geachtet werden.

<details>
<summary><img style="height: 12px" src="../../icons/gradle.svg" alt="gradle"> <b>build.gradle</b></summary>

```groovy
tasks.named("jooqCodegen") {
    dependsOn(tasks.named("flywayMigrate"))
    inputs.files(fileTree("src/main/resources/db/migration"))
}
```
</details>

Ausführliche Informationen sind [in der Dokumentation von jOOQ zu finden](https://www.jooq.org/doc/latest/manual/code-generation/codegen-gradle/).


Die Konfiguration für Maven findet in der pom.xml statt.

<details>
<summary><img style="height: 12px" src="../../icons/file-type-maven.svg" alt="maven"> <b>pom.xml</b></summary>

```xml
<plugin>
    <groupId>org.jooq</groupId>
    <artifactId>jooq-codegen-maven</artifactId>
    <version>3.19.16</version>

    <!-- The plugin should hook into the generate goal -->
    <executions>
        <execution>
            <goals>
                <goal>generate</goal>
            </goals>
        </execution>
    </executions>

    <!-- Manage the plugin's dependency. In this example, we'll use a H2 database -->
    <dependencies>
        <dependency>
            <groupId>com.h2database</groupId>
            <artifactId>h2</artifactId>
            <version>2.3.232</version>
        </dependency>
    </dependencies>
    
    <configuration>
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
</plugin>
```
</details>

Für DLC Persistence ist hierbei insbesondere die notwendige ```CONCURRENCY_VERSION``` zu beachten, damit das optimistische 
Locking von Aggregaten und Entities korrekt umgesetzt wird. 

Weitere Informationen zur Konfiguration von JOOQ finden sich 
(https://www.jooq.org/doc/latest/manual/code-generation/codegen-configuration/)[hier].

### Spring Beans
Speziell für den Datenbank-Zugriff mit jOOQ werden folgende Konfigurationen benötigt:
```DataSourceConnectionProvider```, ```DefaultConfiguration```, ```DefaultDSLContext```.

#### `DataSourceConnectionProvider`
```Java
@Bean
public DataSourceConnectionProvider connectionProvider(DataSource dataSource) {
    return new DataSourceConnectionProvider(new TransactionAwareDataSourceProxy(dataSource));
}
```


#### `DefaultConfiguration`
```Java
@Bean
public DefaultConfiguration configuration(DataSource dataSource) {
    final var jooqConfig = new DefaultConfiguration();
    jooqConfig.settings().setExecuteWithOptimisticLocking(true);
    jooqConfig.setConnectionProvider(connectionProvider(dataSource));
    jooqConfig.set(SQLDialect.H2);
    return jooqConfig;
}
```

Der passende SQLDialect und die Aktivierung von Optimistic Locking ist hier zu beachten.


#### `DefaultDSLContext`
```Java
@Bean
public DefaultDSLContext dslContext(DataSource dataSource) {
    return new DefaultDSLContext(configuration(dataSource));
}
```

Weitere Konfigurationen, müssen speziell für DLC Persistence vorgenommen werden:

#### `JooqDomainPersistenceProvider`
Der JooqDomainPersistenceProvider ermöglicht einen jOOQ spezifischen Zugriff auf alle Domain-Objekte.

```Java
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

Angepasste RecordMappers für abweichendes Mapping-Verhalten zwischen Datenbank-Tabellen und Domänen-Objekten können hier übergeben werden.
Das ```recordPackage``` muss das Package der vom jOOQ-Generator erzeugten Klassen für den Zugriff auf Datenbankobjekte zeigen.  

#### `EntityIdentityProvider`
Der EntityIdentityProvider ermöglicht es, dass neue Entitäten oder AggregateRoots
von außerhalb (z. B. über einen REST-Controller) in die Anwendung eingebracht werden
können und dass für neue Instanzen neue IDs aus den entsprechenden Datenbanksequenzen oder anderen ID-Providern
abgerufen werden.
Wird nur benutzt in Zusammenhang mit ```DlcJacksonModule```, siehe unten.

```Java
@Bean
EntityIdentityProvider identityProvider(DSLContext dslContext) {
    return new JooqEntityIdentityProvider(dslContext);
}
```

#### `SpringPersistenceEventPublisher`
Optional, wird benötigt, um DLC-Persistence-Events über den Spring Event Bus zu veröffentlichen.
DLC Persistence Events veröffentlichen Informationen über Veränderungen an Aggregates
im Rahmen von schreibenden Repository-Zugriffen.

```Java
@Bean
public SpringPersistenceEventPublisher springPersistenceEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
return new SpringPersistenceEventPublisher(applicationEventPublisher);
}
```

---


## Domain-Object-Builders
Domain Object Builder werden von DLC intern benötigt, beispielsweise um Instanzen von Domänenobjekten beim 
objekt-relationalen Mapping oder bei der JSON De-Serialisierung zu erzeugen. Zusätzlich zur folgenden Konfiguration 
finden sich [hier](../features/domainobject_builders_de.md) Beispiele zur Implementierung.

### Spring-Beans
Die folgenden Spring-Beans müssen konfiguriert werden und in einer ```@Configuration``` Klasse als ```@Bean``` bereitgestellt werden:

#### `DomainObjectBuilderProvider`
Der DomainObjectBuilderProvider wird benötigt um mit inner-Builders oder den Lombok-Builders (Lombok ```@Builder```-Annotation) zu arbeiten.

```Java
@Bean
DomainObjectBuilderProvider innerClassDomainObjectBuilderProvider() {
    return new InnerClassDomainObjectBuilderProvider();
}
```

---


## JSON-Mapping
Zusätzlich zur folgenden Konfiguration finden sich [hier](../features/json_mapping_de.md) Beispiele zur Implementierung.

### Spring-Beans
Die folgenden Spring-Beans müssen konfiguriert werden und in einer ```@Configuration``` Klasse als ```@Bean``` bereitgestellt werden:

#### `DlcJacksonModule`
Benötigt für DLC-Jackson Integration, d.h. für JSON-De-Serialisierung.

```Java
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
Zusätzlich zur folgenden Konfiguration finden sich [hier](../features/domain_events_de.md) Beispiele zur Implementierung.

### Spring-Beans
Die folgenden Spring-Beans müssen konfiguriert werden und in einer ```@Configuration``` Klasse als ```@Bean``` bereitgestellt werden:

#### `ChannelRoutingConfiguration`
Stellt eine Konfiguration für das Channel Routing bereit.

```Java
@Bean
public ChannelRoutingConfiguration channelConfiguration(PlatformTransactionManager platformTransactionManager, ServiceProvider serviceProvider){
    var channel = new SpringTxInMemoryChannelFactory(platformTransactionManager, serviceProvider, true).processingChannel("default");
    var router = new DomainEventTypeBasedRouter(List.of(channel));
    router.defineDefaultChannel("default");
    return new ChannelRoutingConfiguration(router);
}
```
Über dieses Routing wird definiert, wie Domain Events technisch verarbeitet werden. In diesem Fall, werden Domain Events
in-memory und transaktionsgebunden von DLC an entsprechende Listener zugestellt. Alternativ zur in-memory Variante könnten diese Domain Events 
auch über einen externen Event Bus (bspw. per JMS) zugestellt werden. Transaktionsgebunden bedeutet, dass die Veröffentlichung der Domain Events je nach Konfiguration
immer über eine Auslöser-Transaktion je nach Konfiguration ```beforeCommit``` oder ```afterCommit``` erfolgt.

#### `ServiceProvider`
Stellt einen Provider für alle benötigten ```ServiceKind``` Objekte bereit. Dies wird benötigt für die in der Route definierte Event-Zustellung.

```Java
public ServiceProvider serviceProvider(List<ServiceKind> serviceKinds){
    return new Services(serviceKinds);
}
```

---


## Open-API-Extension
Zusätzlich zur folgenden Konfiguration finden sich [hier](../features/open_api_extension_de.md) Beispiele zur Implementierung.

### Spring-Beans
Die folgenden Spring-Beans müssen konfiguriert werden und in einer ```@Configuration``` Klasse als ```@Bean``` bereitgestellt werden:

#### `DlcOpenApiCustomizer`
Aktiviert die DLC-/OpenAPI-Integration.

```Java
@Bean
public DlcOpenApiCustomizer openApiCustomizer(SpringDocConfigProperties springDocConfigProperties) {
    return new DlcOpenApiCustomizer(springDocConfigProperties);
}
```

---


|             **Build-Management**             |              **DLC starten**              |
|:--------------------------------------------:|:-----------------------------------------:|
| [<< Vorherige Seite](build_management_de.md) | [Nächste Seite >>](run_application_de.md) |

---

**DE** / [EN](../../english/guides/configuration_en.md)

[Getting Started](../index_de.md) / [DLC Autoconfiguration](autoconfiguration_de.md)

---

# DLC Autoconfiguration

---

Das DLC Framework bietet eine umfassende Autoconfiguration-Funktion über die `@EnableDlc` Annotation. Diese vereinfacht erheblich die Einrichtung einer DLC-Anwendung, da sie automatisch alle wichtigen Features konfiguriert.

## Maven Setup

Folgende Dependency muss zur `pom.xml` hinzugefügt werden:

<details>
<summary><img style="height: 12px" src="../../icons/file-type-maven.svg" alt="maven"> alt="maven"> <b>pom.xml</b></summary>

```xml
<dependency>
    <groupId>io.domainlifecycles</groupId>
    <artifactId>dlc-spring-boot-autoconfig</artifactId>
    <version>2.4.0</version>
</dependency>
```
</details>

## Gradle Setup

Folgende Dependency muss zur `build.gradle` hinzugefügt werden:

<details>
<summary><img style="height: 12px" src="../../icons/gradle.svg" alt="gradle"> <b>build.gradle</b></summary>

```groovy
dependencies {
    implementation 'io.domainlifecycles:dlc-spring-boot-autoconfig:2.4.0'
}
```
</details>

## Minimale Konfiguration

Minimale Konfiguration für eine lauffähige DLC-Anwendung:

<details>
<summary><img style="height: 12px" src="../../icons/java.svg" alt="java"> <b>Application.java</b></summary>

```java
@SpringBootApplication
@EnableDlc(
    dlcDomainBasePackages = "com.example.domain"
)
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
```
</details>

## Erweiterte Konfiguration

Für eine feinere Kontrolle über die aktivierten Features, kann man diese auch einzeln (de-)aktivieren:

<details>
<summary><img style="height: 12px" src="../../icons/java.svg" alt="java"> <b>Application.java</b></summary>

```java
@SpringBootApplication
@EnableDlc(
    dlcDomainBasePackages = "com.example.domain",
    enableJooqPersistenceAutoConfig = true,
    enableDomainEventsAutoConfig = true,
    enableSpringWebAutoConfig = true,
    enableJacksonAutoConfig = true,
    enableBuilderAutoConfig = true,
    enableSpringOpenApiAutoConfig = true,
    jooqRecordPackage = "com.example.jooq.tables.records",
    jooqSqlDialect = SQLDialect.POSTGRES
)
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
```
</details>

## Konfigurationsoptionen

Die `@EnableDlc` Annotation bietet folgende Konfigurationsmöglichkeiten:

| Parameter                         | Standard     | Beschreibung                                       |
|-----------------------------------|--------------|----------------------------------------------------|
| `dlcDomainBasePackages`           | `""`         | **Erforderlich**: Basis-Package für Domain-Klassen |
| `enableSpringWebAutoConfig`       | `true`       | Aktiviert Spring Web Integration                   |
| `enableBuilderAutoConfig`         | `true`       | Aktiviert Builder Pattern Support                  |
| `enableJooqPersistenceAutoConfig` | `true`       | Aktiviert JOOQ Persistence Layer                   |
| `enableDomainEventsAutoConfig`    | `false`      | Aktiviert Domain Events Handling                   |
| `enableJacksonAutoConfig`         | `true`       | Aktiviert Jackson JSON Serialization               |
| `enableSpringOpenApiAutoConfig`   | `true`       | Aktiviert OpenAPI/Swagger Integration              |
| `jooqRecordPackage`               | `""`         | Package der generierten JOOQ Records               |
| `jooqSqlDialect`                  | `DEFAULT`    | SQL Dialekt für JOOQ                               |

## Selektive Feature-Aktivierung

Sie können auch gezielt nur bestimmte Features aktivieren:

<details>
<summary><img style="height: 12px" src="../../icons/java.svg" alt="java"> <b>MinimalApplication.java</b></summary>

```java
@SpringBootApplication
@EnableDlc(
    dlcDomainBasePackages = "com.example.domain",
    enableSpringWebAutoConfig = false,
    enableBuilderAutoConfig = false,
    enableJooqPersistenceAutoConfig = true,
    enableDomainEventsAutoConfig = false,
    enableJacksonAutoConfig = false,
    enableSpringOpenApiAutoConfig = false,
    jooqRecordPackage = "com.example.jooq",
    jooqSqlDialect = SQLDialect.H2
)
public class MinimalApplication {
    public static void main(String[] args) {
        SpringApplication.run(MinimalApplication.class, args);
    }
}
```
</details>

## Wichtiger Hinweis

Mit der `@EnableDlc` Annotation ist die manuelle Initialisierung des Domain Mirrors **nicht mehr erforderlich**. Die Autoconfiguration übernimmt dies automatisch basierend auf den konfigurierten `dlcDomainBasePackages`.

## Custom Beans
Alle von der DLC-Autoconfig bereitgestellten Beans können auch überschrieben werden. Hierbei muss jedoch sichergestellt
werden, dass die Bean-Definitionen in eine separate Spring-Boot-Konfigurationsklasse und nicht in die 
Spring-Boot-App-Klasse eingefügt werden, da dies zu Konflikten im Bean-Erstellungsprozess beim Start führen könnte.

---

|             **Domain-Diagrammer**             |
|:---------------------------------------------:|
| [<< Vorherige Seite](domain_diagrammer_de.md) |

---

**DE** / [EN](../../english/features/autoconfig_en.md)
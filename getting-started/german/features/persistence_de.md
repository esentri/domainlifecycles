[Getting Started](../index_de.md) / [Features](../guides/features_de.md) / [Persistence](persistence_de.md)

---

# Persistence

Das Persistence-Modul von DLC ermöglicht ein vereinfachtes Mapping für in der Datenbank persistierten DomainObjects.
Zu den Funktionen gehören unter anderem:
-   Vereinfachte Aggregate Queries (DLC Fetcher)
-   Vereinfachte Aggregate CRUD Unterstützung (DLC Repositories)
-   Object relationales Auto Mapping
-   Persistenz Action Event Hooks
-   Vollumfänglicher ValueObject support bezüglich Persistenz
-   Unterstützt Java  `final`  Keywords und Java-Optionals innerhalb persistierter Strukturen

## Configuration
Sobald jOOQ als Depencency eingebunden ist, wird DLC jOOQ Persistence automatisch konfiguriert.
Mehr dazu unter [DLC Spring Boot AutoConfig](./../../../dlc-spring-boot-autoconfig/readme.md).

Bevor die DLC/JOOQ Integration genutzt werden kann, muss JOOQ auf Build Management Ebene konfiguriert werden. Im Speziellen 
wird hierfür der JOOQ Code-Generator benötigt, sodass die von DLC benötigten JOOQ-Tables/-Records generiert werden.
[Weitere Informationen](https://www.jooq.org/doc/latest/manual/code-generation/codegen-execution/).

<details>
<summary><img style="height: 12px" src="../../icons/gradle.svg" alt="gradle"> <b>build.gradle</b></summary>

```groovy
plugins {
    id 'org.jooq.jooq-codegen-gradle' version '3.20.6'
}

jooq {
    configuration {
        jdbc {
            driver = 'org.h2.Driver'
            url = 'jdbc:h2:~/my-db'
            user = 'sa'
            password = ''
        }
        generator {
            database {
                name = 'org.jooq.meta.h2.H2Database'
                inputSchema = 'PUBLIC'
            }
            generate {
                daos = true
                pojos = true
            }
            target {
                packageName = 'com.example.records'
                directory = 'build/generated-sources/jooq'
            }
        }
    }
}

dependencies {
    jooqCodegen 'com.h2database:h2:2.4.240'
}
```
</details>

<details>
<summary><img style="height: 12px" src="../../icons/file-type-maven.svg" alt="maven"><b>pom.xml</b></summary>

```xml
<build>
    <plugins>
        <plugin>
            <groupId>org.jooq</groupId>
            <artifactId>jooq-codegen-maven</artifactId>
            <version>${jooq.version}</version>
            <executions>
                <execution>
                    <id>generate-jooq</id>
                    <phase>generate-sources</phase>
                    <goals>
                        <goal>generate</goal>
                    </goals>
                    <configuration>
                        <jdbc>
                            <driver>org.h2.Driver</driver>
                            <url>jdbc:h2:~/my-db</url>
                            <user>sa</user>
                            <password></password>
                        </jdbc>
                        <generator>
                            <database>
                                <name>org.jooq.meta.h2.H2Database</name>
                                <inputSchema>PUBLIC</inputSchema>
                            </database>
                            <generate>
                                <daos>true</daos>
                                <pojos>true</pojos>
                            </generate>
                            <target>
                                <packageName>com.example.records</packageName>
                                <directory>${project.build.directory}/generated-sources/jooq</directory>
                            </target>
                        </generator>
                    </configuration>
                </execution>
            </executions>
        </plugin>
    </plugins>
</build>
```
</details>

**Hinweis:** Der angegebene `target packageName` muss mit dem `jooqRecordPackage` der `@EnableDlc` Annotation übereinstimmen:
```java
@SpringBootApplication
@EnableDlc(
        dlcDomainBasePackages = "com.example.domain", 
        jooqRecordPackage = "com.example.records",
        jooqSqlDialect = SQLDialect.H2
)
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
```

## Implementierung
**Hinweis:** Damit eine reibungslose Implementierung des Repositories möglich ist,
muss das Projekt zuerst einmal kompiliert werden, sodass die entsprechenden JOOQ-Records erstellt werden.

### Repository anlegen
Wie auch schon bei den anderen Domain-Types, lässt sich ein Repository definieren,
indem man von der entsprechenden Klasse erbt. Ein beispielhaftes Repository mit den grundlegenden 
Operationen für die eingangs verwendete Customer-Klasse, könnte so aussehen:
```Java
@Component
public class CustomerRepository extends JooqAggregateRepository<Customer, CustomerId> {

    private static final Logger log = org.slf4j.LoggerFactory.getLogger(CustomerRepository.class);
    private final JooqDomainPersistenceProvider jooqDomainPersistenceProvider;
    
    public CustomerRepository(DSLContext dslContext,
                                SpringPersistenceEventPublisher persistenceEventPublisher,
                                JooqDomainPersistenceProvider jooqDomainPersistenceProvider) {
        super(
            Customer.class,
            dslContext,
            jooqDomainPersistenceProvider,
            persistenceEventPublisher
        );
        this.jooqDomainPersistenceProvider = jooqDomainPersistenceProvider;
    }
}
```

Bereits durch diese grundlegende Definition sind simple CRUD-Operationen für Aggregates möglich, wobei DLC die Aggregate Struktur auf potenziell mehrere
auf Datenbank Ebene abgebildete Tabellen unterstützt.

### Eigene Queries definieren
Komplexere Datenbank-Abfragen lassen sich hervorragend durch das Zusammenspiel zwischen DLC und JOOQ definieren.
Hierbei findet oft der `Fetcher` Anwendung, welcher vereinfachtes Laden der Aggregates aus der Datenbank ermöglicht.
Beispielhafte Implementierungen können dann so aussehen:

#### Find All
```Java
@Component
public class CustomerRepository extends JooqAggregateRepository<Customer, CustomerId> {
    public List<Customer> findAllCustomers() {
        return dslContext.select()
            .from(CUSTOMER)
            .fetch().stream()
            .map(r -> getFetcher().fetchDeep(r.into(CUSTOMER)).resultValue().get()).collect(Collectors.toList());
    }
}
```
`CUSTOMER` ist hierbei die JOOQ Repräsentation der Customer-Tabelle und entsprechende JOOQ-Records der Datenbank.
Der Fechter übernimmt per `fetchDeep` das Laden weiterer jOOQ-Records aus den Tabellen aus welchen sich das Aggregate zusammensetzt.
Zudem übernimmt der das Mapping in die gewünschte Aggregate-Struktur in der Java-Ebene.

#### Find Paginated
```Java
@Component
public class CustomerRepository extends JooqAggregateRepository<Customer, CustomerId> {
    public List<Customer> findCustomersPaginated(int offset, int pageSize) {
        return dslContext.select()
            .from(Customer)
            .orderBy(CUSTOMER.ID)
            .offset(offset)
            .limit(pageSize)
            .fetch().stream()
            .map(r -> getFetcher().fetchDeep(r.into(CUSTOMER)).resultValue().get()).collect(Collectors.toList());
    }
}
```

#### Find Paginated und gefiltert
```Java
@Component
public class CustomerRepository extends JooqAggregateRepository<Customer, CustomerId> {
    public List<Customer> findCustomersPaginatedAndCustomerFirstNameEqualTo(String firstName, int offset, int pageSize) {
        return dslContext.select()
            .from(CUSTOMER)
            .where(ORDER.FIRST_NAME.eq(firstName))
            .orderBy(CUSTOMER.ID)
            .offset(offset)
            .limit(pageSize)
            .fetch().stream()
            .map(r -> getFetcher().fetchDeep(r.into(CUSTOMER)).resultValue().get()).collect(Collectors.toList());
    }
}
```

---
|             **Domain Types**              |            **Domain Events**            |
|:-----------------------------------------:|:---------------------------------------:|
| [<< Vorherige Seite](domain_types_de.md)  | [Nächste Seite >>](domain_events_de.md) |

---

**DE** / [EN](../../english/features/persistence_en.md)

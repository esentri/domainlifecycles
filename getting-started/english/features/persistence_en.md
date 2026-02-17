[Getting Started](../index_en.md) / [Features](../guides/features_en.md) / [Persistence](persistence_en.md)

---

# Persistence
The persistence module of DLC makes it easy to combine the DDD approach with 
a relational database.
Some of the functions it offers are:
-  Type-safe Queries based on jOOQ
-  Support and abstraction of many database engines using JOOQ
-  Simplified aggregate queries (DLC Fetcher)
-  Simplified aggregate CRUD support (DLC repositories)
-  Object relational auto mapping
-  Persistence Action Event hooks
-  Full ValueObject support regarding persistence
-  Supports Java `final` keywords and Java optionals within persisted structures

## Configuration
As soon as jOOQ is provided as dependency, DLC jOOQ Persistence will be enabled automatically.
See [DLC Spring Boot AutoConfig](./../../../dlc-spring-boot-autoconfig/readme.md).

Specifically, you need to use JOOQ's code generator so that the required tables and records are generated.
[More information](https://www.jooq.org/doc/latest/manual/code-generation/codegen-execution/).

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

**Note:** The specified `target packageName` has to correspond to your `jooqRecordPackage` in the `@EnableDlc` annotation:
```java
@SpringBootApplication
@EnableDlc(
        dlcMirrorBasePackages = "com.example.domain", 
        jooqRecordPackage = "com.example.records",
        jooqSqlDialect = SQLDialect.H2)
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
```

## Implementation
**Note:** In order for the repository to be implemented smoothly, the project must first be compiled so that the 
corresponding JOOQ records/tables are created.

### Create repository
As with the other domain types, a repository can be defined 
by extending the corresponding class. An exemplary repository with the basic operations for the
operations for the customer class used at the beginning could look like this:
```Java
@Component
public class CustomerRepository extends JooqAggregateRepository<Customer, CustomerId> {

    private static final Logger log = org.slf4j.LoggerFactory.getLogger(CustomerRepository.class);
    private final JooqDomainPersistenceProvider jooqDomainPersistenceProvider;
    
    public CustomerRepository(DSLContext dslContext,
                                PersistenceEventPublisher persistenceEventPublisher,
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

This basic definition already makes simple CRUD operations possible.

### Define custom queries
More complex queries can be excellently defined through the interaction between DLC and JOOQ.
The `Fetcher` is often used here, which enables simplified loading of the aggregates from the database.
Exemplary implementations can then look like this:

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
'CUSTOMER' is the JOOQ representation of the customer table and the corresponding JOOQ records of the database.
The fetcher uses `fetchDeep` to load further JOOQ records from the tables that make up the aggregate.
It also performs the mapping to the desired aggregate structure at Java level.

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

#### Find Paginated and filtered
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

|         **Domain Types**          |       **Domain Events**        |
|:---------------------------------:|:------------------------------:|
| [<< Previous](domain_types_en.md) | [Next >>](domain_events_en.md) |


---

**EN** / [DE](../../german/features/persistence_de.md)

## DLC jOOQ Integration

`jooq-integration` is the [jOOQ](https://www.jooq.org/) based implementation of [DLC Persistence](../persistence/readme.md)
- Aggregate `Repositories`, `Fetchers`, AutoMapping, `RecordMapper`s, optimistic locking, Persistence Action
events, the Transaction Cache - built on jOOQ's generated, type-safe `UpdatableRecord` classes and its `DSLContext`.
It was DLC's original, and for a long time only, persistence backend; [`jdbc-integration`](../jdbc-integration/readme.md)
is a newer, plain JDBC based alternative for projects that don't want to depend on jOOQ or run a code generation step.

Most of the concepts here - Repositories, Fetcher, AutoMapping, optimistic locking, Persistence Actions,
RecordMapper, TypeConverter, the Transaction Cache, and the jOOQ build/runtime setup itself - are already
documented in detail in [DLC Persistence](../persistence/readme.md), written directly in terms of this module's
own classes (`JooqDomainPersistenceProvider`, `JooqDomainPersistenceConfiguration`, ...). Read that readme first;
this one only adds what's specific to the jOOQ implementation and not already covered there.

- [jOOQ vs. plain JDBC](#jooq-vs-plain-jdbc)
- [Setup](#setup)
    - [Dependency](#dependency)
    - [jOOQ code generation and runtime configuration](#jooq-setup)
    - [DLC Persistence configuration](#persistence-configuration)
    - [Transaction Cache](#transaction-cache)
- [Repositories](#repositories)
- [Fetcher](#fetcher)
- [Object relational mapping](#or-mapping)
- [Optimistic locking](#optimistic-locking)
- [Identity and sequence based id generation](#id-generation)
- [Known limitations](#limitations)

<a name="jooq-vs-plain-jdbc"></a>

### jOOQ vs. plain JDBC

| Aspect                       | `jooq-integration`                                                                   | `jdbc-integration`                                                                                   |
|------------------------------|----------------------------------------------------------------------------------------|---------------------------------------------------------------------------------------------------------|
| Table/record representation  | One generated `UpdatableRecord` subclass per table                                     | One generic `JdbcRecord` class for every table, keyed by physical table/column name                     |
| Schema knowledge              | Baked into generated code at build time (jOOQ code generator)                          | Read once at runtime via `java.sql.DatabaseMetaData` into an immutable `JdbcSchemaMetadata` snapshot    |
| Build setup                   | jOOQ code generation plugin (Maven/Gradle) required, typically combined with Flyway     | None - just a JDBC driver and, if desired, a migration tool such as Flyway                              |
| SQL dialect differences       | Absorbed internally by jOOQ                                                            | Made explicit via a small `JdbcDialect` interface (only "next sequence value" differs per dialect)      |
| Repository base class          | `JooqAggregateRepository`                                                              | `JdbcAggregateRepository`                                                                               |
| Fetcher                       | `JooqAggregateFetcher`                                                                 | `JdbcAggregateFetcher`                                                                                  |
| Persistence configuration      | `JooqDomainPersistenceConfiguration`                                                    | `JdbcDomainPersistenceConfiguration`                                                                     |

Everything above the persister/fetcher/record layer - Aggregate insert/update/delete orchestration, change
detection, optimistic locking, Persistence Action publishing, the `DomainPersistenceProvider` - is inherited
unchanged from the shared [`persistence`](../persistence) module. Both modules are two different technical
backends for exactly the same programming model, and both are auto-configured out of the box in a Spring Boot
application (see [`dlc-spring-boot-autoconfig`](../dlc-spring-boot-autoconfig/readme.md#4-jooq-persistence-autoconfig-dlcjooqpersistenceautoconfiguration) /
[`dlc-spring-boot3-autoconfig`](../dlc-spring-boot3-autoconfig/readme.md)) - jOOQ wins automatically if both are
on the classpath at once.

<a name="setup"></a>

### Setup

<a name="dependency"></a>

#### Dependency

```Groovy
dependencies {
    implementation 'io.domainlifecycles:jooq-integration:3.5.0'
}
```

```XML
<dependency>
    <groupId>io.domainlifecycles</groupId>
    <artifactId>jooq-integration</artifactId>
    <version>3.5.0</version>
</dependency>
```

<a name="jooq-setup"></a>

#### jOOQ code generation and runtime configuration

DLC relies on jOOQ generated `UpdatableRecord` classes representing the accessed database tables, and requires
optimistic locking to be turned on in both the code generator and jOOQ's runtime `Settings`. This is entirely
jOOQ's own setup, not specific to this module - see [DLC Persistence - jOOQ Build configuration](../persistence/readme.md#jooq-build-configuration)
(Maven/Gradle code generator setup, including the mandatory `<recordVersionFields>CONCURRENCY_VERSION</recordVersionFields>`)
and [DLC Persistence - jOOQ runtime configuration](../persistence/readme.md#jooq-runtime-configuration) (the
`DSLContext`/`DefaultConfiguration` bean, `setExecuteWithOptimisticLocking(true)`, the SQL dialect) for the full
setup, and the [sample project](../sample-project) for a working Gradle example combined with Flyway.

<a name="persistence-configuration"></a>

#### DLC Persistence configuration

See [DLC Persistence - DLC Persistence configuration](../persistence/readme.md#persistence-configuration) for the
full `JooqDomainPersistenceConfiguration`/`JooqPersistenceConfigurationBuilder` setup (record package, custom
RecordMappers, TypeConverterProvider, `EntityValueObjectRecordTypeConfiguration`, ...) - it is written directly
against this module's classes. One nuance worth calling out here: `JooqDomainPersistenceProvider` has both a
single-argument constructor (`JooqDomainPersistenceConfiguration` only) and a two-argument one that additionally
takes the `DSLContext` your Repositories are built with; passing the `DSLContext` lets the provider wire the
[Transaction Cache](#transaction-cache) to it centrally, once, at construction time - see below for when the
single-argument constructor is enough.

<a name="transaction-cache"></a>

#### Transaction Cache

`jooq-integration` shares the [Transaction Cache](../persistence/readme.md#transaction-cache) feature with
`jdbc-integration`. It is off by default and switched on via `withTransactionCacheEnabled(true)` on
`JooqPersistenceConfigurationBuilder`, configurable further via `withTransactionCacheProvider(...)`/
`withTransactionCacheMaxSize(...)`.

Activation is simpler here than for plain JDBC: passing your `DSLContext` to `JooqDomainPersistenceProvider`'s
two-argument constructor (see above) registers `TransactionCacheJooqBinder` automatically, which reacts to jOOQ's
own `TransactionListener` events (`dslContext.transaction(...)`) - no manual wrapping of anything is required for
a plain jOOQ-driven transaction. A nested `dslContext.transaction(...)` that rolls back to its savepoint - also
because its commit failed - clears the cache, while the outer transaction keeps it. With the one-argument constructor, no scope is ever opened for jOOQ's own transactions, so the cache is
off there. With JTA but without Spring, set a `JtaTransactionCacheProvider` (see
[`persistence-cache-jakarta-jta`](../persistence-cache-jakarta-jta/readme.md)).

In a Spring Boot application, [`DlcJooqPersistenceAutoConfiguration`](../dlc-spring-boot-autoconfig/readme.md#4-jooq-persistence-autoconfig-dlcjooqpersistenceautoconfiguration)
sets a `SpringTransactionCacheProvider`, following Spring's transactions, with
`dlc.features.persistence.transaction-cache.enabled=true`. For a manual setup (no autoconfig) with Spring-managed
(`@Transactional`) transactions, switch the cache on and set a `SpringTransactionCacheProvider` via
`withTransactionCacheProvider(...)` - see
[`persistence-cache-spring-tx`](../persistence-cache-spring-tx/readme.md).

<a name="repositories"></a>

### Repositories

Extend `io.domainlifecycles.jooq.imp.JooqAggregateRepository`, exactly as described for `JdbcAggregateRepository`
in [`jdbc-integration`'s readme](../jdbc-integration/readme.md#repositories) or generically in
[DLC Persistence - DLC Repositories](../persistence/readme.md#dlc-repositories). `insert(A root)`, `update(A root)`,
`deleteById(I id)`, `findById(I id)`/`findResultById(I id)` and `getFetcher()` all behave as described there.

<a name="fetcher"></a>

### Fetcher

`io.domainlifecycles.jooq.imp.JooqAggregateFetcher` ensures complete Aggregate object trees are loaded, resolving
`1:1`/`1:n` relations between generated jOOQ tables via jOOQ's own table metamodel. See
[DLC Persistence - Queries via Fetcher](../persistence/readme.md#fetcher) for `RecordProvider`-based custom
queries; a `RecordProvider<R1, R2>` here operates on the two concrete, generated record types involved, rather
than on the single generic `JdbcRecord` type used by `jdbc-integration`.

<a name="or-mapping"></a>

### Object relational mapping

AutoMapping conventions, custom `RecordMapper`s and `EntityValueObjectRecordTypeConfiguration` are all documented
in [DLC Persistence](../persistence/readme.md#automapping) directly against this module's generated-Record-based
classes - see [AutoMapping](../persistence/readme.md#automapping), [RecordMapper](../persistence/readme.md#recordmapper),
[record mapped ValueObjects / scalar lists](../persistence/readme.md#record-mapped-valueobjects) and
[EntityValueObjectRecordTypeConfiguration](../persistence/readme.md#entityvalueobjectrecordtypeconfiguration).

<a name="optimistic-locking"></a>

### Optimistic locking

DLC requires each Entity and AggregateRoot to define a `concurrencyVersion` property, backed by a
`CONCURRENCY_VERSION` column, exactly as described in
[DLC Persistence - Optimistic Locking](../persistence/readme.md#optimistic-locking). For jOOQ, this additionally
requires `<recordVersionFields>CONCURRENCY_VERSION</recordVersionFields>` in the code generator configuration and
`jooqConfig.settings().setExecuteWithOptimisticLocking(true)` at runtime (see
[jOOQ code generation and runtime configuration](#jooq-setup) above) - without both, jOOQ's generated `store()`/
`delete()` calls silently skip the version check DLC relies on.

<a name="id-generation"></a>

### Identity and sequence based id generation

`JooqEntityIdentityProvider` resolves sequences dynamically via jOOQ's own schema metadata
(`dslContext.meta().getSequences(...)`), not via generated `Sequences` classes, so a sequence created after the
last code generation run is still found without regenerating anything:

- A `UUID`-valued `Identity` is generated in memory, without a database round-trip.
- Any other `Identity` value is generated from a database sequence named after the identity type's simple name in
  snake case, e.g. `OrderId` &rarr; sequence `ORDER_ID_SEQ` (matched case-insensitively). An identity declared as an
  inner class is prefixed with its enclosing class(es): `Order.Id` &rarr; `ORDER_ID_SEQ`, `Order.OrderId` &rarr;
  `ORDER_ORDER_ID_SEQ`.
- The technical, non-domain-visible primary key of a "record mapped" ValueObject table (or a scalar list's child
  table, see [DLC Persistence](../persistence/readme.md#scalar-lists)) is generated from a sequence named after
  the table, e.g. table `ACTION_CODE` &rarr; sequence `ACTION_CODE_SEQ`.

<a name="limitations"></a>

### Known limitations

- Only single-column primary keys and single-column foreign keys are supported (the same restriction
  `jdbc-integration` inherits from this module).
- Self-referencing single-valued relations (a table with a foreign key back to itself, e.g. a `parent_id`
  hierarchy) are not resolved automatically by `JooqAggregateFetcher` - use a custom repository with a
  `RecordProvider` to fetch such structures explicitly. Self-referencing `1:n` collections (e.g. a tree of child
  rows) are supported without a custom `RecordProvider`.
- Ambiguous foreign key relations (more than one foreign key relationship between the same pair of tables) are
  not resolved automatically; fetch such Aggregates via a custom `RecordProvider`-based repository.

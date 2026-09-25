## DLC JDBC Integration

`jdbc-integration` is a plain JDBC based implementation of [DLC Persistence](../persistence/readme.md).
It provides the same DDD-oriented persistence features as [`jooq-integration`](../jooq-integration) -
Aggregate `Repositories`, `Fetchers`, AutoMapping, `RecordMapper`s, optimistic locking, Persistence Action
events - but without depending on [jOOQ](https://www.jooq.org/) and without any code generation step.

Where `jooq-integration` relies on jOOQ's code generator to produce one `UpdatableRecord` subclass per
database table at build time, `jdbc-integration` reads the schema once at runtime via
`java.sql.DatabaseMetaData` and represents every table row with the same generic `JdbcRecord` class. If your
project cannot or does not want to run a jOOQ code generation step (build-time database access, Maven/Gradle
plugin setup, jOOQ licensing for some database dialects, ...), this module gives you the same DLC persistence
programming model with a plain JDBC `Connection` as its only technical dependency.

Read [DLC Persistence](../persistence/readme.md) first for the concepts that are shared between both
implementations (Repositories, Fetcher, AutoMapping, optimistic locking, Persistence Actions, RecordMapper,
TypeConverter, ...). This readme only covers what is specific to the JDBC based implementation.

- [jOOQ vs. plain JDBC](#jooq-vs-plain-jdbc)
- [Supported database dialects](#dialects)
- [Setup](#setup)
    - [Dependency](#dependency)
    - [Schema metadata](#schema-metadata)
    - [Connection provider](#connection-provider)
    - [DLC Persistence configuration](#persistence-configuration)
    - [Transaction Cache](#transaction-cache)
- [Repositories](#repositories)
- [Fetcher and RecordProvider](#fetcher)
- [Object relational mapping](#or-mapping)
    - [AutoMapping conventions](#automapping)
    - [Custom RecordMapper](#recordmapper)
    - [EntityValueObjectRecordTypeConfiguration](#entityvalueobjectrecordtypeconfiguration)
- [Optimistic locking](#optimistic-locking)
- [Identity and sequence based id generation](#id-generation)
- [Known limitations](#limitations)

<a name="jooq-vs-plain-jdbc"></a>

### jOOQ vs. plain JDBC

| Aspect                                        | `jooq-integration`                                                                 | `jdbc-integration`                                                                              |
|------------------------------------------------|-------------------------------------------------------------------------------------|---------------------------------------------------------------------------------------------------|
| Table/record representation                    | One generated `UpdatableRecord` subclass per table                                  | One generic `JdbcRecord` class for every table, keyed by physical table/column name                 |
| Schema knowledge                                | Baked into generated code at build time (jOOQ code generator)                        | Read once at runtime via `java.sql.DatabaseMetaData` into an immutable `JdbcSchemaMetadata` snapshot |
| Build setup                                     | jOOQ code generation plugin (Maven/Gradle) required, typically combined with Flyway | None - just a JDBC driver and, if desired, a migration tool such as Flyway                          |
| SQL dialect differences                         | Absorbed internally by jOOQ                                                          | Made explicit via a small `JdbcDialect` interface (only "next sequence value" differs per dialect)  |
| Repository base class                           | `JooqAggregateRepository`                                                            | `JdbcAggregateRepository`                                                                          |
| Fetcher                                         | `JooqAggregateFetcher`                                                              | `JdbcAggregateFetcher`                                                                             |
| Persistence configuration                       | `JooqDomainPersistenceConfiguration`                                                 | `JdbcDomainPersistenceConfiguration`                                                               |

Everything above the persister/fetcher/record layer - Aggregate insert/update/delete orchestration, change
detection, optimistic locking, Persistence Action publishing, the `DomainPersistenceProvider` - is inherited
unchanged from the shared [`persistence`](../persistence) module. Both modules are two different technical
backends for exactly the same programming model.

<a name="dialects"></a>

### Supported database dialects

Plain JDBC does not abstract SQL dialect differences the way jOOQ does, so this module makes the (small) set
of differences it actually depends on explicit via `io.domainlifecycles.jdbc.dialect.JdbcDialect`. Obtaining
the next value of a sequence (used for entity identities and for the technical ids of "record mapped"
ValueObjects, see [id generation](#id-generation)) is the one operation every dialect must implement:

- `H2JdbcDialect`
- `PostgresJdbcDialect`
- `OracleJdbcDialect`
- `MySqlJdbcDialect` (standard MySQL has no native `SEQUENCE` object and emulates one)
- `SqlServerJdbcDialect`

`JdbcDialect` also builds the `INSERT`/`UPDATE`/`DELETE` statements `JdbcPersister` executes
(`insertSql(...)`/`updateSql(...)`/`deleteSql(...)`), each with a default implementation generating plain,
unquoted ANSI SQL - the same statements this module always generated, before these hooks existed. A custom
dialect only needs to override what it actually needs to change; the most common case is identifier quoting
(reserved words, case-sensitive identifiers, ...), for which overriding a single method is enough - the
default `insertSql`/`updateSql`/`deleteSql` implementations already route every table and column name through
it:

```Java
public class QuotingPostgresJdbcDialect implements JdbcDialect {

    private final JdbcDialect delegate = new PostgresJdbcDialect();

    @Override
    public String name() {
        return delegate.name();
    }

    @Override
    public long nextSequenceValue(Connection connection, String sequenceName) throws SQLException {
        return delegate.nextSequenceValue(connection, sequenceName);
    }

    @Override
    public String quoteIdentifier(String identifier) {
        return "\"" + identifier + "\"";
    }
}
```

A `JdbcConnectionProvider` and `JdbcDialect` instance are registered once, centrally, on the
`JdbcDomainPersistenceConfiguration` (see [DLC Persistence configuration](#persistence-configuration)) and are
then available to every Repository, Persister and Fetcher built from the resulting
`JdbcDomainPersistenceProvider` - so different Aggregates could in principle even be backed by different
databases within the same application, by building a separate `JdbcDomainPersistenceProvider` (with its own
connection provider and dialect) per database.

<a name="setup"></a>

### Setup

<a name="dependency"></a>

#### Dependency

```Groovy
dependencies {
    implementation 'io.domainlifecycles:jdbc-integration:3.5.0'
}
```

```XML
<dependency>
    <groupId>io.domainlifecycles</groupId>
    <artifactId>jdbc-integration</artifactId>
    <version>3.5.0</version>
</dependency>
```

No jOOQ dependency and no code generation plugin is required. Bring your own JDBC driver for the target
database. As with `jooq-integration`, the use of a migration tool such as [Flyway](https://flywaydb.org/) is
not required by DLC, but recommended for database structure version control.

<a name="schema-metadata"></a>

#### Schema metadata

Instead of generated record classes, this module needs a `JdbcSchemaMetadata` snapshot, read once via
`java.sql.DatabaseMetaData` from a JDBC connection to the fully migrated database:

```Java
try (Connection connection = dataSource.getConnection()) {
    JdbcSchemaMetadata schemaMetadata = JdbcSchemaMetadata.read(connection);
}
```

Optionally narrow the introspected tables to a single schema:

```Java
JdbcSchemaMetadata schemaMetadata = JdbcSchemaMetadata.read(connection, "MY_SCHEMA");
```

The snapshot contains table names, column names/types, primary keys and foreign keys for every table visible
through the connection, and is immutable afterward. Only single-column primary keys and single-column foreign
keys are supported (see also [known limitations](#limitations)).

<a name="connection-provider"></a>

#### Connection provider

`JdbcConnectionProvider` is the plain JDBC analogue of jOOQ's `DSLContext`/`ConnectionProvider`: the persister,
fetcher and id providers call `getConnection()` at the point of use, not once at construction time, so an
implementation backed by a transaction manager can hand out the connection bound to the currently active
transaction.

```Java
public class MyJdbcConnectionProvider implements JdbcConnectionProvider {

    private final DataSource dataSource;

    public MyJdbcConnectionProvider(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Connection getConnection() {
        return DataSourceUtils.getConnection(dataSource); // e.g. Spring, to join the current transaction
    }
}
```

For simple, single-connection setups (and for this module's own tests), `SingleJdbcConnectionProvider` always
returns one fixed `Connection` instance.

<a name="persistence-configuration"></a>

#### DLC Persistence configuration

A minimal configuration example of a `JdbcDomainPersistenceProvider`:

```Java
@Bean
public JdbcDomainPersistenceProvider domainPersistenceProvider(
        DomainObjectBuilderProvider domainObjectBuilderProvider,
        JdbcSchemaMetadata schemaMetadata,
        JdbcConnectionProvider connectionProvider,
        JdbcDialect dialect
        ) {
    return new JdbcDomainPersistenceProvider(
        JdbcDomainPersistenceConfiguration.JdbcPersistenceConfigurationBuilder
            .newConfig()
            .withDomainObjectBuilderProvider(domainObjectBuilderProvider)
            .withSchemaMetadata(schemaMetadata)
            .withConnectionProvider(connectionProvider)
            .withDialect(dialect)
            .make());
}
```

`withSchemaMetadata(...)`, `withConnectionProvider(...)` and `withDialect(...)` are the only mandatory settings
- everything else falls back to a sensible default, mirroring `JooqDomainPersistenceConfiguration`'s extension
points. The resulting `JdbcDomainPersistenceProvider` exposes them back as `connectionProvider`, `dialect` and
`schemaMetadata` fields that every Repository, Persister and Fetcher built from it reads directly - so none of
those need the connection provider, dialect or schema metadata as a separate constructor parameter of their own
(see [Repositories](#repositories)). The remaining, optional settings:

- `withCustomRecordMappers(...)`: register custom `RecordMapper`s, see [RecordMapper](#recordmapper).
- `withTypeConverterProvider(...)`: customize data type conversions, see
  [`persistence`'s TypeConverter section](../persistence/readme.md#typeconverter) - the same
  `DefaultTypeConverterProvider` and predefined converters are used by both persistence implementations.
- `withIgnoredDomainObjectFields(...)` / `withIgnoredRecordProperties(...)`: exclude fields/columns from
  AutoMapping.
- `withEntityValueObjectRecordTypeConfiguration(...)`: override AutoMapping's table naming convention for a
  ValueObject, see [EntityValueObjectRecordTypeConfiguration](#entityvalueobjectrecordtypeconfiguration).
- `withTableToEntityTypeMatcher(...)`, `withRecordEntityPropertyMatcher(...)`,
  `withRecordPropertyProvider(...)`, `withRecordPropertyAccessor(...)`, `withNewRecordInstanceProvider(...)`,
  `withRecordMirrorInstanceProvider(...)`: lower-level extension points, only needed for advanced customization
  beyond what the above options cover.

The resulting `JdbcDomainPersistenceProvider` instance is passed into every Repository, exactly as
`JooqDomainPersistenceProvider` is for `jooq-integration`.

<a name="transaction-cache"></a>

#### Transaction Cache

`jdbc-integration` shares the [Transaction Cache](../persistence/readme.md#transaction-cache) feature with
`jooq-integration` - enabled by default via `withTransactionCacheEnabled(...)`/`withTransactionCacheProvider(...)`/
`withTransactionCacheMaxSize(...)` on `JdbcPersistenceConfigurationBuilder`, identical to the jOOQ side.

In a Spring Boot application, [`DlcJdbcPersistenceAutoConfiguration`](../dlc-spring-boot-autoconfig/readme.md#5-jdbc-persistence-autoconfig-dlcjdbcpersistenceautoconfiguration)
wires the Spring-aware binder automatically - nothing to configure by hand.

For a manual setup (no autoconfig), both the native and the Spring-aware binder need to be wired by hand. For
a plain JDBC transaction (driven via commit/rollback on the connection provider itself, no Spring involved),
wrap your `JdbcConnectionProvider` with `TransactionCacheAwareConnectionProvider`. For a Spring-managed
(`@Transactional`) transaction, wrap it with `io.domainlifecycles.jdbc.cache.SpringTransactionCacheAwareConnectionProvider`
instead (or in addition - see [`persistence-spring-tx`](../persistence-spring-tx/readme.md) for how the two
binders coexist), using the same `ThreadBoundTransactionCacheProvider` instance passed to
`JdbcDomainPersistenceConfiguration`.

<a name="repositories"></a>

### Repositories

Extend `io.domainlifecycles.jdbc.imp.JdbcAggregateRepository` the same way you would extend
`JooqAggregateRepository`:

```Java
@Component
public class OrderRepository extends JdbcAggregateRepository<Order, OrderId> {

    public OrderRepository(JdbcDomainPersistenceProvider jdbcDomainPersistenceProvider,
                            PersistenceEventPublisher persistenceEventPublisher) {
        super(
            Order.class,
            jdbcDomainPersistenceProvider,
            persistenceEventPublisher
        );
    }
}
```

`insert(A root)`, `update(A root)`, `deleteById(I id)`, `findById(I id)`/`findResultById(I id)` and
`getFetcher()` all behave exactly as described for `JooqAggregateRepository` in
[DLC Persistence - DLC Repositories](../persistence/readme.md#dlc-repositories). A custom finder method that
needs the connection, dialect or schema metadata directly (e.g. to run its own SQL) reads them off the injected
`jdbcDomainPersistenceProvider.connectionProvider` / `.dialect` / `.schemaMetadata` fields, rather than
requiring them as separate constructor parameters of its own.

<a name="fetcher"></a>

### Fetcher and RecordProvider

`JdbcAggregateFetcher` plays the same role as `JooqAggregateFetcher`: it ensures complete Aggregate object
trees are loaded, resolving `1:1`/`1:n` relations between tables via the foreign keys captured in
`JdbcSchemaMetadata`, instead of jOOQ's generated, live table metamodel.

Where jOOQ hands back a typed, generated record for a hand-written query, plain JDBC only ever hands back a
`ResultSet`, which still needs to be mapped onto a `JdbcRecord` (by physical column name, via
`JdbcSchemaMetadata`) before the fetcher can use it. Rather than hand-rolling that
`PreparedStatement`/`ResultSet`/`JdbcRecord` mapping loop in every custom finder,
`io.domainlifecycles.jdbc.util.JdbcRecordMapper` provides it as a set of static helpers
(`mapRow`, `selectWithSql`, `selectByColumn`, `selectOne`, `selectOneByColumn`) - this is the same mapping
`JdbcAggregateFetcher` uses internally to resolve foreign keys. `selectByColumn`/`selectOneByColumn` build
their `SELECT` via `JdbcDialect.selectByColumnSql(...)` (see [dialects](#dialects)), so a custom dialect's
`quoteIdentifier(...)` override is honored there exactly as it is for `JdbcPersister`'s statements - `sql`
passed to `selectWithSql`/`selectOne` directly is, by definition, already fully written by the caller and is
not touched.

A typical custom `findAll`-style query, resolving the resulting rows into full Aggregates via the fetcher:

```Java
public Stream<Order> findAllOrders() {
    var fetcher = getFetcher();
    var table = schemaMetadata.table("ORDER");
    var records = JdbcRecordMapper.selectWithSql(connectionProvider, table, "SELECT * FROM " + table.qualifiedName());
    return records.stream()
        .map(record -> fetcher.fetchDeep(record).resultValue().get());
}
```

For performance-sensitive queries, a `RecordProvider` can be attached to a fetcher to pre-fetch and supply
child records (e.g. via a single joined query), exactly as with the jOOQ integration - see
[DLC Persistence - Queries via Fetcher](../persistence/readme.md#fetcher) for the general pattern; the only
difference is that a `RecordProvider<JdbcRecord, JdbcRecord>` operates on the generic `JdbcRecord` type on both
sides, rather than on two distinct generated record types. Extending
`io.domainlifecycles.jdbc.imp.JdbcRecordProvider` instead of implementing `RecordProvider<JdbcRecord,
JdbcRecord>` directly gives access to the same `JdbcRecordMapper`-backed helpers as `protected` instance
methods, so only `provide(JdbcRecord)`/`provideCollection(JdbcRecord)` need to be written:

```Java
var itemTable = schemaMetadata.table("ORDER_ITEM");
fetcher.withRecordProvider(
    new JdbcRecordProvider(jdbcDomainPersistenceProvider) {
        @Override
        public Collection<JdbcRecord> provideCollection(JdbcRecord parentRecord) {
            return selectByColumn(itemTable, "ORDER_ID", parentRecord.get("ID"));
        }
    },
    Order.class,
    OrderItem.class,
    List.of("orderItems"));
```

<a name="or-mapping"></a>

### Object relational mapping

<a name="automapping"></a>

#### AutoMapping conventions

The naming and structural conventions for AutoMapping are exactly the ones described in
[DLC Persistence - AutoMapping](../persistence/readme.md#automapping) (Java `UpperCamelCase`/`lowerCamelCase`
mapped to database `SNAKE_CASE`, one table per Entity, "record mapped" ValueObjects for `1:n` relations,
nested `n`-level ValueObject structures, ...) - all naming conventions are shared between both persistence
implementations, so an existing `jooq-integration` based schema can generally be reused as-is.

Column name matching is intentionally lenient: property names are matched against the actual columns of the
resolved table by normalizing both sides (lower-cased, with underscores stripped), rather than requiring an
exact reconstruction of the expected column name - so minor, pre-existing inconsistencies in a schema's
underscore placement (e.g. `my_vo_value2` for a property `myVoValue2`) do not require a custom `RecordMapper`.

<a name="recordmapper"></a>

#### Custom RecordMapper

Custom RecordMappers implement `io.domainlifecycles.persistence.mapping.RecordMapper<JdbcRecord, ..., ...>` or
extend `io.domainlifecycles.persistence.mapping.AbstractRecordMapper<JdbcRecord, ..., ...>`, exactly as
described in [DLC Persistence - RecordMapper](../persistence/readme.md#recordmapper). Values are read from and
written to a `JdbcRecord` by physical column name via `get(String columnName)`/`set(String columnName, Object
value)`, instead of via generated, type-safe getters/setters:

```Java
public class VehicleJdbcRecordMapper extends AbstractRecordMapper<JdbcRecord, Vehicle, Vehicle> {

    private final DomainMirror domainMirror;

    public VehicleJdbcRecordMapper(DomainMirror domainMirror) {
        this.domainMirror = domainMirror;
    }

    @Override
    public DomainObjectBuilder<Vehicle> recordToDomainObjectBuilder(JdbcRecord record) {
        if (record == null) {
            return null;
        }
        if (Bike.class.getSimpleName().equals(record.get("TYPE"))) {
            return new InnerClassDomainObjectBuilder(Bike.builder()
                .setId(new VehicleId((Long) record.get("ID")))
                .setGears((Integer) record.get("GEARS"))
                .setLengthCm((Integer) record.get("LENGTH_CM"))
                .setConcurrencyVersion((Long) record.get("CONCURRENCY_VERSION")), domainMirror);
        }
        // ... Car case ...
        throw new IllegalStateException("Vehicles are only Cars or Bikes!");
    }

    @Override
    public JdbcRecord from(Vehicle vehicle, Vehicle root) {
        var record = new JdbcRecord("VEHICLE");
        record.set("ID", vehicle.getId().value());
        record.set("CONCURRENCY_VERSION", vehicle.concurrencyVersion());
        // ...
        return record;
    }
}
```

Custom RecordMappers are registered the same way as for `jooq-integration`, via
`.withCustomRecordMappers(customRecordMappers)` on the `JdbcPersistenceConfigurationBuilder`.

<a name="entityvalueobjectrecordtypeconfiguration"></a>

#### EntityValueObjectRecordTypeConfiguration

`JdbcEntityValueObjectRecordTypeConfiguration` is the plain JDBC analogue of the jOOQ integration's
`EntityValueObjectRecordTypeConfiguration`: it configures an explicit table for a ValueObject (or a
`List<ValueObject>`/`List<Identity>`/`List<Enum>` field's child records) whenever AutoMapping's naming
convention cannot resolve one on its own. The only difference is a physical table name (`String`) here, where
the jOOQ integration carries a generated record `Class`, since every table in this module is represented by
the same `JdbcRecord` class:

```Java
var jdbcDomainPersistenceConfiguration = JdbcDomainPersistenceConfiguration
    .JdbcPersistenceConfigurationBuilder
    .newConfig()
    ...
    .withEntityValueObjectRecordTypeConfiguration(
        new JdbcEntityValueObjectRecordTypeConfiguration(
            Order.class,
            ActionCode.class,
            "ACTION_CODE", // the target table, instead of the ORDER_ACTION_CODES assumed by AutoMapping
            "actionCodes"
        )
    )
    ...
    .make();
```

<a name="optimistic-locking"></a>

### Optimistic locking

DLC requires each Entity and AggregateRoot to define a `concurrencyVersion` property, backed by a
`CONCURRENCY_VERSION` column, exactly as described in
[DLC Persistence - Optimistic Locking](../persistence/readme.md#optimistic-locking). No separate
configuration step is needed here (unlike jOOQ's `setExecuteWithOptimisticLocking(true)` /
`recordVersionFields`): `JdbcPersister` checks and increments `CONCURRENCY_VERSION` explicitly as part of every
generated `UPDATE` statement, and forces it to `1` on `INSERT`, matching jOOQ's `recordVersionFields` code
generation behaviour so both implementations expose the same version semantics after an insert.

<a name="id-generation"></a>

### Identity and sequence based id generation

Where jOOQ uses its generated `Sequences` classes, this module resolves sequence names by convention and
obtains the next value via the configured `JdbcDialect`:

- A `UUID`-valued `Identity` is generated in memory, without a database round-trip.
- Any other `Identity` value is generated from a database sequence named after the identity type's simple
  name in snake case, e.g. `OrderId` &rarr; sequence `ORDER_ID_SEQ`.
- The technical, non-domain-visible primary key of a "record mapped" ValueObject table is generated from a
  sequence named after the table, e.g. table `ACTION_CODE` &rarr; sequence `ACTION_CODE_SEQ` - see
  [DLC Persistence - record mapped ValueObjects](../persistence/readme.md#record-mapped-valueobjects).

<a name="limitations"></a>

### Known limitations

- Only single-column primary keys and single-column foreign keys are supported (the same restriction already
  imposed by the jOOQ based integration).
- Self-referencing single-valued relations (a table with a foreign key back to itself, e.g. a `parent_id`
  hierarchy) are not resolved automatically by `JdbcAggregateFetcher` - use a custom repository with a
  `RecordProvider` to fetch such structures explicitly, as `jooq-integration` also requires for this case.
  Self-referencing `1:n` collections (e.g. a tree of child rows) are supported without a custom
  `RecordProvider`.
- Ambiguous foreign key relations (more than one foreign key relationship between the same pair of tables) are
  not resolved automatically; fetch such Aggregates via a custom `RecordProvider`-based repository.

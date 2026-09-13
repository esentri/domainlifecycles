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
of differences it actually depends on explicit via `io.domainlifecycles.jdbc.dialect.JdbcDialect`. The only
dialect-specific operation is obtaining the next value of a sequence (used for entity identities and for the
technical ids of "record mapped" ValueObjects, see [id generation](#id-generation)):

- `H2JdbcDialect`
- `PostgresJdbcDialect`
- `OracleJdbcDialect`
- `MySqlJdbcDialect` (standard MySQL has no native `SEQUENCE` object and emulates one)
- `SqlServerJdbcDialect`

A `JdbcDialect` instance is passed explicitly to every `JdbcAggregateRepository`, so different Aggregates
could in principle even be backed by different databases within the same application.

<a name="setup"></a>

### Setup

<a name="dependency"></a>

#### Dependency

```Groovy
dependencies {
    implementation 'io.domainlifecycles:jdbc-integration:3.4.0'
}
```

```XML
<dependency>
    <groupId>io.domainlifecycles</groupId>
    <artifactId>jdbc-integration</artifactId>
    <version>3.4.0</version>
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
        JdbcSchemaMetadata schemaMetadata
        ) {
    return new JdbcDomainPersistenceProvider(
        JdbcDomainPersistenceConfiguration.JdbcPersistenceConfigurationBuilder
            .newConfig()
            .withDomainObjectBuilderProvider(domainObjectBuilderProvider)
            .withSchemaMetadata(schemaMetadata)
            .make());
}
```

`withSchemaMetadata(...)` is the only mandatory setting - everything else falls back to a sensible default,
mirroring `JooqDomainPersistenceConfiguration`'s extension points:

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

<a name="repositories"></a>

### Repositories

Extend `io.domainlifecycles.jdbc.imp.JdbcAggregateRepository` the same way you would extend
`JooqAggregateRepository`:

```Java
@Component
public class OrderRepository extends JdbcAggregateRepository<Order, OrderId> {

    public OrderRepository(JdbcConnectionProvider connectionProvider,
                            JdbcDialect dialect,
                            JdbcSchemaMetadata schemaMetadata,
                            JdbcDomainPersistenceProvider jdbcDomainPersistenceProvider,
                            PersistenceEventPublisher persistenceEventPublisher) {
        super(
            Order.class,
            connectionProvider,
            dialect,
            schemaMetadata,
            jdbcDomainPersistenceProvider,
            persistenceEventPublisher
        );
    }
}
```

`insert(A root)`, `update(A root)`, `deleteById(I id)`, `findById(I id)`/`findResultById(I id)` and
`getFetcher()` all behave exactly as described for `JooqAggregateRepository` in
[DLC Persistence - DLC Repositories](../persistence/readme.md#dlc-repositories): the additional `JdbcDialect`
and `JdbcSchemaMetadata` constructor parameters are this module's only difference, needed for sequence access
and table/foreign-key resolution respectively.

<a name="fetcher"></a>

### Fetcher and RecordProvider

`JdbcAggregateFetcher` plays the same role as `JooqAggregateFetcher`: it ensures complete Aggregate object
trees are loaded, resolving `1:1`/`1:n` relations between tables via the foreign keys captured in
`JdbcSchemaMetadata`, instead of jOOQ's generated, live table metamodel.

A typical custom `findAll`-style query, resolving the resulting rows into full Aggregates via the fetcher:

```Java
public Stream<Order> findAllOrders() {
    var fetcher = getFetcher();
    try (Connection connection = connectionProvider.getConnection();
         PreparedStatement statement = connection.prepareStatement("SELECT * FROM \"ORDER\"");
         ResultSet resultSet = statement.executeQuery()) {
        List<Order> result = new ArrayList<>();
        while (resultSet.next()) {
            var record = mapRow(resultSet, schemaMetadata.table("ORDER"));
            result.add(fetcher.fetchDeep(record).resultValue().get());
        }
        return result.stream();
    } catch (SQLException e) {
        throw DLCPersistenceException.fail("Query failed.", e);
    }
}
```

For performance-sensitive queries, a `RecordProvider` can be attached to a fetcher to pre-fetch and supply
child records (e.g. via a single joined query), exactly as with the jOOQ integration - see
[DLC Persistence - Queries via Fetcher](../persistence/readme.md#fetcher) for the general pattern; the only
difference is that a `RecordProvider<JdbcRecord, JdbcRecord>` operates on the generic `JdbcRecord` type on both
sides, rather than on two distinct generated record types.

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

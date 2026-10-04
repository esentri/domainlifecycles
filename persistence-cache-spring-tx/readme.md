## DLC Spring Transaction Cache Provider

`persistence-cache-spring-tx` binds DLC's [Transaction Cache](../persistence/readme.md#transaction-cache) feature to
Spring-managed transactions - `@Transactional` or a `TransactionTemplate`, with a `DataSourceTransactionManager` as
well as a `JtaTransactionManager`. It works for both persistence implementations,
[`jooq-integration`](../jooq-integration) and [`jdbc-integration`](../jdbc-integration/readme.md#transaction-cache),
and is a module of its own, so that neither of them depends on Spring.

### Why this module exists

The transaction cache is only correct if DLC learns reliably when a transaction begins and ends. jOOQ raises
`TransactionListener` events for its own transactions (`dslContext.transaction(...)`), but not for a transaction
that Spring began and will commit or roll back. Plain JDBC has no such events at all. Spring itself, though, tells
every `TransactionSynchronization` registered for a transaction when it is suspended, resumed, rolled back to a
savepoint and completed.

### `SpringTransactionCacheProvider`

`io.domainlifecycles.persistence.spring.cache.SpringTransactionCacheProvider` keeps the cache of a transaction as a
resource of that transaction in Spring's `TransactionSynchronizationManager`, created on first use and tied to the
transaction by a `TransactionSynchronization`:

- The cache lives as long as its transaction, across all its reads and writes - obtaining and releasing connections
  in between does not affect it.
- A transaction suspended for another one (`PROPAGATION_REQUIRES_NEW`) puts its cache aside. Spring only unbinds its
  own resources on suspension, so the synchronization unbinds the cache itself; the other transaction, finding no
  cache, creates one of its own, and the suspended transaction gets its cache back when it resumes - it never sees
  what the other one loaded, even if that one rolled back.
- A rollback to a savepoint (`PROPAGATION_NESTED`) clears the cache, since entries loaded since the savepoint may
  hold state that was never committed. This needs Spring 6.2 or later, which notifies synchronizations of savepoint
  rollbacks.
- The cache is emptied and unbound once the transaction completes, whether committed or rolled back.
- Outside a transaction - none at all, or where Spring only activates transaction synchronization without one, e.g.
  for `PROPAGATION_SUPPORTS` - there is no cache.

Spring raises no event when a transaction begins - none is needed: the first access to the cache within a
transaction, while loading or before writing an aggregate, always finds that transaction active. No connection
provider needs to be decorated.

### Usage

For both `jooq-integration` (via [`DlcJooqPersistenceAutoConfiguration`](../dlc-spring-boot-autoconfig/readme.md#4-jooq-persistence-autoconfig-dlcjooqpersistenceautoconfiguration))
and `jdbc-integration` (via [`DlcJdbcPersistenceAutoConfiguration`](../dlc-spring-boot-autoconfig/readme.md#5-jdbc-persistence-autoconfig-dlcjdbcpersistenceautoconfiguration)),
this is wired automatically - both the Spring Boot 3 and Spring Boot 4 autoconfig modules wire the Transaction
Cache to Spring's own transaction management by default, with nothing to configure by hand in a Spring Boot
application. `dlc.features.persistence.transaction-cache.enabled=false` switches it off,
`dlc.features.persistence.transaction-cache.max-size` limits the number of aggregates held per transaction (256 by
default).

For a manual setup (no autoconfig), set the provider on the persistence configuration, for example for
`jooq-integration`:

```java
var configuration = JooqDomainPersistenceConfiguration.JooqPersistenceConfigurationBuilder.newConfig()
    // ...
    .withTransactionCacheProvider(new SpringTransactionCacheProvider<UpdatableRecord<?>>())
    .make();
```

and for `jdbc-integration`, typed `SpringTransactionCacheProvider<JdbcRecord>`, via
`JdbcPersistenceConfigurationBuilder.withTransactionCacheProvider(...)`. A constructor argument limits the number of
aggregates held per transaction (256 by default). See [Transaction Cache](../persistence/readme.md#transaction-cache)
for the full picture, including how to disable the feature entirely.

### Dependency

| Feature                                    | Relevant for          | Dependency (groupId:artifactId)             |
|---------------------------------------------|------------------------|---------------------------------------------|
| Spring transaction binding for the DLC transaction cache | application developers without the Spring Boot autoconfig | `io.domainlifecycles:persistence-cache-spring-tx` |

Pulled in transitively by the Spring Boot autoconfig modules and starters - with those, application code does not
need to depend on it directly.

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
  for `PROPAGATION_SUPPORTS` or `PROPAGATION_NOT_SUPPORTED` - there is no cache.
- A cache is only handed out to the transaction it was created for: one still bound to the thread although its
  transaction ended without completing it there - e.g. completed by a JTA transaction manager on another thread after
  a timeout - is dropped, and the next transaction gets a cache of its own.
- It needs Spring 6.2 or later (Spring Boot 3.4 or later): before, Spring does not report a rollback to a savepoint,
  so the provider hands out no cache and logs a warning once.

Spring raises no event when a transaction begins - none is needed: the first access to the cache within a
transaction, while loading or before writing an aggregate, always finds that transaction active. No connection
provider needs to be decorated.

### Usage

For both `jooq-integration` (via [`DlcJooqPersistenceAutoConfiguration`](../dlc-spring-boot-autoconfig/readme.md#4-jooq-persistence-autoconfig-dlcjooqpersistenceautoconfiguration))
and `jdbc-integration` (via [`DlcJdbcPersistenceAutoConfiguration`](../dlc-spring-boot-autoconfig/readme.md#5-jdbc-persistence-autoconfig-dlcjdbcpersistenceautoconfiguration)),
the Spring Boot 3 and Spring Boot 4 autoconfig modules - and so the DLC Spring Boot starters - set this up with
`@EnableDlc(transactionCacheEnabled = true)` or `dlc.features.persistence.transaction-cache.enabled=true` - the
cache is off by default.
`dlc.features.persistence.transaction-cache.max-size` limits the number of aggregates held per transaction (256 by
default).

For a manual setup (no autoconfig), set the provider on the persistence configuration, for example for
`jooq-integration`:

```java
var configuration = JooqDomainPersistenceConfiguration.JooqPersistenceConfigurationBuilder.newConfig()
    // ...
    .withTransactionCacheEnabled(true)
    .withTransactionCacheProvider(new SpringTransactionCacheProvider<UpdatableRecord<?>>())
    .make();
```

and for `jdbc-integration`, typed `SpringTransactionCacheProvider<JdbcRecord>`, via
`JdbcPersistenceConfigurationBuilder.withTransactionCacheProvider(...)`. A constructor argument limits the number of
aggregates held per transaction (256 by default). See [Transaction Cache](../persistence/readme.md#transaction-cache)
for the full picture, including when to empty the cache via `clearCurrentTransactionCache()`.

### Dependency

| Feature                                    | Relevant for          | Dependency (groupId:artifactId)             |
|---------------------------------------------|------------------------|---------------------------------------------|
| Spring transaction binding for the DLC transaction cache | application developers without the Spring Boot autoconfig | `io.domainlifecycles:persistence-cache-spring-tx` |

Pulled in transitively by the Spring Boot autoconfig modules and starters - with those, application code does not
need to depend on it directly.

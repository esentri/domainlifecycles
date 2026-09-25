## DLC Spring Transaction Cache Binder

`persistence-spring-tx` binds DLC's [Transaction Cache](../persistence/readme.md#transaction-cache) feature to a
Spring-managed (`@Transactional`) transaction. It is a small, dedicated module used by both persistence
implementations - [`jooq-integration`](../jooq-integration) and [`jdbc-integration`](../jdbc-integration/readme.md#transaction-cache)
- rather than code living in either of them, so it stays usable without pulling `spring-tx` onto the classpath
of a non-Spring project (`compileOnly`, not `api`/`implementation`).

### Why this module exists

The transaction cache needs to know when a transaction begins and ends, so it can open and close a per-transaction
cache scope at the right time. Each persistence technology already has its own native way of observing that:

- jOOQ raises a `TransactionListener` event (`TransactionCacheJooqBinder`).
- Plain JDBC's own `Connection` proxy observes `commit()`/`rollback()` (`TransactionCacheAwareConnectionProvider`).

Both of these are bypassed by a transaction that Spring itself began and will commit or roll back
(`@Transactional`, `DataSourceTransactionManager`): jOOQ's `TransactionListener` never fires unless application
code explicitly calls `dslContext.transaction(...)` itself, and Spring commits/rolls back the physical JDBC
`Connection` directly, never through the proxy either integration's own binder hands out. On the primary Spring
Boot integration path, the transaction cache would otherwise silently never activate - no error, no log, just a
cache that never holds anything.

### `SpringTransactionCacheBinder`

`io.domainlifecycles.persistence.spring.cache.SpringTransactionCacheBinder` is the one class this module adds.
Unlike jOOQ's `TransactionListener`, Spring raises no "transaction begin" event of its own to react to eagerly,
so the binder is designed to be called opportunistically:

```java
var binder = new SpringTransactionCacheBinder<>(transactionCacheProvider);
binder.ensureScopeOpenForCurrentTransaction();
```

- Called with no active Spring transaction on the current thread, it is a no-op.
- Called for the first time within a given Spring transaction, it opens a cache scope and registers a
  `TransactionSynchronization` that closes it again in `afterCompletion` (Spring raises no earlier event
  suitable for closing it either).
- Called again within the same transaction, it is a no-op (idempotent), so it is always safe to call
  unconditionally.

This makes it safe to call from whatever point a persistence-technology-specific integration already touches on
*every* operation regardless of transactional state - the one thing both integrations have in common is their own
connection provider abstraction:

- `jooq-integration`'s `SpringTransactionCacheAwareConnectionProvider` decorates jOOQ's `ConnectionProvider` and
  calls the binder before every `acquire()`.
- `jdbc-integration`'s `SpringTransactionCacheAwareConnectionProvider` decorates this project's own
  `JdbcConnectionProvider` and calls the binder before every `getConnection()`.

Both decorators are meant to be used *alongside*, not instead of, each integration's own native binder
(`TransactionCacheJooqBinder` / `TransactionCacheAwareConnectionProvider`): outside of an active Spring
transaction, `ensureScopeOpenForCurrentTransaction()` is a no-op, so standalone usage of either technology (no
Spring, or an explicit `dslContext.transaction(...)` call) continues to work exactly as before, through the
native binder alone.

### Usage

For both `jooq-integration` (via [`DlcJooqPersistenceAutoConfiguration`](../dlc-spring-boot-autoconfig/readme.md#4-jooq-persistence-autoconfig-dlcjooqpersistenceautoconfiguration))
and `jdbc-integration` (via [`DlcJdbcPersistenceAutoConfiguration`](../dlc-spring-boot-autoconfig/readme.md#5-jdbc-persistence-autoconfig-dlcjdbcpersistenceautoconfiguration)),
this is wired automatically - both the Spring Boot 3 and Spring Boot 4 autoconfig modules wire the Transaction
Cache to Spring's own transaction management by default, with nothing to configure by hand in a Spring Boot
application.

For a manual setup (no autoconfig), wire the same shared `ThreadBoundTransactionCacheProvider` into both the
connection provider and the `DomainPersistenceProvider`, for example for `jooq-integration`:

```java
@Bean
public ThreadBoundTransactionCacheProvider<UpdatableRecord<?>> dlcTransactionCacheProvider() {
    return new ThreadBoundTransactionCacheProvider<>();
}

@Bean
public ConnectionProvider connectionProvider(
    DataSource dataSource, ThreadBoundTransactionCacheProvider<UpdatableRecord<?>> transactionCacheProvider) {
    var dataSourceConnectionProvider =
        new DataSourceConnectionProvider(new TransactionAwareDataSourceProxy(dataSource));
    return new SpringTransactionCacheAwareConnectionProvider(
        dataSourceConnectionProvider, new SpringTransactionCacheBinder<>(transactionCacheProvider));
}
```

The same `transactionCacheProvider` bean must also be passed to `JooqDomainPersistenceConfiguration`
(`.withTransactionCacheProvider(...)`) or `JdbcDomainPersistenceConfiguration`, so that the connection provider
and the persistence provider agree on what "the current transaction's cache" is - see
[Transaction Cache](../persistence/readme.md#transaction-cache) for the full picture, including how to disable
the feature entirely.

### Dependency

| Feature                                    | Relevant for          | Dependency (groupId:artifactId)             |
|---------------------------------------------|------------------------|---------------------------------------------|
| Spring transaction binding for the DLC transaction cache | only internally used   | `io.domainlifecycles:persistence-spring-tx` |

Pulled in transitively by both `io.domainlifecycles:jooq-integration` and `io.domainlifecycles:jdbc-integration` -
application code does not need to depend on it directly.

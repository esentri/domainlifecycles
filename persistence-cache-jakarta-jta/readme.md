## DLC JTA Transaction Cache Provider

`persistence-cache-jakarta-jta` binds DLC's [Transaction Cache](../persistence/readme.md#transaction-cache) feature to JTA
transactions without Spring - e.g. in a Jakarta EE application server or with a standalone JTA transaction manager.
It works for both persistence implementations, [`jooq-integration`](../jooq-integration) and
[`jdbc-integration`](../jdbc-integration/readme.md#transaction-cache).

With Spring, use Spring's `JtaTransactionManager` instead: the Spring binding of
[`persistence-cache-spring-tx`](../persistence-cache-spring-tx/readme.md), wired by the Spring Boot autoconfig, covers it.

### Why this module exists

The transaction cache is only correct if DLC learns reliably when a transaction begins and ends. Under JTA, the
transaction manager commits and rolls back - neither jOOQ's own transaction events nor anything happening on a JDBC
connection reveal the end of a transaction. JTA's `TransactionSynchronizationRegistry` does: it holds resources per
transaction and notifies registered synchronizations once a transaction completes.

### `JtaTransactionCacheProvider`

`io.domainlifecycles.persistence.jta.cache.JtaTransactionCacheProvider` keeps the cache of a transaction as a
resource of that transaction in the `TransactionSynchronizationRegistry`:

- the cache is created on first use within a transaction and lives as long as the transaction, across all its reads
  and writes,
- it is emptied once the transaction completes - committed or rolled back,
- a transaction suspended for another one gets its own cache back when it resumes, and the other one never sees it,
- outside an active transaction - none at all, or one marked for rollback - there is no cache.

No connection decorator is needed, and the connections are obtained and released as usual.

### Usage

```groovy
dependencies {
    implementation 'io.domainlifecycles:persistence-cache-jakarta-jta:3.5.0'
}
```

Hand the registry of your transaction manager to the provider and set it on the persistence configuration -
e.g. in a Jakarta EE application server, where the registry is available via
`@Resource TransactionSynchronizationRegistry` or JNDI (`java:comp/TransactionSynchronizationRegistry`):

```java
var cacheProvider = new JtaTransactionCacheProvider<JdbcRecord>(transactionSynchronizationRegistry);

JdbcDomainPersistenceConfiguration.JdbcPersistenceConfigurationBuilder.newConfig()
    // ...
    .withTransactionCacheEnabled(true)     // the cache is off by default
    .withTransactionCacheProvider(cacheProvider)
    .make();
```

For `jooq-integration`, the provider is typed `JtaTransactionCacheProvider<UpdatableRecord<?>>` and set via
`JooqPersistenceConfigurationBuilder.withTransactionCacheProvider(...)`. A second constructor argument limits the
number of aggregates held per transaction (256 by default). After changing an aggregate the transaction already
loaded bypassing DLC's repositories, empty the cache via `cacheProvider.clearCurrentTransactionCache()` - see
[Failures, and emptying the cache](../persistence/readme.md#transaction-cache-clear).

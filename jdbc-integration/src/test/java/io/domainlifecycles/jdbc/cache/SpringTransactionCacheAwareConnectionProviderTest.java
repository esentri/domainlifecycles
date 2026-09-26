package io.domainlifecycles.jdbc.cache;

import io.domainlifecycles.jdbc.connection.JdbcConnectionProvider;
import io.domainlifecycles.jdbc.records.JdbcRecord;
import io.domainlifecycles.persistence.cache.ThreadBoundTransactionCacheProvider;
import io.domainlifecycles.persistence.spring.cache.SpringTransactionCacheBinder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.sql.Connection;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

/**
 * Verifies {@link SpringTransactionCacheAwareConnectionProvider}, the one reliable cache-activation hook
 * under a purely Spring-managed ({@code @Transactional}) transaction - unlike {@link
 * TransactionCacheAwareConnectionProvider}'s {@code commit()}/{@code rollback()} proxying, which such a
 * transaction bypasses entirely.
 */
class SpringTransactionCacheAwareConnectionProviderTest {

    private final ThreadBoundTransactionCacheProvider<JdbcRecord> transactionCacheProvider =
        new ThreadBoundTransactionCacheProvider<>();

    private final SpringTransactionCacheBinder<JdbcRecord> binder =
        new SpringTransactionCacheBinder<>(transactionCacheProvider);

    private static final class FixedConnectionProvider implements JdbcConnectionProvider {
        private final Connection connection;

        private FixedConnectionProvider(Connection connection) {
            this.connection = connection;
        }

        @Override
        public Connection getConnection() {
            return connection;
        }
    }

    @AfterEach
    void cleanUpAnyLeftoverSynchronization() {
        if (TransactionSynchronizationManager.isSynchronizationActive()) {
            TransactionSynchronizationManager.clearSynchronization();
        }
    }

    @Test
    void getConnectionOutsideATransactionDelegatesWithoutOpeningAScope() {
        var expectedConnection = mock(Connection.class);
        var provider = new SpringTransactionCacheAwareConnectionProvider(
            new FixedConnectionProvider(expectedConnection), binder);

        var returned = provider.getConnection();

        assertThat(returned).isSameAs(expectedConnection);
        assertThat(transactionCacheProvider.currentTransactionCache()).isEmpty();
    }

    @Test
    void getConnectionWithinAnActiveTransactionOpensTheScopeBeforeDelegating() {
        TransactionSynchronizationManager.initSynchronization();
        var expectedConnection = mock(Connection.class);
        var provider = new SpringTransactionCacheAwareConnectionProvider(
            new FixedConnectionProvider(expectedConnection), binder);

        var returned = provider.getConnection();

        assertThat(returned).isSameAs(expectedConnection);
        assertThat(transactionCacheProvider.currentTransactionCache())
            .as("the scope must be open once an active Spring transaction is touched")
            .isPresent();
    }

    @Test
    void repeatedGetConnectionWithinTheSameTransactionDoesNotReopenTheScope() {
        TransactionSynchronizationManager.initSynchronization();
        var provider = new SpringTransactionCacheAwareConnectionProvider(
            new FixedConnectionProvider(mock(Connection.class)), binder);

        provider.getConnection();
        var firstScope = transactionCacheProvider.currentTransactionCache().orElseThrow();

        provider.getConnection();

        assertThat(transactionCacheProvider.currentTransactionCache())
            .as("a second call within the same transaction must not replace the already open scope")
            .contains(firstScope);
    }
}

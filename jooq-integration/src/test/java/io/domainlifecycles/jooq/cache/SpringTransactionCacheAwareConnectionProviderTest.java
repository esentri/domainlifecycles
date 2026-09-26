package io.domainlifecycles.jooq.cache;

import io.domainlifecycles.persistence.cache.ThreadBoundTransactionCacheProvider;
import io.domainlifecycles.persistence.spring.cache.SpringTransactionCacheBinder;
import org.jooq.ConnectionProvider;
import org.jooq.UpdatableRecord;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.sql.Connection;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Verifies {@link SpringTransactionCacheAwareConnectionProvider}, the one reliable cache-activation hook
 * under a purely Spring-managed ({@code @Transactional}) transaction - unlike {@link
 * TransactionCacheJooqBinder}'s {@code Configuration} {@code TransactionListener}, which never fires for a
 * transaction Spring's own {@code DataSourceTransactionManager} began and will commit/roll back.
 */
class SpringTransactionCacheAwareConnectionProviderTest {

    private final ThreadBoundTransactionCacheProvider<UpdatableRecord<?>> transactionCacheProvider =
        new ThreadBoundTransactionCacheProvider<>();

    private final SpringTransactionCacheBinder<UpdatableRecord<?>> binder =
        new SpringTransactionCacheBinder<>(transactionCacheProvider);

    @AfterEach
    void cleanUpAnyLeftoverSynchronization() {
        if (TransactionSynchronizationManager.isSynchronizationActive()) {
            TransactionSynchronizationManager.clearSynchronization();
        }
    }

    @Test
    void acquireOutsideATransactionDelegatesWithoutOpeningAScope() {
        var delegate = mock(ConnectionProvider.class);
        var expectedConnection = mock(Connection.class);
        when(delegate.acquire()).thenReturn(expectedConnection);
        var provider = new SpringTransactionCacheAwareConnectionProvider(delegate, binder);

        var returned = provider.acquire();

        assertThat(returned).isSameAs(expectedConnection);
        assertThat(transactionCacheProvider.currentTransactionCache()).isEmpty();
    }

    @Test
    void acquireWithinAnActiveTransactionOpensTheScopeBeforeDelegating() {
        TransactionSynchronizationManager.initSynchronization();
        var delegate = mock(ConnectionProvider.class);
        var expectedConnection = mock(Connection.class);
        when(delegate.acquire()).thenReturn(expectedConnection);
        var provider = new SpringTransactionCacheAwareConnectionProvider(delegate, binder);

        var returned = provider.acquire();

        assertThat(returned).isSameAs(expectedConnection);
        assertThat(transactionCacheProvider.currentTransactionCache())
            .as("the scope must be open once an active Spring transaction is touched")
            .isPresent();
    }

    @Test
    void repeatedAcquireWithinTheSameTransactionDoesNotReopenTheScope() {
        TransactionSynchronizationManager.initSynchronization();
        var delegate = mock(ConnectionProvider.class);
        when(delegate.acquire()).thenReturn(mock(Connection.class));
        var provider = new SpringTransactionCacheAwareConnectionProvider(delegate, binder);

        provider.acquire();
        var firstScope = transactionCacheProvider.currentTransactionCache().orElseThrow();

        provider.acquire();

        assertThat(transactionCacheProvider.currentTransactionCache())
            .as("a second call within the same transaction must not replace the already open scope")
            .contains(firstScope);
    }

    @Test
    void releaseDelegatesWithoutTouchingTheBinder() {
        var delegate = mock(ConnectionProvider.class);
        var provider = new SpringTransactionCacheAwareConnectionProvider(delegate, binder);
        var connection = mock(Connection.class);

        provider.release(connection);

        verify(delegate).release(connection);
        assertThat(transactionCacheProvider.currentTransactionCache()).isEmpty();
    }
}

package io.domainlifecycles.persistence.spring.cache;

import io.domainlifecycles.persistence.cache.ThreadBoundTransactionCacheProvider;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.transaction.support.TransactionSynchronizationUtils;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Verifies {@link SpringTransactionCacheBinder} directly against Spring's {@link TransactionSynchronizationManager}
 * - simulating an active/completing transaction without a real {@code DataSource} or
 * {@code PlatformTransactionManager} - so this stays independent of how any particular persistence
 * technology's Spring Boot auto-configuration wires things up.
 */
class SpringTransactionCacheBinderTest {

    private final ThreadBoundTransactionCacheProvider<Object> transactionCacheProvider =
        new ThreadBoundTransactionCacheProvider<>();

    private final SpringTransactionCacheBinder<Object> binder =
        new SpringTransactionCacheBinder<>(transactionCacheProvider);

    @AfterEach
    void cleanUpAnyLeftoverSynchronization() {
        if (TransactionSynchronizationManager.isSynchronizationActive()) {
            TransactionSynchronizationManager.clearSynchronization();
        }
    }

    @Test
    void doesNothingWithoutAnActiveTransaction() {
        binder.ensureScopeOpenForCurrentTransaction();

        assertThat(transactionCacheProvider.currentTransactionCache()).isEmpty();
    }

    @Test
    void opensScopeOnceForAnActiveTransactionAndClosesItOnCompletion() {
        TransactionSynchronizationManager.initSynchronization();

        binder.ensureScopeOpenForCurrentTransaction();
        assertThat(transactionCacheProvider.currentTransactionCache())
            .as("a scope must be open once the transaction is active")
            .isPresent();

        //idempotent: a second call within the same transaction must not open (or lose) a scope
        binder.ensureScopeOpenForCurrentTransaction();
        assertThat(transactionCacheProvider.currentTransactionCache()).isPresent();

        TransactionSynchronizationUtils.triggerAfterCompletion(TransactionSynchronization.STATUS_COMMITTED);
        TransactionSynchronizationManager.clearSynchronization();

        assertThat(transactionCacheProvider.currentTransactionCache())
            .as("the scope must be closed once the transaction completes")
            .isEmpty();
    }

    @Test
    void opensASeparateScopeForEachSubsequentTransaction() {
        TransactionSynchronizationManager.initSynchronization();
        binder.ensureScopeOpenForCurrentTransaction();
        TransactionSynchronizationUtils.triggerAfterCompletion(TransactionSynchronization.STATUS_COMMITTED);
        TransactionSynchronizationManager.clearSynchronization();
        assertThat(transactionCacheProvider.currentTransactionCache()).isEmpty();

        TransactionSynchronizationManager.initSynchronization();
        binder.ensureScopeOpenForCurrentTransaction();

        assertThat(transactionCacheProvider.currentTransactionCache())
            .as("a fresh transaction must get its own scope, not a stale reference to the previous one's")
            .isPresent();
    }
}

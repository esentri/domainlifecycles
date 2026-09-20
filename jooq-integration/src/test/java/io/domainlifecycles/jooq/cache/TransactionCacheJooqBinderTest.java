package io.domainlifecycles.jooq.cache;

import io.domainlifecycles.persistence.cache.ThreadBoundTransactionCacheProvider;
import org.jooq.Configuration;
import org.jooq.TransactionContext;
import org.jooq.TransactionListenerProvider;
import org.jooq.UpdatableRecord;
import org.jooq.impl.DefaultTransactionListenerProvider;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class TransactionCacheJooqBinderTest {

    private static TransactionContext ctx() {
        return mock(TransactionContext.class);
    }

    @Test
    public void beginStartOpensAScope() {
        var cacheProvider = new ThreadBoundTransactionCacheProvider<UpdatableRecord<?>>();
        var binder = new TransactionCacheJooqBinder(cacheProvider);

        binder.beginStart(ctx());

        assertThat(cacheProvider.currentTransactionCache()).isPresent();
    }

    @Test
    public void commitEndClosesTheScope() {
        var cacheProvider = new ThreadBoundTransactionCacheProvider<UpdatableRecord<?>>();
        var binder = new TransactionCacheJooqBinder(cacheProvider);

        binder.beginStart(ctx());
        binder.commitEnd(ctx());

        assertThat(cacheProvider.currentTransactionCache()).isEmpty();
    }

    @Test
    public void rollbackEndClosesTheScope() {
        var cacheProvider = new ThreadBoundTransactionCacheProvider<UpdatableRecord<?>>();
        var binder = new TransactionCacheJooqBinder(cacheProvider);

        binder.beginStart(ctx());
        binder.rollbackEnd(ctx());

        assertThat(cacheProvider.currentTransactionCache()).isEmpty();
    }

    @Test
    public void onlyTheOutermostCommitEndClosesTheScope() {
        var cacheProvider = new ThreadBoundTransactionCacheProvider<UpdatableRecord<?>>();
        var binder = new TransactionCacheJooqBinder(cacheProvider);

        binder.beginStart(ctx());
        binder.beginStart(ctx());
        binder.commitEnd(ctx());
        assertThat(cacheProvider.currentTransactionCache()).isPresent();

        binder.commitEnd(ctx());
        assertThat(cacheProvider.currentTransactionCache()).isEmpty();
    }

    @Test
    public void registerOnAppendsTheBinderWhenNoneIsRegisteredYet() {
        var cacheProvider = new ThreadBoundTransactionCacheProvider<UpdatableRecord<?>>();
        var configuration = mock(Configuration.class);
        when(configuration.transactionListenerProviders()).thenReturn(new TransactionListenerProvider[0]);

        TransactionCacheJooqBinder.registerOn(configuration, cacheProvider);

        verify(configuration).setAppending(any(TransactionListenerProvider.class));
    }

    @Test
    public void registerOnDoesNothingWhenAlreadyRegisteredForTheSameProvider() {
        var cacheProvider = new ThreadBoundTransactionCacheProvider<UpdatableRecord<?>>();
        var existingBinder = new TransactionCacheJooqBinder(cacheProvider);
        var configuration = mock(Configuration.class);
        when(configuration.transactionListenerProviders())
            .thenReturn(new TransactionListenerProvider[]{new DefaultTransactionListenerProvider(existingBinder)});

        TransactionCacheJooqBinder.registerOn(configuration, cacheProvider);

        verify(configuration, never()).setAppending(any(TransactionListenerProvider.class));
    }

    @Test
    public void registerOnAppendsASecondBinderForADifferentProvider() {
        var firstCacheProvider = new ThreadBoundTransactionCacheProvider<UpdatableRecord<?>>();
        var secondCacheProvider = new ThreadBoundTransactionCacheProvider<UpdatableRecord<?>>();
        var existingBinder = new TransactionCacheJooqBinder(firstCacheProvider);
        var configuration = mock(Configuration.class);
        when(configuration.transactionListenerProviders())
            .thenReturn(new TransactionListenerProvider[]{new DefaultTransactionListenerProvider(existingBinder)});

        TransactionCacheJooqBinder.registerOn(configuration, secondCacheProvider);

        verify(configuration).setAppending(any(TransactionListenerProvider.class));
    }
}

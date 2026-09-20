package io.domainlifecycles.jdbc.cache;

import io.domainlifecycles.jdbc.connection.JdbcConnectionProvider;
import io.domainlifecycles.jdbc.records.JdbcRecord;
import io.domainlifecycles.persistence.cache.AggregateCacheKey;
import io.domainlifecycles.persistence.cache.ThreadBoundTransactionCacheProvider;
import io.domainlifecycles.domain.types.Identity;
import io.domainlifecycles.persistence.fetcher.FetcherResult;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.Savepoint;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class TransactionCacheAwareConnectionProviderTest {

    private record TestId(Long value) implements Identity<Long> {
    }

    private static AggregateCacheKey key() {
        return new AggregateCacheKey("some.Aggregate", new TestId(1L));
    }

    private static final class FixedConnectionProvider implements JdbcConnectionProvider {
        private Connection connection;

        private FixedConnectionProvider(Connection connection) {
            this.connection = connection;
        }

        @Override
        public Connection getConnection() {
            return connection;
        }
    }

    @Test
    public void firstGetConnectionOpensAScope() {
        var cacheProvider = new ThreadBoundTransactionCacheProvider<JdbcRecord>();
        var provider = new TransactionCacheAwareConnectionProvider(
            new FixedConnectionProvider(mock(Connection.class)), cacheProvider);

        provider.getConnection();

        assertThat(cacheProvider.currentTransactionCache()).isPresent();
    }

    @Test
    public void repeatedGetConnectionWithTheSameUnderlyingConnectionDoesNotReopenTheScope() {
        var cacheProvider = new ThreadBoundTransactionCacheProvider<JdbcRecord>();
        var provider = new TransactionCacheAwareConnectionProvider(
            new FixedConnectionProvider(mock(Connection.class)), cacheProvider);

        provider.getConnection();
        cacheProvider.currentTransactionCache().orElseThrow().put(key(), new FetcherResult<>(null, null));

        provider.getConnection();

        assertThat(cacheProvider.currentTransactionCache().orElseThrow().take(key())).isPresent();
    }

    @Test
    public void commitOnTheReturnedConnectionClosesTheScope() throws Exception {
        var cacheProvider = new ThreadBoundTransactionCacheProvider<JdbcRecord>();
        var realConnection = mock(Connection.class);
        var provider = new TransactionCacheAwareConnectionProvider(
            new FixedConnectionProvider(realConnection), cacheProvider);

        var proxy = provider.getConnection();
        proxy.commit();

        verify(realConnection).commit();
        assertThat(cacheProvider.currentTransactionCache()).isEmpty();
    }

    @Test
    public void rollbackOnTheReturnedConnectionClosesTheScope() throws Exception {
        var cacheProvider = new ThreadBoundTransactionCacheProvider<JdbcRecord>();
        var realConnection = mock(Connection.class);
        var provider = new TransactionCacheAwareConnectionProvider(
            new FixedConnectionProvider(realConnection), cacheProvider);

        var proxy = provider.getConnection();
        proxy.rollback();

        verify(realConnection).rollback();
        assertThat(cacheProvider.currentTransactionCache()).isEmpty();
    }

    @Test
    public void rollbackToASavepointDoesNotCloseTheScope() throws Exception {
        var cacheProvider = new ThreadBoundTransactionCacheProvider<JdbcRecord>();
        var realConnection = mock(Connection.class);
        var provider = new TransactionCacheAwareConnectionProvider(
            new FixedConnectionProvider(realConnection), cacheProvider);
        var savepoint = mock(Savepoint.class);

        var proxy = provider.getConnection();
        proxy.rollback(savepoint);

        verify(realConnection).rollback(savepoint);
        assertThat(cacheProvider.currentTransactionCache()).isPresent();
    }

    @Test
    public void aNewUnderlyingConnectionReopensTheScopeEvenWithoutCommitOrRollback() {
        var cacheProvider = new ThreadBoundTransactionCacheProvider<JdbcRecord>();
        var delegate = new FixedConnectionProvider(mock(Connection.class));
        var provider = new TransactionCacheAwareConnectionProvider(delegate, cacheProvider);

        provider.getConnection();
        cacheProvider.currentTransactionCache().orElseThrow().put(key(), new FetcherResult<>(null, null));

        //a caller managing the connection's transaction boundary itself (bypassing commit()/rollback() on
        //the object this provider handed out) still gets a correctly isolated, empty scope for what the
        //delegate now reports as a different underlying connection
        delegate.connection = mock(Connection.class);
        provider.getConnection();

        assertThat(cacheProvider.currentTransactionCache().orElseThrow().take(key())).isEmpty();
    }
}

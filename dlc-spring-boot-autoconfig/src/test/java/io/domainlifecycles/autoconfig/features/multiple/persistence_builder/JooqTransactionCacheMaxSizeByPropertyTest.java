package io.domainlifecycles.autoconfig.features.multiple.persistence_builder;

import io.domainlifecycles.autoconfig.model.persistence.TestRootSimpleId;
import io.domainlifecycles.jooq.imp.provider.JooqDomainPersistenceProvider;
import io.domainlifecycles.persistence.cache.AggregateCacheKey;
import io.domainlifecycles.persistence.fetcher.FetcherResult;
import io.domainlifecycles.persistence.spring.cache.SpringTransactionCacheProvider;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(
    classes = TestApplicationPersistenceAndBuilderAutoConfig.class,
    properties = "dlc.features.persistence.transaction-cache.max-size=1")
@ActiveProfiles({"test", "test-dlc-domain", "test-dlc-persistence"})
class JooqTransactionCacheMaxSizeByPropertyTest {

    @Autowired
    private JooqDomainPersistenceProvider jooqDomainPersistenceProvider;

    @Autowired
    private PlatformTransactionManager transactionManager;

    @Test
    void testTheCacheHoldsAtMostTheConfiguredNumberOfAggregates() {
        assertThat(jooqDomainPersistenceProvider.transactionCacheProvider).isInstanceOf(SpringTransactionCacheProvider.class);
        var first = new AggregateCacheKey("some.Aggregate", new TestRootSimpleId(1L));
        var second = new AggregateCacheKey("some.Aggregate", new TestRootSimpleId(2L));

        new TransactionTemplate(transactionManager).executeWithoutResult(status -> {
            var cache = jooqDomainPersistenceProvider.transactionCacheProvider.currentTransactionCache().orElseThrow();
            cache.put(first, new FetcherResult<>(null, null));
            cache.put(second, new FetcherResult<>(null, null));

            assertThat(cache.take(first)).as("evicted beyond the maximal size").isEmpty();
            assertThat(cache.take(second)).isPresent();
        });
    }
}

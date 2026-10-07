package io.domainlifecycles.autoconfig.features.multiple.persistence_builder;

import io.domainlifecycles.jooq.imp.provider.JooqDomainPersistenceProvider;
import io.domainlifecycles.persistence.cache.NoOpTransactionCacheProvider;
import io.domainlifecycles.persistence.cache.TransactionCacheProvider;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = TestApplicationPersistenceAndBuilderAutoConfig.class)
@ActiveProfiles({"test", "test-dlc-domain", "test-dlc-persistence"})
class JooqTransactionCacheOffByDefaultTest {

    @Autowired
    private JooqDomainPersistenceProvider jooqDomainPersistenceProvider;

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private PlatformTransactionManager transactionManager;

    @Test
    void testNoTransactionCacheProviderIsCreated() {
        assertThat(applicationContext.getBeansOfType(TransactionCacheProvider.class)).isEmpty();
    }

    @Test
    void testThereIsNoCacheWithinATransaction() {
        assertThat(jooqDomainPersistenceProvider.transactionCacheProvider).isInstanceOf(NoOpTransactionCacheProvider.class);
        new TransactionTemplate(transactionManager).executeWithoutResult(status ->
            assertThat(jooqDomainPersistenceProvider.transactionCacheProvider.currentTransactionCache()).isEmpty());
    }
}

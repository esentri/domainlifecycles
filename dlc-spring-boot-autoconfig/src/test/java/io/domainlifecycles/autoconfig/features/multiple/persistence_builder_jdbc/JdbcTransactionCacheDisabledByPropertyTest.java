package io.domainlifecycles.autoconfig.features.multiple.persistence_builder_jdbc;

import io.domainlifecycles.jdbc.imp.provider.JdbcDomainPersistenceProvider;
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

@SpringBootTest(
    classes = TestApplicationJdbcPersistenceAndBuilderAutoConfig.class,
    properties = "dlc.features.persistence.transaction-cache.enabled=false")
@ActiveProfiles({"test", "test-dlc-domain", "test-dlc-persistence"})
class JdbcTransactionCacheDisabledByPropertyTest {

    @Autowired
    private JdbcDomainPersistenceProvider jdbcDomainPersistenceProvider;

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
        assertThat(jdbcDomainPersistenceProvider.transactionCacheProvider).isInstanceOf(NoOpTransactionCacheProvider.class);
        new TransactionTemplate(transactionManager).executeWithoutResult(status ->
            assertThat(jdbcDomainPersistenceProvider.transactionCacheProvider.currentTransactionCache()).isEmpty());
    }
}
